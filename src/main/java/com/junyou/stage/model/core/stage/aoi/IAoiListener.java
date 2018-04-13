/**
 * 
 */
package com.junyou.stage.model.core.stage.aoi;

import com.junyou.stage.model.core.stage.IStageElement;

/**
 * @description 元素Aoi监听接口
 * @author ShiJie Chi
 * @created 2011-11-29上午11:20:58
 */
public interface IAoiListener {
	
	/**
	 * 角色进入
	 * @param element
	 * @param 进入aoi的方向
	 */
	public void elementEnter(IStageElement element,int direction);
	
	/**
	 * 角色离开
	 * @param element
	 * @param direction 离开aoi的方向
	 */
	public void elementLeave(IStageElement element,int direction);
	
}
