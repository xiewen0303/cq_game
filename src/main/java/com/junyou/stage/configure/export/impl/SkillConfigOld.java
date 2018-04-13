package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.List;

import com.junyou.gameconfig.constants.EnemyHostileType;
import com.junyou.stage.configure.SkillFirePathType;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.configure.SkillTargetChooseType;

/**
 * 
 * @description 技能
 *
 * @author LiuJuan
 * @created 2011-12-12 下午04:43:10
 */
public class SkillConfigOld {

	private String skillCategory;
	private String skillId;
	private int skillLevel;
	private SkillTargetChooseType targetType;
	private int damageType;
	private int consumeType;
	private Float consume1;
	private Float consume2;
	private String cd1;
	private String cd2;
	private int rang;
	private boolean isRang;
	private int maxTarget;
	private int maxHurt;
	private boolean voltCalculate;
	private boolean criticalCalculate;
	private boolean dodgeCalculate;
	private float formulea;
	private float formuleb;
	private float formulec;	
	private float formuled;
	private List<SkillEffectConfig> effectConfigs = new ArrayList<SkillEffectConfig>();
	private float baseTohit;
	private float baseCritical;
	private int baseDodgeDefance;

	//此处配置攻击者使用该技能对被攻击者产生的仇恨
	private float hatredN1;
	//此处配置被该技能攻击后，被攻击者对攻击者产生的仇恨
	private float hatredN2;
	
	//是否无视防御
	private boolean wuShiFang;
	
	//不计算命中:true
	private boolean isNoCalcMZ;
	
	private SkillFirePathType skillFirePathType;
	private SkillFireType skillFireType;
	
	//rad	radius	width 路径选取
	private float pathSectorRad;
	private float pathRadius;
	private float pathWidth;
	
	private String trapId;
	private int trapCount;
	
	//是否选敌对目标 （能够被当前攻击模式攻击的目标即为敌人）  0.否  1.是  2.选取同阵营的目标（人物、召唤物）
	private EnemyHostileType ishostile;
	
	//对目标产生位移  0:不位移     大于0:位移格数
	private int weiYi;
	
	//召唤的怪物类型 1:小怪,2:神兽
	private int zhType;
	
	//关联其它技能ID
	private String guanlianSkill;
	
	/**
	 * 关联其它技能ID
	 * @return
	 */
	public String getGuanlianSkill() {
		return guanlianSkill;
	}

	/**
	 * 是否关联其它技能
	 * @return true:是
	 */
	public boolean isGuanlianSkill(){
		if(guanlianSkill == null || "".equals(guanlianSkill)){
			return false;
		}else{
			return true;
		}
	}

	public void setGuanlianSkill(String guanlianSkill) {
		this.guanlianSkill = guanlianSkill;
	}


	/**
	 * 是否不计算命中机率   true:不计算
	 * @return
	 */
	public boolean isNoCalcMZ() {
		return isNoCalcMZ;
	}


	public void setNoCalcMZ(boolean isNoCalcMZ) {
		this.isNoCalcMZ = isNoCalcMZ;
	}
	
	/**
	 * 召唤的怪物类型 1:小怪,2:神兽
	 * @return
	 */
	public int getZhType() {
		return zhType;
	}
	public void setZhType(int zhType) {
		this.zhType = zhType;
	}
	/**
	 * 对目标产生位移  0:不位移     大于0:位移格数
	 * @return
	 */
	public int getWeiYi() {
		return weiYi;
	}
	public void setWeiYi(int weiYi) {
		this.weiYi = weiYi;
	}
	/**
	 * 是否选敌对目标 （能够被当前攻击模式攻击的目标即为敌人）  0.否  1.是  2.选取同阵营的目标（人物、召唤物）
	 * @return
	 */
	public EnemyHostileType isIshostile() {
		return ishostile;
	}
	public void setIshostile(EnemyHostileType ishostile) {
		this.ishostile = ishostile;
	}
	
	
	
	/**
	 * 是否无视防御
	 * @return
	 */
	public boolean isWuShiFang() {
		return wuShiFang;
	}
	public void setWuShiFang(boolean wuShiFang) {
		this.wuShiFang = wuShiFang;
	}
	/**
	 * 技能大类编号
	 * @return
	 */
	public String getSkillCategory() {
		return skillCategory;
	}
	public void setSkillCategory(String skillCategory) {
		this.skillCategory = skillCategory;
	}
	/**
	 * 技能的ID代码
	 * @return
	 */
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	/**
	 * 技能等级
	 * @return
	 */
	public int getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	/**
	 * 消耗类型
	 * @return
	 */
	public int getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(int consumeType) {
		this.consumeType = consumeType;
	}
	/**
	 * 消耗值1
	 * @return
	 */
	public Float getConsume1() {
		return consume1;
	}
	public void setConsume1(Float consume1) {
		this.consume1 = consume1;
	}
	/**
	 * 消耗值2
	 * @return
	 */
	public Float getConsume2() {
		return consume2;
	}
	public void setConsume2(Float consume2) {
		this.consume2 = consume2;
	}
	/**
	 * 公共CD1编号
	 * @return
	 */
	public String getCd1() {
		return cd1;
	}
	public void setCd1(String cd1) {
		this.cd1 = cd1;
	}
	/**
	 * 公共CD2编号
	 * @return
	 */
	public String getCd2() {
		return cd2;
	}
	public void setCd2(String cd2) {
		this.cd2 = cd2;
	}
	public SkillTargetChooseType getTargetType() {
		return targetType;
	}
	public void setTargetType(SkillTargetChooseType targetType) {
		this.targetType = targetType;
	}
	/**
	 * 输出的类型
	 * @return
	 */
	public int getDamageType() {
		return damageType;
	}
	public void setDamageType(int damageType) {
		this.damageType = damageType;
	}
	/**
	 * 怪物技能射程
	 * @return
	 */
	public int getRang() {
		return rang;
	}
	public void setRang(int rang) {
		this.rang = rang;
	}
	/**
	 * 是否为远程技能
	 * @return
	 */
	public boolean isRang() {
		return isRang;
	}
	public void setIsRang(boolean isRang) {
		this.isRang = isRang;
	}
	/**
	 * 最大目标数
	 * @return
	 */
	public int getMaxTarget() {
		return maxTarget;
	}
	public void setMaxTarget(int maxTarget) {
		this.maxTarget = maxTarget;
	}
	/**
	 * 对一个目标造成的最大伤害次数
	 * @return
	 */
	public int getMaxHurt() {
		return maxHurt;
	}
	public void setMaxHurt(int maxHurt) {
		this.maxHurt = maxHurt;
	}
	/**
	 * 是否计算闪避
	 * @return
	 */
	public boolean getVoltCalculate() {
		return voltCalculate;
	}
	public void setVoltCalculate(boolean voltCalculate) {
		this.voltCalculate = voltCalculate;
	}
	/**
	 * 技能基础命中率
	 * @return
	 */
	public float getBaseTohit() {
		return baseTohit;
	}
	public void setBaseTohit(float baseTohit) {
		this.baseTohit = baseTohit;
	}
	/**
	 * 是否计算暴击
	 * @return
	 */
	public boolean getCriticalCalculate() {
		return criticalCalculate;
	}
	public void setCriticalCalculate(boolean criticalCalculate) {
		this.criticalCalculate = criticalCalculate;
	}
	/**
	 * 是否计算格挡
	 * @return
	 */
	public boolean isDodgeCalculate() {
		return dodgeCalculate;
	}
	public void setDodgeCalculate(boolean dodgecalculate) {
		this.dodgeCalculate = dodgecalculate;
	}
	/**
	 * 基础技能暴击率
	 * @return
	 */
	public float getBaseCritical() {
		return baseCritical;
	}
	public void setBaseCritical(float baseCritical) {
		this.baseCritical = baseCritical;
	}
	/**
	 * 基础技能破击
	 * @return
	 */
	public int getBaseDodgeDefance() {
		return baseDodgeDefance;
	}
	public void setBaseDodgeDefance(int baseDodgeDefance) {
		this.baseDodgeDefance = baseDodgeDefance;
	}
	/**
	 * 公式参数a的值
	 * @return
	 */
	public float getFormulea() {
		return formulea;
	}
	public void setFormulea(float formulea) {
		this.formulea = formulea;
	}
	/**
	 * 公式参数b的值
	 * @return
	 */
	public float getFormuleb() {
		return formuleb;
	}
	public void setFormuleb(float formuleb) {
		this.formuleb = formuleb;
	}
	/**
	 * 公式参数c的值
	 * @return
	 */
	public float getFormulec() {
		return formulec;
	}
	public void setFormulec(float formulec) {
		this.formulec = formulec;
	}
	/**
	 * 公式参数c的值
	 * @return
	 */
	public float getFormuled() {
		return formuled;
	}
	public void setFormuled(float formuled) {
		this.formuled = formuled;
	}
	/**
	 * 让目标中的效果
	 * @return
	 */
	public List<SkillEffectConfig> getEffectConfigs() {
		return effectConfigs;
	}
	public void setEffectConfigs(List<SkillEffectConfig> effectConfigs) {
		this.effectConfigs = effectConfigs;
	}
	public List<Integer> getBuffIds() {
		List<Integer> buffids = new ArrayList<Integer>();
		buffids.add(1);
		return buffids;
	}
	
	/**
	 * 抵抗几率
	 */
	public float getResistSkillRate() {
		return 0.0f;
	}
	
	/**
	 * 抵抗buff几率
	 */
	public float getResistBuffRate(){
		return 0.0f;
	}
	
	/**
	 * 触发技能几率 
	 */
	public float getTriggerSkillRate() {
		return 0;
	}
	
	/**
	 * 仇恨系数1
	 * @param hatredN1
	 */
	public void setHatredN1(Float hatredN1) {
		this.hatredN1 = hatredN1;
	}
	
	/**
	 * 此处配置攻击者使用该技能对被攻击者产生的仇恨1
	 * @return
	 */
	public float getHatredN1(){
		return hatredN1;
	}
	
	
	/**
	 * 此处配置被该技能攻击后，被攻击者对攻击者产生的仇恨2
	 * @return
	 */
	public float getHatredN2() {
		return hatredN2;
	}
	
	
	/**
	 * 此处配置被该技能攻击后，被攻击者对攻击者产生的仇恨2
	 * @param hatredN2
	 */
	public void setHatredN2(float hatredN2) {
		this.hatredN2 = hatredN2;
	}
	
	
	public SkillFirePathType getSkillFirePathType() {
		return skillFirePathType;
	}
	public String getTrapId() {
		return trapId;
	}
	public int getTrapCount() {
		return trapCount;
	}
	public void setTrapCount(int trapCount) {
		this.trapCount = trapCount;
	}
	public void setTrapId(String trapId) {
		this.trapId = trapId;
	}
	public void setSkillFirePathType(SkillFirePathType skillFirePathType) {
		this.skillFirePathType = skillFirePathType;
	}
	public void setSkillFireType(SkillFireType skillFireType) {
		this.skillFireType = skillFireType;
	}
	
	/**
	 * 技能施法类型(普通技能、陷阱等)
	 * 
		0:普通技能
		1:穿越技能
		2:陷阱技能
		3:召唤技能
		4:特殊处理技能
		5:特殊处理技能
	 */
	public SkillFireType getSkillFireType() {
		return skillFireType;
	}
	/**
	 * 获取施法路径为扇形的半径
	 */
	public float getPathSectorRad() {
		return pathSectorRad;
	}
	/**
	 * 
	 */
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
