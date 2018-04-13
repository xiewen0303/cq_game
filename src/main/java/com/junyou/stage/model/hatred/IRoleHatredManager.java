package com.junyou.stage.model.hatred;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.hatred.IHatredManager;

public interface IRoleHatredManager extends IHatredManager {

	/**
	 * 定时战斗状态检测
	 */
	void scheduleFightStateCheck();
	
	/**
	 * 在无视攻击模式可攻击列表
	 */
	boolean inIgnoreBattleAttackableList(IFighter target);
	
	/**
	 * 获取最后攻击的目标
	 */
	public IHatred getLastActiveAttackTarget();

}
