package com.junyou.stage.model.stage.zhengbasai;

import com.junyou.stage.model.element.role.IRole;

public class HcZhengBaSaiRoleOnline {
	private IRole role;
	/** 进入时间 */
	private long enterTime = 0L;
	/** 离开时长 */
	private long leaveTime = 0L;

	public IRole getRole() {
		return role;
	}

	public void setRole(IRole role) {
		this.role = role;
	}

	public long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
	}

}
