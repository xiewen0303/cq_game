package com.junyou.bus.touzi.dao.filter;

import com.junyou.bus.touzi.entity.RoleTouzi;
import com.kernel.data.dao.IQueryFilter;

/**
 * 投资计划过滤器
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-6-8 下午4:04:44
 */
public class RoleTouziFilter implements IQueryFilter<RoleTouzi>{

	private Integer configId;
	
	public RoleTouziFilter(Integer configId){
		this.configId = configId;
	}
	@Override
	public boolean check(RoleTouzi entity) {
		if(entity.getConfigId().equals(configId)){
			return true;
		}
		return false;
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
