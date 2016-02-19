package com.example.fanyangsz.oschina.Beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by fanyang.sz on 2016/1/12.
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class NoticeDetail extends BaseBean implements Serializable{
	
	@XStreamAlias("result")
	private Result result;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
	
}
