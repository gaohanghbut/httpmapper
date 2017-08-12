package cn.yxffcode.httpmapper.core.http;

import cn.yxffcode.httpmapper.core.MappedRequest;
import org.apache.http.HttpResponse;

import java.util.concurrent.Future;

/**
 * @author gaohang on 8/5/17.
 */
public interface HttpExecutor {
  Future<HttpResponse> execute(final MappedRequest mappedRequest, Object params);
}
