package com.junyou.bus.shouchong.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.shouchong.entity.RefbRoleShouchong;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


/**
 * 热发布首充
 * @author DaoZheng Yuan
 * 2015年5月19日 下午8:30:36
 */
@Repository
public class RefbRoleShouchongDao extends BusAbsCacheDao<RefbRoleShouchong> implements IDaoOperation<RefbRoleShouchong> {
	
	public List<RefbRoleShouchong> initRefbRoleShouchong(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}