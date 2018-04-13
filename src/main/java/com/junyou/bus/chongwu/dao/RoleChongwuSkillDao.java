package com.junyou.bus.chongwu.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.chongwu.entity.RoleChongwuSkill;


@Repository
public class RoleChongwuSkillDao extends BusAbsCacheDao<RoleChongwuSkill> implements IDaoOperation<RoleChongwuSkill> {

	public List<RoleChongwuSkill> initRoleChongwuSkill(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}