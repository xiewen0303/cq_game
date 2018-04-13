package com.junyou.bus.equip.filter;

import com.junyou.bus.equip.entity.RoleXiangwei;
import com.kernel.data.dao.IQueryFilter;

/**   
 * 玩家头衔IdFilter过滤器
 * @author LiNing  
 * @version  2014-4-25 下午2:19:28  
 * 
 */
public class RoleXiangweiFilter implements IQueryFilter<RoleXiangwei> {
	
	private Integer buwei;
	
	
	public RoleXiangweiFilter(Integer buwei) {
		this.buwei = buwei;
	}
	
	@Override
	public boolean check(RoleXiangwei entity) {
		
		boolean flag = (entity.getBuwei().equals(buwei));
		
		if(flag){
			return true;
		}
		
		return false;
	}

	@Override
	public boolean stopped() {
		return false;
	}
}
