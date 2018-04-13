package com.junyou.stage.model.hatred;

import java.util.concurrent.TimeUnit;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.AbsHatredManager;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.hatred.IHatredRule;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.state.AiFightState;

public class MonsterHatredManager2 extends AbsHatredManager {

	public MonsterHatredManager2(IFighter owner, IHatredRule hatredRule) {
		super(owner, hatredRule);
	}

	@Override
	public IFighter getHatredBelongElement(IFighter element) {
		return element.getOwner();
	}
	
	@Override
	public void addActiveHatred(IFighter target, int val) {
		add(target, val);
		addActiveHatredHandle(target,val);
	}
	
	private void add(IFighter element, int i) {
		
		IFighter fighter = getHatredBelongElement(element);
		
		if(fighter instanceof IRole){
			IRole role = (IRole)fighter;
			if(role.isGm()){
				return;//GM不增加仇恨
			}
		}
		
		if( hatredRule.getHatred(fighter) == null ){
			initHatredElement(fighter);
		}
		
		hatredRule.incrHatred(fighter, i);
	}
	
	/**
	 * 初次添加element作为仇恨对象
	 * @param element
	 */
	private void initHatredElement(IFighter element){
		listenerHelper.addHatred(element);
	}

	@Override
	public void addActiveHatredHandle(IFighter target, int val) {
		
		//巡逻状态下，进入战斗状态
		IAi ai = ((IMonster)fighter).getAi();
		if(ai == null){
			return;
		}
		if(!fighter.getStateManager().contains(StateType.FIGHT)){
			fighter.getStateManager().add(new AiFightState());
			ai.interruptSchedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
		}else if(ai.isStop()){
			ai.interruptSchedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
		}		
		
	}

	@Override
	public void addPassiveHatredHandle(IFighter target, int val) {
		
		//巡逻状态下，进入战斗状态
		IAi ai = ((IMonster)fighter).getAi();
		if(ai == null){
			return;
		}
		if(!fighter.getStateManager().contains(StateType.FIGHT)){
			fighter.getStateManager().add(new AiFightState());
			ai.interruptSchedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
		}else if(ai.isStop()){
			ai.interruptSchedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
		}
		
	}

	@Override
	public IHatred getLastActiveAttackTarget() {
		return getHatredest();
	}

}
