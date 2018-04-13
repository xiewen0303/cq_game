package com.junyou.bus.xiuxian.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.xiuxian.entity.RefbXiuxian;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefbXiuxianDao extends AbsDao<RefbXiuxian> implements IDaoOperation<RefbXiuxian> {
	
	@SuppressWarnings("unchecked")
	public List<RefbXiuxian> loadRefbXiuxianBySubId(int subId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("subId", subId);
		
		return (List<RefbXiuxian>)query("selectMultiRefbXiuxian",queryParams);
	}
	
	public void insertRefbXiuxian(RefbXiuxian refbXiuxian){
		insert(refbXiuxian, refbXiuxian.getId(), AccessType.getDirectDbType());
	}
	
	public void updateRefbXiuxian(RefbXiuxian refbXiuxian){
		update(refbXiuxian, refbXiuxian.getId(), AccessType.getDirectDbType());
	}
}