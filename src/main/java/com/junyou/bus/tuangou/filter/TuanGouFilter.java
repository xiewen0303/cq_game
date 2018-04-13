package com.junyou.bus.tuangou.filter;

import com.junyou.bus.tuangou.entity.RefbRoleTuangou;
import com.kernel.data.dao.IQueryFilter;

/**
 * 团购数据过滤器
 * @author ZHONGDIAN
 * @date 2015-1-31 下午6:15:04 
 */
public class TuanGouFilter implements IQueryFilter<RefbRoleTuangou>{

	private Integer subId;
	
	public TuanGouFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefbRoleTuangou entity) {
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
