package com.example.fanyangsz.oschina.Api;

import android.text.TextUtils;
import android.util.Log;

import com.example.fanyangsz.oschina.Api.cache.ICacheUtility;
import com.example.fanyangsz.oschina.Api.http.HttpConfig;
import com.example.fanyangsz.oschina.Api.http.IHttpUtility;
import com.example.fanyangsz.oschina.Api.http.Params;
import com.example.fanyangsz.oschina.Api.setting.Setting;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig.CacheMode;
import com.example.fanyangsz.oschina.Support.util.Logger;

import java.io.File;
import java.util.Map;

/**
 * Created by fanyang.sz on 2016/9/27.
 */

public class HttpLogic implements IHttpUtility {

    private static String TAG = HttpLogic.class.getSimpleName();
    // 缓存类型
    public static final String CACHE_UTILITY = "cache_utility";
    // 网络协议
    public static final String HTTP_UTILITY = "http";
    private CacheMode mCacheMode;

    @Override
    public <T> T doGet(HttpConfig config, Setting actionSetting, Params params, Class<T> responseCls) {
        String action = actionSetting.getValue();
        ICacheUtility cacheUtility = null;
        // 配置的缓存模式
        String cacheUtilityStr = getSettingValue(actionSetting, CACHE_UTILITY);
        if (!TextUtils.isEmpty(cacheUtilityStr)) {
            try {
                cacheUtility = (ICacheUtility) Class.forName(cacheUtilityStr).newInstance();
            } catch (Exception e) {
                Log.w(TAG, "CacheUtility 没有配置或者配置错误");
            }
        }

        ICacheUtility.Cache<T> cache = null;

        // 缓存是否在action中配置打开
        boolean cacheEnable = true;
        if (cacheEnable && (mCacheMode == CacheMode.cachePriority || mCacheMode != CacheMode.disable)) {
            // 拉取缓存数据
            if (cacheUtility != null) {
                try {
                    cache = cacheUtility.findCacheData(actionSetting, params, responseCls);
                } catch (Throwable e) {
                    Logger.printExc(getClass(), e);
                }
                if (cache != null && Logger.DEBUG) {
                    String state = cache.expired() ? "无效" : "有效";
                    // if (cache.getT() != null && cache.getT() instanceof IResult) {
                    // state = ((IResult) cache.getT()).expired() ? "无效" : "有效";
                    // }
                    Logger.d(TAG, String.format(" %s 有效期 %s, action = %s", cacheUtility.getClass().getSimpleName(),
                            state, actionSetting.getValue()));
                }

                if (cache != null && !cache.expired()) {
                    // 文件缓存 拉到数据后 刷新内存
                    Logger.v(TAG, String.format("获取缓存, action = %s", actionSetting.getValue()));
                }
            }
        }



        return null;
    }

    @Override
    public <T> T doPost(HttpConfig config, Setting action, Params params, Class<T> responseCls, Object requestObj) {
        return null;
    }

    @Override
    public <T> T uploadFile(HttpConfig config, Setting action, Params params, Map<String, byte[]> entityMap,
            File[] files, Params headers, Class<T> responseClass) {
        return null;
    }

    public static String getSettingValue(Setting setting, String type) {
        if (setting.getExtras().get(type) != null) return setting.getExtras().get(type).getValue();
        return null;
    }
}
