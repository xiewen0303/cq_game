package com.junyou.stage.configure.export.impl;

/**
 * 
 * @description 技能效果
 *
 * @author LiuJuan
 * @created 2011-12-13 上午11:25:10
 */
public class SkillEffectConfig {

	private String addeffect;
	private float rate;
	/**
	 * 效果
	 * @return
	 */
	public String getAddeffect() {
		return addeffect;
	}
	public void setAddeffect(String addeffect) {
		this.addeffect = addeffect;
	}
	/**
	 * 几率
	 * @return
	 */
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
}
