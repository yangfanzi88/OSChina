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
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/6/7.
 */
public class CircleDetailsActivity extends ActionBarActivity {
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

    View loading,fail,content;
    FrameLayout frameLayout;

    WebViewClient mWebViewClient;
    Intent intent;
    TweetBean tweetBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        tweetBean= (TweetBean) intent.getSerializableExtra("data");
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
        new HttpSDK().getAvatarImage(getApplicationContext(), tweetBean.getPortrait(), avatarImage);
        userNameText.setText(tweetBean.getAuthor());
        publishTimeText.setText(tweetBean.getPubDate());
        fromText.setText(tweetBean.getAppclient()+"");
        contentTextview.setText(tweetBean.getBody());
        if(!TextUtils.isEmpty(tweetBean.getImgBig())){
            detailImage.setVisibility(View.VISIBLE);
            new HttpSDK().getTweetImage(getApplicationContext(), tweetBean.getImgSmall(), detailImage);
        }
        commentCountTextView.setText(tweetBean.getCommentCount());

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
    }

    /*private void initWebView(){

        detailWebView.loadUrl(HttpSDK.TWEET_URL+tweetBean.getId());

        mWebViewClient = new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loading.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                fail.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                loading.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                fail.setVisibility(View.GONE);

            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loading.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
                fail.setVisibility(View.VISIBLE);
            }
        };

        detailWebView.setWebViewClient(mWebViewClient);
    }*/
}
