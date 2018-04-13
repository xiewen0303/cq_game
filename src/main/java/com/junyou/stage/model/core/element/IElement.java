/**
 * 
 */
package com.junyou.stage.model.core.element;

import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.schedule.StageScheduleExecutor;


/**
 * @description 场景元素
 * @author ShiJie Chi
 * @created 2011-11-25下午1:50:24
 */
public interface IElement extends IStageElement {
	/**
	 * 定时业务
	 */
	public StageScheduleExecutor getScheduler();
	
	/**
	 * 获取状态管理器
	 * @param
	 */
	public IStateManager getStateManager();
	

	public Object getStageData();
}
