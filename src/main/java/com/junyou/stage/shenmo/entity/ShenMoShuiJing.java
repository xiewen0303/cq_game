package com.junyou.stage.shenmo.entity;

import java.util.concurrent.TimeUnit;

import com.junyou.bus.shenmo.configure.ShenMoScoreConfig;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.schedule.StageTokenRunable;

/**
 * 神魔战场水晶
 * @author LiuYu
 * 2015-9-25 上午10:35:47
 */
public class ShenMoShuiJing extends Monster{
	public ShenMoShuiJing(Long id, MonsterConfig monsterConfig,ShenMoScoreConfig config) {
		super(id, null, monsterConfig);
		this.teamId = config.getTeamId();
		this.gsTime = config.getGsTime();
		this.gsJf = config.getGsScore();
	}
	private int teamId;
	private int gsTime;
	private int gsJf;
	
	@Override
	protected void scheduleDisappearHandle(IHarm harm) {
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{getId(),getElementType().getVal()});
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
	}
	
	public void stopScoreSchedule(){
		getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_SHENMO_ADD_SCORE);
	}
	
	public void startScoreSchedule(){
		if(teamId > 0){
 			StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.SHENMO_ADD_SHUIJING_SCORE, new Object[]{gsJf,teamId});
			getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_SHENMO_ADD_SCORE, runable, gsTime, TimeUnit.SECONDS);
		}
	}

	public int getTeamId() {
		return teamId;
	}
	
	@Override
	public void enterStageHandle(IStage stage) {
	}
	
	
}
