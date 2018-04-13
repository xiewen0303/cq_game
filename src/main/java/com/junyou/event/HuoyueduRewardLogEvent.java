package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *活跃度日志
 * @date 2015-6-5
 */
public class HuoyueduRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray item;// 获得物品

	public HuoyueduRewardLogEvent(long userRoleId, JSONArray item) {
		super(LogPrintHandle.HUOYUEDU_LOG);
		this.userRoleId = userRoleId;
		this.item = item;
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