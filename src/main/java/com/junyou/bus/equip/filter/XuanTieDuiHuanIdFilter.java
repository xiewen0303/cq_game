package com.junyou.bus.equip.filter;

import com.junyou.bus.equip.entity.XuantieDuihuan;
import com.kernel.data.dao.IQueryFilter;

/**   
 * 玩家头衔IdFilter过滤器
 * @author LiNing  
 * @version  2014-4-25 下午2:19:28  
 * 
 */
public class XuanTieDuiHuanIdFilter implements IQueryFilter<XuantieDuihuan> {
	
	private Integer configId;
	
	
	public XuanTieDuiHuanIdFilter(Integer configId) {
		this.configId = configId;
	}
	
	@Override
	public boolean check(XuantieDuihuan entity) {
		
		boolean flag = (entity.getConfigId().equals(configId));
		
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
