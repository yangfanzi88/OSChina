package com.example.fanyangsz.oschina.Api.cache;



import com.example.fanyangsz.oschina.Api.http.Params;
import com.example.fanyangsz.oschina.Api.setting.Setting;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存接口
 *
 * @author wangdan
 */
public interface ICacheUtility {

    int CORE_POOL_SIZE = 5;
    int MAXIMUM_POOL_SIZE = 128;
    int KEEP_ALIVE = 1;

    ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);

    Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory);

    <T> Cache<T> findCacheData(Setting action, Params params, Class<T> responseCls);

    void addCacheData(Setting action, Params params, Object responseObj);

    class Cache<T> {

        private T t;

        // true-缓存到期
        private boolean expired;

        public Cache() {

        }

        public Cache(T t, boolean expired) {
            this.t = t;
            this.expired = expired;
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public boolean expired() {
            return expired;
        }

        public void setExpired(boolean expired) {
            this.expired = expired;
        }

    }

}
