package com.junyou.bus.xiuxian.filter;

import com.junyou.bus.xiuxian.entity.RefbRoleXiuxian;
import com.kernel.data.dao.IQueryFilter;

/**
 * 根据子活动id和具体配置id查询
 * @author DaoZheng Yuan
 * 2015年6月7日 上午11:36:40
 */
public class RefbXiuXianFilter  implements IQueryFilter<RefbRoleXiuxian>{

	/**
	 * 子活动ID
	 */
	private int subId;
	/**
	 * 具体配置id
	 */
	private int configId;
	
	private boolean found = false;
	
	public RefbXiuXianFilter(int subId, int configId) {
		this.subId = subId;
		this.configId = configId;
	}
	
	
	@Override
	public boolean check(RefbRoleXiuxian entity) {
		boolean flag = entity.getSubId().intValue() == subId && entity.getConfigId().intValue() == configId;
		if(flag){
			found = true;
		}
		return flag;
	}



	@Override
	public boolean stopped() {
		return found;
	}

}
