package com.example.fanyangsz.oschina.view.CircleView;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.TweetAdapter;

/**
 * Created by fanyang.sz on 2016/2/23.
 */
public class CircleContentOneFragment extends Fragment implements HttpSDK.OnTweetCallBack,
        RefreshListView.OnRefreshListener {

    private RefreshListView listView;
    private TweetAdapter myAdapter;
    private TweetBeans.TweetList currentTweet = new TweetBeans.TweetList();
    private int currentPage = 0;
    private View view, loadingView, failView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_content, container, false);
        listView = (RefreshListView) view.findViewById(R.id.listView_one);
        loadingView = view.findViewById(R.id.loading);
        failView = view.findViewById(R.id.fail);

        failView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE, Context.MODE_PRIVATE);
        currentPage = sharedPreferences.getInt(CacheConfig.KEY_TWEET_PAGE,0);
        requestTweets(currentPage, CacheConfig.CacheMode.auto);
        listView.setOnRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position < listView.getAdapter().getCount() - 1) {
                    TweetBean bean = myAdapter.getDatas().getTweet().get(position - 1);
                    /*String url = bean.getUrl();
                    String title = bean.getTitle();
                    if (TextUtils.isEmpty(url)) {
                        url = HttpSDK.NEWS_URL + bean.getId();
                    }*/
                    Intent intent = new Intent(getActivity(), CircleDetailsActivity.class);
//                    intent.putExtra("urlWebView", url);
//                    intent.putExtra("newsTitle",title);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", bean);
                    intent.putExtras(bundle);
                    intent.putExtra("id",bean.getId());
                    getActivity().startActivity(intent);
                } else {
                    return;
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentTweet = null;
    }

    private void requestTweets(int currentPage, Enum cacheMode) {
        HttpSDK.newInstance().getTweet(this, currentPage, TweetBean.NORMAL_TWEET, cacheMode);
    }

    @Override
    public void onError() {
//        listView = null;
        Log.e("TweetBeans", "ERROR");
        loadingView.setVisibility(View.GONE);
        failView.setVisibility(View.VISIBLE);
        if (currentPage == 0) {
            listView.hideHeaderView();
        } else {
            listView.hideFooterView();
        }

        view.findViewById(R.id.error_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestTweets(currentPage, CacheConfig.CacheMode.auto);
            }
        });
    }

    @Override
    public void onSuccess(TweetBeans.TweetList tweets) {
        if (tweets != null) {
            if (currentPage == 0) {
                listView.hideHeaderView();
                if(currentTweet != null && currentTweet.getTweet() != null){
                    if(!currentTweet.getTweet().get(0).getPubDate().equals(tweets.getTweet().get(0).getPubDate())){
                        currentTweet = tweets;
                        myAdapter = new TweetAdapter(currentTweet, getActivity());
                        listView.setAdapter(myAdapter);
                    }
                }else{
                    currentTweet = tweets;
                    myAdapter = new TweetAdapter(currentTweet, getActivity());
                    listView.setAdapter(myAdapter);
                }

            } else {
                listView.hideFooterView();
                if(currentTweet!=null&&currentTweet.getTweet()!=null){
                    currentTweet.getTweet().addAll( tweets.getTweet());
                    myAdapter.notifyDataSetChanged();
                }else{
                    currentTweet.setTweet(tweets.getTweet());
                    myAdapter =  new TweetAdapter(currentTweet,getActivity());
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
        requestTweets(currentPage, CacheConfig.CacheMode.servicePriority);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CacheConfig.KEY_TWEET_PAGE,currentPage).apply();
    }

    @Override
    public void onLoadingMore() {
        currentPage++;
        requestTweets(currentPage, CacheConfig.CacheMode.servicePriority);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CacheConfig.SHARED_PAGE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CacheConfig.KEY_TWEET_PAGE,currentPage).apply();
    }
}
