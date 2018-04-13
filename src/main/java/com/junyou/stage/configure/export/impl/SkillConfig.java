package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.stage.configure.SkillFirePathType;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.configure.SkillTargetChooseType;
import com.junyou.utils.collection.ReadOnlyList;

/**
 * 技能
 * @author LiuYu
 * @date 2015-2-13 上午11:56:46
 */
public class SkillConfig {
	private String id;
	private int needJob;
	private String cd1;
	private String cd2;
	private int gongshi;
	private boolean noDef;
	private boolean noMiss;
	//此处配置攻击者使用该技能对被攻击者产生的仇恨
	private float hatredN1;
	//此处配置被该技能攻击后，被攻击者对攻击者产生的仇恨
	private float hatredN2;
	private ReadOnlyList<String> effs;
	private SkillFireType skillFireType;
	private int attackerMove;
	private int weiyi;
	//是否选敌对目标
	private int ishostile;
	private int range;
	private int maxTarget;
	
	//rad	radius	width 路径选取
	private float pathSectorRad;
	private float pathRadius;
	private float pathWidth;
	
	private SkillTargetChooseType targetType;
	private SkillFirePathType skillFirePathType;
	
	private Map<Integer,SkillXueXiConfig> xuexiConfigs = new HashMap<>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNeedJob() {
		return needJob;
	}
	public void setNeedJob(int needJob) {
		this.needJob = needJob;
	}
	public String getCd1() {
		return cd1;
	}
	public void setCd1(String cd1) {
		this.cd1 = cd1;
	}
	public String getCd2() {
		return cd2;
	}
	public void setCd2(String cd2) {
		this.cd2 = cd2;
	}
	public int getGongshi() {
		return gongshi;
	}
	public void setGongshi(int gongshi) {
		this.gongshi = gongshi;
	}
	public boolean isNoDef() {
		return noDef;
	}
	public void setNoDef(boolean noDef) {
		this.noDef = noDef;
	}
	public boolean isNoMiss() {
		return noMiss;
	}
	public void setNoMiss(boolean noMiss) {
		this.noMiss = noMiss;
	}
	public float getHatredN1() {
		return hatredN1;
	}
	public void setHatredN1(float hatredN1) {
		this.hatredN1 = hatredN1;
	}
	public float getHatredN2() {
		return hatredN2;
	}
	public void setHatredN2(float hatredN2) {
		this.hatredN2 = hatredN2;
	}
	public ReadOnlyList<String> getEffs() {
		return effs;
	}
	public void setEffs(List<String> effs) {
		this.effs = new ReadOnlyList<>(effs);
	}
	/**
	 * 技能施法类型(普通技能、陷阱等)
	 * 
		0:普通技能
		1:怒气技能
	 */
	public SkillFireType getSkillFireType() {
		return skillFireType;
	}
	public void setSkillFireType(SkillFireType skillFireType) {
		this.skillFireType = skillFireType;
	}
	
	
	public int getAttackerMove() {
		return attackerMove;
	}
	public void setAttackerMove(int attackerMove) {
		this.attackerMove = attackerMove;
	}
	public int getWeiyi() {
		return weiyi;
	}
	public void setWeiyi(int weiyi) {
		this.weiyi = weiyi;
	}
	public int getIshostile() {
		return ishostile;
	}
	public void setIshostile(int ishostile) {
		this.ishostile = ishostile;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public int getMaxTarget() {
		return maxTarget;
	}
	public void setMaxTarget(int maxTarget) {
		this.maxTarget = maxTarget;
	}
	public SkillXueXiConfig getXuexiConfig(Integer level) {
		return xuexiConfigs.get(level);
	}
	public void addXuexiConfigs(SkillXueXiConfig xuexiConfig) {
		this.xuexiConfigs.put(xuexiConfig.getSkillLevel(), xuexiConfig);
	}
	public SkillFirePathType getSkillFirePathType() {
		return skillFirePathType;
	}
	public void setSkillFirePathType(SkillFirePathType skillFirePathType) {
		this.skillFirePathType = skillFirePathType;
	}
	public SkillTargetChooseType getTargetType() {
		return targetType;
	}
	public void setTargetType(SkillTargetChooseType targetType) {
		this.targetType = targetType;
	}
	public float getPathSectorRad() {
		return pathSectorRad;
	}
	public void setPathSectorRad(float pathSectorRad) {
		this.pathSectorRad = pathSectorRad;
	}
	public float getPathRadius() {
		return pathRadius;
	}
	public void setPathRadius(float pathRadius) {
		this.pathRadius = pathRadius;
	}
	public float getPathWidth() {
		return pathWidth;
	}
	public void setPathWidth(float pathWidth) {
		this.pathWidth = pathWidth;
	}
	
	
	
}
