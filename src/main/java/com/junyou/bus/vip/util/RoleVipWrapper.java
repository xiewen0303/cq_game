package com.junyou.bus.vip.util;

import com.junyou.bus.vip.entity.RoleVipInfo;

public class RoleVipWrapper {
	private RoleVipInfo roleVipInfo;
	public RoleVipWrapper(RoleVipInfo roleVipInfo){
		this.roleVipInfo = roleVipInfo;
	}
	
	public Integer getVipLevel(){
		return roleVipInfo.getVipLevel();
	}
	
	public Long getVipExp(){
		return roleVipInfo.getVipExp();
	}
	
	public Integer getVipId(){
		return roleVipInfo.getVipId();
	}
}
