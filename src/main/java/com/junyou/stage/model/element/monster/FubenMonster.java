package com.junyou.stage.model.element.monster;

import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.schedule.StageTokenRunable;

/**
 * 副本怪物
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-26 上午10:16:23
 */
public class FubenMonster extends Monster{

	public FubenMonster(Long id,MonsterConfig monsterConfig) {
		super(id, null, monsterConfig);
	}

	@Override
	protected void scheduleDisappearHandle(IHarm harm) {
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{getId(),getElementType().getVal()});
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
	}
}