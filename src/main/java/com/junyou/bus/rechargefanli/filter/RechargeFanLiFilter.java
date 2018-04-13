package com.junyou.bus.rechargefanli.filter;

import com.junyou.bus.rechargefanli.entity.RefabuRefanli;
import com.kernel.data.dao.IQueryFilter;

/**
 * 棋盘数据过滤器
 * @author ZHONGDIAN
 * @date 2015-1-31 下午6:15:04 
 */
public class RechargeFanLiFilter implements IQueryFilter<RefabuRefanli>{

	private Integer subId;
	
	public RechargeFanLiFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuRefanli entity) {
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
