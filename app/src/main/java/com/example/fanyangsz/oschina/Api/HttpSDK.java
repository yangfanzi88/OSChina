package com.example.fanyangsz.oschina.Api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.Support.util.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanyang.sz on 2016/1/4.
 */
public class HttpSDK {
    public static String NEWS_URL = "http://m.oschina.net/news/";
    private final String SDK_BASE_URL = "http://www.oschina.net/";
    RequestQueue mQueue;

    public interface OnNewsCallback {

        void onError();

        void onSuccess(NewsBeans.NewsList news);

    }
    public void getInfoNews(Context context, final OnNewsCallback callback,int page){
        getNews(context,callback,page,0,null);
    }
    public void getHotNews(Context context, final OnNewsCallback callback,int page){
        getNews(context,callback,page,0,"week");
    }
    public void getNews(Context context, final OnNewsCallback callback,int page ,int catalog, String show){
        Log.e("123", SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog +"&pageIndex="+ page + "&show=" +show );
        mQueue = Volley.newRequestQueue(context);
        String url = SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog +"&pageIndex="+ page + "&show=" +show ;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("TAG", response);
                        Log.e("123", "success");
                        InputStream is = new ByteArrayInputStream(response.getBytes());
                        NewsBeans.NewsList newsBeans = XmlUtils.toBean(NewsBeans.NewsList.class,is);
                        callback.onSuccess(newsBeans);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Log.e("123","fail");

                        callback.onError();
                    }
                });
        mQueue.add(stringRequest);
        mQueue.start();
    }

    public interface onLoginCallBack{

        void onError();

        void onSuccess(LoginUserBean s);
    }
    public void login(Context context, final String username, final String password, final onLoginCallBack callback){
        mQueue = Volley.newRequestQueue(context);
        String url = SDK_BASE_URL + "action/api/login_validate";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        InputStream is = new ByteArrayInputStream(s.getBytes());
                        LoginUserBean login = XmlUtils.toBean(LoginUserBean.class, is);
                        callback.onSuccess(login);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onError();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("pwd",password);
//                params.put("keep_login", 1);
                return params;
            }
        };
        mQueue.add(stringRequest);
        mQueue.start();
    }

    public interface OnTweetCallBack{
        void onError();

        void onSuccess(TweetBeans.TweetList datas);
    }

    public void getTweet(Context context,final OnTweetCallBack callBack,int page){
        Log.e("123", SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex="+ page );
        String url = SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex="+ page;
        mQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        InputStream is = new ByteArrayInputStream(s.getBytes());
                        TweetBeans.TweetList Tweets = XmlUtils.toBean(TweetBeans.TweetList.class, is);
                        callBack.onSuccess(Tweets);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callBack.onError();
                    }
                });
        mQueue.add(stringRequest);
        mQueue.start();
    }
}