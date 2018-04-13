package com.junyou.bus.xingkongbaozang.filter;

import com.junyou.bus.xingkongbaozang.entity.RefabuXkbz;
import com.kernel.data.dao.IQueryFilter;

/**
 * 棋盘数据过滤器
 * @author zhongdian
 * @date 2015-1-31 下午6:15:04 
 */
public class XkbzFilter implements IQueryFilter<RefabuXkbz>{

	private Integer subId;
	
	public XkbzFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuXkbz entity) {
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
