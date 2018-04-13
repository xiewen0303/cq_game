package com.junyou.stage.model.core.state;

public interface IStateManagerListener {
	
	/**
	 * 增加状态
	 * @param state
	 */
	public void addState(IState state);
	
	/**
	 * 移除状态
	 * @param state
	 */
	public void removeState(IState state);
	
	/**
	 * 覆盖状态
	 * @param state
	 */
	public void replaceState(IState state);
	
	/**
	 * 清除状态
	 * @param state
	 */
	public void clearState(IState state);
	
}
