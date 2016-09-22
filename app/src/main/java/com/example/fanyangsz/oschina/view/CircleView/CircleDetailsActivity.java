package com.example.fanyangsz.oschina.view.CircleView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.CommentBeans;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.CommentAdapter;
import com.example.fanyangsz.oschina.widgets.RefreshLayout;

/**
 * Created by fanyang.sz on 2016/6/7.
 */
public class CircleDetailsActivity extends ActionBarActivity implements HttpSDK.onCommentCallBack,
        RefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
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
    RefreshLayout refreshLayout;
    ListView commentListView;
    TextView noCommentTextView;

    View loading, fail, content;
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
        tweetBean = (TweetBean) intent.getSerializableExtra("data");
        id = (int) intent.getIntExtra("id", 0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(true);// 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
        actionBar.setTitle(R.string.tweet_details);

        setContentView(R.layout.layout_news_detials);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        loading = findViewById(R.id.loading);
        fail = findViewById(R.id.fail);

        content = View.inflate(getBaseContext(), R.layout.layout_tweet_detials, null);
        avatarImage = (ImageView) content.findViewById(R.id.iv_avatar);
        userNameText = (TextView) content.findViewById(R.id.tv_name);
        publishTimeText = (TextView) content.findViewById(R.id.tv_time);
        fromText = (TextView) content.findViewById(R.id.tv_from);
//        detailWebView = (WebView)content.findViewById(R.id.webview);
        contentTextview = (TextView) content.findViewById(R.id.tweet_detail_content);
        detailImage = (ImageView) content.findViewById(R.id.tweet_detail_image);
        likeImageView = (ImageView) content.findViewById(R.id.tv_like_state);
        commentCountTextView = (TextView) content.findViewById(R.id.tv_comment_count);
        likeUsersTextView = (TextView) content.findViewById(R.id.tv_likeusers);
        refreshLayout = (RefreshLayout) content.findViewById(R.id.swipRefresh);
        commentListView = (ListView) content.findViewById(R.id.listView_comment);
        noCommentTextView = (TextView) content.findViewById(R.id.tweet_no_comment);

//        commentListView.setOnRefreshListener(this);

    /*    //改变加载显示的颜色
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);
        //设置背景颜色
        swipeRefreshLayout.setBackgroundColor(Color.YELLOW);
        //设置初始时的大小
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //设置监听
        swipeRefreshLayout.setOnRefreshListener(this);
        //设置向下拉多少出现刷新
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        //设置刷新出现的位置
        swipeRefreshLayout.setProgressViewEndTarget(false, 200);*/
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
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
//        testWindow();
    }

    //以下代码将一个button添加到屏幕坐标为(100, 300)的位置上。
    /*void testWindow() {
        final Button mButton = new Button(this);
        mButton.setText("YFYF你的手机出现了bug");
        mButton.setBackgroundColor(Color.parseColor("#00000000"));
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0, PixelFormat.TRANSPARENT);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;
        final WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        manager.addView(mButton, layoutParams);

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = rawX - mButton.getMeasuredWidth() / 2;
                        layoutParams.y = rawY - mButton.getMeasuredHeight() / 2;
                        manager.updateViewLayout(mButton, layoutParams);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }*/

    void dataBind() {
        HttpSDK.newInstance().getTweetImage(tweetBean.getPortrait(), avatarImage, HttpSDK.IMAGE_TYPE_1);
        userNameText.setText(tweetBean.getAuthor());
        publishTimeText.setText(tweetBean.getPubDate());
        fromText.setText(tweetBean.getAppclient() + "");
        contentTextview.setText(tweetBean.getBody());
        if (!TextUtils.isEmpty(tweetBean.getImgBig())) {
            detailImage.setVisibility(View.VISIBLE);
            HttpSDK.newInstance().getTweetImage(tweetBean.getImgSmall(), detailImage, HttpSDK.IMAGE_TYPE_0);
        }
        commentCountTextView.setText(tweetBean.getCommentCount());

        //点赞的人
        if (tweetBean.getLikeCount() == 0) {
            likeUsersTextView.setVisibility(View.GONE);
        } else {
            likeUsersTextView.setVisibility(View.VISIBLE);
            String s = tweetBean.getLikeUser().get(0).getName()
                    + " 等" + tweetBean.getLikeCount() + "人觉得很赞";

            SpannableStringBuilder builder = new SpannableStringBuilder(s);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(getBaseContext().getResources().getColor(R.color.tab_background));
            builder.setSpan(blueSpan, 0, tweetBean.getLikeUser().get(0).getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            likeUsersTextView.setText(builder);
        }

        //评论列表
        if (!tweetBean.getCommentCount().equals("0")) {
            commentCount = Integer.parseInt(tweetBean.getCommentCount());
            noCommentTextView.setVisibility(View.GONE);
            commentListView.setVisibility(View.VISIBLE);
            resquestComments(currentPage);
        }
    }

    void resquestComments(int currentPage) {
        HttpSDK.newInstance().getComment(this, id, currentPage, CommentBeans.CATALOG_TWEET);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess(CommentBeans.CommentList datas) {
        if (currentPage == 0)
            refreshLayout.setRefreshing(false);
        /*else
            refreshLayout.setLoading(false);*/
        if (datas != null && datas.getComments().size() != 0) {
            if (currentPage == 0) {
//                commentListView.hideHeaderView();

                currentComment = datas;
                myAdapter = new CommentAdapter(currentComment, this);
                commentListView.setAdapter(myAdapter);
            } else {
//                commentListView.hideFooterView();

                currentComment.getComments().addAll(datas.getComments());
                myAdapter.notifyDataSetChanged();

                Toast.makeText(getBaseContext(), String.format("更新了%1$s条数据", datas.getComments().size()), Toast.LENGTH_SHORT).show();
            }
            if (currentComment.getComments().size() >= commentCount){
                isAll = true;
                refreshLayout.setLoading(false);
            }


        }

    }

    /*@Override
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
    }*/

    @Override
    public void onRefresh() {
        currentPage = 0;
        resquestComments(currentPage);
    }

    @Override
    public void onLoad() {
        if (isAll) {
            refreshLayout.setLoading(false);
        } else {
            currentPage++;
            resquestComments(currentPage);
        }
    }
}
