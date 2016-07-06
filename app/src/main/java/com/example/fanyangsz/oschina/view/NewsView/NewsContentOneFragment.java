package com.example.fanyangsz.oschina.view.NewsView;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.NewsBean;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.RefreshListView.RefreshListView;
import com.example.fanyangsz.oschina.adapter.NewsAdapter;

/**
 * Created by fanyang.sz on 2015/12/31.
 */
public class NewsContentOneFragment extends Fragment implements HttpSDK.OnNewsCallback,
        RefreshListView.OnRefreshListener{

    private RefreshListView listView;

    NewsAdapter myAdapter;
    NewsBeans.NewsList currentNews;
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

        requestNews(currentPage);

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

    private void requestNews(int currentPage){
        HttpSDK.newInstance().getInfoNews( this, currentPage);
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
        if(news != null)
        {
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

    }

    @Override
    public void onDownPullRefresh() {
        currentPage = 0;
//        new HttpSDK().getInfoNews(getActivity(), this, currentPage);
        requestNews(currentPage);
    }

    @Override
    public void onLoadingMore() {
        currentPage ++;
        requestNews(currentPage);
//        new HttpSDK().getInfoNews(getActivity(), this, currentPage);
//        listView.hideFooterView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*public class NewsItemView extends {

        ImageView todayIcon;
        TextView newsTitle;
        TextView content;
        TextView source;
        TextView time;
        TextView comment;
        public NewsItemView(){}
        @Override
        public void bindingData(View convertView, Serializable data) {
            newsTitle.setText(((NewsBean) data).getTitle());
            content.setText(((NewsBean) data).getBody());
            source.setText(((NewsBean) data).getAuthor());
            time.setText(((NewsBean) data).getPubDate());
            comment.setText(((NewsBean) data).getCommentCount()+"");
        }

        @Override
        public void bindingView(View convertView) {
            View view = View.inflate(getContext(), R.layout.list_item_news, null);
            todayIcon = (ImageView) view.findViewById(R.id.iv_tip);
            newsTitle = (TextView) view.findViewById(R.id.tv_title);
            content = (TextView) view.findViewById(R.id.tv_description);
            source = (TextView) view.findViewById(R.id.tv_source);
            time = (TextView) view.findViewById(R.id.tv_time);
            comment = (TextView) view.findViewById(R.id.tv_comment_count);
        }

        @Override
        public int inflateViewId() {
            return R.layout.list_item_news;
        }
    }*/

}
