package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by fanyang.sz on 2016/5/31.
 */

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class BlogBeans extends BaseBean {
    public final static String PREF_READED_BLOG_LIST = "readed_blog_list.pref";

    public static final String CATALOG_LATEST = "latest";
    public static final String  CATALOG_RECOMMEND = "recommend";

    @XStreamAlias("pagesize")
    private int pagesize;

    @XStreamAlias("blogs")
    private Blogs blogs;

    @XStreamAlias("blogsCount")
    private int blogsCount;

    public int getPageSize() {
        return pagesize;
    }

    public void setPageSize(int pageSize) {
        this.pagesize = pageSize;
    }

    public Blogs getBlogs() {
        return blogs;
    }

    public void setBlogs(Blogs blogs) {
        this.blogs = blogs;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getBlogsCount() {
        return blogsCount;
    }

    public void setBlogsCount(int blogsCount) {
        this.blogsCount = blogsCount;
    }

    @SuppressWarnings("serial")
    @XStreamAlias("oschina")
    public static class Blogs{
        @XStreamAlias("blogs")
        private List<BlogBean> blog;

        public List<BlogBean> getBlog() {
            return blog;
        }

        public void setBlog(List<BlogBean> blog) {
            this.blog = blog;
        }
    }
}
