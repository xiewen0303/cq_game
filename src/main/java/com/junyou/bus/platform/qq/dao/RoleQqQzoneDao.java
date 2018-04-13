package com.junyou.bus.platform.qq.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.qq.entity.RoleQqQzone;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleQqQzoneDao extends BusAbsCacheDao<RoleQqQzone> implements IDaoOperation<RoleQqQzone> {

    public List<RoleQqQzone> initRoleQqQzone(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}