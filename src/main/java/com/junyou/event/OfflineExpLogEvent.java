package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 离线经验日志
 * @author chenjianye
 * @date 2015-4-20
 */
public class OfflineExpLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public OfflineExpLogEvent(long userRoleId,String roleName,int rewardType,long offlineTotal) {
		super(LogPrintHandle.OFFLINE_EXP);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.rewardType = rewardType;
		this.offlineTotal = offlineTotal;
	}

	private long userRoleId;
	private String roleName;
	private int rewardType;
	private long offlineTotal;

	public long getUserRoleId() {
		return userRoleId;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public int getRewardType() {
		return rewardType;
	}

	public long getOfflineTotal() {
		return offlineTotal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}