package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *没牌炼狱boss通关奖励日志
 */
public class LianyuBossRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private int configId;//关卡id
	private int completeTime;//通过时间
	private JSONArray item;// 获得物品

	public LianyuBossRewardLogEvent(long userRoleId, JSONArray item,int completeTime,int configId) {
		super(LogPrintHandle.LIANYU_BOSS);
		this.userRoleId = userRoleId;
		this.item = item;
		this.completeTime = completeTime;
		this.configId = configId;
	}

	public int getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(int completeTime) {
		this.completeTime = completeTime;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public long getUserRoleId() {
		return userRoleId;
	}

	public JSONArray getItem() {
		return item;
	}

	public void setItem(JSONArray item) {
		this.item = item;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}