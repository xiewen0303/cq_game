package com.junyou.stage.model.stage.active;

import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author LiuYu
 * 2015-8-5 下午2:59:46
 */
public class RefbActiveFubenStage extends PublicFubenStage{
	
	private Long kickTime;
	
	
	public RefbActiveFubenStage(String id, Integer mapId, Integer lineNo,AOIManager aoiManager, PathInfoConfig pathInfoConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig, StageType.REFB_ACTIVE);
	}
	
	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ElementType.isRole(element.getElementType())){
			Role role = (Role)element;
			role.startLeaveSchedule(kickTime);
		}
	}
	
	public void setKickTime(Long kickTime) {
		this.kickTime = kickTime;
	}

	@Override
	public boolean isCanRemove(){
		return kickTime < GameSystemTime.getSystemMillTime() && (getAllRoleIds() == null || getAllRoleIds().length < 1);
		//return kickTime < GameSystemTime.getSystemMillTime() && getAllRoleIds().length < 1;
	}

	@Override
	public void enterNotice(Long userRoleId) {
	}

	@Override
	public void exitNotice(Long userRoleId) {
	}

	@Override
	public boolean isFubenMonster() {
		return false;
	}
	@Override
	public boolean isOpen(){
		return kickTime >= GameSystemTime.getSystemMillTime();
	}
	
}
