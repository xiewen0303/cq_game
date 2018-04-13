package com.junyou.stage.model.core.stage;

import com.junyou.stage.model.core.skill.IHarm;

/**
 * 生命周期
 * @author zl
 * @date 2012-8-9 下午14:27:25 
 */
public interface IStageElementLifeCycle {
	
	/**
	 * 死亡处理
	 */
	public void deadHandle(IHarm harm);
	
	/**
	 * 离开场景的处理
	 * @param stage 
	 */
	public void leaveStageHandle(IStage stage);
	
	/**
	 * 进入场景的处理
	 * @param stage 
	 */
	public void enterStageHandle(IStage stage);
}
