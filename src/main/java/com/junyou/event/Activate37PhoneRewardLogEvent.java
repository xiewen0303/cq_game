package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 激活37手机礼包奖励
 * 
 * @author dsh
 * @date 2015-6-5
 */
public class Activate37PhoneRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;

	public Activate37PhoneRewardLogEvent(long userRoleId) {
		super(LogPrintHandle._37_PHONE_REWARD_ACTICATE);
		this.userRoleId = userRoleId;
	}

	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}