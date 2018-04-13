package com.junyou.bus.huiyanshijin.entity;

import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;

public class HuiYanShiJinLog extends AbsVersion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String roleName;
	
	
	private int gold;
	
	private Long userRoleId;
	
	private Integer subId;
	
	
	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	private long createTime;
	 
	
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	
}
