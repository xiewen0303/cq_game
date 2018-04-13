package com.junyou.bus.tongyong.entity;

import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;

public class ActityCountLog extends AbsVersion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long userRoleId;
	
	private Integer count;
	
	private long updateTime;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	 
	
	
}
