package com.junyou.bus.firstChargeRebate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.firstChargeRebate.entity.RefabuFirstChargeRebate;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RefabuFirstChargeRebateDao extends BusAbsCacheDao<RefabuFirstChargeRebate> implements IDaoOperation<RefabuFirstChargeRebate> {

    public List<RefabuFirstChargeRebate> initRefabuFirstChargeRebate(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}