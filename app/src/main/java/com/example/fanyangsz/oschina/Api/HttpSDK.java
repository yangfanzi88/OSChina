package com.example.fanyangsz.oschina.Api;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fanyangsz.oschina.Api.http.Params;
import com.example.fanyangsz.oschina.Api.setting.Setting;
import com.example.fanyangsz.oschina.Beans.BlogBean;
import com.example.fanyangsz.oschina.Beans.BlogBeans;
import com.example.fanyangsz.oschina.Beans.CommentBeans;
import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.Beans.NewsBean;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.Beans.UserInformation;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.Cache.BitmapCache;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;
import com.example.fanyangsz.oschina.Support.Cache.InternalStorage;
import com.example.fanyangsz.oschina.Support.Cache.SharedPreSaveObject;
import com.example.fanyangsz.oschina.Support.util.JSONHelper;
import com.example.fanyangsz.oschina.Support.util.JsonObjectPostRequest;
import com.example.fanyangsz.oschina.Support.util.Logger;
import com.example.fanyangsz.oschina.Support.util.MyHurlStack;
import com.example.fanyangsz.oschina.Support.util.MyResponse;
import com.example.fanyangsz.oschina.Support.util.XmlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.apache.http.cookie.Cookie;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.FILE_BLOG;
import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.FILE_NEWS;
import static com.example.fanyangsz.oschina.Support.Cache.CacheConfig.FILE_TWEET;
import static com.example.fanyangsz.oschina.Support.util.Utils.replaceBlank;

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
//    private static List<Cookie> cookies = new ArrayList<>();

    public static String getBaseUrl(){
        return SDK_BASE_URL;
    }

    private HttpSDK() {
    }

    private HttpSDK(Context context) {
//        mQueue = Volley.newRequestQueue(context, new MyHurlStack());
        mQueue = Volley.newRequestQueue(context, new HurlStack());
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


    private static final Pattern pattern = Pattern.compile("Set-Cookie\\d*");

    public void login(final String username, final String password, final onLoginCallBack callback) {
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
//                    Cookies0 = responseHeaders.get("Set-Cookie0");
//                    Cookies1 = responseHeaders.get("Set-Cookie1");
//                    Cookies2 = responseHeaders.get("Set-Cookie2");
                    Cookies = responseHeaders.get("Set-Cookie");
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
                /*try {
                    MyResponse myResponse = new MyResponse();
                    String jsonString =
                            new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    myResponse.setJsonString(jsonString);

                    // get all cookie
                    String cookieFromResponse;

                    for (String key : response.headers.keySet()) {
                        Matcher matcher = pattern.matcher(key);
                        if (matcher.find()) {
                            cookieFromResponse = response.headers.get(key);
                            cookieFromResponse = cookieFromResponse.substring(0, cookieFromResponse.indexOf(";"));
                            String keyValue[] = cookieFromResponse.split("=", -1);
                            Cookie cookie = new BasicClientCookie(keyValue[0], keyValue[1]);
                            cookies.add(cookie);
                        }
                    }
                    String dataString = new String(response.data, "UTF-8");

                    myResponse.setCookies(cookies);

                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }*/
            }
        };

        mQueue.add(stringRequest);
//        mQueue.start();

        /*HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("user_name", username);
        mMap.put("password", password);
        JsonObjectPostRequest jsonObjectPostRequest = new JsonObjectPostRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Cookies = jsonObject.getString("Cookie");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject.get("status").equals("success")) {
                        //登录成功
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError();
            }
        },mMap);
        mQueue.add(jsonObjectPostRequest);*/
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
        if (mode == CacheConfig.CacheMode.servicePriority || InternalStorage.isDataFailure(mContext, FILE_BLOG + type) || result != null|| result.getBlog().isEmpty() ) {
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
            if (!InternalStorage.isDataFailure(mContext, FILE_TWEET + uid) && result != null &&  !result.getTweet().isEmpty()) {
                callBack.onSuccess(result);
            }
        }
        if (mode == CacheConfig.CacheMode.servicePriority ||InternalStorage.isDataFailure(mContext, FILE_TWEET + uid) || result == null || result.getTweet().isEmpty()) {
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


    //发表动弹接口和回调
    public interface onPubTweetCallBack {
        void onSuccess();

        void onError();
    }

    public void pubTweet(final onPubTweetCallBack callBack, final TweetBean tweetBean) {
//        String url = SDK_BASE_URL + "action/api/tweet_pub" + "?uid = " + tweetBean.getAuthorid() + "&msg=" + tweetBean.getBody();
        String url = SDK_BASE_URL + "action/api/tweet_pub";
        Logger.e(TAG, url);

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
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                /*StringBuilder sb = new StringBuilder();
                for (Cookie cookie : cookies) {
                    if(!cookie.getValue().isEmpty())
                        sb.append(cookie).append("; ");
                }*/
                HashMap<String, String> localHashMap = new HashMap<>();
//                localHashMap.put("Cookie", sb.toString());
//                localHashMap.put("Set-Cookie0", Cookies0);
//                localHashMap.put("Set-Cookie1", Cookies1);
//                localHashMap.put("Set-Cookie2", Cookies2);
                localHashMap.put("Set-Cookie",Cookies);
                return localHashMap;
            }

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
    public interface onCommentCallBack {
        void onError();

        void onSuccess(CommentBeans.CommentList datas);
    }

    public void getComment( int id, int page, int catalog,Response.Listener<CommentBeans.CommentList> listener, Response.ErrorListener errorListener) {
        String url = SDK_BASE_URL + "action/api/comment_list" + "?id=" + id
                + "&catalog=" + catalog + "&pageIndex=" + page + "&clientType=" + "android";
        Logger.e(TAG, url);

        /*StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                InputStream is = new ByteArrayInputStream(s.getBytes());
                CommentBeans.CommentList comments = XmlUtils.toBean(CommentBeans.CommentList.class, is);
                callback.onSuccess(comments);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError();
            }
        });

        mQueue.add(stringRequest);*/

        Setting action = getAction("getComment", "action/api/comment_list", "获取动弹的评论");

        Params params = new Params();
        params.addParameter("pageIndex",String.valueOf(page));
        params.addParameter("id",String.valueOf(id));
        params.addParameter("catalog",String.valueOf(catalog));


        NetworkRequest request = new NetworkRequest(Request.Method.GET,action,params,CommentBeans.CommentList.class,null,listener,errorListener);
        mQueue.add(request);
    }


    //点赞接口和回调
    public interface onAgreeTweetCallBack {
        void onError();

        void onSuccess();
    }

    public void postAgreeTweet(final onAgreeTweetCallBack callBack, final int tweetId, final int authorId, final int uid) {
//        String url = SDK_BASE_URL + "action/api/tweet_like" + "?tweetid=" + tweetId
//                + "uid=" +authorId + "ownerOfTweet = " + authorId;
        String url = SDK_BASE_URL + "action/api/tweet_like";
        Logger.e(TAG, url);

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("tweetid", tweetId + "");
                map.put("uid", uid + "");
                map.put("ownerOfTweet", authorId + "");
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    //用户中心信息回调接口
    public interface onUserCenterCallBack{
        void onError();

        void onSuccess(UserInformation userInformation);
    }
    public void getUserCenter(final onUserCenterCallBack callBack, String hisid, String hisname, int page){
        String url = SDK_BASE_URL + "action/api/user_information" + "?uid=2594538" + "&hisuid=" + hisid + "&hisname="+ hisname + "&pageIndex=" + page;
        Logger.e(TAG, url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                InputStream is = new ByteArrayInputStream(s.getBytes());
                UserInformation userInformation = XmlUtils.toBean(UserInformation.class, is);
                callBack.onSuccess(userInformation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callBack.onError();
            }
        });

        mQueue.add(stringRequest);
    }

    /*******************************************************************************/
    protected Setting getAction(String type, String value, String desc) {
        Setting setting = new Setting();

        setting.setType(type);
        setting.setValue(value);
        setting.setDescription(desc);

        return setting;
    }
}