package com.junyou.public_.trade.filter;

import com.junyou.stage.model.core.stage.IElementSearchFilter;
import com.junyou.stage.model.core.stage.IStageElement;

public class StageElementFilter implements IElementSearchFilter {
	
	private long userRoleId;
	private boolean isFound;
	
	
	public StageElementFilter(long userRoleId) {
		super();
		this.userRoleId = userRoleId;
	}

	@Override
	public boolean check(IStageElement target) {
		if(target.getId().equals(userRoleId)){
			isFound=true;
			return true;
		}
		return false;
	}

	@Override
	public boolean isEnough() {
		return isFound;
	} 

}
