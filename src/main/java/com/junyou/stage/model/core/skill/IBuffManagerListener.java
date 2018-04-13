package com.junyou.stage.model.core.skill;

public interface IBuffManagerListener {
	/**
	 * 增加buff
	 * @param buff
	 */
	public void addBuff(IBuff buff);
	
	/**
	 * 移除buff
	 * @param buff
	 */
	public void removeBuff(IBuff buff);
}
