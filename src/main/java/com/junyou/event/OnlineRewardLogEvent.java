package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 在线奖励日志
 * @author chenjianye
 * @date 2015-4-16
 */
public class OnlineRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public OnlineRewardLogEvent(long userRoleId,String roleName,JSONArray goods,long onlineTime) {
		super(LogPrintHandle.ONLINE_REWARD);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.goods = goods;
		this.onlineTime = onlineTime;
	}

	private long userRoleId;
	private String roleName;
	private JSONArray goods;
	private long onlineTime;

	public long getUserRoleId() {
		return userRoleId;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public JSONArray getGoods() {
		return goods;
	}
	
}