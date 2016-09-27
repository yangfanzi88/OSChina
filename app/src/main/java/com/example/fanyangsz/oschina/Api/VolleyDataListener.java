package com.example.fanyangsz.oschina.Api;

/**
 * Created by fanyang.sz on 2016/9/24.
 */

public interface VolleyDataListener<T> {
    public void onDataChanged(T data);
    public void onErrorHappened(String errorCode, String errorMessage);
}
