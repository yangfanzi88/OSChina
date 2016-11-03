package com.example.fanyangsz.oschina.Support.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.Beans.User;
import com.example.fanyangsz.oschina.Support.Cache.CacheConfig;
import com.example.fanyangsz.oschina.Support.Cache.SharedPreSaveObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangdan on 15-3-26.
 */
public class Utils {
    public static Bitmap.Config displayBitmapConfig = Bitmap.Config.ARGB_8888;
    private static String ua;

    public static int dip2px(Context context, int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int px2dip(Context context, int pxValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(Context context, int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static int length(String paramString) {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.substring(j, j + 1).matches("[Α-￥]")) {
                i += 2;
            } else {
                i++;
            }
        }

        if (i % 2 > 0) {
            i = 1 + i / 2;
        } else {
            i = i / 2;
        }

        return i;
    }
    public static String getUA(Context context) {
        if (TextUtils.isEmpty(ua)) {
            String miei = "";
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                miei = telephonyManager.getDeviceId();
            } catch (Throwable e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            ua = "OTS_" + getVersionName(context) + "_" +
                    miei + "_" + Build.MODEL + "_" + Build.VERSION.SDK_INT;

        }
        return ua;
    }

    public static int getScreenHeight(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String getVersionName(Context context){
        String versionName = "";
        try{
            versionName =  context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }catch (Exception e){e.printStackTrace();}
        return versionName;
    }

    public static String getUserCookie(Context context){
        return  (String) SharedPreSaveObject.readObject(context, CacheConfig.SHARED_USER_LOGIN, CacheConfig.KEY_USER_COOKIE);
    }

    public static User getCurrentUser(Context context){
        LoginUserBean loginUserBean = (LoginUserBean) SharedPreSaveObject.readObject(context, CacheConfig.SHARED_USER_LOGIN, CacheConfig.KEY_USER_LOGIN);
        if(loginUserBean!=null && loginUserBean.getUser() != null)
            return loginUserBean.getUser();
        return null;
    }
}
