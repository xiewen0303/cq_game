package com.junyou.bus.daytask.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.junyou.bus.daytask.entity.TaskDay;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class TaskDayDao extends BusAbsCacheDao<TaskDay> implements IDaoOperation<TaskDay> {

	public List<TaskDay> initAll(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	} 
}