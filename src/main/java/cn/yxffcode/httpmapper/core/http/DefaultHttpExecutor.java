package cn.yxffcode.httpmapper.core.http;

import cn.yxffcode.httpmapper.core.MappedRequest;
import cn.yxffcode.httpmapper.core.RequestPostProcessor;
import cn.yxffcode.httpmapper.core.StopRequestException;
import cn.yxffcode.httpmapper.core.cfg.Configuration;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.HttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 8/6/17.
 */
public class DefaultHttpExecutor implements HttpExecutor, AutoCloseable {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpExecutor.class);

  private final HttpAsyncClient httpClient;

  private final Configuration configuration;

  public DefaultHttpExecutor(HttpClientFactory factory, Configuration configuration) {
    this.httpClient = factory.create();
    this.configuration = configuration;
    if (this.httpClient instanceof CloseableHttpAsyncClient) {
      ((CloseableHttpAsyncClient) this.httpClient).start();
    }
  }

  @Override
  public void close() throws Exception {
    if (this.httpClient instanceof Closeable) {
      ((Closeable) this.httpClient).close();
    }
  }

  @Override
  public Future<HttpResponse> execute(MappedRequest mappedRequest, Object params) {
    checkNotNull(mappedRequest);
    HttpUriRequest httpRequest = buildHttpRequest(mappedRequest, params);
    LOGGER.debug("execute http request:mapperedRequest={}, httpRequest={}", mappedRequest, httpRequest);
    //execute http
    final Future<HttpResponse> future = httpClient.execute(httpRequest, null);

    return Reflection.newProxy(Future.class, new AbstractInvocationHandler() {
      @Override
      protected Object handleInvocation(Object o, Method method, Object[] objects) throws Throwable {
        if (!method.getName().equals("get")) {
          return method.invoke(future, objects);
        }
        final HttpResponse httpResponse = (HttpResponse) method.invoke(future, objects);
        final Iterable<RequestPostProcessor> postProcessors = configuration.getPostProcessors(mappedRequest.getId());
        if (postProcessors != null) {
          for (RequestPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessResponse(httpResponse, mappedRequest);
          }
        }
        return method.invoke(future, objects);
      }
    });
  }

  private HttpUriRequest buildHttpRequest(MappedRequest mappedRequest, Object parameterObject) {
    final Map<String, Object> params = mappedRequest.resolveParams(parameterObject);
    try {
      switch (mappedRequest.getHttpMethod()) {
        case PUT: {
          final HttpPut httpPut = new HttpPut();
          invokeBeforeRequest(httpPut, mappedRequest, params);
          httpPut.setURI(new URI(mappedRequest.rendUrl(params)));
          return httpPut;
        }
        case DELETE: {
          final HttpDelete httpDelete = new HttpDelete();
          invokeBeforeRequest(httpDelete, mappedRequest, params);
          httpDelete.setURI(new URI(mappedRequest.rendUrl(params)));

          return httpDelete;
        }
        case GET: {
          final HttpGet httpGet = new HttpGet();
          invokeBeforeRequest(httpGet, mappedRequest, params);
          httpGet.setURI(new URI(mappedRequest.rendUrl(params)));
          return httpGet;
        }
        case POST: {
          final HttpPost httpPost = new HttpPost();
          invokeBeforeRequest(httpPost, mappedRequest, params);
          if (httpPost.getEntity() == null && params != null && params.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(Iterables.transform(params.entrySet(),
                (Function<Map.Entry<String, Object>, NameValuePair>) en ->
                    new BasicNameValuePair(en.getKey(), String.valueOf(en.getValue())))));
          }
          httpPost.setURI(new URI(mappedRequest.rendUrl(params)));
          return httpPost;
        }
        default:
          throw new UnsupportedOperationException("不支持的http method:" + mappedRequest.getHttpMethod());
      }
    } catch (URISyntaxException e) {
      throw Throwables.propagate(e);
    }
  }

  private void invokeBeforeRequest(HttpUriRequest httpUriRequest, MappedRequest mappedRequest, Map<String, Object> params) {
    final Iterable<RequestPostProcessor> postProcessors = configuration.getPostProcessors(mappedRequest.getId());
    if (postProcessors != null) {
      for (RequestPostProcessor postProcessor : postProcessors) {
        if (!postProcessor.postProcessRequest(httpUriRequest, mappedRequest, params)) {
          throw new StopRequestException("请求停止，RequestPostProcessor处理失败:" + mappedRequest + " params:" + params);
        }
      }
    }
  }

}
