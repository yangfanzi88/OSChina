package com.example.fanyangsz.oschina.Support.Cache;

/**
 * Created by fanyang.sz on 2016/7/12.
 */

public class CacheConfig {

    public enum CacheMode {
        /**
         * 有缓存且有效，返回缓存<br/>
         * 有缓存但无效，拉取服务数据，如果拉取失败，仍然返回无效缓存<br/>
         * 没有缓存，拉取服务数据
         */
        auto,
        /**
         * 在{@link #auto}的基础上，不管存不存在缓存，都拉取服务数据更新缓存
         */
        servicePriority,


        //从其他项目移植过来，后面两种没有用到
        /**
         * 每次拉取数据，都优先拉取缓存
         */
        cachePriority,
        /**
         * 只拉取服务数据
         */
        disable
    }

    // 所有模块缓存默认刷新时间
    public static final long REFRESH_INTERVAL = 3 * 60 * 1000;

    /**
     *sharedprefence要用到的字段
     */
    //登陆表名、键名
    public static String SHARED_USER_LOGIN = "user";
    public static String KEY_USER_LOGIN = "loginStatus";
    //新闻资讯的表名、键名。动弹的表名、键名。博客的表名、键名。
    public static String SHARED_PAGE = "page";
    public static String KEY_NEWS_PAGE = "news_page";
    public static String KEY_HOT_NEWS_PAGE = "hot_news_page";
    public static String KEY_TWEET_PAGE = "tweet_page";
    public static String KEY_HOT_TWEET_PAGE = "hot_tweet_page";
    public static String KEY_BLOG_PAGE = "tweet_page";
    public static String KEY_HOT_BLOG_PAGE = "hot_tweet_page";
    /**
     *文件保存要用到字段
     */
    public static String FILE_NEWS = "news";//资讯和热点文件名不一样：newsnull和newsweek
    public static String FILE_BLOG = "blog";//博客和推荐用到的文件名不一样
    public static String FILE_TWEET = "tweet";//动弹文件名
}
