package com.junyou.kuafumatch.manager;

import com.junyou.context.GameServerContext;

public class KuafuRoleLoginStateHelper {
	
	public static void roleOnline(Long userRoleId) {
		GameServerContext.getPublicRoleStateService().change2PublicOnline(userRoleId);
	}
}
