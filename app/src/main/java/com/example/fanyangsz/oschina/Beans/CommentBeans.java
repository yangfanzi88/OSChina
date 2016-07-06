package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fanyang.sz on 2016/6/22.
 */

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class CommentBeans extends BaseBean{
    public final static int CATALOG_NEWS = 1;
    public final static int CATALOG_POST = 2;
    public final static int CATALOG_TWEET = 3;
    public final static int CATALOG_ACTIVE = 4;
    public final static int CATALOG_MESSAGE = 4;// 动态与留言都属于消息中心

    @XStreamAlias("pagesize")
    private int pageSize;
    @XStreamAlias("allCount")
    private int allCount;
    @XStreamAlias("comments")
    private CommentList commentslist;

    public int getPageSize() {
        return pageSize;
    }

    public int getAllCount() {
        return allCount;
    }

    public CommentList getCommentslist() {
        return commentslist;
    }

    public void setCommentslist(CommentList commentslist) {
        this.commentslist = commentslist;
    }

    @SuppressWarnings("serial")
    @XStreamAlias("oschina")
    public static class CommentList{
        @XStreamAlias("comments")
        private List<CommentBean> comments;

        public List<CommentBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentBean> comments) {
            this.comments = comments;
        }
    }



    public void sortList() {
        Collections.sort(commentslist.comments, new Comparator<CommentBean>() {

            @Override
            public int compare(CommentBean lhs, CommentBean rhs) {
                return lhs.getPubDate().compareTo(rhs.getPubDate());
            }
        });
    }
}
