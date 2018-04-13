package com.junyou.bus.branchtask.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.branchtask.entity.TaskBranch;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class TaskBranchDao extends BusAbsCacheDao<TaskBranch> implements IDaoOperation<TaskBranch> {

	public List<TaskBranch> initAll(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	} 
}