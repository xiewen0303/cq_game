package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 改名日志
 */
public class ModifyNameLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private String beforeName;
	private String afterName;

	public ModifyNameLogEvent(long userRoleId, String beforeName,
			String afterName) {
		super(LogPrintHandle.USER_MODIFY_NAME);
		this.userRoleId = userRoleId;
		this.beforeName = beforeName;
		this.afterName = afterName;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public String getBeforeName() {
		return beforeName;
	}

	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}

	public String getAfterName() {
		return afterName;
	}

	public void setAfterName(String afterName) {
		this.afterName = afterName;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}