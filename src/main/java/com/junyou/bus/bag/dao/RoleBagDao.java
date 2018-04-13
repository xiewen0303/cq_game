package com.junyou.bus.bag.dao;

 
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:36:39
 *@Description: 
 */

@Repository
public class RoleBagDao extends BusAbsCacheDao<RoleItem> {

	public List<RoleItem> initAll(long userRoleId) {
		QueryParamMap<String, Object> map = new QueryParamMap<String, Object>();
		map.put("userRoleId", userRoleId);
		map.put("isDelete", 0); 
		return getRecords(map, userRoleId,AccessType.getDirectDbType());
	}

    /**
     * 查询暗金装备统计日志列表
     * @param goodsIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> queryAnjinEquipCalcList(List<String> goodsIds) {
        QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("goodsIds", goodsIds);
        return query("queryAnjinEquipCalcList", queryParams);
    }
	 

    /**
     * 查询神武装备统计日志列表
     * @param goodsIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> querySwEquipCalcList(List<String> goodsIds) {
        QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("goodsIds", goodsIds);
        return query("querySwEquipCalcList", queryParams);
    }
}
