package com.junyou.bus.happycard.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.happycard.entity.RefabuHappyCard;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuHappyCardDao extends BusAbsCacheDao<RefabuHappyCard> implements IDaoOperation<RefabuHappyCard> {

	public List<RefabuHappyCard> initRefabuHappyCard(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}