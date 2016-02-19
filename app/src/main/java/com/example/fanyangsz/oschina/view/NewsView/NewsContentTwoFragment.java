package com.example.fanyangsz.oschina.view.NewsView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.NewsAdapter;

/**
 * Created by fanyang.sz on 2015/12/31.
 */
public class NewsContentTwoFragment extends Fragment implements HttpSDK.OnNewsCallback,
        RefreshListView.OnRefreshListener {

    private RefreshListView listView;
    RequestQueue mQueue;
    NewsAdapter myAdapter;
    NewsBeans.NewsList currentNews;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mQueue = Volley.newRequestQueue(getActivity());
        View view = inflater.inflate(R.layout.layout_content,container,false);
        listView = (RefreshListView)view.findViewById(R.id.listView_one);
        listView.setVisibility(View.GONE);
        new HttpSDK().getNews(getActivity(), this, currentPage , 3);

        listView.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onError() {
        listView = null;
        Log.e("NewsBeans", "ERROR");
    }

    @Override
    public void onSuccess(NewsBeans.NewsList news) {
        if(currentPage == 0){
            listView.hideHeaderView();
            currentNews = news;
            myAdapter = new NewsAdapter(currentNews,getActivity());
            listView.setAdapter(myAdapter);
        } else{
            listView.hideFooterView();
            currentNews.getNews().addAll( news.getNews());
            myAdapter.notifyDataSetChanged();
        }
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownPullRefresh() {
        currentPage = 0;
        new HttpSDK().getNews(getActivity(), this, currentPage, 3);
    }

    @Override
    public void onLoadingMore() {
        currentPage ++;
        new HttpSDK().getNews(getActivity(), this, currentPage, 3);
    }
}
