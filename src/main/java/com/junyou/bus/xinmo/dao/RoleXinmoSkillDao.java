package com.junyou.bus.xinmo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xinmo.entity.RoleXinmoSkill;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleXinmoSkillDao extends BusAbsCacheDao<RoleXinmoSkill> implements IDaoOperation<RoleXinmoSkill> {

    public List<RoleXinmoSkill> initRoleXinmoSkill(Long userRoleId) {
        QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
        queryParams.put("userRoleId", userRoleId);
        return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
    }
}