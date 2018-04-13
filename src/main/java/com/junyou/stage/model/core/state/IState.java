/**
 * 
 */
package com.junyou.stage.model.core.state;




/**
 * @description 状态接口
 * @author ShiJie Chi
 * @created 2011-12-26上午11:21:26
 */
public interface IState {
	
	/**
	 * 获取类型
	 * @param
	 */
	public StateType getType();
	
	/**
	 * 获取负载数据
	 */
	public <T> T getData();
	
	/**
	 * 设置负载数据
	 */
	public void setData(Object data);
}
