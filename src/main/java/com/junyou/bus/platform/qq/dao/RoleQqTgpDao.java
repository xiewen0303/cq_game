package com.junyou.bus.platform.qq.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.qq.entity.RoleQqTgp;
import com.junyou.bus.platform.qq.vo.UserRoleVo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.constants.GameConstants;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleQqTgpDao extends BusAbsCacheDao<RoleQqTgp> implements IDaoOperation<RoleQqTgp> {

	public List<RoleQqTgp> initRoleQqTgp(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	/**
	 * 根据角色名查询角色信息
	 * @param roleName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UserRoleVo getUserRoleVoByParams(String roleName){
		
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("roleName", roleName);
		queryParams.put("isDel", GameConstants.NOT_IS_DEL);
		 
		 List<UserRoleVo> friendVos = query("selectUserRoleVoByParams", queryParams);
		 if(friendVos!=null && friendVos.size()>0){
			 return friendVos.get(0);
		 }
		 
		return null;
	}
}