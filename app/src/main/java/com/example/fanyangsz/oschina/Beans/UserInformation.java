package com.example.fanyangsz.oschina.Beans;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * 个人信息专用实体类
 *
 * Created by fanyang.sz on 2016/8/1.
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class UserInformation extends BaseBean {
	
	@XStreamAlias("user")
	private User user;
	
	@XStreamAlias("pagesize")
	private int pageSize;
	
	@XStreamAlias("activies")
	private List<Active> activeList;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<Active> getActiveList() {
		return activeList;
	}

	public void setActiveList(List<Active> activeList) {
		this.activeList = activeList;
	}
}