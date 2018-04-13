package com.junyou.bus.email.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.email.entity.EmailRelation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.constants.GameConstants;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class EmailRelationDao extends BusAbsCacheDao<EmailRelation> implements IDaoOperation<EmailRelation> {

	public List<EmailRelation> initEmailRelation(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	public void delEmailRelationByIds(String emailIds){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("ids", emailIds);
		delete("deleteEmailRelationByIds", queryParams);
	}
	
	public Integer selectEmailCount(Long userRoleId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		queryParams.put("isDelete", GameConstants.EMAIL_NOT_DEL);
		@SuppressWarnings("rawtypes")
		List list = query("selectRecordsCountEmailRelation", queryParams);
		if(list != null && list.size() > 0){
			return (Integer)list.get(0);
		}
		return 0;
	}
}