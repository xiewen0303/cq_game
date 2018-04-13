package com.junyou.bus.lunpan.filter;

import com.junyou.bus.lunpan.entity.RefabuLunpan;
import com.kernel.data.dao.IQueryFilter;

/**
 * 
 *@Description 充值轮盘数据过滤器
 *@Author Yang Gao
 *@Since 2016-6-4
 *@Version 1.1.0
 */
public class LunPanFilter implements IQueryFilter<RefabuLunpan>{

	private Integer subId;
	
	public LunPanFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuLunpan entity) {
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
