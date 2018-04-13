package com.junyou.bus.wuxing.filter;

import com.junyou.bus.wuxing.entity.RoleWuxingJingpoItem;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description  五行魔神精魄过滤类
 *@Author Yang Gao
 *@Since 2016-5-11
 *@Version 1.1.0
 */
public class WuxingJingpoFilter implements IQueryFilter<RoleWuxingJingpoItem>{
    
	private Long guid;
	private Integer position;
	private Integer slot;
	
	
	public WuxingJingpoFilter(Long guid, Integer position, Integer slot){
		this.guid = guid;
		this.position = position;
		this.slot = slot;
	}
	
	@Override
	public boolean check(RoleWuxingJingpoItem entity) {
        if (null != this.guid && null != entity.getGuid()) {
            return this.guid.longValue() == entity.getGuid().longValue();
        }
        if (null != this.position && null == this.slot) {
            return this.positionFlag(entity.getPosition());
        }
        if (null != this.slot && null == this.position) {
            return this.slotFlag(entity.getSlot());
        }
        if(null != this.position && null != this.slot){
            return this.positionFlag(entity.getPosition()) && this.slotFlag(entity.getSlot());
        }
        
        return false;
	}

	@Override
	public boolean stopped() {
	    return false;
	}

	private boolean positionFlag(Integer _position){
	    if(null == this.position || null == _position){
	        return false;
	    }
	    if(this.position.intValue() != _position.intValue()){
	        return false;
	    }
	    return true;
	}
	
	private boolean slotFlag(Integer _slot){
	    if(null == this.slot || null == _slot){
	        return false;
	    }
	    if(this.slot.intValue() != _slot.intValue()){
	        return false;
	    }
	    return true;
	}
	

}
