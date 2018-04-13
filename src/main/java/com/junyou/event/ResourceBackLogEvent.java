package com.junyou.event;

import java.util.Set;

import com.junyou.log.LogPrintHandle;

/**
 * 资源找回日志
 * @author chenjianye
 * @date 2015-4-20
 */
public class ResourceBackLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public ResourceBackLogEvent(long userRoleId,String roleName,long money,long exp,Set<Integer> backType,int cashOrFree) {
		super(LogPrintHandle.RESOURCE_BACK);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.money = money;
		this.exp = exp;
		this.backType = backType;
		this.cashOrFree = cashOrFree;
	}

	private long userRoleId;
	private String roleName;
	private long money;
	private long exp;
	private Set<Integer> backType;
	private int cashOrFree;

	public long getUserRoleId() {
		return userRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public long getMoney() {
		return money;
	}

	public long getExp() {
		return exp;
	}

	public Set<Integer> getBackType() {
		return backType;
	}

	public int getCashOrFree() {
		return cashOrFree;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}