/**
 * 
 */
package com.junyou.stage.model.element.monster;

import com.junyou.stage.model.attribute.monster.MonsterFightAttribute;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.fight.statistic.IMonsterFightStatistic;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-16下午1:44:35
 */
public interface IMonster extends IFighter {
	
	public void setFightStatistic(IMonsterFightStatistic fightStatistic);
	
	public void setMonsterFightAttribute(MonsterFightAttribute monsterFightAttribute);

	public void setBuffManager(IBuffManager buffManager);

	public String getMonsterId();

	public IAi getAi();
	
	public void setAi(IAi ai);
	
	public void setHatredManger(IHatredManager hatredManager);
	
	public void setCamp(Integer camp);
	
	/**
	 * 刷新怪物巡逻AI(连续3次心跳都是巡逻状态就停止怪物AI,等待下一次仇恨触发)
	 */
	public void refreshXunLouState();
	
	/**
	 * 是否停止怪物AI
	 * @return true:停止
	 */
	public boolean isStopAi();
	
	/**
	 * 怪物类型
	 * 0 或者不填 普通怪物，不做特殊处理<br/>
	 * 1 副本炮台
	 * @return
	 */
	public int getMonsterType();
	/**
	 * 怪物回血
	 */
	public void monsterHuiFuHp();

	/**
	 * 剩余鞭尸时间
	 */
	public Integer getLeftBianShiTime();

	/**
	 * 不可移动
	 * @return true:不可移动
	 */
	public boolean isNoMove();
	
	/**
	 * 是否是不可攻击其它人
	 * @return true:不可攻击其它人
	 */
	public boolean isNoAttack();

	/**
	 * 是否是不可被攻击
	 * @return true:不可被攻击
	 */
	public boolean isNoBeiAttrack();
	
	/**
	 * 怪物品阶
	 * @return
	 */
	public int getRank();
}
