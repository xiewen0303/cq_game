package com.junyou.bus.role;

import com.junyou.bus.role.entity.UserRole;

/**
 * 玩家OutWrapper
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-9 下午5:31:17 
 */
public class UserRoleOutputWrapper {
	
	/**ChooseRoleVO 玩家角色信息Vo**/
	public static Object[] chooseRoleVO(UserRole userRole){
		return new Object[]{userRole.getConfigId(), userRole.getId(), userRole.getName(), userRole.getLevel(),userRole.getIsGm()};
	}
}
