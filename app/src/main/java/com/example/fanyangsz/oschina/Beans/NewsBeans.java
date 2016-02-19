package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by fanyang.sz on 2016/1/4.
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class NewsBeans extends BaseBean {

    public final static String PREF_READED_NEWS_LIST = "readed_news_list.pref";

    public final static int CATALOG_ALL = 1;
    public final static int CATALOG_INTEGRATION = 2;
    public final static int CATALOG_SOFTWARE = 3;

    public final static int CATALOG_WEEK = 4;
    public final static int CATALOG_MONTH = 5;

    @XStreamAlias("catalog")
    private int catalog;

    @XStreamAlias("pagesize")
    private int pageSize;

    @XStreamAlias("newscount")
    private int newsCount;

    @XStreamAlias("newslist")
    private NewsList newslist;

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }

    public NewsList getNewslist() {
        return newslist;
    }

    public void setNewslist(NewsList newslist) {
        this.newslist = newslist;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    @SuppressWarnings("serial")
    @XStreamAlias("oschina")
    public static class NewsList{
        @XStreamAlias("newslist")
        private List<NewsBean> news;

        public List<NewsBean> getNews() {
            return news;
        }

        public void setNews(List<NewsBean> news) {
            this.news = news;
        }
    }
}
