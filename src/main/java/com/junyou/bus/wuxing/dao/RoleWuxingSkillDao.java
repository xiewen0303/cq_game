package com.junyou.bus.wuxing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.wuxing.entity.RoleWuxingSkill;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleWuxingSkillDao extends BusAbsCacheDao<RoleWuxingSkill> implements IDaoOperation<RoleWuxingSkill> {

    public List<RoleWuxingSkill> initRoleWuxingSkill(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);

        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}