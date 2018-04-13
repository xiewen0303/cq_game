package com.junyou.bus.platform.qq.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.qq.entity.TencentYaoqing;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class TencentYaoqingDao extends BusAbsCacheDao<TencentYaoqing> implements IDaoOperation<TencentYaoqing> {


	public Integer getYaoQingCount(String iopenId,Integer level){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("iopenId", iopenId);
		queryParams.put("level", level);
		
		
		List<TencentYaoqing> ty =  query("selectSeccessYaoQingCount", queryParams);
		if(ty == null){
			return 0;
		}
		return ty.size();
	}
	
}