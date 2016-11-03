package com.example.fanyangsz.oschina.view.CircleView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.User;
import com.example.fanyangsz.oschina.Beans.UserInformation;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.Support.util.UIHelper;
import com.example.fanyangsz.oschina.adapter.ActiveAdapter;
import com.example.fanyangsz.oschina.widgets.CircleImageView;

import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by fanyang.sz on 2016/7/28.
 */

public class UserCenterActivity extends ActionBarActivity implements View.OnClickListener{

    UserInformation currentInfo = new UserInformation();

    View loading,fail,content;
    FrameLayout frameLayout;
    RefreshListView listView;
    CircleImageView userAvatarImage;
    ImageView userGender;
    TextView userName,userScore,userFollowingCount,userFollowerCount, userLoginTime, leaveMsg, followUser, userBlog, userInfo;
    AlertDialog mInformationDialog;

    ActiveAdapter myAdapter;
    String hisid,hisname;
    private int currentPage = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(true);// 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
        actionBar.setTitle(R.string.actionbar_title_profile);

        setContentView(R.layout.layout_news_detials);
        frameLayout = (FrameLayout)findViewById(R.id.framelayout);
        loading = findViewById(R.id.loading);
        fail = findViewById(R.id.fail);

        frameLayout.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        fail.setVisibility(View.GONE);

        content = View.inflate(getBaseContext(), R.layout.layout_usercenter, null);
        listView = (RefreshListView) content.findViewById(R.id.lv_user_active);
        listView.setOnRefreshListener(onRefreshListener);
        initView();
        frameLayout.addView(content, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Intent intent = getIntent();
        hisid = intent.getStringExtra("hisid");
        hisname = intent.getStringExtra("hisname");

        requestUserInfo(0);
    }
    private void initView(){
        View header = View.inflate(getBaseContext(), R.layout.layout_usercenter_header, null);
        userAvatarImage = (CircleImageView) header.findViewById(R.id.iv_avatar);
        userAvatarImage.setOnClickListener(this);
        userGender = (ImageView) header.findViewById(R.id.iv_gender);
        userName = (TextView) header.findViewById(R.id.tv_name);
        userScore = (TextView) header.findViewById(R.id.tv_score);
        header.findViewById(R.id.ly_following).setOnClickListener(this);
        userFollowingCount = (TextView) header.findViewById(R.id.tv_following_count);
        header.findViewById(R.id.ly_follower).setOnClickListener(this);
        userFollowerCount = (TextView) header.findViewById(R.id.tv_follower_count);
        userLoginTime = (TextView) header.findViewById(R.id.tv_latest_login_time);
        leaveMsg = (TextView)header.findViewById(R.id.tv_private_message);
        leaveMsg.setOnClickListener(this);
        followUser = (TextView) header.findViewById(R.id.tv_follow_user);
        followUser.setOnClickListener(this);
        header.findViewById(R.id.tv_blog).setOnClickListener(this);
        header.findViewById(R.id.tv_information).setOnClickListener(this);


        listView.addHeaderView(header);
//        frameLayout.addView(header, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void bindData(UserInformation data){
        User user = data.getUser();
        HttpSDK.newInstance().getTweetImage(user.getPortrait(),userAvatarImage, 1);
        if(user.getGender().equals("1")){
            userGender.setImageResource(R.drawable.userinfo_icon_male);
        }else{
            userGender.setImageResource(R.drawable.userinfo_icon_female);
        }
        userName.setText(user.getName());
        userScore.setText(String.valueOf(user.getScore()));
        userFollowingCount.setText(String.valueOf(user.getFollowers()));
        userFollowerCount.setText(String.valueOf(user.getFans()));
        userLoginTime.setText(getString(R.string.latest_login_time,StringUtils.friendlyTime(user.getLatestonline())));
    }

    private void requestUserInfo(int currentPage){
        HttpSDK.newInstance().getUserCenter(hisid, hisname, currentPage, onSuccess, onError);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_avatar:
                Intent intent = new Intent(this, CircleImageActivity.class);
                intent.putExtra("bigUrl", UIHelper.getLargeAvatar(currentInfo.getUser().getPortrait()));
                this.startActivity(intent);
                break;
            case R.id.ly_following:
                break;
            case R.id.ly_follower:
                break;
            case R.id.tv_private_message:
                break;
            case R.id.tv_follow_user:
                break;
            case R.id.tv_blog:

                break;
            case R.id.tv_information:
                if (mInformationDialog == null) {
                    mInformationDialog = new AlertDialog.Builder(this).show();
                    View view = LayoutInflater.from(this).inflate(
                            R.layout.layout_usercenter_information, null);
                    ((TextView) view.findViewById(R.id.tv_join_time)).setText(StringUtils.friendlyTime(currentInfo.getUser().getJointime()));
                    ((TextView) view.findViewById(R.id.tv_location)).setText(currentInfo.getUser().getFrom());
                    ((TextView) view.findViewById(R.id.tv_development_platform)).setText(currentInfo.getUser().getDevplatform());
                    ((TextView) view.findViewById(R.id.tv_academic_focus)).setText(currentInfo.getUser().getExpertise());
                    mInformationDialog.setContentView(view);
                }

                mInformationDialog.show();
                break;
        }
    }

    private Response.Listener onSuccess = new Response.Listener() {
        @Override
        public void onResponse(Object o) {
            UserInformation userInformation = (UserInformation)o;
            if(userInformation != null && (userInformation.getUser() != null || !userInformation.getActiveList().isEmpty())){
                frameLayout.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                listView.hideFooterView();

                bindData(userInformation);
                if(currentInfo != null && currentInfo.getActiveList() != null){
                    currentInfo.getActiveList().addAll(userInformation.getActiveList());
                    myAdapter.notifyDataSetChanged();
                }else {
                    currentInfo = userInformation;
                    myAdapter = new ActiveAdapter(getBaseContext(), currentInfo);
                    listView.setAdapter(myAdapter);
                }

            }
        }
    };

    private Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            frameLayout.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            fail.setVisibility(View.VISIBLE);
        }
    };

    RefreshListView.OnRefreshListener onRefreshListener = new RefreshListView.OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            listView.hideHeaderView();
        }

        @Override
        public void onLoadingMore() {
            currentPage ++;
            requestUserInfo(currentPage);
        }
    };
}
