package com.junyou.bus.bag.filter;

import com.junyou.bus.bag.entity.RoleItemUseCsxz; 
import com.kernel.data.dao.IQueryFilter; 

public class PropGoodsIdFilter implements IQueryFilter<RoleItemUseCsxz> {

	private int xzId;
	
	private boolean found = false;
	
	
	public PropGoodsIdFilter(int xzId) {
		this.xzId = xzId;
	}

	@Override
	public boolean check(RoleItemUseCsxz entity) {
		boolean flag = entity.getXzId().equals(xzId);
		if(flag){
			found = true;
		}
		
		return flag;
	}

	@Override
	public boolean stopped() {
		return found;
	}

}
