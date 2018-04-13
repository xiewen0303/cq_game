package com.junyou.stage.model.element.monster;

import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.schedule.StageTokenRunable;

/**
 * 图腾
 * @author LiuYu
 * @date 2015-4-21 下午7:09:53
 */
public class TutengMonster extends Monster{
	
	private long guildId;

	public TutengMonster(Long id,MonsterConfig monsterConfig,long guildId) {
		super(id, null, monsterConfig);
		this.guildId = guildId;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.TUTENG;
	}


	public Long getGuildId() {
		return guildId;
	}
	
	public void setGuildId(long guildId){
		this.guildId = guildId;
	}
	
	@Override
	protected void scheduleDisappearHandle(IHarm harm) {
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{getId(),getElementType().getVal()});
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
	}
	
	@Override
	public void leaveStageHandle(IStage stage) {
		this.getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE);
	}
	
}