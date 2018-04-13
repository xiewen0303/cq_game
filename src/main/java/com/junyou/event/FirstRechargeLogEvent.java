package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 首次充值日志
 * @author LiuYu
 * @date 2015-4-23 下午8:32:31
 */
public class FirstRechargeLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	
	private long userRoleId;
	private int level;
	public FirstRechargeLogEvent(long userRoleId, int level) {
		super(LogPrintHandle.PLAYER_FIRST_RECHARE);
		this.userRoleId = userRoleId;
		this.level = level;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public int getLevel() {
		return level;
	}
	
	

	
}