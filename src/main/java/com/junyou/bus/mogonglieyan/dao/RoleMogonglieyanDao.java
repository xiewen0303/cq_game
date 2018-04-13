package com.junyou.bus.mogonglieyan.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.mogonglieyan.entity.RoleMogonglieyan;

@Repository
public class RoleMogonglieyanDao extends BusAbsCacheDao<RoleMogonglieyan> implements IDaoOperation<RoleMogonglieyan> {

    public List<RoleMogonglieyan> initRoleMogonglieyan(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}