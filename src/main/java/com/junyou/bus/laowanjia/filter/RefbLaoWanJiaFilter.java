package com.junyou.bus.laowanjia.filter;

import com.junyou.bus.laowanjia.entity.RefbLaowanjia;
import com.kernel.data.dao.IQueryFilter;

/**
 * 棋盘数据过滤器
 * @author zhongdian
 * @date 2015-1-31 下午6:15:04 
 */
public class RefbLaoWanJiaFilter implements IQueryFilter<RefbLaowanjia>{

	private Integer subId;
	
	public RefbLaoWanJiaFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefbLaowanjia entity) {
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
