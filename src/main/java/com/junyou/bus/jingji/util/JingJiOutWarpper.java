package com.junyou.bus.jingji.util;

import com.junyou.bus.jingji.entity.RoleJingji;

public class JingJiOutWarpper {
	public static Object[] getRoleJingjiVo(RoleJingji roleJingji){
		return new Object[]{
				roleJingji.getUserRoleId(),
				roleJingji.getName(),
				roleJingji.getLevel(),
				roleJingji.getZplus(),
				roleJingji.getRank(),
				roleJingji.getConfigId(),
				roleJingji.getZuoqi()
		};
	}
}
