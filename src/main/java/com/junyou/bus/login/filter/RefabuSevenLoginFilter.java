package com.junyou.bus.login.filter;

import com.junyou.bus.login.entity.RefabuSevenLogin;
import com.kernel.data.dao.IQueryFilter;

/**
 * 过滤器
 * @author ZHONGDIAN
 * @date 2015-1-31 下午6:15:04 
 */
public class RefabuSevenLoginFilter implements IQueryFilter<RefabuSevenLogin>{

	private Integer subId;
	
	public RefabuSevenLoginFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuSevenLogin entity) {
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
