package com.junyou.stage.model.core.skill;

import com.junyou.stage.model.core.element.IFighter;


/**
 * @description buff
 * @author ShiJie Chi
 * @date 2012-3-2 下午5:01:29 
 */
public interface IBuff {

	/**
	 * 唯一标识
	 */
	Long getId();
	
	/**
	 * buffId
	 */
	String getBuffId();

	/**
	 * buff总类
	 */
	String getBuffCategory();
	
	/**
	 * buff基础命中
	 * @return
	 */
	public float getBaseHit();
	
	/**
	 * 获取持续时间
	 * @return
	 */
	public int getDuration();

	/**
	 * 等级
	 */
	Integer getLevel();

	/**
	 * buff层数
	 */
	int getLayer();

	/**
	 * 激活
	 */
	void start();

	/**
	 * 终止
	 */
	void end();

	/**
	 * 设置层数
	 */
	void setLayer(int newLayer);
	
	/**
	 * 特殊效果类型的值(有可能是技能ID )
	 * @return
	 */
	String getSpecialEffectValue();
	
	/**
	 * 特殊效果定时触发
	 */
	public void scheduleSpecialEffectTrigger();

	/**
	 * 获取施法者id
	 */
	Long getAttackerId();
	
	
	
	/**
	 * 特殊效果
	 */
	public String getSpecialEffect();

	/**
	 * 创建时间
	 */
	Long getStartTime();
	
	public void recoverStartTime(Long recoverStartTime);

	/**
	 * 获取额外数据
	 */
	<T> T getAdditionalData();

	/**
	 * 客户端所需buff信息格式
	 */
	Object getClientMsg();
	
	public Object getTrigerMsg();
	
	/**
	 * 设置buff进入的状态
	 */
	public void setStateType(Integer stateType);
	
	/**
	 * 获取buff拥有者
	 * @return
	 */
	public IFighter getOwner();
	
	/**
	 * 获取buff攻击者
	 * @return
	 */
	public IFighter getAttacker();
	
	/**
	 * 获取触发物品配置ID
	 * @return
	 */
	public String getTriggerGoodsId();
	
	public void setTriggerGoodsId(String triggerGoodsId);

	/**
	 * 死亡后是否消失
	 * @return true:消失
	 */
	public boolean isDeadClear();

	public void setDeadClear(boolean deadClear);
	
	/**
	 * 下线是否消失
	 * @return true:消失
	 */
	public boolean isOfflineClear();

	public void setOfflineClear(boolean offlineClear);
	
	/***
	 * 是否切换地图消失
	 * @return
	 */
	public boolean isChangeClear();
}
