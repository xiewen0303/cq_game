package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 激活称号日志
 * 
 * @author dsh
 * @date 2015-6-5
 */
public class ActivateChenghaoLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private int id;
	public ActivateChenghaoLogEvent(long userRoleId, int id) {
		super(LogPrintHandle.CHENHGAO_ACTIVATE);
		this.userRoleId = userRoleId;
		this.id = id;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}