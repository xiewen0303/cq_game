package com.junyou.bus.wuxing.filter;

import com.junyou.bus.wuxing.entity.TangbaoWuxingSkill;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description  糖宝五行魔神技能数据过滤类
 *@Author Yang Gao
 *@Since 2016-6-13
 *@Version 1.1.0
 */
public class TangbaoWuxingSkillFilter implements IQueryFilter<TangbaoWuxingSkill>{
    
	private Integer type;
	
	public TangbaoWuxingSkillFilter(Integer type){
		this.type = type;
	}
	
	@Override
	public boolean check(TangbaoWuxingSkill entity) {
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
