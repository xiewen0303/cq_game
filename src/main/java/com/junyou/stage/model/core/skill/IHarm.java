/**
 * 
 */
package com.junyou.stage.model.core.skill;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.skill.harm.HarmType;

/**
 * @description 伤害
 * @author ShiJie Chi
 * @created 2011-12-6下午4:54:32
 */
public interface IHarm {

	/**
	 * 伤害类型
	 * @param
	 */
	public HarmType getType();

	/**
	 * 伤害值
	 * @param
	 */
	public long getVal();
	
	/**
	 * 攻击者
	 */
	public IFighter getAttacker();

	/**
	 * 目标
	 */
	public IFighter getTarget();
	
	/**
	 * 
	 */
	public ISkill getSkill();
	
	public Integer getKouHpValue();

	public void setKouHpValue(Integer kouHpValue);
	
	public Integer getXiHpValue();

	public void setXiHpValue(Integer xiHpValue);
	
	public Integer getHarmTypeValue();
	

	public void setHarmTypeValue(Integer harmTypeValue);
	
	public long getDdVal();
	
	public long getFsVal();
	
}
