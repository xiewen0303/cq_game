package com.junyou.bus.shoplimit.dao;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.shoplimit.entity.ShopLimitInfo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class ShopLimitInfoDao extends BusAbsCacheDao<ShopLimitInfo> implements IDaoOperation<ShopLimitInfo> {
	
	public ShopLimitInfo initShopLimitInfo(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}