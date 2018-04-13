/**
 * 
 */
package com.junyou.stage.model.core.element;

import java.util.List;

import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.skill.PublicCdManager;

/**
 * @description 战士
 * @author ShiJie Chi
 * @created 2011-12-5下午3:42:20
 */
public interface IFighter extends IElement {

	/**
	 * 获取阵营
	 */
	Integer getCamp();
	
	/**
	 * 等级
	 * @param
	 */
	int getLevel();
	
	/**
	 * 获取技能
	 * @param
	 */
	ISkill getSkill(String skillCategory);
	
	/**
	 * 增加技能
	 * @param
	 */
	void addSkill(ISkill skill);
	
	/**
	 * 获取所有技能
	 * @return
	 */
	List<ISkill> getSkills();
	
	/**
	 * 移除指定技能
	 * @param category
	 */
	public void removeSkill(String category);
	
	/**
	 * 移除所有技能
	 */
	void clearSkills();

	/**
	 * 获取战斗统计
	 * @param
	 */
	IFightStatistic getFightStatistic();

	/**
	 * 获取战斗属性
	 * @param
	 */
	IFightAttribute getFightAttribute();
	
	/**
	 * 获取buff管理器
	 */
	IBuffManager getBuffManager();
	
	/**
	 * 死亡处理
	 */
	void deadHandle(IHarm harm);
	
	/**
	 * 仇恨管理器
	 */
	public IHatredManager getHatredManager();
	
	/**
	 * CD管理器
	 * @return
	 */
	public PublicCdManager getPublicCdManager();
	
	/**
	 * 战斗模式
	 */
	BattleMode getBattleMode();
	
	/**
	 * 设置战斗模式
	 */
	public void setBattleMode(BattleMode battleMode);
	
	/**
	 * 获取移动数据
	 * @return
	 */
	public Object getMoveData();
	
	/**
	 * 是否是PVE
	 * @param target	被攻击目标
	 * @return
	 */
	public boolean isPve(IFighter target);
	
	/**
	 * 获取主人
	 * @return
	 */
	public IFighter getOwner();
	
	/**
	 * 战斗状态时的心跳时间(毫秒)
	 * @return
	 */
	public int getHeartTime();

}
