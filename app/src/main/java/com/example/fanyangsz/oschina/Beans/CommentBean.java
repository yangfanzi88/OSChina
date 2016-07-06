package com.example.fanyangsz.oschina.Beans;

import android.os.Parcel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论实体类
 * Created by fanyang.sz on 2016/6/22.
 */

@SuppressWarnings("serial")
@XStreamAlias("comment")
public class CommentBean extends BaseBean implements Serializable {
    @XStreamAlias("portrait")
    private String portrait;

    @XStreamAlias("content")
    private String content;

    @XStreamAlias("author")
    private String author;

    @XStreamAlias("authorid")
    private int authorId;

    @XStreamAlias("pubDate")
    private String pubDate;

    @XStreamAlias("appclient")
    private int appClient;

    @XStreamAlias("replies")
    private List<Reply> replies = new ArrayList<Reply>();

    @XStreamAlias("refers")
    private List<Refer> refers = new ArrayList<Refer>();


    public int getAppClient() {
        return appClient;
    }

    public void setAppClient(int appClient) {
        this.appClient = appClient;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public List<Refer> getRefers() {
        return refers;
    }

    public void setRefers(List<Refer> refers) {
        this.refers = refers;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    @XStreamAlias("reply")
    public static class Reply implements Serializable{
        @XStreamAlias("rauthor")
        public String rauthor;
        @XStreamAlias("rpubDate")
        public String rpubDate;
        @XStreamAlias("rcontent")
        public String rcontent;

        public Reply() {
        }

        public Reply(Parcel source) {
            rauthor = source.readString();
            rpubDate = source.readString();
            rcontent = source.readString();
        }

        public String getRauthor() {
            return rauthor;
        }
        public void setRauthor(String rauthor) {
            this.rauthor = rauthor;
        }
        public String getRpubDate() {
            return rpubDate;
        }
        public void setRpubDate(String rpubDate) {
            this.rpubDate = rpubDate;
        }
        public String getRcontent() {
            return rcontent;
        }
        public void setRcontent(String rcontent) {
            this.rcontent = rcontent;
        }

        /*@Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(rauthor);
            dest.writeString(rpubDate);
            dest.writeString(rcontent);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<Reply> CREATOR = new Creator<Reply>() {

            @Override
            public Reply[] newArray(int size) {
                return new Reply[size];
            }

            @Override
            public Reply createFromParcel(Parcel source) {
                return new Reply(source);
            }
        };*/

    }

    @XStreamAlias("refer")
    public static class Refer implements Serializable{

        @XStreamAlias("refertitle")
        public String refertitle;
        @XStreamAlias("referbody")
        public String referbody;

        public Refer() {
        }

        public Refer(Parcel source) {
            referbody = source.readString();
            refertitle = source.readString();
        }

        public String getRefertitle() {
            return refertitle;
        }
        public void setRefertitle(String refertitle) {
            this.refertitle = refertitle;
        }
        public String getReferbody() {
            return referbody;
        }
        public void setReferbody(String referbody) {
            this.referbody = referbody;
        }

        /*@Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(referbody);
            dest.writeString(refertitle);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<Refer> CREATOR = new Creator<Comment.Refer>() {

            @Override
            public Refer[] newArray(int size) {
                return new Refer[size];
            }

            @Override
            public Refer createFromParcel(Parcel source) {
                return new Refer(source);
            }
        };*/
    }

    /*public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }

        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }
    };*/
}
