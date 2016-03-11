package com.example.fanyangsz.oschina.view.CircleView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.TweetAdapter;

/**
 * Created by fanyang.sz on 2016/2/23.
 */
public class CircleContentOneFragment extends Fragment implements HttpSDK.OnTweetCallBack,
        RefreshListView.OnRefreshListener {

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

        new HttpSDK().getTweet(getActivity(), this, 2);

        return view;
    }

    @Override
    public void onError() {
        listView = null;
        Log.e("TweetBeans", "ERROR");
        loadingView.setVisibility(View.GONE);
        failView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(TweetBeans.TweetList datas) {
        if(datas != null){
            if(currentPage == 0){
                listView.hideHeaderView();
                currentTweet = datas;
                myAdapter = new TweetAdapter(currentTweet,getActivity());
                listView.setAdapter(myAdapter);
            }else{
                listView.hideFooterView();
                currentTweet.getTweet().addAll(datas.getTweet());
                myAdapter.notifyDataSetChanged();
            }
            loadingView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDownPullRefresh() {
        currentPage = 0;
        new HttpSDK().getTweet(getActivity(),this,currentPage);
    }

    @Override
    public void onLoadingMore() {
        currentPage++;
        new HttpSDK().getTweet(getActivity(),this,currentPage);
    }
}
