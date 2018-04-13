package com.junyou.bus.setting.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.setting.entity.RoleSetting;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

/**
 * 角色设置（快捷键等）
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-16 下午5:11:44
 */
@Repository
public class RoleSettingDao extends BusAbsCacheDao<RoleSetting> implements IDaoOperation<RoleSetting> {

	public List<RoleSetting> initRoleSetting(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}