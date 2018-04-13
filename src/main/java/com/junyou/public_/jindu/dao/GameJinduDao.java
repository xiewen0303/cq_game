package com.junyou.public_.jindu.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.constants.GameConstants;
import com.junyou.public_.jindu.entity.GameJindu;
import com.junyou.public_.share.dao.PublicAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class GameJinduDao extends PublicAbsCacheDao<GameJindu> implements IDaoOperation<GameJindu> {

	public List<GameJindu> initGameJindu() {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		
		return getRecords(queryParams, GameConstants.DEFAULT_ROLE_ID, AccessType.getDirectDbType());
	}
}