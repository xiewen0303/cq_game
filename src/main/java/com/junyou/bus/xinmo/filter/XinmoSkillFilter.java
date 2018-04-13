package com.junyou.bus.xinmo.filter;

import com.junyou.bus.xinmo.entity.RoleXinmoSkill;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description  心魔技能数据过滤类
 *@Author Yang Gao
 *@Since 2016-5-11
 *@Version 1.1.0
 */
public class XinmoSkillFilter implements IQueryFilter<RoleXinmoSkill>{
    
	private Integer type;
	
	public XinmoSkillFilter(Integer type){
		this.type = type;
	}
	
	@Override
	public boolean check(RoleXinmoSkill entity) {
	    Integer xm_type = entity.getXinmoType();
        if (null != xm_type && xm_type.equals(type)) {
            return true;
        }
        return false;
	}

	@Override
	public boolean stopped() {
	    return false;
	}

}
