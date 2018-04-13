package com.junyou.bus.leichong.filter;

import com.junyou.bus.leichong.entity.Leichong;
import com.kernel.data.dao.IQueryFilter;

/**
 * 棋盘数据过滤器
 * @author zhongdian
 * @date 2015-1-31 下午6:15:04 
 */
public class LeiChongFilter implements IQueryFilter<Leichong>{

	private Integer subId;
	
	public LeiChongFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(Leichong entity) {
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
