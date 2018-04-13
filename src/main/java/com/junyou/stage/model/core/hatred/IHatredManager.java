package com.junyou.stage.model.core.hatred;

import java.util.List;

import com.junyou.stage.model.core.element.IFighter;

public interface IHatredManager{

	/**
	 * 获取仇恨最大者
	 */
	IHatred getHatredest();
	
	/**
	 * 目标是否在仇恨列表内
	 * @param target
	 * @return true:在
	 */
	public boolean checkTargetHatred(IFighter target);

	/**
	 * 增加仇恨值
	 */
//	void add(IFighter role, int i);

	/**
	 * 增加伤害仇恨值
	 */
//	void addHarmHatred(IFighter attacker, HarmType type, int val);
	
	/**
	 * 增加主动仇恨
	 */
	void addActiveHatred(IFighter target,int val);
	
	/**
	 * 增加被动仇恨
	 */
	void addPassiveHatred(IFighter target,int val);
	
	/**
	 * 刷新仇恨
	 */
	void refreshHatred();

	/**
	 * 死亡处理
	 */
	void deadHandle();

	/**
	 * 重置
	 */
	void clear();

	/**
	 * 增加内部仇恨(仇恨列表里的元素都增加指定仇恨)
	 */
	void addInsideHatred(Integer hatredVal);
	
	/**
	 * 获取最后攻击的目标
	 */
	public IHatred getLastActiveAttackTarget();
	
	
	public void addListener(IHatredManagerListener listener);
	
	/**
	 * 从仇恨列表中移除目标
	 * @param targetRoleId
	 */
	public void clearHatred(long targetId);
	
	/**
	 * 获取所有仇恨目标
	 * @return
	 */
	public List<Long> getAllHaters();
	
}
