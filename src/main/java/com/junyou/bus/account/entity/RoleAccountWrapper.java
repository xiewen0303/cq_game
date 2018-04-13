package com.junyou.bus.account.entity;

import com.junyou.bus.account.entity.RoleAccount;

/**
 * 角色货币Wrapper
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-15 下午3:44:57 
 */
public class RoleAccountWrapper {
	private RoleAccount roleAccount;
	
	public RoleAccountWrapper(RoleAccount roleAccount){
		this.roleAccount = roleAccount;
	}
	
	/**
	 * 绑定元宝（礼券）
	 * @return
	 */
	public long getBindYb(){
		return roleAccount.getBindYb();
	}
	
	/**
	 * 金币
	 * @return
	 */
	public long getJb(){
		return roleAccount.getJb();
	}
	
	/**
	 * 元宝
	 * @return
	 */
	public long getYb(){
		return roleAccount.getReYb() + roleAccount.getNoReYb();
	}
	
	public String getUserId(){
		return roleAccount.getUserId();
	}
	
	public String getServerId(){
		return roleAccount.getServerId();
	}
}
