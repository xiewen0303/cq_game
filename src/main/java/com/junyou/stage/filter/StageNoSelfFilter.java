package com.junyou.stage.filter;
 
import com.junyou.stage.model.core.stage.IElementSearchFilter;
import com.junyou.stage.model.core.stage.IStageElement;

public class StageNoSelfFilter implements IElementSearchFilter {

	private Long userRoleId;
//	private Long targetRoleId;
	
	private boolean found = false; 
 
	public StageNoSelfFilter(Long userRoleId) {
		this.userRoleId = userRoleId;
//		this.targetRoleId = targetRoleId;
	}

	@Override
	public boolean check(IStageElement entity) {
		return !entity.getId().equals(userRoleId);//  &&  !entity.getId().equals(targetRoleId);
	}

	@Override
	public boolean isEnough() {
		return found;
	}
}
