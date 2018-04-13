package com.junyou.bus.extremeRecharge.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.extremeRecharge.entity.RfbExtremeRecharge;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RfbExtremeRechargeDao extends BusAbsCacheDao<RfbExtremeRecharge> implements IDaoOperation<RfbExtremeRecharge> {
    public List<RfbExtremeRecharge> initRfbExtremeRecharge(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}
