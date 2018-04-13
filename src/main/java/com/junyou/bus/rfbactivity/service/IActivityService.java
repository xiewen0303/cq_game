package com.junyou.bus.rfbactivity.service;


public interface IActivityService {
	/**
	 * 获得子活动的领奖状态  subId = 1:int(0-没奖励，1-有奖励)
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	boolean getChildFlag(long userRoleId,int subId);
	
	/**
	 * 检查Icon 奖励提示
	 * @param userRoleId
	 * @param subId
	 */
	void checkIconFlag(long userRoleId,int subId);
	
	/**
	 * 检测活动数据是否需要重置之前的db数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	void updateCheck(long userRoleId, int subId);
}
