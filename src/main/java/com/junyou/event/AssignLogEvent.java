package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 签到奖励日志
 * @author chenjianye
 * @date 2015-4-16
 */
public class AssignLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public AssignLogEvent(long userRoleId,String roleName,int assignType,int currRetroactive, int currAssignCount,int assignDays) {
		super(LogPrintHandle.ASSIGN);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.assignType = assignType;
		this.currRetroactive = currRetroactive;
		this.currAssignCount = currAssignCount;
		this.assignDays = assignDays;
	}

	private long userRoleId;
	private String roleName;
	private int assignType;
	private int currRetroactive;
	private int currAssignCount;
	private int assignDays;

	public long getUserRoleId() {
		return userRoleId;
	}
	
	public int getCurrRetroactive() {
		return currRetroactive;
	}

	public int getCurrAssignCount() {
		return currAssignCount;
	}

	public int getAssignDays() {
		return assignDays;
	}

	public int getAssignType() {
		return assignType;
	}

	public String getRoleName() {
		return roleName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}