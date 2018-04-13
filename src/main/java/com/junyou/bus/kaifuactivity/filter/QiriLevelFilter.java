package com.junyou.bus.kaifuactivity.filter;

import com.junyou.bus.kaifuactivity.entity.QiriLevel;
import com.kernel.data.dao.IQueryFilter;

/**
 * 热发布争霸活动过滤器
 * @author ZHONGDIAN
 * @date 2015-1-31 下午6:15:04 
 */
public class QiriLevelFilter implements IQueryFilter<QiriLevel>{

	private Integer subId;
	
	public QiriLevelFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(QiriLevel entity) {
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
