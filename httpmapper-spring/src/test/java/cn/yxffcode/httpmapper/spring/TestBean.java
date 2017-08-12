package cn.yxffcode.httpmapper.spring;

/**
 * @author gaohang on 8/11/17.
 */
public class TestBean {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "TestBean{" +
        "name='" + name + '\'' +
        '}';
  }
}
