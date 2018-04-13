package com.junyou.bus.shouchong.filter;

import com.junyou.bus.shouchong.entity.RefbRoleShouchong;
import com.kernel.data.dao.IQueryFilter;

/**
 * 根据子活动ID查询首充记录
 * @author DaoZheng Yuan
 * 2015年5月19日 下午9:31:40
 */
public class RefbShouCongFilter implements IQueryFilter<RefbRoleShouchong>{

	/**
	 * 子活动ID
	 */
	private int subId;
	
	private boolean found = false;
	
	public RefbShouCongFilter(int subId){
		this.subId = subId;
	}
	
	@Override
	public boolean check(RefbRoleShouchong entity) {
		boolean flag = entity.getSubActivityId().intValue() == subId;
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
