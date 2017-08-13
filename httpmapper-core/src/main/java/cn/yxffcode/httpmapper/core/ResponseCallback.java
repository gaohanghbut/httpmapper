package cn.yxffcode.httpmapper.core;

/**
 * @author gaohang on 8/13/17.
 */
public interface ResponseCallback<T> {
  void apply(T result);
}
