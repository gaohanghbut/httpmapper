package cn.yxffcode.httpmapper.core.http;

import org.apache.http.nio.client.HttpAsyncClient;

/**
 * @author gaohang on 8/6/17.
 */
public interface HttpClientFactory {
  HttpAsyncClient create();
}
