package com.example.fanyangsz.oschina.Support.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;

/**
 * Created by fanyang.sz on 2016/7/7.
 */

public class MyResponse{

    private static String TAG = "MyResponse";

    private int status = 0;

    private String jsonString = "";

    private List<Cookie> cookies = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }
}
