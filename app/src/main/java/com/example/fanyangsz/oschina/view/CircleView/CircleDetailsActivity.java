package com.example.fanyangsz.oschina.view.CircleView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.CommentBeans;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.CommentAdapter;

/**
 * Created by fanyang.sz on 2016/6/7.
 */
public class CircleDetailsActivity extends ActionBarActivity implements HttpSDK.onCommentCallBack, RefreshListView.OnRefreshListener {
    ImageView avatarImage;
    TextView userNameText;
    TextView publishTimeText;
    TextView fromText;
//    WebView detailWebView;
    TextView contentTextview;
    ImageView detailImage;
    ImageView likeImageView;
    TextView commentCountTextView;
    TextView likeUsersTextView;
    RefreshListView commentListView;
    TextView noCommentTextView;

    View loading,fail,content;
    FrameLayout frameLayout;

    Intent intent;
    TweetBean tweetBean;
    int id;

    private int currentPage = 0;
    CommentAdapter myAdapter;
    CommentBeans.CommentList currentComment;
    boolean isAll = false;
    int commentCount = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        tweetBean= (TweetBean) intent.getSerializableExtra("data");
        id = (int) intent.getIntExtra("id",0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(true);// 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
        actionBar.setTitle(R.string.tweet_details);

        setContentView(R.layout.layout_news_detials);
        frameLayout = (FrameLayout)findViewById(R.id.framelayout);
        loading = findViewById(R.id.loading);
        fail = findViewById(R.id.fail);

        content = View.inflate(getBaseContext(),R.layout.layout_tweet_detials,null);
        avatarImage = (ImageView)content.findViewById(R.id.iv_avatar);
        userNameText = (TextView)content.findViewById(R.id.tv_name);
        publishTimeText = (TextView)content.findViewById(R.id.tv_time);
        fromText = (TextView)content.findViewById(R.id.tv_from);
//        detailWebView = (WebView)content.findViewById(R.id.webview);
        contentTextview = (TextView) content.findViewById(R.id.tweet_detail_content);
        detailImage = (ImageView) content.findViewById(R.id.tweet_detail_image);
        likeImageView = (ImageView)content.findViewById(R.id.tv_like_state);
        commentCountTextView = (TextView)content.findViewById(R.id.tv_comment_count);
        likeUsersTextView = (TextView)content.findViewById(R.id.tv_likeusers);
        commentListView = (RefreshListView)content.findViewById(R.id.listView_comment);
        noCommentTextView = (TextView) content.findViewById(R.id.tweet_no_comment);

        commentListView.setOnRefreshListener(this);

        detailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CircleImageActivity.class);
                intent.putExtra("bigUrl", tweetBean.getImgBig());
                startActivity(intent);
            }
        });

        dataBind();
        frameLayout.addView(content, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        /*WebSettings settings = detailWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        initWebView();*/
    }

    void dataBind(){
        HttpSDK.newInstance().getTweetImage(tweetBean.getPortrait(), avatarImage, HttpSDK.IMAGE_TYPE_1);
        userNameText.setText(tweetBean.getAuthor());
        publishTimeText.setText(tweetBean.getPubDate());
        fromText.setText(tweetBean.getAppclient()+"");
        contentTextview.setText(tweetBean.getBody());
        if(!TextUtils.isEmpty(tweetBean.getImgBig())){
            detailImage.setVisibility(View.VISIBLE);
            HttpSDK.newInstance().getTweetImage(tweetBean.getImgSmall(), detailImage, HttpSDK.IMAGE_TYPE_0);
        }
        commentCountTextView.setText(tweetBean.getCommentCount());

        //点赞的人
        if(tweetBean.getLikeCount() == 0){
            likeUsersTextView.setVisibility(View.GONE);
        }
        else{
            likeUsersTextView.setVisibility(View.VISIBLE);
            String s = tweetBean.getLikeUser().get(0).getName()
                    + " 等" + tweetBean.getLikeCount() +"人觉得很赞";

            SpannableStringBuilder builder = new SpannableStringBuilder(s);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(getBaseContext().getResources().getColor(R.color.tab_background));
            builder.setSpan(blueSpan, 0, tweetBean.getLikeUser().get(0).getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            likeUsersTextView.setText(builder);
        }

        //评论列表
        if(!tweetBean.getCommentCount().equals("0")){
            commentCount = Integer.parseInt(tweetBean.getCommentCount());
            noCommentTextView.setVisibility(View.GONE);
            commentListView.setVisibility(View.VISIBLE);
            resquestComments(currentPage);
        }
    }

    void resquestComments(int currentPage){
        HttpSDK.newInstance().getComment(this,id,currentPage, CommentBeans.CATALOG_TWEET);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess(CommentBeans.CommentList datas) {
        if(datas != null && datas.getComments().size() != 0){
            if(currentPage == 0){
                commentListView.hideHeaderView();
                currentComment = datas;
                myAdapter = new CommentAdapter(currentComment,this);
                commentListView.setAdapter(myAdapter);
            }else {
                commentListView.hideFooterView();
                currentComment.getComments().addAll(datas.getComments());
                myAdapter.notifyDataSetChanged();
            }
            if(currentComment.getComments().size() >= commentCount)
                isAll = true;
        }

    }

    @Override
    public void onDownPullRefresh() {
        currentPage = 0;
        resquestComments(currentPage);
    }

    @Override
    public void onLoadingMore() {
        if(isAll){
            commentListView.hideFooterView();
        }else {
            currentPage ++;
            resquestComments(currentPage);
        }
    }
}
