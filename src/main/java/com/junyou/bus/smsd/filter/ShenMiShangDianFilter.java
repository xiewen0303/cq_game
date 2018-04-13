package com.junyou.bus.smsd.filter;

import com.junyou.bus.smsd.entity.ShenmiShangdian;
import com.kernel.data.dao.IQueryFilter;

/**
 * 棋盘数据过滤器
 * @author ZHONGDIAN
 * @date 2015-1-31 下午6:15:04 
 */
public class ShenMiShangDianFilter implements IQueryFilter<ShenmiShangdian>{

	private Integer subId;
	
	public ShenMiShangDianFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(ShenmiShangdian entity) {
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
