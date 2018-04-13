package com.junyou.stage.configure.export.impl;

import java.util.List;

import com.junyou.utils.collection.ReadOnlyList;


/**
 * 技能学习配置
 * @author LiuYu
 * @date 2015-2-13 上午11:55:55
 */
public class SkillXueXiConfig{

	private int minLevel;
	private int skillLevel;
	private String needItem;
	private int zplus;
	private float formulea;
	private float formuleb;
	private float formulec;	
	private float formuled;
	private int zhenqi;
	private int gold;
	private int shulian;
	private ReadOnlyList<SkillEffectConfig> effectConfigs;
	public int getMinLevel() {
		return minLevel;
	}
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	public int getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	public String getNeedItem() {
		return needItem;
	}
	public void setNeedItem(String needItem) {
		this.needItem = needItem;
	}
	public int getZplus() {
		return zplus;
	}
	public void setZplus(int zplus) {
		this.zplus = zplus;
	}
	public float getFormulea() {
		return formulea;
	}
	public void setFormulea(float formulea) {
		this.formulea = formulea;
	}
	public float getFormuleb() {
		return formuleb;
	}
	public void setFormuleb(float formuleb) {
		this.formuleb = formuleb;
	}
	public float getFormulec() {
		return formulec;
	}
	public void setFormulec(float formulec) {
		this.formulec = formulec;
	}
	public float getFormuled() {
		return formuled;
	}
	public void setFormuled(float formuled) {
		this.formuled = formuled;
	}
	public int getZhenqi() {
		return zhenqi;
	}
	public void setZhenqi(int zhenqi) {
		this.zhenqi = zhenqi;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getShulian() {
		return shulian;
	}
	public void setShulian(int shulian) {
		this.shulian = shulian;
	}
	public ReadOnlyList<SkillEffectConfig> getEffectConfigs() {
		return effectConfigs;
	}
	public void setEffectConfigs(List<SkillEffectConfig> effectConfigs) {
		this.effectConfigs = new ReadOnlyList<>(effectConfigs);
	}
	
	
}
