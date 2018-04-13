package com.junyou.bus.extremeRecharge.filter;

import com.junyou.bus.extremeRecharge.entity.RfbExtremeRecharge;
import com.kernel.data.dao.IQueryFilter;

public class RfbExtremeRechargeFilter implements IQueryFilter<RfbExtremeRecharge>{
	private Integer subId;
	
	public RfbExtremeRechargeFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RfbExtremeRecharge entity) {
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
