package com.junyou.bus.jueban.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.jueban.entity.RefabuJueban;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RefabuJuebanDao extends BusAbsCacheDao<RefabuJueban> implements IDaoOperation<RefabuJueban> {

    public List<RefabuJueban> initRefabuJueban(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}