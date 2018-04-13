package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *拉霸日志
 */
public class LaBaGetLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;  
	private int reward; //奖励等级 0特等，1一等，2参与奖
	private JSONArray item;// 获得物品

	public LaBaGetLogEvent(long userRoleId, JSONArray item,int reward ) {
		super(LogPrintHandle.LABA_LOG);
		this.userRoleId = userRoleId;
		this.item = item;
		this.reward  = reward;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getReward() {
		return reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
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