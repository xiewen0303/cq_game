package com.junyou.bus.giftcard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.giftcard.entity.GiftCardPlatform;
import com.junyou.bus.share.dao.BusAbsCacheDao;


@Repository
public class GiftCardPlatformDao extends BusAbsCacheDao<GiftCardPlatform> implements IDaoOperation<GiftCardPlatform> {

	public List<GiftCardPlatform> initGiftCardPlatform(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}