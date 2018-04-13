package com.junyou.bus.wuxing.filter;

import com.junyou.bus.wuxing.entity.RoleWuxing;
import com.kernel.data.dao.IQueryFilter;

/**
 * 
 *@Description 五行数据过滤器
 *@Author Yang Gao
 *@Since 2016-8-4
 *@Version 1.1.0
 */
public class WuXingFilter implements IQueryFilter<RoleWuxing>{

	private Integer type;
	
	public WuXingFilter(Integer type){
		this.type = type;
	}
	@Override
	public boolean check(RoleWuxing entity) {
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
