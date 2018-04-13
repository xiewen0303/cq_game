package com.junyou.bus.shenqi.filter;

import com.junyou.bus.shenqi.entity.ShenQiEquip;
import com.kernel.data.dao.IQueryFilter;

public class ShenQiEquipFilter implements IQueryFilter<ShenQiEquip> {
	private Integer shenQiId;
	private Integer slot;
	
	public ShenQiEquipFilter(Integer shenQiId,Integer slot){
		this.shenQiId=shenQiId;
		this.slot = slot;
	}
	
	@Override
	public boolean check(ShenQiEquip entity) {
		if(slot == null){
			return entity.getShenQiId().equals(shenQiId);
		}
		return entity.getShenQiId().equals(shenQiId) && entity.getSlot().equals(slot); 
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
