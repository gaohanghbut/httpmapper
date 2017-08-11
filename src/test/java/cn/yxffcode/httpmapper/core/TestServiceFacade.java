package cn.yxffcode.httpmapper.core;

import java.util.concurrent.Future;

/**
 * @author gaohang on 8/11/17.
 */
public interface TestServiceFacade {

  @Request("http://localhost:8080/home/index.json?name=#{name}")
  Future<JsonResult<TestBean>> get(@HttpParam("name") String name);

  @Request("http://localhost:8080/home/index.json?name=#{name}")
  @POST
  Future<JsonResult<TestBean>> post(@HttpParam("name") String name);

}
