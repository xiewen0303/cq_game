package com.junyou.stage.model.core.stage;
/**
 * 获取其他玩家
 * @author LiuYu
 * @date 2015-2-4 下午6:41:43
 */
public class OtherRolesSearchFilter implements IElementSearchFilter {

	private Long userRoleId;
	
	public OtherRolesSearchFilter(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	@Override
	public boolean check(IStageElement target) {
		return !target.getId().equals(userRoleId);
	}

	@Override
	public boolean isEnough() {
		return false;
	}

}
