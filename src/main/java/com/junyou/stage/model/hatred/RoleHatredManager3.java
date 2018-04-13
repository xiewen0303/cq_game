package com.junyou.stage.model.hatred;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.AiHeartBeatInterval;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.AbsHatredManager;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.hatred.IHatredRule;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.state.AiFightState;
import com.junyou.stage.model.state.RoleFightState;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.datetime.GameSystemTime;

public class RoleHatredManager3 extends AbsHatredManager implements IRoleHatredManager {
	
	private Map<Long,Long> ignoreBattleAttackableList;
	
	public static final Integer FIGHT_STATE_CHECK_PERIOD = 10000;
	public static final Integer IGNORE_BATTLE_DURATION = 60000;
	
	private IHatred lastActiveAttackTarget;
	

	public RoleHatredManager3(IFighter owner, IHatredRule hatredRule) {
		super(owner, hatredRule);
		hatredRule.setHatredExpireTime(FIGHT_STATE_CHECK_PERIOD);
	}

	@Override
	public IFighter getHatredBelongElement(IFighter element) {
		return element.getOwner();
	}

	@Override
	public void addActiveHatredHandle(IFighter target, int val) {
		
		//如果未进入战斗状态、则进入战斗状态
		if(!fighter.getStateManager().contains(StateType.FIGHT)){
			fighter.getStateManager().add(new RoleFightState());
			scheduleFightStateCheck();
		}
		
		lastActiveAttackTarget = hatredRule.getHatred(target);
		
		//通知宠物进行攻击目标
		IRole role = (IRole)fighter;
		Pet pet = role.getPet();
		if(null != pet){	
			if(pet.getStateManager().contains(StateType.XUNLUO)){
				
				if(!pet.getStateManager().isDead()){
					pet.getStateManager().add(new AiFightState());
					pet.getAi().interruptSchedule(AiHeartBeatInterval.EMPLOYEE_CHANGE_TARGET, TimeUnit.SECONDS);
				}
			}
		}
		//启动神器攻击定时
		role.checkShenqiAttackSchedule();
	}

	@Override
	public void addPassiveHatredHandle(IFighter target, int val) {
		
		//如果未进入战斗状态、则进入战斗状态
		if(!fighter.getStateManager().contains(StateType.FIGHT)){
			fighter.getStateManager().add(new RoleFightState());
			scheduleFightStateCheck();
		}
		
//		IHatred hatred = null;
//		if(null != (hatred = hatredRule.getHatred(target)) && val > 0 && hatred.getVal() == val){
//			//目标为新建立仇恨，进入无视模式可攻击列表
//			if(null == ignoreBattleAttackableList){
//				ignoreBattleAttackableList = new HashMap<Long, Long>();
//			}
//			ignoreBattleAttackableList.put(target.getId(), GameSystemTime.getSystemMillTime());
//			
//		}else{
//			
//			if(inIgnoreBattleAttackableList(target)){
//				ignoreBattleAttackableList.put(target.getId(), GameSystemTime.getSystemMillTime());
//			}
//			
//		}
		
		lastActiveAttackTarget = hatredRule.getHatred(target);
		
		//通知宠物进行攻击目标
		IRole role = (IRole)fighter;
		Pet pet = role.getPet();
		if(null != pet){
			if(pet.getStateManager().contains(StateType.XUNLUO)){
				
				if(!pet.getStateManager().isDead()){
					pet.getStateManager().add(new AiFightState());
					pet.getAi().interruptSchedule(AiHeartBeatInterval.EMPLOYEE_CHANGE_TARGET, TimeUnit.SECONDS);
				}
			}
		}
		//启动神器攻击定时
		role.checkShenqiAttackSchedule();
	}

	@Override
	public void scheduleFightStateCheck() {
		if(fighter == null || fighter.getStage() == null){
			return;
		}
		StageTokenRunable runable = new StageTokenRunable(fighter.getId(), fighter.getStage().getId(), InnerCmdType.INNER_FIGHT_STATE_CHECK, null);
		
		int period = getFightStateCheckPeriod();
		fighter.getScheduler().schedule(fighter.getId().toString(), GameConstants.COMPONENT_FIGHT_STATE,runable, period, TimeUnit.MILLISECONDS);	
	}
	
	/**
	 * 获取战斗状态检测间隔
	 */
	private int getFightStateCheckPeriod() {
		IHatred hatred = hatredRule.getLastHatred();
		if(hatred == null){
			return 0;
		}
		
		int period = FIGHT_STATE_CHECK_PERIOD - (int)(GameSystemTime.getSystemMillTime() - hatred.getlasttime());
		if(period < 0) period = 0;
		
		return period;
	}

	@Override
	public boolean inIgnoreBattleAttackableList(IFighter target) {
		
		if(null != ignoreBattleAttackableList){
			
			Long lastAttackTime = ignoreBattleAttackableList.get(target.getId());
			if(null != lastAttackTime && GameSystemTime.getSystemMillTime() - lastAttackTime < IGNORE_BATTLE_DURATION){
				return true;
			}
			
			ignoreBattleAttackableList.remove(target.getId());
		}
		
		return false;
	}

	@Override
	public void deadHandle() {
		super.deadHandle();
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public IHatred getLastActiveAttackTarget() {
		return lastActiveAttackTarget;
	}
	
	@Override
	public void refreshHatred() {
		hatredRule.refreshHatred();
		if(lastActiveAttackTarget != null){
			lastActiveAttackTarget = hatredRule.getHatred(lastActiveAttackTarget.getId());
		}
	}
	
	
	
	
}
