package com.example.fanyangsz.oschina.Api;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fanyangsz.oschina.Beans.BlogBeans;
import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.BitmapCache;
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
    public static String BLOG_URL = "http://m.oschina.net/blog/";
    public static String TWEET_URL = "http://m.oschina.net/tweets/";
    private final String SDK_BASE_URL = "http://www.oschina.net/";
    static RequestQueue mQueue;

    /*

    private HttpSDK(){}

    public static HttpSDK newInstance (){
        return new HttpSDK();
    }*/

    //新闻资讯接口和回调
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
    public void getNews(Context context, final OnNewsCallback callBack,int page ,int catalog, String show){
        Log.e("123", SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog +"&pageIndex="+ page + "&show=" +show );
        if(mQueue==null){
            mQueue = Volley.newRequestQueue(context);
        }

        String url = SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog +"&pageIndex="+ page + "&show=" +show ;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Log.d("TAG", response);
                        Log.e("123", "success");
                        InputStream is = new ByteArrayInputStream(response.getBytes());
                        NewsBeans.NewsList newsBeans = XmlUtils.toBean(NewsBeans.NewsList.class,is);
                        callBack.onSuccess(newsBeans);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Log.e("123","fail");

                        callBack.onError();
                    }
                });
        mQueue.add(stringRequest);
//        mQueue.start();
    }
    //博客接口和回调
    public interface onBlogCallBack{
        void onError();

        void onSuccess(BlogBeans.Blogs blogs);
    }
    public void getBlog(Context context, final onBlogCallBack callBack, int page, String type){
        Log.e("123", SDK_BASE_URL + "action/api/blog_list" + "?pageIndex="+ page + "&type=" + type);
//        mQueue = Volley.newRequestQueue(context);
        String url = SDK_BASE_URL + "action/api/blog_list" + "?pageIndex="+ page + "&type=" + type;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Log.d("TAG", response);
                        Log.e("123", "success");
                        InputStream is = new ByteArrayInputStream(response.getBytes());
                        BlogBeans.Blogs blogs = XmlUtils.toBean(BlogBeans.Blogs.class,is);
                        callBack.onSuccess(blogs);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Log.e("123","fail");

                        callBack.onError();
                    }
                });
        mQueue.add(stringRequest);
//        mQueue.start();
    }
    //登陆接口和回调
    public interface onLoginCallBack{

        void onError();

        void onSuccess(LoginUserBean s);
    }
    public void login(Context context, final String username, final String password, final onLoginCallBack callback){
//        mQueue = Volley.newRequestQueue(context);
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
//        mQueue.start();
    }


    //动弹接口和回调
    public interface OnTweetCallBack{
        void onError();

        void onSuccess(TweetBeans.TweetList datas);
    }

    public void getTweet(Context context,final OnTweetCallBack callBack,int page){
        Log.e("123", SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex="+ page );
        String url = SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex="+ page;
//        mQueue = Volley.newRequestQueue(context);
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
//        mQueue.start();
    }

    public void getTweetImage(Context context, String url, ImageView view){
//        mQueue = Volley.newRequestQueue(context);
        ImageLoader loader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, R.drawable.ic_share,R.drawable.ic_share);
        loader.get(url, listener);
    }

    public void getAvatarImage(Context context, String url, ImageView view){

//        mQueue = Volley.newRequestQueue(context);
        ImageLoader loader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, R.drawable.widget_dface,R.drawable.widget_dface);
        loader.get(url, listener);
    }
}