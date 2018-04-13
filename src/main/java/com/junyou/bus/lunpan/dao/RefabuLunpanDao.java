package com.junyou.bus.lunpan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.lunpan.entity.RefabuLunpan;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RefabuLunpanDao extends BusAbsCacheDao<RefabuLunpan> implements IDaoOperation<RefabuLunpan> {

    public List<RefabuLunpan> initRefabuLunpan(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}