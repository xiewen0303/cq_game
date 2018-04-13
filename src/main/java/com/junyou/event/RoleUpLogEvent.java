package com.junyou.event;

import com.junyou.log.LogPrintHandle;



/**
 * 角色升级日志事件
 * @author DaoZheng Yuan
 * 2014年11月28日 下午4:55:06
 */
public class RoleUpLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;
	
	private long roleId;
	private String roleName;
	private int beforeLevel;
	private int afterLevel;
	
	public RoleUpLogEvent(long roleId,String roleName,int beforeLevel,int afterLevel) {
		super(LogPrintHandle.ROLE_UP_LEVEL);
		
		this.roleId = roleId;
		this.roleName = roleName;
		this.beforeLevel = beforeLevel;
		this.afterLevel = afterLevel;
	}
	
	

	public long getRoleId() {
		return roleId;
	}



	public String getRoleName() {
		return roleName;
	}



	public int getBeforeLevel() {
		return beforeLevel;
	}



	public int getAfterLevel() {
		return afterLevel;
	}

}
