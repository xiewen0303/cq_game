package com.junyou.bus.shenqi.filter;

import com.junyou.bus.shenqi.entity.ShenQiJinjie;
import com.kernel.data.dao.IQueryFilter;

public class ShenQiJinJieFilter implements IQueryFilter<ShenQiJinjie> {
	private Integer shenQiId;
	
	public ShenQiJinJieFilter(Integer shenQiId){
		this.shenQiId=shenQiId;
	}
	
	@Override
	public boolean check(ShenQiJinjie entity) {
		return entity.getShenQiId().equals(shenQiId); 
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
