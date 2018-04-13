package com.junyou.stage.model.stage.fuben;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 公共副本场景
 * @author LiuYu
 * @date 2015-6-23 上午11:19:09
 */
public abstract class PublicFubenStage  extends AoiStage{
	
	public PublicFubenStage(String id, Integer mapId, Integer lineNo,AOIManager aoiManager, PathInfoConfig pathInfoConfig,StageType stageType) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig, stageType);
	}
	
	private boolean open;
	
	/**
	 * 活动开始
	 */
	public void start(){
		open = true;
	}
	
	/**
	 * 活动结束
	 */
	public void stop(){
		open = false;
	}
	
	/**
	 * 活动是否进行中
	 * @return
	 */
	public boolean isOpen(){
		return open;
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ElementType.isRole(element.getElementType())){
			enterNotice(element.getId());
			
			 if(getStageType() == StageType.CHAOS ||
				getStageType() == StageType.XIANGONG ||
				getStageType() == StageType.CAMP||
				getStageType() == StageType.HCZBS_WAR||
				getStageType() == StageType.KUAFUBOSS)
			 {
				Role role = (Role)element;
				IBuff buff = BuffFactory.create(role, role, GameConstants.SF_BUFF);
				role.getBuffManager().addBuff(buff);
				role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			 }
		}
	}
	
	@Override
	public void leave(IStageElement element) {
		if(ElementType.isRole(element.getElementType())){
			exitNotice(element.getId());
		}
		super.leave(element);
	}
	/**
	 * 进入场景通知
	 * @param userRoleId
	 */
	public abstract void enterNotice(Long userRoleId);
	/**
	 * 退出场景通知
	 * @param userRoleId
	 */
	public abstract void exitNotice(Long userRoleId);
	
	/**
	 * 是否创建副本怪物
	 * @return
	 */
	public abstract boolean isFubenMonster();
	
	public void kickAll(){
		for (IStageElement role : getAllElements(ElementType.ROLE)) {
			StageMsgSender.send2StageControl(role.getId(), InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		}
	}
}
