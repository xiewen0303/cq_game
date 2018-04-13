package com.junyou.bus.xinmo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xinmo.entity.RoleXinmoMoshen;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleXinmoMoshenDao extends BusAbsCacheDao<RoleXinmoMoshen> implements IDaoOperation<RoleXinmoMoshen> {

    public List<RoleXinmoMoshen> initRoleXinmoMoshen(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}