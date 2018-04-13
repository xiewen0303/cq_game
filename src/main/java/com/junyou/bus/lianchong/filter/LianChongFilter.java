package com.junyou.bus.lianchong.filter;

import com.junyou.bus.lianchong.entity.RoleLianchong;
import com.kernel.data.dao.IQueryFilter;

/**
 * @date 2015-1-31 下午6:15:04 
 */
public class LianChongFilter implements IQueryFilter<RoleLianchong>{

	private Integer subId;
	
	public LianChongFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RoleLianchong entity) {
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
