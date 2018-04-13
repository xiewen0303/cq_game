package com.junyou.event;

import com.junyou.log.LogPrintHandle;
 

/**
 * 在线时长日志
 */
public class OnlineTimeLogEvent extends AbsGameLogEvent {

	 
	private static final long serialVersionUID = 1L;
	private long userRoleId;//玩家id
	private String name;//玩家名
	private long onlineTime;//在线时长
	private long dayTime;//在线日期

	public OnlineTimeLogEvent(long userRoleId, String name,long onlineTime, long dayTime) {
		super(LogPrintHandle.ONLINE_TIME);
		this.userRoleId = userRoleId;
		this.name = name;
		this.onlineTime = onlineTime;
		this.dayTime = dayTime;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public String getName() {
		return name;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public long getDayTime() {
		return dayTime;
	}
	
	
	
}