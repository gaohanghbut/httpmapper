package cn.yxffcode.httpmapper.spring;

import cn.yxffcode.httpmapper.core.ResponseCallback;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author gaohang on 8/13/17.
 */
public class HttpMapperAutoConfigurerTest {
  @Test
  public void postProcessBeanDefinitionRegistry() throws Exception {
    final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("http-mapper.xml");

    final TestServiceFacade testServiceFacade = ctx.getBean(TestServiceFacade.class);

    testServiceFacade.getString("name", new ResponseCallback<JsonResult<TestBean>>() {
      @Override
      public void apply(JsonResult<TestBean> result) {
        System.out.println(result);
      }
    });

  }

}