package com.junyou.bus.xunbao.filter;

import com.junyou.bus.xunbao.entity.RefbXunbao;
import com.kernel.data.dao.IQueryFilter;

public class RefbXunBaoFilter implements IQueryFilter<RefbXunbao>{

	private Integer subId;
	
	public RefbXunBaoFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefbXunbao entity) {
		if(entity.getSubId().equals(subId)){
			return true;
		}
		return false;
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
