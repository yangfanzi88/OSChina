package com.example.fanyangsz.oschina.Api.http;

import com.example.fanyangsz.oschina.Api.setting.Setting;

import java.io.File;
import java.util.Map;

/**
 * Created by fanyang.sz on 2016/9/22.
 */

public interface IHttpUtility {
    <T> T doGet(HttpConfig config, Setting action, Params params, Class<T> responseCls);

    <T> T doPost(HttpConfig config, Setting action, Params params, Class<T> responseCls, Object requestObj);

    <T> T uploadFile(HttpConfig config, Setting action, Params params, Map<String, byte[]> entityMap, File[] files, Params headers, Class<T> responseClass);
}
