package com.example.fanyangsz.oschina.Api;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.fanyangsz.oschina.Api.http.Params;
import com.example.fanyangsz.oschina.Api.setting.Setting;
import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.Support.Cache.SharedPreSaveObject;
import com.example.fanyangsz.oschina.Support.util.XmlUtils;


/**
 * Created by fanyang.sz on 2016/9/24.
 */

public class NetworkRequest<T> extends Request<T> {
    private final Class<T> clazz;
    private final Listener<T> listener;


    private Map<String, String> mHeader = new HashMap<String, String>();
    private Map<String, String> mParams = new HashMap<>();
    private int method;

    /**
     * @param actionSetting
     * @param clazz 我们最终的转化类型
     * @param appendHeader 请求附带的头信息
     * @param listener
     * @param errorListener
     */
    public NetworkRequest(int method, Setting actionSetting, Params params, Class<T> clazz,
            Map<String, String> appendHeader, Listener<T> listener, ErrorListener errorListener) {
        super(method, paramstoString(String.format("%s%s", HttpSDK.getBaseUrl(), actionSetting.getValue()),
                params.getVaules(), method), errorListener);
        this.clazz = clazz;
        this.listener = listener;
        this.method = method;
        mParams = params.getVaules();
        if (appendHeader != null) mHeader.putAll(appendHeader);

        setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public NetworkRequest(Setting actionSetting, Params params, Class<T> clazz,
                          Map<String, String> appendHeader, Listener<T> listener, ErrorListener errorListener){
        this(Method.GET,actionSetting,params,clazz,appendHeader,listener,errorListener);
    }

    private static String paramstoString(String mURL, Map<String, String> paramsMap, int method) {
        if (method == Method.GET) {
            int index = mURL.lastIndexOf("?");

            StringBuffer urlParam = new StringBuffer(mURL);
            Set<Map.Entry<String, String>> entries = paramsMap.entrySet();
            boolean first = true;
            for (Map.Entry<String, String> entry : entries) {
                if (!first)
                    urlParam.append("&");
                else {

                    if (index < 0)
                        urlParam.append("?");
                    else if (index != mURL.length() - 1) urlParam.append("&");

                    first = false;
                }
                urlParam.append(entry.getKey()).append("=");
                if (!TextUtils.isEmpty(entry.getValue())) {
                    String text = URLEncoder.encode(entry.getValue());
                    urlParam.append(text);
                }
            }

            String urlstr = urlParam.toString();
            if (urlstr.contains("+")) {
                urlstr = urlstr.replaceAll("\\+", "%20");
            }
            mURL = urlstr;
        }
        return mURL;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // 默认返回 return Collections.emptyMap();
        return mHeader;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (method == Method.GET)
            return super.getParams();
        else
            return mParams;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            /**
             * 得到返回的数据
             */
            String Str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            /**
             * 转化成对象
             */
            InputStream is = new ByteArrayInputStream(Str.getBytes());
            T result;
            if (clazz != null) {
                result = XmlUtils.toBean(clazz, is);
            } else {
                result = null;
            }


            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
