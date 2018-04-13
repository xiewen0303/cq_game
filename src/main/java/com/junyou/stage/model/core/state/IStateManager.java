/**
 * 
 */
package com.junyou.stage.model.core.state;

/**
 * @description 状态管理器接口
 * @author ShiJie Chi
 * @created 2011-12-26上午11:19:57
 */
public interface IStateManager{
	
	/**
	 * 新状态进入
	 * @param
	 */
	public void add(IState state);

	/**
	 * 移除状态
	 * @param
	 */
	public boolean remove(StateType stateType);
	
	/**
	 * 行为在当前状态下是否禁止
	 * @param
	 */
	public boolean isForbidden(StateEventType event);
	
	/**
	 * 是否包含指定状态
	 * @param
	 */
	public boolean contains(StateType stateType);
	
	/**
	 * 是否死亡
	 */
	public boolean isDead();
	
	/**
	 * 是否隐身
	 * @return
	 */
	public boolean isYinShen();
	
	/**
	 * 客户端状态标志（true 活着 false 死亡）
	 */
	public boolean getClientState();
	
	/**
	 * 清理
	 */
	public void clear();
	
	/**
	 * 增加监听器
	 */
	public void addListener(IStateManagerListener listener);
	
}
