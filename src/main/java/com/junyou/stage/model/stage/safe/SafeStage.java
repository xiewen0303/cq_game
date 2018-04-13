package com.junyou.stage.model.stage.safe;

import java.util.Map;

import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementSearchFilter;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 安全场景
 * @author LiuYu
 * @date 2015-3-21 下午7:09:33
 */
public class SafeStage  extends AoiStage{
	private Long[] roleIds = new Long[0];
	
	@Override
	public Long[] getSurroundRoleIds(Point from, IElementSearchFilter filter) {
		return roleIds;
	}

	@Override
	public Long[] getSurroundRoleIds(Point from) {
		return roleIds;
	}
	

	@Override
	public Long[] getNoSelfSurroundRoleIds(Point from, Long selfId) {
		return roleIds;
	}

	public SafeStage(String id, Integer mapId,AOIManager aoiManager, PathInfoConfig pathInfoConfig,StageType stageType) {
		super(id, mapId, 0, aoiManager, pathInfoConfig,stageType);
	}

	@Override
	public void moveTo(IStageElement element, int x, int y) {
		element.setPosition(x, y);
	}

	@Override
	public void leave(IStageElement element) {
		Map<Long,IStageElement> elementMap = getElementMap(element.getElementType());
		elementMap.remove(element.getId());
		element.leaveStageHandle(this);
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		Map<Long,IStageElement> elementMap = getElementMap(element.getElementType());
		elementMap.put(element.getId(), element);
		element.setStage(this);
		element.setPosition(x, y);
		
		if(ElementType.isRole(element.getElementType())){
			if(getStageType() == StageType.START_GAME_SAFE_STAGE){
				element.enterStageHandle(this);
			}else if(getStageType() == StageType.SAFE_STAGE){
				//进入竞技场安全场景
				StageMsgSender.send2Bus(element.getId(), InnerCmdType.ENTER_SAFE_SUCCESS_START_FIGHT, null);
			}else if(getStageType() == StageType.KF_JINGJI_STAGE){
				//进入跨服竞技场安全场景
				StageMsgSender.send2Bus(element.getId(), InnerCmdType.ENTER_SAFE_SUCCESS_START_FIGHT_KF, null);
			}
		}
	}
	
	public boolean isCanDazuo(){
		return false;
	}
	
	
}
