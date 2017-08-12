package cn.yxffcode.httpmapper.spring;

/**
 * @author gaohang on 8/11/17.
 */
public class JsonResult<T> {

  public static <T> JsonResult<T> success(T payload) {
    return new JsonResult<T>(payload);
  }

  private boolean success;
  private String errmsg;
  private T payload;

  public JsonResult() {
  }

  public JsonResult(T payload) {
    this.payload = payload;
    this.success = true;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }

  public T getPayload() {
    return payload;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "JsonResult{" +
        "success=" + success +
        ", errmsg='" + errmsg + '\'' +
        ", payload=" + payload +
        '}';
  }
}
