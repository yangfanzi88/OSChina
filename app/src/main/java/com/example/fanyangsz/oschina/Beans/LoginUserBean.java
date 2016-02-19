package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by fanyang.sz on 2016/1/12.
 */

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class LoginUserBean extends BaseBean implements Serializable{

    @XStreamAlias("result")
    private Result result;

    @XStreamAlias("user")
    private User user;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

