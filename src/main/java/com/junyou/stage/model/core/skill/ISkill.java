/**
 * 
 */
package com.junyou.stage.model.core.skill;

import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.configure.export.impl.SkillXueXiConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightResultCalculator;
import com.junyou.stage.model.fight.calculator.FightResultCalculatorType;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.hatred.ISkillHatred;




/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-5下午3:43:02
 */
public interface ISkill {

	/**
	 * 是否不计算命中机率   true:不计算
	 * @return
	 */
	public boolean isNoCalcMZ();
	
	/**
	 * 是否无视对方防御 true:无视
	 * @return
	 */
	public boolean isNoDef();
	
	/**
	 * 是否在cd中
	 * @param fighter TODO
	 * @param
	 */
	boolean isCding(PublicCdManager cdManager);

	/**
	 * 进入cd状态
	 * @param fighter TODO
	 * @param
	 */
	void toCd(IFighter fighter);
	
	/**
	 * 获取伤害类型 
	 * @return
	 */
	public FightResultCalculatorType getDamageType();

	/**
	 * @description 技能id(含等级信息)
	 */
	String getId();

	/**
	 * 获取技能目标选取类型
	 * @param
	 */
	ISkillTarget getTarget();
	
	/**
	 * 获取技能释放消耗
	 * @param
	 */
	ISkillConsume getConsume();

	/**
	 * 战斗结果计算器（技能输出类型）
	 * @param
	 */
	IFightResultCalculator getFightResultCalculator();
	
	/**
	 * 获取技能施法路径
	 */
	ISkillFirePath getSkillFirePath();

	/**
	 * 获取技能等级
	 */
	Integer getLevel();

	/**
	 * 当前技能剩余cd时间(单位：毫秒)
	 */
	Integer getDynamicCd(PublicCdManager cdManager);

	/**
	 * 技能仇恨
	 */
	ISkillHatred getSkillHatred();
	
	/**
	 * 获取本级技能属性
	 * @return
	 */
	SkillXueXiConfig getSkillXueXiConfig();

	/**
	 * 技能id
	 * @return
	 */
	String getCategory();

	SkillConfig getSkillConfig();
	
}
