package com.junyou.state.jingji.entity;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;

/**
 * @author LiuYu
 * 2015-7-3 下午2:29:14
 */
public interface IJingjiFighter extends IFighter{
	/**
	 * 设置战斗
	 * @param jingjiFight
	 */
	public void setJingjiFight(JingjiFight jingjiFight);
	/**
	 * 获取战力
	 * @return
	 */
	public long getZpuls();
	/**
	 * 获取攻击技能
	 * @return
	 */
	public ISkill getSkill();
	/**
	 * 获取最近CD
	 * @return
	 */
	public int getMinCd();
	/**
	 * 而获取当前经验
	 * @return
	 */
	public long getCurHp();
	
	/**
	 * 获取职业
	 * @return
	 */
	public int getConfigId();
	/**
	 * 获取坐骑等级
	 * @return
	 */
	public int getZuoqi();
	/**
	 * 获取翅膀等级
	 * @return
	 */
	public int getChibang();
}
