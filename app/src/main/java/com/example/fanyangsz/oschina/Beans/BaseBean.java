package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by fanyang.sz on 2016/1/4.
 */
@SuppressWarnings("serial")
public class BaseBean {

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
