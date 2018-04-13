/**
 * 
 */
package com.junyou.stage.model.core.attribute;

import com.junyou.stage.model.core.skill.IHarm;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-14下午1:52:56
 */
public interface IAttributeListener {
	
	/**
	 * @param maxHp 生命
	 */
	public void setMaxHp(long maxHp);
	
	/**
	 * @param attack 攻击
	 */
	public void setAttack(long attack);
	
	/**
	 * @param defense 防御
	 */
	public void setDefense(long defense);
	
	/**
	 * @param shanBi 闪避几率
	 */
	public void setShanBi(long shanBi);
	
	/**
	 * @param mingZhong 命中几率
	 */
	public void setMingZhong(long mingZhong);
	
	/**
	 * @param baoJi 暴击几率
	 */
	public void setBaoji(long baoJi);
	
	/**
	 * @param kangBao 抗暴几率
	 */
	public void setKangBao(long kangBao);
	
	/**
	 * @param speed 移动
	 */
	public void setSpeed(long speed);
	
	/**
	 * @param zhanLi 战力
	 */
	public void setZhanLi(long zhanLi);
	
	/**
	 * PK惩罚 x48
	 * @param chengfa
	 */
	public void setChengfa(long chengfa);
	
	/**
	 * 设置当前HP
	 * @param curHp
	 */
	public void setCurHp(long curHp);
	
	/**
	 * 受伤
	 * @param
	 */
	public void hurt(IHarm harm);
	
	/**
	 * 治疗
	 * @param
	 */
	public void heal(IHarm harm);
}
