package cn.yxffcode.httpmapper.spring;

import cn.yxffcode.httpmapper.core.RequestPostProcessor;
import cn.yxffcode.httpmapper.core.ResponseHandler;
import cn.yxffcode.httpmapper.core.http.HttpClientFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.List;

/**
 * @author gaohang on 8/12/17.
 */
public class HttpMapperAutoConfigurer implements BeanDefinitionRegistryPostProcessor {

  private HttpClientFactory httpClientFactory;
  private List<RequestPostProcessor> commonRequestPostProcessors;
  private ResponseHandler defaultResponseHandler;
  private String[] basePackages;

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    final HttpMapperScanner scanner = new HttpMapperScanner(registry, httpClientFactory,
        commonRequestPostProcessors, defaultResponseHandler);
    scanner.doScan(basePackages);
    scanner.getConfiguration();
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
  }

}
