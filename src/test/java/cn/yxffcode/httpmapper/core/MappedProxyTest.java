package cn.yxffcode.httpmapper.core;

import cn.yxffcode.httpmapper.core.cfg.Configuration;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author gaohang on 8/11/17.
 */
public class MappedProxyTest {

  @Test
  public void handleInvocation() throws ExecutionException, InterruptedException, IOException, NoSuchMethodException {

    final Configuration configuration = Configuration.newBuilder()
        .parse(TestServiceFacade.class)
        .setDefaultResponseHandler(new FastJsonResponseHandler())
        .build();

    final TestServiceFacade testServiceFacade = configuration.newMapper(TestServiceFacade.class);
    JsonResult<TestBean> result = testServiceFacade.get("name").get();
    System.out.println(result);

    result = testServiceFacade.post("name").get();
    System.out.println(result);

  }

}