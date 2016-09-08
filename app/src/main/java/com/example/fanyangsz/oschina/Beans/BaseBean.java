package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by fanyang.sz on 2016/1/4.
 */
@SuppressWarnings("serial")
public class BaseBean implements Serializable {

    private static final long serialVersionUID = 5947827332427115190L;
    @XStreamAlias("id")
    protected int id;

    protected String cacheKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

}
