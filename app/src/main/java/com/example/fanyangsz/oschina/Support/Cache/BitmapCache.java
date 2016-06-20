package com.example.fanyangsz.oschina.Support.Cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by fanyang.sz on 2016/6/6.
 */
public class BitmapCache implements ImageLoader.ImageCache {
    //LruCache对象
    private LruCache<String, Bitmap> lruCache;
    //设置最大缓存为10Mb，大于这个值会启动自动回收
    private int max = 10*1024*1024;

    public BitmapCache(){
        //初始化LruCache
        lruCache = new LruCache<String, Bitmap>(max){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

    }

    @Override
    public Bitmap getBitmap(String s) {
        return lruCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        lruCache.put(s, bitmap);
    }
}
