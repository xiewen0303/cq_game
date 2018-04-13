package com.junyou.bus.jinfeng.dao;

import org.springframework.stereotype.Component;

import com.junyou.bus.jinfeng.entity.FengHao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;


@Component
public class FengHaoDao extends AbsDao<FengHao> {

	public Object insert2DB(FengHao fengHao) {
		return insert(fengHao, fengHao.getUserRoleId(), AccessType.getDirectDbType());
	}

	public FengHao selectFromDB(Long roleId) {
		return load(roleId, roleId, AccessType.getDirectDbType());
	}

	public Object deleteFromDB(Long roleId) {
		return delete(roleId, roleId, AccessType.getDirectDbType());
	}

	public int updateFromDB(FengHao fengHao) {
		return update(fengHao, fengHao.getUserRoleId(), AccessType.getDirectDbType());
	}

}