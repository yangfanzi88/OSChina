package com.example.fanyangsz.oschina.Api.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanyang.sz on 2016/9/22.
 */

public class Params implements Serializable{
    private static final long serialVersionUID = 4801654811733634235L;

    private Map<String, String> mParameters = new HashMap<String, String>();
    private List<String> mKeys = new ArrayList<String>();

    public Params(){}

    public Params(String[] keys, String[] values){
        for (int i = 0; i < keys.length ; i++) {
            mKeys.add(keys[i]);
            mParameters.put(keys[i], values[i]);
        }
    }

    public Params(String key, String value){
        mKeys.add(key);
        mParameters.put(key, value);
    }

    public int size(){
        return mKeys.size();
    }

    public List<String> getKeys(){
        return  mKeys;
    }

    public void addParameter(String key, String value) {
        if (!mKeys.contains(key)) {
            mKeys.add(key);
        }
        mParameters.put(key, value);
    }

    public String getParameter(String key) {
        return mParameters.get(key);
    }

    public Map<String, String> getVaules() {
        return mParameters;
    }

    public void remove(String key) {
        if (mKeys.contains(key)) {
            mKeys.remove(key);
            mParameters.remove(key);
        }
    }

    public void addParams(Params params) {
        for (String key : params.getKeys()) {
            if (!mKeys.contains(key)) {
                mKeys.add(key);
            }
            mParameters.put(key, params.getParameter(key));
        }
    }
}
