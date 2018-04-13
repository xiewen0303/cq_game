package com.junyou.bus.wuxing.filter;

import com.junyou.bus.wuxing.entity.TangbaoWuxing;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description 糖宝五行魔神过滤器
 *@Author Yang Gao
 *@Since 2016-6-8
 *@Version 1.1.0
 */
public class TangbaoWuXingFilter implements IQueryFilter<TangbaoWuxing>{

	private Integer type;
	
	public TangbaoWuXingFilter(Integer type){
		this.type = type;
	}
	@Override
	public boolean check(TangbaoWuxing entity) {
		if(entity.getWuxingType() == type.intValue()){
			return true;
		}
		return false;
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
