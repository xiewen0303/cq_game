package com.junyou.bus.zhuanpan.entity;

import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;

public class ZhuanPanLog extends AbsVersion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String roleName;
	
	private String goodsId;
	
	private int goodsCount;
	
	private Long userRoleId;
	
	
//	private String logContent;
	
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

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	
//	public String getLogContent() {
//		return logContent;
//	}
//
//	public void setLogContent(String logContent) {
//		this.logContent = logContent;
//	}
}
