package cn.yxffcode.httpmapper.spring;

import cn.yxffcode.httpmapper.core.MappedRequest;
import cn.yxffcode.httpmapper.core.RequestPostProcessor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

/**
 * @author gaohang on 8/12/17.
 */
public class KeepHeaderPostProcessor implements RequestPostProcessor {

  @Override
  public boolean postProcessRequest(HttpUriRequest request, MappedRequest mr, Map<String, Object> params) {
    request.addHeader("testHeader", "testHeader");
    return true;
  }

  @Override
  public void postProcessResponse(HttpResponse response, MappedRequest mr) {
  }
}
