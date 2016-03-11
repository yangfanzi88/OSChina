package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by fanyang.sz on 2016/2/23.
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetBeans extends BaseBean {
    public final static int CATALOG_LATEST = 0;
    public final static int CATALOG_HOT = -1;
    public final static int CATALOG_ME = 1;

    @XStreamAlias("tweetcount")
    private int tweetCount;
    @XStreamAlias("pagesize")
    private int pagesize;
    @XStreamAlias("tweets")
    private TweetList tweetslist;

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public TweetList getTweetslist() {
        return tweetslist;
    }

    public void setTweetslist(TweetList tweetslist) {
        this.tweetslist = tweetslist;
    }

    @SuppressWarnings("serial")
    @XStreamAlias("oschina")
    public static class TweetList{
        @XStreamAlias("tweets")
        private List<TweetBean> tweet;

        public List<TweetBean> getTweet() {
            return tweet;
        }

        public void setTweet(List<TweetBean> tweet) {
            this.tweet = tweet;
        }
    }
}
