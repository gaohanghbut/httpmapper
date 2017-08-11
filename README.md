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