# httpmapper
httpmapper是对httpasyncclient的简单封装，用于发送http请求并将返回数据转换成对象，以mapper接口的方式使用http-client，应用代码只需要写个interface的声明即可

## Get started
声明mapper接口：
```java
public interface TestServiceFacade {

  @Request("http://localhost:8080/home/index.json?name=#{name}")
  Future<JsonResult<TestBean>> get(@HttpParam("name") String name);

  @Request("http://localhost:8080/home/index.json?name=#{name}")
  @POST
  Future<JsonResult<TestBean>> post(@HttpParam("name") String name);

}
```

初始化接口：
```java
public void handleInvocation() throws Exception {

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

```

## 定制HttpClient
可以调用Configuration.setHttpClientFactory()方法提供一个HttpClientFactory
来替代默认的DefaultHttpClientFactory，用于定制HttpClient对象

## 使用RequestPostProcessor
RequestPostProcessor可对请求进行拦截，接口定义如下：
```java
public interface RequestPostProcessor {
  boolean postProcessRequest(HttpUriRequest request, MappedRequest mr, Map<String, Object> params);

  void postProcessResponse(HttpResponse response, MappedRequest mr);
}

```
在RequestPostProcessor中可对HttpRequest和HttpResponse做任何的定制.
例如：
```java
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

```
有了RequestPostProcessor后，可以通过@PostProcessors在mapper接口上使用，三种级别的使用，

1.全局的RequestPostProcessor

使用Configuration设置全局的RequestPostProcessor，全局的RequestPostProcessor对所有请求有效

2.接口级别的RequestPostProcessor

将@RequestPostProcessors标记在接口上
```java
@PostProcessors({KeepHeaderPostProcessor.class})
public interface TestServiceFacade {

  /**
  * 异步调用
  */
  @Request("http://localhost:8080/home/index.json?name=#{name}&test=1")
  Future<JsonResult<TestBean>> get(@HttpParam("name") String name);

  /**
  * 同步调用
  */
  @Request("http://localhost:8080/home/index.json?name=#{name}")
  @POST
  JsonResult<TestBean> post(@HttpParam("name") String name);
}
```

2.方法级别的RequestPostProcessor

将@RequestPostProcessors标记在方法上
```java
public interface TestServiceFacade {

  @Request("http://localhost:8080/home/index.json?name=#{name}&test=1")
  @PostProcessors({KeepHeaderPostProcessor.class})
  Future<JsonResult<TestBean>> get(@HttpParam("name") String name);

  @Request("http://localhost:8080/home/index.json?name=#{name}")
  @POST
  Future<JsonResult<TestBean>> post(@HttpParam("name") String name);
}

```
一个mapper接口的方法在发送请求时使用的RequestPostProcessor为：
全局 + 接口级别 + 方法级别