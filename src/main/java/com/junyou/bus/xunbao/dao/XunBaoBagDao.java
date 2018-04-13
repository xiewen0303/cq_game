package com.junyou.bus.xunbao.dao;

 
import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xunbao.entity.XunBaoBag;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:36:39
 *@Description: 
 */

@Repository
public class XunBaoBagDao extends BusAbsCacheDao<XunBaoBag> {

	public List<XunBaoBag> initAll(long userRoleId) {
		QueryParamMap<String, Object> map = new QueryParamMap<String, Object>();
		map.put("userRoleId", userRoleId);
		return getRecords(map, userRoleId,AccessType.getDirectDbType());
	}
 
	 

}
