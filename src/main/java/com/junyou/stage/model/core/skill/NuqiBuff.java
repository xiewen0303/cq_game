package com.junyou.stage.model.core.skill;

import com.junyou.constants.GameConstants;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.element.role.IRole;

/**
 * 怒气技能BUFF
 * @author LiuYu
 * 2015-6-9 上午9:32:14
 */
public class NuqiBuff implements IBuff{
	
	private IRole owner;
	
	private String buffCategory;//怒气技能id
	
	private int layer;
	
	public NuqiBuff(IRole role,String skillId){
		this.owner = role;
		this.buffCategory = skillId;
	}

	@Override
	public Long getId() {
		return GameConstants.NUQI_BUFF_GUID;
	}

	@Override
	public String getBuffId() {
		return GameConstants.NUQI_BUFF_ID;
	}

	@Override
	public String getBuffCategory() {
		return buffCategory;
	}

	@Override
	public float getBaseHit() {
		return 0;
	}

	@Override
	public int getDuration() {
		return 0;
	}

	@Override
	public Integer getLevel() {
		return 0;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public void start() {
		owner.nuqiSkill(getBuffCategory());
	}

	@Override
	public void end() {
		owner.stopNuqiSkill(getBuffCategory());
	}

	@Override
	public void setLayer(int newLayer) {
		layer = newLayer;
	}

	@Override
	public String getSpecialEffectValue() {
		return null;
	}

	@Override
	public void scheduleSpecialEffectTrigger() {
		
	}

	@Override
	public Long getAttackerId() {
		return null;
	}

	@Override
	public String getSpecialEffect() {
		return null;
	}

	@Override
	public Long getStartTime() {
		return null;
	}

	@Override
	public void recoverStartTime(Long recoverStartTime) {
	}

	@Override
	public <T> T getAdditionalData() {
		return null;
	}

	@Override
	public Object getClientMsg() {
		return null;
	}

	@Override
	public Object getTrigerMsg() {
		return null;
	}

	@Override
	public void setStateType(Integer stateType) {
	}

	@Override
	public IFighter getOwner() {
		return owner;
	}

	@Override
	public IFighter getAttacker() {
		return owner;
	}

	@Override
	public String getTriggerGoodsId() {
		return null;
	}

	@Override
	public void setTriggerGoodsId(String triggerGoodsId) {
	}

	@Override
	public boolean isDeadClear() {
		return true;
	}

	@Override
	public void setDeadClear(boolean deadClear) {
	}

	@Override
	public boolean isOfflineClear() {
		return true;
	}

	@Override
	public void setOfflineClear(boolean offlineClear) {
		
	}

	@Override
	public boolean isChangeClear() {
		return true;
	}
	
}
