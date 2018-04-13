package com.junyou.stage.model.element.monster.ai;

import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.schedule.StageTokenRunable;

public class DefaultAi implements IAi {
	
	protected IFighter fighter;
	
	private boolean stop;//心跳是否已停止
	
	public DefaultAi(IFighter fighter) {
		this.fighter = fighter;
	}

	@Override
	public void schedule(int delay,TimeUnit unit) {
		if(fighter.getStage() != null){
			StageTokenRunable attackTask = new StageTokenRunable(fighter.getId(), fighter.getStage().getId(), getAiHandleCommand(), new Object[]{fighter.getStage().getId(),fighter.getId(),fighter.getElementType().getVal()});
			fighter.getScheduler().schedule(fighter.getId().toString(), GameConstants.COMPONENT_AI_HEARTBEAT, attackTask, delay, TimeUnit.MILLISECONDS);
			stop = false;
		}
		
	}
	
	@Override
	public void interruptSchedule(int delay, TimeUnit unit) {
		
		fighter.getScheduler().cancelSchedule(fighter.getId().toString(), GameConstants.COMPONENT_AI_HEARTBEAT);
		schedule(delay, unit);
		
	}
	

	@Override
	public void stop() {
		fighter.getScheduler().cancelSchedule(fighter.getId().toString(), GameConstants.COMPONENT_AI_HEARTBEAT);
		stop = true;
	}
	
	/**
	 * 获取ai处理指令
	 */
	protected Short getAiHandleCommand() {
		return InnerCmdType.AI_HANDLE;
	}
	
	public boolean isStop(){
		return stop;
	}
}
