package com.junyou.stage.model.core.stage;

import com.junyou.stage.model.element.role.IRole;

/**
 * 获取其他队长
 * @author LiuYu
 * @date 2015-2-4 下午6:41:43
 */
public class OtherTeamLeaderSearchFilter implements IElementSearchFilter {

	private Long userRoleId;
	
	public OtherTeamLeaderSearchFilter(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	@Override
	public boolean check(IStageElement target) {
		if(!ElementType.isRole(target.getElementType())){
			return false;
		}
		IRole role = (IRole)target;
		if(target.getId().equals(userRoleId)){
			return false;
		}
		if(role.getTeamMember() == null || !role.getTeamMember().isLeader()){
			return false;
		}
		return true;
	}

	@Override
	public boolean isEnough() {
		return false;
	}

}
