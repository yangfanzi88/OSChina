package com.example.fanyangsz.oschina.view.NewsView;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.NewsBean;
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

    View view,loadingView,failView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mQueue = Volley.newRequestQueue(getActivity());
        view = inflater.inflate(R.layout.layout_content,container,false);
        listView = (RefreshListView)view.findViewById(R.id.listView_one);
        loadingView = view.findViewById(R.id.loading);
        failView = view.findViewById(R.id.fail);

        failView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        requestNews(currentPage);

        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < listView.getAdapter().getCount()) {
                    NewsBean bean = myAdapter.getDatas().getNews().get(position-1);
                    String url = bean.getUrl();
                    String title = bean.getTitle();
                    if (TextUtils.isEmpty(url)) {
                        url = HttpSDK.NEWS_URL + bean.getId();
                    }
                    Intent intent = new Intent(getActivity(), NewsDetialsActivity.class);
                    intent.putExtra("urlWebView", url);
                    intent.putExtra("newsTitle",title);
                    Log.e("webview",url);
                    getActivity().startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void requestNews(int currentPage){
        new HttpSDK().getHotNews(getActivity(), this, currentPage );
    }

    @Override
    public void onError() {
//        listView = null;
        Log.e("NewsBeans", "ERROR");
        loadingView.setVisibility(View.GONE);
        failView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.error_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNews(currentPage);
            }
        });
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
        loadingView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownPullRefresh() {
        currentPage = 0;
        requestNews(currentPage);
//        new HttpSDK().getHotNews(getActivity(), this, currentPage);
    }

    @Override
    public void onLoadingMore() {
        currentPage ++;
//        requestNews(currentPage);
//        new HttpSDK().getHotNews(getActivity(), this, currentPage);
    }
}
