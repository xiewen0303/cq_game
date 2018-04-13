package com.junyou.bus.xianqi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xianqi.entity.XianqiFuben;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class XianqiFubenDao extends BusAbsCacheDao<XianqiFuben> implements IDaoOperation<XianqiFuben> {

    public List<XianqiFuben> initXianqiFuben(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);

        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}