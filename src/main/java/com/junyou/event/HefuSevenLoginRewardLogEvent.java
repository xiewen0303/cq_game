package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *合服七日登陆奖励日志
 * @date 2015-4-18
 */
public class HefuSevenLoginRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public HefuSevenLoginRewardLogEvent(long userRoleId,String roleName,JSONArray goods, int rewardType) {
		super(LogPrintHandle.HEFU_SEVEN_DAY_REWARD);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.goods = goods;
		this.rewardType = rewardType;
	}

	private long userRoleId;
	private String roleName;
	private JSONArray goods;
	private int rewardType;

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getRewardType() {
		return rewardType;
	}

	public String getRoleName() {
		return roleName;
	}

	public JSONArray getGoods() {
		return goods;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}