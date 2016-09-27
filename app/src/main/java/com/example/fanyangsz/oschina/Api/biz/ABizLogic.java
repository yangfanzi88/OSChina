package com.example.fanyangsz.oschina.Api.biz;

import android.content.Context;

import com.example.fanyangsz.oschina.Api.http.HttpConfig;
import com.example.fanyangsz.oschina.Api.http.IHttpUtility;
import com.example.fanyangsz.oschina.Api.http.Params;
import com.example.fanyangsz.oschina.Api.setting.Setting;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;

import java.io.File;
import java.util.Map;

/**
 * Created by fanyang.sz on 2016/9/22.
 */

public class ABizLogic implements IHttpUtility {

    public static String TAG = ABizLogic.class.getSimpleName();

    public IHttpUtility mHttpUtility;
    public CacheConfig.CacheMode mCacheMode;
    public ABizLogic(Context context){
//        mHttpUtility = configHttpUtility(context);
        mCacheMode = CacheConfig.CacheMode.disable;
    }
//    private IHttpUtility configHttpUtility(Context context){
//        return new OschinaHttpUtility(context);
//    }
    @Override
    public <T> T doGet(HttpConfig config, Setting action, Params params, Class<T> responseCls) {
        return null;
    }

    @Override
    public <T> T doPost(HttpConfig config, Setting action, Params params, Class<T> responseCls, Object requestObj) {
        return null;
    }

    @Override
    public <T> T uploadFile(HttpConfig config, Setting action, Params params, Map<String, byte[]> entityMap, File[] files, Params headers, Class<T> responseClass) {
        return null;
    }
}
