package cn.yxffcode.httpmapper.core;

/**
 * @author gaohang on 8/13/17.
 */
public class RequestFaildException extends RuntimeException {
  public RequestFaildException() {
  }

  public RequestFaildException(String message) {
    super(message);
  }

  public RequestFaildException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestFaildException(Throwable cause) {
    super(cause);
  }

  public RequestFaildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
