package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 宝石系统日志
 * 
 * @date 2015-6-5
 */
public class YaoshenPoshenLogEvent extends AbsGameLogEvent {
	private static final long serialVersionUID = 1L;
	
	private int category; //1镶嵌，2卸下
	private String oldPoshenId; //替换的情况下会有旧的值
	private String poshenId; 
	private long userRoleId;

	public YaoshenPoshenLogEvent(long userRoleId, int category, String oldPoshenId, String poshenId) {
		super(LogPrintHandle.YAOSHEN_HUNPO_POSHEN_LOG);
		this.category = category;
		this.userRoleId = userRoleId;
		this.oldPoshenId = oldPoshenId;
		this.poshenId  = poshenId;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getOldPoshenId() {
		return oldPoshenId;
	}

	public void setOldPoshenId(String oldPoshenId) {
		this.oldPoshenId = oldPoshenId;
	}

	public String getPoshenId() {
		return poshenId;
	}

	public void setPoshenId(String poshenId) {
		this.poshenId = poshenId;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	 

}