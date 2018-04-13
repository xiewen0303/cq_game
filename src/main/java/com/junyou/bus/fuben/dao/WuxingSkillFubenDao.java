package com.junyou.bus.fuben.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.fuben.entity.WuxingSkillFuben;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class WuxingSkillFubenDao extends BusAbsCacheDao<WuxingSkillFuben> implements IDaoOperation<WuxingSkillFuben> {

    public List<WuxingSkillFuben> initWuxingSkillFuben(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}