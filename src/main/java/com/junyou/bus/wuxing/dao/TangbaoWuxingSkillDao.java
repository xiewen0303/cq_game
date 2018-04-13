package com.junyou.bus.wuxing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.wuxing.entity.TangbaoWuxingSkill;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class TangbaoWuxingSkillDao extends BusAbsCacheDao<TangbaoWuxingSkill> implements IDaoOperation<TangbaoWuxingSkill> {

    public List<TangbaoWuxingSkill> initTangbaoWuxingSkill(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}