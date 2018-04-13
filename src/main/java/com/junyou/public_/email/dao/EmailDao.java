package com.junyou.public_.email.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.public_.email.entity.Email;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class EmailDao extends AbsDao<Email> implements IDaoOperation<Email> {

	public List<Email> initEmail(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	/**
	 * 
	 * @param roleCreateTime
	 * @param offlineTime	单位秒
	 * @return
	 */
	public List<Email> needCheckEmails(Long roleCreateTime,Long offlineTime) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("checkTime", roleCreateTime);
		queryParams.put("offlineTime", offlineTime);
		queryParams.put("quanfu", true);
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
	
	public void delEmailByIds(String emailIds){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("ids", emailIds);
		delete("deleteEmailByIds", queryParams);
	}

	@SuppressWarnings("unchecked")
	public List<Email> getEmailByTime(String content,String startTime,String endTime){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("content", content);
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);
		
		return query("selectSingleByParamsEmailByTime", queryParams);
	}
	@SuppressWarnings("unchecked")
	public List<Email> getEmailByTimeAndContent(String content,String createTime,Integer emailType){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("content", content);
		queryParams.put("createTime", createTime);
		queryParams.put("emailType", emailType);
		
		return query("selectEmailByTimeAndContent", queryParams);
	}
}