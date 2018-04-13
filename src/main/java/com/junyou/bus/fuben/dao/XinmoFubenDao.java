package com.junyou.bus.fuben.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.fuben.entity.XinmoFuben;

@Repository
public class XinmoFubenDao extends BusAbsCacheDao<XinmoFuben> implements IDaoOperation<XinmoFuben> {

    public List<XinmoFuben> initXinmoFuben(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}