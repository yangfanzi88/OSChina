package com.example.fanyangsz.oschina.Api;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fanyangsz.oschina.Api.http.Params;
import com.example.fanyangsz.oschina.Api.setting.Setting;
import com.example.fanyangsz.oschina.Beans.BlogBeans;
import com.example.fanyangsz.oschina.Beans.CommentBeans;
import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.Beans.NewsBean;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.Beans.User;
import com.example.fanyangsz.oschina.Beans.UserInformation;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.BitmapCache;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;
import com.example.fanyangsz.oschina.Support.Cache.InternalStorage;
import com.example.fanyangsz.oschina.Support.Cache.SharedPreSaveObject;
import com.example.fanyangsz.oschina.Support.util.Logger;
import com.example.fanyangsz.oschina.Support.util.MyHurlStack;
import com.example.fanyangsz.oschina.Support.util.Utils;
import com.example.fanyangsz.oschina.Support.util.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.FILE_BLOG;
import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.FILE_NEWS;
import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.FILE_TWEET;
import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.KEY_USER_COOKIE;
import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.SHARED_USER_LOGIN;

/**
 * Created by fanyang.sz on 2016/1/4.
 */
public class HttpSDK {
    private static final String TAG = "HttpSDK API";
    private static Context mContext;

    public static String NEWS_URL = "http://m.oschina.net/news/";
    public static String BLOG_URL = "http://m.oschina.net/blog/";
    public static String TWEET_URL = "http://m.oschina.net/tweets/";
    private static final String SDK_BASE_URL = "http://www.oschina.net/";

    public static int IMAGE_TYPE_0 = 0;//tweet图片
    public static int IMAGE_TYPE_1 = 1;//头像

    private static RequestQueue mQueue;
    private static String Cookies0;
    private static String Cookies1;
    private static String Cookies2;
    private static String Cookies;

    public static String getBaseUrl() {
        return SDK_BASE_URL;
    }

    private HttpSDK() {
    }

    private HttpSDK(Context context) {
        mQueue = Volley.newRequestQueue(context, new MyHurlStack());
//        mQueue = Volley.newRequestQueue(context, new HurlStack());
    }

    public static HttpSDK newInstance() {
        return new HttpSDK();
    }

    public static HttpSDK newInstance(Context context) {
        mContext = context;
        return new HttpSDK(context);
    }


    //登陆接口和回调
    public interface onLoginCallBack {

        void onError();

        void onSuccess(LoginUserBean s);
    }


    public void login(final String username, final String password, final onLoginCallBack callback) {

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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onError();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("pwd", password);
                params.put("keep_login", 1 + "");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // TODO Auto-generated method stub
                try {
                    Map<String, String> responseHeaders = response.headers;
                    Cookies0 = responseHeaders.get("Set-Cookie0");
                    Cookies1 = responseHeaders.get("Set-Cookie1");
                    Cookies2 = responseHeaders.get("Set-Cookie2");
//                    Cookies = responseHeaders.get("Set-Cookie");
                    Cookies = Cookies0 + ";" + Cookies1 + ";" + Cookies2 + ";";
                    SharedPreSaveObject.saveObject(mContext, SHARED_USER_LOGIN, KEY_USER_COOKIE, Cookies);
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        mQueue.add(stringRequest);
    }

    //新闻资讯接口和回调
    public interface OnNewsCallback {

        void onError();

        void onSuccess(NewsBeans.NewsList news);

    }

    public void getInfoNews(final OnNewsCallback callback, int page, Enum mode) {
        getNews(callback, page, 0, null, mode);
    }

    public void getHotNews(final OnNewsCallback callback, int page, Enum mode) {
        getNews(callback, page, 0, "week", mode);
    }

    public void getNews(final OnNewsCallback callBack, final int page, int catalog, final String show, Enum mode) {
        Logger.e(TAG, SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog + "&pageIndex=" + page + "&show=" + show);
        String url = SDK_BASE_URL + "action/api/news_list" + "?catalog=" + catalog + "&pageIndex=" + page + "&show=" + show;

        NewsBeans.NewsList result = new NewsBeans.NewsList();
        ArrayList<NewsBean> list = new ArrayList<>();
        //先去判断下缓存是否有数据且有效
        if (mode == CacheConfig.CacheMode.auto) {
            try {
                String s = InternalStorage.fileGet(mContext, FILE_NEWS + show);
//                list.addAll(JSONHelper.parseCollection(s, List.class, NewsBean.class));
//                result.setNews(list);
                InputStream is = new ByteArrayInputStream(s.getBytes());
                result = XmlUtils.toBean(NewsBeans.NewsList.class, is);
            } catch (Exception e) {
            }
            if (!InternalStorage.isDataFailure(mContext, FILE_NEWS + show) && result != null && !result.getNews().isEmpty()) {
                callBack.onSuccess(result);
            }
        }
        if (mode == CacheConfig.CacheMode.servicePriority || InternalStorage.isDataFailure(mContext, FILE_NEWS + show) || result == null || result.getNews().isEmpty()) {
            StringRequest stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                        Log.d("TAG", response);
                            Logger.e(TAG, "success");
                            InputStream is = new ByteArrayInputStream(response.getBytes());
                            NewsBeans.NewsList result = XmlUtils.toBean(NewsBeans.NewsList.class, is);
                            callBack.onSuccess(result);

                            //服务器的有效数据先保存到本地文件
                            try {
                                if (page == 0) {
                                    InternalStorage.fileDelete(mContext, FILE_NEWS + show);
                                    InternalStorage.fileSave(mContext, FILE_NEWS + show, response);
                                } else {
                                    InternalStorage.fileAppend(mContext, FILE_NEWS + show, response, "newslist");
                                }
                            } catch (IOException e) {
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logger.e("TAG", error.getMessage(), error);
                            Logger.e(TAG, "fail");
                            callBack.onError();
                        }
                    });
            mQueue.add(stringRequest);
        }

//        mQueue.start();
    }


    //博客接口和回调
    public interface onBlogCallBack {
        void onError();

        void onSuccess(BlogBeans.Blogs blogs);
    }

    public void getBlog(final onBlogCallBack callBack, final int page, final String type, Enum mode) {
        Logger.e(TAG, SDK_BASE_URL + "action/api/blog_list" + "?pageIndex=" + page + "&type=" + type);
//        mQueue = Volley.newRequestQueue(context);
        String url = SDK_BASE_URL + "action/api/blog_list" + "?pageIndex=" + page + "&type=" + type;

        BlogBeans.Blogs result = new BlogBeans.Blogs();
        if (mode == CacheConfig.CacheMode.auto) {
            try {
                String s = InternalStorage.fileGet(mContext, FILE_BLOG + type);
                InputStream is = new ByteArrayInputStream(s.getBytes());
                result = XmlUtils.toBean(BlogBeans.Blogs.class, is);
            } catch (Exception e) {
            }
            if (!InternalStorage.isDataFailure(mContext, FILE_BLOG + type) && result != null && !result.getBlog().isEmpty()) {
                callBack.onSuccess(result);
            }
        }
        if (mode == CacheConfig.CacheMode.servicePriority || InternalStorage.isDataFailure(mContext, FILE_BLOG + type) || result != null || result.getBlog().isEmpty()) {
            StringRequest stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                        Log.d("TAG", response);
                            Logger.e(TAG, "success");
                            InputStream is = new ByteArrayInputStream(response.getBytes());
                            BlogBeans.Blogs blogs = XmlUtils.toBean(BlogBeans.Blogs.class, is);
                            callBack.onSuccess(blogs);

                            //服务器的有效数据先保存到本地文件
                            try {
                                if (page == 0) {
                                    InternalStorage.fileDelete(mContext, FILE_BLOG + type);
                                    InternalStorage.fileSave(mContext, FILE_BLOG + type, response);
                                } else {
                                    InternalStorage.fileAppend(mContext, FILE_BLOG + type, response, "blogs");
                                }
                            } catch (IOException e) {
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logger.e("TAG", error.getMessage(), error);
                            Logger.e(TAG, "fail");

                            callBack.onError();
                        }
                    });
            mQueue.add(stringRequest);
//        mQueue.start();
        }
    }


    //动弹接口和回调
    public interface OnTweetCallBack {
        void onError();

        void onSuccess(TweetBeans.TweetList datas);
    }

    public void getTweet(final OnTweetCallBack callBack, final int page, final int uid, Enum mode) {
        //id=0是普通动弹  id=-1热门动弹
        Logger.e(TAG, SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex=" + page + "&uid=" + uid);
        String url = SDK_BASE_URL + "action/api/tweet_list" + "?pageIndex=" + page + "&uid=" + uid;
//        mQueue = Volley.newRequestQueue(context);

        TweetBeans.TweetList result = new TweetBeans.TweetList();
        if (mode == CacheConfig.CacheMode.auto) {
            try {
                String s = InternalStorage.fileGet(mContext, FILE_TWEET + uid);
//                list.addAll(JSONHelper.parseCollection(s, List.class, TweetBean.class));

                InputStream is = new ByteArrayInputStream(s.getBytes());
                result = XmlUtils.toBean(TweetBeans.TweetList.class, is);
                Logger.sysout(result.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!InternalStorage.isDataFailure(mContext, FILE_TWEET + uid) && result != null && !result.getTweet().isEmpty()) {
                callBack.onSuccess(result);
            }
        }
        if (mode == CacheConfig.CacheMode.servicePriority || InternalStorage.isDataFailure(mContext, FILE_TWEET + uid) || result == null || result.getTweet().isEmpty()) {
            StringRequest stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            InputStream is = new ByteArrayInputStream(s.getBytes());
                            TweetBeans.TweetList Tweets = XmlUtils.toBean(TweetBeans.TweetList.class, is);
                            callBack.onSuccess(Tweets);

                            //服务器的有效数据先保存到本地文件
                            try {
                                if (page == 0) {
                                    InternalStorage.fileDelete(mContext, FILE_TWEET + uid);
                                    InternalStorage.fileSave(mContext, FILE_TWEET + uid, s);
                                } else {
                                    InternalStorage.fileAppend(mContext, FILE_TWEET + uid, s, "tweets");
                                }
                            } catch (IOException e) {
                            }
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

    }

    public void getTweetImage(String url, ImageView view, int type) {
//        mQueue = Volley.newRequestQueue(context);
        ImageLoader loader = new ImageLoader(mQueue, new BitmapCache());
        int defaultImage = (type == IMAGE_TYPE_0) ? R.drawable.ic_share : R.drawable.widget_dface;
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultImage, defaultImage);
        loader.get(url, listener);
    }

    public void pubTweet(final TweetBean tweetBean, Response.Listener listener, Response.ErrorListener errorListener) {
//        String url = SDK_BASE_URL + "action/api/tweet_pub" + "?uid = " + tweetBean.getAuthorid() + "&msg=" + tweetBean.getBody();
        String url = SDK_BASE_URL + "action/api/tweet_pub";
        Logger.e(TAG, url);

        Setting action = getAction("pubTweet","action/api/tweet_pub","发布动弹");
        Params params = new Params();
        params.addParameter("uid", String.valueOf(tweetBean.getAuthorid()));
        params.addParameter("msg", tweetBean.getBody());

        NetworkRequest request = new NetworkRequest(Request.Method.POST, action, params, null, getCookie(), listener, errorListener);
        mQueue.add(request);
    }

    public void getComment(int id, int page, int catalog, Response.Listener<CommentBeans.CommentList> listener, Response.ErrorListener errorListener) {
        String url = SDK_BASE_URL + "action/api/comment_list" + "?id=" + id
                + "&catalog=" + catalog + "&pageIndex=" + page + "&clientType=" + "android";
        Logger.e(TAG, url);

        Setting action = getAction("getComment", "action/api/comment_list", "获取动弹的评论");

        Params params = new Params();
        params.addParameter("pageIndex", String.valueOf(page));
        params.addParameter("id", String.valueOf(id));
        params.addParameter("catalog", String.valueOf(catalog));

        NetworkRequest request = new NetworkRequest(action, params, CommentBeans.CommentList.class, null, listener, errorListener);
        mQueue.add(request);
    }

    public void postAgreeTweet(final int tweetId, final int authorId, final int uid, Response.Listener listener, Response.ErrorListener errorListener) {
//        String url = SDK_BASE_URL + "action/api/tweet_like" + "?tweetid=" + tweetId
//                + "uid=" +authorId + "ownerOfTweet = " + authorId;
        String url = SDK_BASE_URL + "action/api/tweet_like";
        Logger.e(TAG, url);

        Setting action = getAction("postAgreeTweet", "action/api/tweet_like", "动弹点赞");
        Params params = new Params();
        params.addParameter("tweetid", String.valueOf(tweetId));
        params.addParameter("uid", String.valueOf(uid));
        params.addParameter("ownerOfTweet", String.valueOf(authorId));

        NetworkRequest request = new NetworkRequest(Request.Method.POST, action, params, null, getCookie(), listener, errorListener);
        mQueue.add(request);
    }



    public void getUserCenter(String hisid, String hisname, int page, Response.Listener listener, Response.ErrorListener errorlistener) {
        String url = SDK_BASE_URL + "action/api/user_information" + "?uid=2594538" + "&hisuid=" + hisid + "&hisname=" + hisname + "&pageIndex=" + page;
        Logger.e(TAG, url);

        Setting action = getAction("getUserCenter","action/api/user_information","获取用户信息");

        Params params = new Params();
        String uid="";
        User user = Utils.getCurrentUser(mContext);
        if(user!=null){
            uid = user.getId()+"";
        }
        params.addParameter("uid",uid);
        params.addParameter("hisuid",hisid);
        params.addParameter("hisname",hisname);
        params.addParameter("pageIndex",String.valueOf(page));

        Request request = new NetworkRequest(action,params,UserInformation.class,null,listener,errorlistener);
        mQueue.add(request);
    }

    /*******************************************************************************/
    protected Setting getAction(String type, String value, String desc) {
        Setting setting = new Setting();

        setting.setType(type);
        setting.setValue(value);
        setting.setDescription(desc);

        return setting;
    }

    private HashMap<String, String> getCookie(){
        HashMap<String, String> header = new HashMap<>();
        header.put("Cookie", Utils.getUserCookie(mContext));
        return header;
    }
}