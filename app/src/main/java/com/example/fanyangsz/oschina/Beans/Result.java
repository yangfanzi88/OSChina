package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;


/**
 * Created by fanyang.sz on 2016/1/12.
 */
@SuppressWarnings("serial")
@XStreamAlias("result")
public class Result implements Serializable {

    @XStreamAlias("errorCode")
    private int errorCode;

    @XStreamAlias("errorMessage")
    private String errorMessage;

    public boolean OK() {
	return errorCode == 1;
    }

    public int getErrorCode() {
	return errorCode;
    }

    public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }
}
