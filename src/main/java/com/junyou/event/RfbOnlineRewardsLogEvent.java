package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 热发布在线奖励
 * @date 2015-6-5
 */
public class RfbOnlineRewardsLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray item;// 获得物品
	private int id; //奖励配置id

	public RfbOnlineRewardsLogEvent(long userRoleId, JSONArray item, int id) {
		super(LogPrintHandle.RFB_ONLINE_REWARDS);
		this.userRoleId = userRoleId;
		this.item = item;
		this.id = id;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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