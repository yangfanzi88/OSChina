package com.example.fanyangsz.oschina.view.NewsView;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.NewsBean;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.NewsAdapter;

/**
 * Created by fanyang.sz on 2015/12/31.
 */
public class NewsContentOneFragment extends Fragment implements HttpSDK.OnNewsCallback,
        RefreshListView.OnRefreshListener{

    private RefreshListView listView;

    NewsAdapter myAdapter;
    NewsBeans.NewsList currentNews = new NewsBeans.NewsList();
    private int currentPage = 0;

    View view,loadingView,failView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("yfyf","onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("yfyf", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("yfyf", "onCreateView");

        view = inflater.inflate(R.layout.layout_content, container, false);
        listView = (RefreshListView)view.findViewById(R.id.listView_one);
        loadingView = view.findViewById(R.id.loading);
        failView = view.findViewById(R.id.fail);

        failView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE,Context.MODE_PRIVATE);
        currentPage = sharedPreferences.getInt(CacheConfig.KEY_NEWS_PAGE,0);
        requestNews(currentPage, CacheConfig.CacheMode.auto);

        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0 && position < listView.getAdapter().getCount()-1) {
                    NewsBean bean = myAdapter.getDatas().getNews().get(position-1);
                    String url = bean.getUrl();
                    String title = bean.getTitle();

                    Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                    if (TextUtils.isEmpty(url)) {
                        url = HttpSDK.NEWS_URL + bean.getId();
                        intent.putExtra("hideHead",true);
                    }else {
                        intent.putExtra("hideHead",false);
                    }
                    intent.putExtra("urlWebView", url);
                    intent.putExtra("newsTitle",title);
                    Log.e("webview",url);
                    getActivity().startActivity(intent);

                    bean.setHaveRead(true);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("yfyf", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("yfyf", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("yfyf", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("yfyf", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("yfyf", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("yfyf", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("yfyf", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("yfyf", "onDetach");
    }

    private void requestNews(int currentPage, Enum cacheMode){
        HttpSDK.newInstance().getInfoNews( this, currentPage, cacheMode);
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
                requestNews(currentPage, CacheConfig.CacheMode.auto);
            }
        });
    }

    @Override
    public void onSuccess(NewsBeans.NewsList news) {
        if(news != null)
        {
            if(currentPage == 0){
                listView.hideHeaderView();
                currentNews = news;
                myAdapter = new NewsAdapter(currentNews,getActivity());
                listView.setAdapter(myAdapter);
//                listState[0] = 0;
//                listState[1] = 0;
            } else{
                listView.hideFooterView();
                if(currentNews != null && currentNews.getNews() != null && !currentNews.getNews().isEmpty()){
                    currentNews.getNews().addAll( news.getNews());
                    myAdapter.notifyDataSetChanged();
                }else{
                    currentNews.setNews(news.getNews());
                    myAdapter = new NewsAdapter(currentNews,getActivity());
                    listView.setAdapter(myAdapter);
                }
            }

            loadingView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDownPullRefresh() {
        currentPage = 0;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CacheConfig.KEY_NEWS_PAGE,currentPage).apply();
//        new HttpSDK().getInfoNews(getActivity(), this, currentPage);
        requestNews(currentPage, CacheConfig.CacheMode.servicePriority);
    }

    @Override
    public void onLoadingMore() {
        currentPage ++;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CacheConfig.KEY_NEWS_PAGE,currentPage).apply();
        requestNews(currentPage, CacheConfig.CacheMode.servicePriority);
//        new HttpSDK().getInfoNews(getActivity(), this, currentPage);
//        listView.hideFooterView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
