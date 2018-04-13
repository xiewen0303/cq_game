package com.junyou.bus.tanbao.filter;

import com.junyou.bus.tanbao.entity.RefabuTanbao;
import com.kernel.data.dao.IQueryFilter;

/**
 * 探索宝藏数据过滤器
 * @author zhongdian
 * @date 2015-1-31 下午6:15:04 
 */
public class TanSuoBaoZangFilter implements IQueryFilter<RefabuTanbao>{

	private Integer subId;
	
	public TanSuoBaoZangFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuTanbao entity) {
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
