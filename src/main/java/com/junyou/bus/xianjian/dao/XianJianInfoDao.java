package com.junyou.bus.xianjian.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.vo.XianJianRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class XianJianInfoDao extends BusAbsCacheDao<XianJianInfo> implements IDaoOperation<XianJianInfo> {

	public XianJianInfo initXianJianInfo(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}
 
	public XianJianInfo dbLoadXianJianInfo(long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		List<XianJianInfo> list =  getRecords(queryParams);
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取等级大于level的所有玩家ID
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getRoleIdByLevel(int level) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("level", level);
		return query("selectByParamsXianJianLevel", queryParams);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<XianJianRankVo> getXianJianRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectXianJianRankToChiBangRankVo", queryParams);
	
	}
}