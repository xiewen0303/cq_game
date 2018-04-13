package com.junyou.stage.model.skill.buff;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.BuffConstant;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.skill.buff.specialeffect.ISpecialEffectHandler;
import com.junyou.stage.model.skill.buff.specialeffect.SpecialEffectHandlerFactory;
import com.junyou.stage.model.state.BuffStateFactory;
import com.junyou.stage.model.state.YinShen;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.datetime.GameSystemTime;

public class Buff implements IBuff {
	
	private Long id;
	private String category;
	private String buffId;
	private Integer level;
	
	private int layer = 1;
	private long startTime;
	private int duration;
	
	private int period = -1;
	private String specialEffect;
	private boolean firstTriggered = false;
	
	
	private Long attackerId;
	
	private IFighter attacker;
	private IFighter owner;
	private Map<String,Long> attributes;
	
	private Integer stateType;

	private Object additionalData;
	
	private String specialEffectValue;
	
	private float baseHit;
	
	private String triggerGoodsId;
	private boolean deadClear;
	private boolean offlineClear;
	private boolean changeClear;
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
	
	public Buff(Long id,String category, Integer level, String buffId,float baseHit) {
		this.id = id;
		this.category = category;
		this.level = level;
		this.buffId = buffId;
		this.baseHit = baseHit;
	}

	
	/**
	 * 死亡后是否消失
	 * @return true:消失
	 */
	public boolean isDeadClear() {
		return deadClear;
	}



	public void setDeadClear(boolean deadClear) {
		this.deadClear = deadClear;
	}

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
	 * 下线是否消失
	 * @return true:消失
	 */
	public boolean isOfflineClear() {
		return offlineClear;
	}


	public void setOfflineClear(boolean offlineClear) {
		this.offlineClear = offlineClear;
	}


	public String getTriggerGoodsId() {
		return triggerGoodsId;
	}



	public void setTriggerGoodsId(String triggerGoodsId) {
		this.triggerGoodsId = triggerGoodsId;
	}



	@Override
	public String getBuffId() {
		return buffId;
	}

	@Override
	public String getBuffCategory() {
		return category;
	}

	
	public float getBaseHit() {
		return baseHit;
	}

	public void setBaseHit(float baseHit) {
		this.baseHit = baseHit;
	}

	@Override
	public Integer getLevel() {
		return level;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setLayer(int newLayer) {
		this.layer = newLayer;
	}
	
	
	
	public String getSpecialEffectValue() {
		return specialEffectValue;
	}

	public void setSpecialEffectValue(String specialEffectValue) {
		this.specialEffectValue = specialEffectValue;
	}

	@Override
	public void start() {
		
		//增加属性
		if(null != attributes && attributes.size() != 0){
			for(int i = 0 ; i< layer ; i++){
				this.owner.getFightAttribute().incrBaseAttribute(BaseAttributeType.BUFF,attributes);
			}
		}
		
		if(null != stateType){
			IState state = BuffStateFactory.create(stateType);
			if(null != state){
				
				if(StateType.YINSHEN.equals(state.getType())){
					//隐身状态
					YinShen ys = (YinShen) state;
					ys.setBuffGuid(getId());
					ys.setBuffConfigId(getBuffId());
				}
				
				this.owner.getStateManager().add(state);
			}
		}
		
		//加上额外状态[不可移动,不可攻击,不可被攻击]
		addOtherState();

		if(startTime == 0){
			startTime = GameSystemTime.getSystemMillTime();
		}
		
		if(duration >= 0){
			
			if(owner.getStage() != null){
				StageTokenRunable runable = new StageTokenRunable(owner.getId(), owner.getStage().getId(), InnerCmdType.INNER_BUFF_END, new Object[]{getBuffCategory(),getId(),owner.getId(),owner.getElementType().getVal(),owner.getStage().getId()});
				owner.getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_BUFF_END, runable, duration, TimeUnit.MILLISECONDS);
			}
		}
	
		scheduleSpecialEffectTrigger();
	}
	
	/**
	 * 加上额外状态[不可移动,不可攻击,不可被攻击]
	 */
	private void addOtherState(){
		//不可移动
		if(isMoveAble()){
			IState state = BuffStateFactory.create(StateType.NO_MOVE.getVal());
			this.owner.getStateManager().add(state);
		}
		//不可攻击
		if(isAttackAble()){
			IState state = BuffStateFactory.create(StateType.NO_ATTACK.getVal());
			this.owner.getStateManager().add(state);
		}
		//不可被攻击
		if(isBeiAttackAble()){
			IState state = BuffStateFactory.create(StateType.NO_ATTACKED.getVal());
			this.owner.getStateManager().add(state);
		}
	}

	
	public void scheduleSpecialEffectTrigger(){
		
		if(null != specialEffect){
			
			if(!firstTriggered){
				ISpecialEffectHandler handler = SpecialEffectHandlerFactory.getHandler(specialEffect);
				handler.triggerHandle(this);
				
				firstTriggered = true;
			}
			
			if(period > 0){
				
				if(owner.getStage() != null){
					StageTokenRunable runable = new StageTokenRunable(owner.getId(), owner.getStage().getId(), InnerCmdType.INNER_BUFF_TRIGGER, new Object[]{getBuffCategory(),getId(),owner.getId(),owner.getElementType().getVal(),owner.getStage().getId()});
					owner.getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_BUFF_TRIGGER, runable, period, TimeUnit.MILLISECONDS);
				}
			}
		}
		
	}
	
	@Override
	public void end() {
		
		owner.getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_BUFF_END);
		
		//去除属性
		if(null != attributes){
			for(int i = 0 ; i< layer ; i++){
				this.owner.getFightAttribute().descBaseAttribute(BaseAttributeType.BUFF,attributes);
			}
		}
		
		if(null != stateType){
			StateType existType = BuffStateFactory.getType(stateType);
			if(null != existType){
				this.owner.getStateManager().remove(existType);
			}
		}
		
		//删除额外状态[不可移动,不可攻击,不可被攻击]
		removeOtherState();
		
		//特殊效果结束
		if(null != specialEffect){
			ISpecialEffectHandler handler = SpecialEffectHandlerFactory.getHandler(specialEffect);
			handler.overHandle(this);
			
			owner.getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_BUFF_END);
		}
	}

	
	/**
	 * 删除额外状态[不可移动,不可攻击,不可被攻击]
	 */
	private void removeOtherState(){
		//不可移动
		if(isMoveAble()){
			this.owner.getStateManager().remove(StateType.NO_MOVE);
		}
		//不可攻击
		if(isAttackAble()){
			this.owner.getStateManager().remove(StateType.NO_ATTACK);
		}
		//不可被攻击
		if(isBeiAttackAble()){
			this.owner.getStateManager().remove(StateType.NO_ATTACKED);
		}
	}
	
	/**
	 * 设置buff拥有者
	 */
	public void setOwner(IFighter owner) {
		this.owner = owner;
	}
	
	/**
	 * 获取buff拥有者
	 * @return
	 */
	public IFighter getOwner(){
		return owner;
	}

	/**
	 * 设置buff附带属性
	 */
	public void setAttributes(Map<String, Long> attributes) {
		this.attributes = new ReadOnlyMap<String, Long>(attributes);
	}

	/**
	 * 设置攻击者id
	 */
	public void setAttackerId(Long attackerId) {
		this.attackerId = attackerId;
	}

	/**
	 * 设置持续时间
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * 获取持续时间
	 * @return
	 */
	public int getDuration(){
		return duration;
	}
	
	/**
	 * 设置执行周期
	 */
	public void setSpecialPeriod(int period) {
		this.period = period;
	}

	@Override
	public Long getAttackerId() {
		return attackerId;
	}

	/**
	 * 设置特殊效果
	 */
	public void setSpecialEffect(String specialEffect) {
		this.specialEffect = specialEffect;
	}

	public String getSpecialEffect() {
		return specialEffect;
	}

	@Override
	public Long getStartTime() {
		return startTime;
	}
	
	public void recoverStartTime(Long recoverStartTime){
		this.startTime = recoverStartTime;
	}

	/**
	 * 获取额外数据
	 */
	@Override
	public <T> T getAdditionalData() {
		return (T)additionalData;
	}

	/**
	 * 设置额外数据
	 */
	public void setAdditionalData(Object additionalData) {
		this.additionalData = additionalData;
	}

	@Override
	public Object getClientMsg() {
		int clientDuration = BuffConstant.FOREVER_BUFF;
		if(duration != clientDuration){
			clientDuration = duration - ((int)(GameSystemTime.getSystemMillTime() - startTime));
		}
		return new Object[]{getId(),getBuffCategory(),clientDuration,getAttackerId()};
	}
	
	public Object getTrigerMsg(){
		return new Object[]{getOwner().getId(),getId()};
	}

	public void setStateType(Integer stateType) {
		this.stateType = stateType;
	}


	public void setAttacker(IFighter attacker) {
		this.attacker = attacker;
		this.attackerId = attacker.getId();
	}


	@Override
	public IFighter getAttacker() {
		return attacker;
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
}
