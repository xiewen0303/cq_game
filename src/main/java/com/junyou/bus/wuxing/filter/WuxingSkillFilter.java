package com.junyou.bus.wuxing.filter;

import com.junyou.bus.wuxing.entity.RoleWuxingSkill;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description  五行魔神技能数据过滤类
 *@Author Yang Gao
 *@Since 2016-5-11
 *@Version 1.1.0
 */
public class WuxingSkillFilter implements IQueryFilter<RoleWuxingSkill>{
    
	private Integer type;
	
	public WuxingSkillFilter(Integer type){
		this.type = type;
	}
	
	@Override
	public boolean check(RoleWuxingSkill entity) {
	    Integer wx_type = entity.getWuxingType();
        if (null != wx_type && wx_type.intValue() == type) {
            return true;
        }
        return false;
	}

	@Override
	public boolean stopped() {
	    return false;
	}

}
