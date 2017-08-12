package cn.yxffcode.httpmapper.spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author gaohang on 8/13/17.
 */
public class HttpMapperAutoConfigurerTest {
  @Test
  public void postProcessBeanDefinitionRegistry() throws Exception {
    final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("http-mapper.xml");

    final TestServiceFacade testServiceFacade = ctx.getBean(TestServiceFacade.class);

    System.out.println(testServiceFacade.getString("name"));
  }

}