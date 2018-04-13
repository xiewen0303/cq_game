package com.junyou.bus.rolestage.dao;

import org.springframework.stereotype.Repository;

import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;


@Repository
public class RoleStageDao extends BusAbsCacheDao<RoleStage> implements IDaoOperation<RoleStage> {

	/**
	 * 创建场景直接访问库（创建角色时则创建）
	 * @param roleStage
	 * @param userRoleId
	 */
	public void createRoleStageFromDb(RoleStage roleStage, Long userRoleId) {
		insert(roleStage, userRoleId, AccessType.getDirectDbType());
	}
	
	/**
	 * 获取场景直接访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleStage getRoleStageFromDb(Long userRoleId) {
		return load(userRoleId, userRoleId, AccessType.getDirectDbType());
	}
}