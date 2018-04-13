package com.junyou.bus.xiaofei.filter;

import com.junyou.bus.xiaofei.entity.RefabuXiaofei;
import com.kernel.data.dao.IQueryFilter;

/**
 * 消费数据过滤器
 * @author ZHONGDIAN
 * @date 2015-1-31 下午6:15:04 
 */
public class XiaoFeiFilter implements IQueryFilter<RefabuXiaofei>{

	private Integer subId;
	
	public XiaoFeiFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuXiaofei entity) {
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
