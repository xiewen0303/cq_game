package com.junyou.bus.yabiao.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.yabiao.entity.Yabiao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

/**
 * 押镖Dao
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-13 上午11:09:19
 */
@Repository
public class YabiaoDao extends BusAbsCacheDao<Yabiao> implements IDaoOperation<Yabiao> {
	public List<Yabiao> initYabiao(Long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);

		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}