package com.junyou.stage.model.core.skill;

import java.util.Collection;

import com.junyou.stage.model.element.componentlistener.FightListener;

public interface IBuffManager {

	/**
	 * 增加buff
	 */
	public void addBuff(IBuff buff);
	
	/**
	 * 恢复buff但不激活
	 */
	public void recoverBuff(IBuff buff);
	
	public void startReadyForRecoveredBuffsAll();
	
	/**
	 * 恢复prop buff但不激活
	 * @param buff
	 */
	public void readyOrRecoverBuff(IBuff buff);
	
	/**
	 * 删除buff
	 * @param id 唯一标识
	 * @param category buff总类
	 */
	public void removeBuff(Long id, String category);

	/**
	 * 获取buff
	 * @param buffCategory buff总类
	 * @param id 唯一标识(可选)
	 */
	public IBuff getBuff(String category, Long id);

	/**
	 * 获取所有buff
	 */
	public Collection<IBuff> getBuffs();

	/**
	 * 激活所有buff
	 */
	public void startAll();

	/**
	 * 清理死亡需消失的buff
	 */
	public void clearBuffsByDead();

	/**
	 * 清理所有buff
	 */
	public void clearAll();
	
	/**
	 * 客户端所需buff信息格式
	 */
	public Object getBuffClientMsgs();

	public void addListener(FightListener fightListener);
}
