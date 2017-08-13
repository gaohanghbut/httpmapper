package cn.yxffcode.httpmapper.spring;

import cn.yxffcode.httpmapper.core.FastJsonResponseHandler;
import cn.yxffcode.httpmapper.core.HttpParam;
import cn.yxffcode.httpmapper.core.POST;
import cn.yxffcode.httpmapper.core.PostProcessors;
import cn.yxffcode.httpmapper.core.Request;
import cn.yxffcode.httpmapper.core.Response;
import cn.yxffcode.httpmapper.core.ResponseCallback;
import cn.yxffcode.httpmapper.core.ToStringResponseHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author gaohang on 8/11/17.
 */
@Component
@Response(FastJsonResponseHandler.class)
public interface TestServiceFacade {

  @Request("http://localhost:8080/home/index.json?name=#{name}&test=1")
  @PostProcessors({KeepHeaderPostProcessor.class})
  Future<JsonResult<TestBean>> get(@HttpParam("name") String name);

  @Request("http://localhost:8080/home/index.json?name=#{name}&test=1")
  @Response(ToStringResponseHandler.class)
  String getString(@HttpParam("name") String name);

  @Request("http://localhost:8080/home/index.json?name=#{name}&test=1")
  void getString(@HttpParam("name") String name, ResponseCallback<JsonResult<TestBean>> callback);

  @Request("http://localhost:8080/home/index.json?name=#{name}")
  @POST
  JsonResult<TestBean> post(@HttpParam("name") String name);

}
