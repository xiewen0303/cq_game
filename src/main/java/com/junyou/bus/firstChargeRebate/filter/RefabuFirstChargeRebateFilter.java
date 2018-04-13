package com.junyou.bus.firstChargeRebate.filter;

import com.junyou.bus.firstChargeRebate.entity.RefabuFirstChargeRebate;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description 首冲返利数据过滤器
 *@Author Yang Gao
 *@Since 2016-6-6
 *@Version 1.1.0
 */
public class RefabuFirstChargeRebateFilter implements IQueryFilter<RefabuFirstChargeRebate>{

	private Integer subId;
	
	public RefabuFirstChargeRebateFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuFirstChargeRebate entity) {
		if(entity.getSubId().intValue() == subId){
			return true;
		}
		return false;
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
