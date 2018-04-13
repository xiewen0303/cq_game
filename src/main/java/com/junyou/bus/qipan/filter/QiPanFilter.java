package com.junyou.bus.qipan.filter;

import com.junyou.bus.qipan.entity.Qipan;
import com.kernel.data.dao.IQueryFilter;

/**
 * 棋盘数据过滤器
 * @author ZHONGDIAN
 * @date 2015-1-31 下午6:15:04 
 */
public class QiPanFilter implements IQueryFilter<Qipan>{

	private Integer subId;
	
	public QiPanFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(Qipan entity) {
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
