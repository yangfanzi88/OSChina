package com.example.fanyangsz.oschina.view.CircleView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.TweetAdapter;

/**
 * Created by fanyang.sz on 2016/6/14.
 */
public class CircleContentTwoFragment extends Fragment implements HttpSDK.OnTweetCallBack,
        RefreshListView.OnRefreshListener  {

    private RefreshListView listView;
    private TweetAdapter myAdapter;
    private TweetBeans.TweetList currentTweet;
    private int currentPage = 0;
    private View view,loadingView,failView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_content, container, false);
        listView = (RefreshListView)view.findViewById(R.id.listView_one);
        loadingView = view.findViewById(R.id.loading);
        failView = view.findViewById(R.id.fail);

        failView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        requestTweets(currentPage);
        listView.setOnRefreshListener(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void requestTweets(int currentPage){
        new HttpSDK().getTweet(getActivity(), this, currentPage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess(TweetBeans.TweetList datas) {

    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {

    }
}
