package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description 效果 
 *
 * @author LiuJuan
 * @date 2012-3-12 下午1:52:41
 */
public class BuffConfig{

	private String effectId;
	private String effectName;
	private int effectLevel;
	private String effectType;
	private int lastTime;
	private List<String> cover = new ArrayList<String>();
	private int maxStack;
	private Map<String, Long> effectPro = new HashMap<>();
	
	private String skill;
	private int interval;
	private String specialEffect;
	
	//效果命中
	private float baseHit;
	//死亡后效果是否消失
	private boolean deadClear;
	//下线后效果是否消失
	private boolean offlineClear;
	//切换地图后消失
	private boolean changeClear;
	//移动后清除
	private boolean moveClear;
	
	//是否不可攻击
	private boolean attackAble;
	//是否不可移动
	private boolean moveAble;
	//是否不可被攻击
	private boolean beiAttackAble;
	//是否不可被怪物攻击
	private boolean beiAttackAble1;
	//是否不可骑乘
	private boolean rideAble;
	//是否不可使用消耗品
	private boolean consumAble;
	//是否不可传送
	private boolean teleportAble;
	//是否只对普通怪物生效
	private boolean isMonsterNomal;

	/**
	 * 是否不可攻击
	 * @return true:不可攻击
	 */
	public boolean isAttackAble() {
		return attackAble;
	}

	public void setAttackAble(boolean attackAble) {
		this.attackAble = attackAble;
	}

	/**
	 * 是否不可移动
	 * @return true:不可移动
	 */
	public boolean isMoveAble() {
		return moveAble;
	}

	public void setMoveAble(boolean moveAble) {
		this.moveAble = moveAble;
	}

	/**
	 * 是否不可被攻击
	 * @return true:不可被攻击
	 */
	public boolean isBeiAttackAble() {
		return beiAttackAble;
	}

	public void setBeiAttackAble(boolean beiAttackAble) {
		this.beiAttackAble = beiAttackAble;
	}

	/**
	 * 下线后效果是否消失
	 */
	public boolean isOfflineClear() {
		return offlineClear;
	}

	public void setOfflineClear(boolean offlineClear) {
		this.offlineClear = offlineClear;
	}

	/**
	 * 死亡后效果是否消失
	 * @return true:消失
	 */
	public boolean isDeadClear() {
		return deadClear;
	}

	public void setDeadClear(boolean deadClear) {
		this.deadClear = deadClear;
	}

	/**
	 * 效果基础命中
	 * @return
	 */
	public float getBaseHit() {
		return baseHit;
	}

	public void setBaseHit(float baseHit) {
		this.baseHit = baseHit;
	}

	/**
	 * 效果编号
	 * @return
	 */
	public String getEffectId() {
		return effectId;
	}

	public void setEffectId(String effectId) {
		this.effectId = effectId;
	}

	/**
	 * 效果名字
	 * @return
	 */
	public String getEffectName() {
		return effectName;
	}

	public void setEffectName(String effectName) {
		this.effectName = effectName;
	}
	/**
	 * 效果等级
	 * @return
	 */
	public int getEffectLevel() {
		return effectLevel;
	}

	public void setEffectLevel(int effectLevel) {
		this.effectLevel = effectLevel;
	}
	/**
	 * 计算抵抗用的类型编号
	 * @return
	 */
	public String getEffectType() {
		return effectType;
	}

	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}
	/**
	 * 持续时间（ms)
	 * @return
	 */
	public int getLastTime() {
		return lastTime;
	}

	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	/**
	 * 效果覆盖
	 * @return
	 */
	public List<String> getCover() {
		return cover;
	}
	public void setCover(List<String> cover) {
		this.cover = cover;
	}
	
	/**
	 * 最大叠加层数
	 * @return
	 */
	public int getMaxStack() {
		return maxStack;
	}

	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}
	/**
	 * 效果作用的效果项
	 * @return
	 */
	public Map<String, Long> getEffectPro() {
		return effectPro;
	}

	public void setEffectPro(Map<String, Long> effectPro) {
		this.effectPro = effectPro;
	}

	/**
	 * 可覆盖的buffs
	 */
	public List<String> getExcludeBuffCategory() {
		
		return cover;
	}

	/**
	 * 能否叠加
	 */
	public boolean canOverlap() {
		return (maxStack > 1) ? true:false;
	}

	public boolean isChangeClear() {
		return changeClear;
	}

	public void setChangeClear(boolean changeClear) {
		this.changeClear = changeClear;
	}

	public boolean isMoveClear() {
		return moveClear;
	}

	public void setMoveClear(boolean moveClear) {
		this.moveClear = moveClear;
	}

	public boolean isBeiAttackAble1() {
		return beiAttackAble1;
	}

	public void setBeiAttackAble1(boolean beiAttackAble1) {
		this.beiAttackAble1 = beiAttackAble1;
	}

	public boolean isRideAble() {
		return rideAble;
	}

	public void setRideAble(boolean rideAble) {
		this.rideAble = rideAble;
	}

	public boolean isConsumAble() {
		return consumAble;
	}

	public void setConsumAble(boolean consumAble) {
		this.consumAble = consumAble;
	}

	public boolean isTeleportAble() {
		return teleportAble;
	}

	public void setTeleportAble(boolean teleportAble) {
		this.teleportAble = teleportAble;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getSpecialEffect() {
		return specialEffect;
	}

	public void setSpecialEffect(String specialEffect) {
		this.specialEffect = specialEffect;
	}

	public boolean isMonsterNomal() {
		return isMonsterNomal;
	}

	public void setMonsterNomal(boolean isMonsterNomal) {
		this.isMonsterNomal = isMonsterNomal;
	}
	
	
}
