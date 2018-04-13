package com.junyou.bus.tuangou.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.tuangou.entity.RefbRoleTuangou;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefbRoleTuangouDao extends BusAbsCacheDao<RefbRoleTuangou> implements IDaoOperation<RefbRoleTuangou> {

	public List<RefbRoleTuangou> initRefbRoleTuangou(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	public List<RefbRoleTuangou> getRefbRoleTuangou() {
		
		return getRecords(null, null, AccessType.getDirectDbType());
	}
	
	/**
	 * 根据subId 删除活动数据
	 * @param subId
	 * @param maxSize
	 * @return
	 */
	public void dbDeleteBySubId() {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String,Object>();
		
		delete("deleteTuanGouBySubId", queryParams);
	}
	
	public List<Integer> selectAllDianShuBySubId(Integer subId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("subId", subId);
		
		return query("selectAllDianShuBySubId", queryParams);
	}
	
	/**
	 * 根据子活动ID和数字查库
	 * @param subId
	 * @param num
	 * @return
	 */
	public List<RefbRoleTuangou> getTuanGouBySubIdAndNum(Integer subId,Integer dianShu){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("subId", subId);
		queryParams.put("dianShu", dianShu);
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
	
	
	
}