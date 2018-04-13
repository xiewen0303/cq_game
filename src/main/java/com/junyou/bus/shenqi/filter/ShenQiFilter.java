package com.junyou.bus.shenqi.filter;

import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.kernel.data.dao.IQueryFilter;

public class ShenQiFilter implements IQueryFilter<ShenQiInfo> {
	private Integer shenQiId;
	
	public ShenQiFilter(Integer shenQiId){
		this.shenQiId=shenQiId;
	}
	
	@Override
	public boolean check(ShenQiInfo entity) {
		return entity.getShenQiId().equals(shenQiId); 
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
