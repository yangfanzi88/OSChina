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
import android.widget.AdapterView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.BlogBean;
import com.example.fanyangsz.oschina.Beans.BlogBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.BlogsAdapter;

/**
 * Created by fanyang.sz on 2016/1/8.
 */
public class NewsContentThreeFragment extends Fragment implements HttpSDK.onBlogCallBack, RefreshListView.OnRefreshListener{
    private RefreshListView listView;

    BlogsAdapter myAdapter;
    BlogBeans.Blogs currentBlogs = new BlogBeans.Blogs();
    private int currentPage = 0;

    View view,loadingView,failView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_content,container,false);
        listView = (RefreshListView)view.findViewById(R.id.listView_one);
        loadingView = view.findViewById(R.id.loading);
        failView = view.findViewById(R.id.fail);

        failView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE, Context.MODE_PRIVATE);
        currentPage = sharedPreferences.getInt(CacheConfig.KEY_BLOG_PAGE,0);
        requestBlogs(currentPage, CacheConfig.CacheMode.auto);

        listView.setOnRefreshListener(this);
        /*listView.setAdapter(new ArrayAdapter<String>(
                container.getContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        "123", "b", "c", "d", "e", "f", "g", "h", "i","j","k","l","m","n"
                }));*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0 && position < listView.getAdapter().getCount()-1) {
                    BlogBean bean = myAdapter.getDatas().getBlog().get(position-1);
                    String url = bean.getUrl();
                    String title = bean.getTitle();
                    if (TextUtils.isEmpty(url)) {
                        url = HttpSDK.NEWS_URL + bean.getId();
                    }
                    Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                    intent.putExtra("urlWebView", url);
                    intent.putExtra("newsTitle",title);
                    Log.e("webview",url);
                    getActivity().startActivity(intent);
                }else {
                    return;
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void requestBlogs(int currentPage, Enum cacheMode){
        HttpSDK.newInstance().getBlog(this, currentPage, BlogBeans.CATALOG_LATEST, cacheMode);
    }

    @Override
    public void onError() {
        Log.v("Blog", "ERROR");
        loadingView.setVisibility(View.GONE);
        failView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.error_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBlogs(currentPage,CacheConfig.CacheMode.auto);
            }
        });
    }

    @Override
    public void onSuccess(BlogBeans.Blogs blogs) {
        if(currentPage == 0){
            listView.hideHeaderView();
            currentBlogs = blogs;
            myAdapter = new BlogsAdapter(currentBlogs,getActivity());
            listView.setAdapter(myAdapter);
        } else{
            listView.hideFooterView();
            if(currentBlogs!=null&&currentBlogs.getBlog()!=null){
                currentBlogs.getBlog().addAll( blogs.getBlog());
                myAdapter.notifyDataSetChanged();
            }else{
                currentBlogs.setBlog(blogs.getBlog());
                myAdapter =  new BlogsAdapter(currentBlogs,getActivity());
                listView.setAdapter(myAdapter);
            }
        }
        loadingView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownPullRefresh() {
        currentPage = 0;
        requestBlogs(currentPage,CacheConfig.CacheMode.servicePriority);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CacheConfig.KEY_BLOG_PAGE,currentPage).apply();
    }

    @Override
    public void onLoadingMore() {
        currentPage ++;
        requestBlogs(currentPage,CacheConfig.CacheMode.servicePriority);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CacheConfig.KEY_BLOG_PAGE,currentPage).apply();
    }
}
