package com.example.fanyangsz.oschina.Api;

import android.content.Context;
import android.text.TextUtils;
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
import com.example.fanyangsz.oschina.Beans.CommentBeans;
import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.BitmapCache;
import com.example.fanyangsz.oschina.Support.util.Logger;
import com.example.fanyangsz.oschina.Support.util.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanyang.sz on 2016/1/4.
 */
public class HttpSDK {
    private static final String TAG = "HttpSDK API";

    public static String NEWS_URL = "http://m.oschina.net/news/";
    public static String BLOG_URL = "http://m.oschina.net/blog/";
    public static String TWEET_URL = "http://m.oschina.net/tweets/";
    private final String SDK_BASE_URL = "http://www.oschina.net/";

    public static int IMAGE_TYPE_0 = 0;//tweet图片
    public static int IMAGE_TYPE_1 = 1;//头像

    private static RequestQueue mQueue;

    private HttpSDK(){}
    private HttpSDK(Context context){
        mQueue = Volley.newRequestQueue(context);
    }

    public static HttpSDK newInstance (){
        return new HttpSDK();
    }
    public static HttpSDK newInstance(Context context){
        return new HttpSDK(context);
    }

    //新闻资讯接口和回调
    public interface OnNewsCallback {

        void onError();

        void onSuccess(NewsBeans.NewsList news);

    }
    public void getInfoNews(final OnNewsCallback callback,int page){
        getNews(callback,page,0,null);
    }
    public void getHotNews( final OnNewsCallback callback,int page){
        getNews(callback,page,0,"week");
    }
    public void getNews( final OnNewsCallback callBack,int page ,int catalog, String show){
        Logger.e(TAG, SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog +"&pageIndex="+ page + "&show=" +show );
        /*if(mQueue==null){
            mQueue = Volley.newRequestQueue(context);
        }*/

        String url = SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog +"&pageIndex="+ page + "&show=" +show ;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Log.d("TAG", response);
                        Logger.e(TAG, "success");
                        InputStream is = new ByteArrayInputStream(response.getBytes());
                        NewsBeans.NewsList newsBeans = XmlUtils.toBean(NewsBeans.NewsList.class,is);
                        callBack.onSuccess(newsBeans);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e("TAG", error.getMessage(), error);
                        Logger.e(TAG,"fail");

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
    public void getBlog( final onBlogCallBack callBack, int page, String type){
        Logger.e(TAG, SDK_BASE_URL + "action/api/blog_list" + "?pageIndex="+ page + "&type=" + type);
//        mQueue = Volley.newRequestQueue(context);
        String url = SDK_BASE_URL + "action/api/blog_list" + "?pageIndex="+ page + "&type=" + type;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Log.d("TAG", response);
                        Logger.e(TAG, "success");
                        InputStream is = new ByteArrayInputStream(response.getBytes());
                        BlogBeans.Blogs blogs = XmlUtils.toBean(BlogBeans.Blogs.class,is);
                        callBack.onSuccess(blogs);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e("TAG", error.getMessage(), error);
                        Logger.e(TAG,"fail");

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
    public void login( final String username, final String password, final onLoginCallBack callback){
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
                params.put("keep_login", 1+"");
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

    public void getTweet(final OnTweetCallBack callBack,int page, int uid){
        //id=0是普通动弹  id=-1热门动弹
        Logger.e(TAG, SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex="+ page +"&uid=" + uid);
        String url = SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex="+ page +"&uid=" + uid;
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

    public void getTweetImage(String url, ImageView view, int type){
//        mQueue = Volley.newRequestQueue(context);
        ImageLoader loader = new ImageLoader(mQueue, new BitmapCache());
        int defaultImage = (type == IMAGE_TYPE_0)? R.drawable.ic_share:R.drawable.widget_dface;
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultImage, defaultImage);
        loader.get(url, listener);
    }



    //发表动弹接口和回调
    public interface onPubTweetCallBack{
        void onSuccess();
        void onError();
    }
    public void pubTweet(final onPubTweetCallBack callBack, final TweetBean tweetBean){
//        String url = SDK_BASE_URL + "action/api/tweet_pub" + "?uid = " + tweetBean.getAuthorid() + "&msg=" + tweetBean.getBody();
        String url = SDK_BASE_URL + "action/api/tweet_pub";
        Logger.e(TAG,url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callBack.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callBack.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<>();
                map.put("uid", String.valueOf(tweetBean.getAuthorid()));
                map.put("msg", tweetBean.getBody());
                if (!TextUtils.isEmpty(tweetBean.getImageFilePath())) {
                    try {
                        map.put("img", String.valueOf(new File(tweetBean.getImageFilePath())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(tweetBean.getAudioPath())) {
                    try {
                        map.put("amr", String.valueOf(new File(tweetBean.getAudioPath())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return map;
            }
        };

        mQueue.add(stringRequest);
    }



    //评论接口和回调
    public interface onCommentCallBack{
        void onError();

        void onSuccess(CommentBeans.CommentList datas);
    }
    public void getComment(final onCommentCallBack callback, int id, int page, int catalog){
        String url = SDK_BASE_URL + "action/api/comment_list" + "?id=" + id
                + "&catalog=" + catalog + "&pageIndex=" + page + "&clientType=" + "android";
        Logger.e(TAG,url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                InputStream is = new ByteArrayInputStream(s.getBytes());
                CommentBeans.CommentList comments = XmlUtils.toBean(CommentBeans.CommentList.class,is);
                callback.onSuccess(comments);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError();
            }
        });

        mQueue.add(stringRequest);
    }



    //点赞接口和回调
    public interface onAgreeTweetCallBack{
        void onError();

        void onSuccess();
    }
    public void postAgreeTweet(final onAgreeTweetCallBack callBack, final int tweetId, final int authorId, final int uid ){
//        String url = SDK_BASE_URL + "action/api/tweet_like" + "?tweetid=" + tweetId
//                + "uid=" +authorId + "ownerOfTweet = " + authorId;
        String url = SDK_BASE_URL + "action/api/tweet_like";
        Logger.e(TAG,url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callBack.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callBack.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("tweetid", tweetId+"");
                map.put("uid", uid+"");
                map.put("ownerOfTweet", authorId + "");
                return map;
            }
        };
        mQueue.add(stringRequest);
    }
}