package com.junyou.bus.zhuanpan.filter;

import com.junyou.bus.zhuanpan.entity.RefabuZhuanpan;
import com.kernel.data.dao.IQueryFilter;

/**
 * 转盘数据过滤器
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-31 下午6:15:04 
 */
public class ZhuanPanFilter implements IQueryFilter<RefabuZhuanpan>{

	private Integer subId;
	
	public ZhuanPanFilter(Integer subId){
		this.subId = subId;
	}
	@Override
	public boolean check(RefabuZhuanpan entity) {
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
