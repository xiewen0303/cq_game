package com.junyou.stage.model.hatred;

import java.util.List;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.HatredListenerHelper;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.hatred.IHatredManagerListener;
import com.junyou.stage.model.state.AiFightState;

public class PetHatredManager implements IHatredManager {

	private HatredListenerHelper listenerHelper = new HatredListenerHelper();
	
	private IFighter fighter;
	
	
	
	public PetHatredManager(IFighter fighter) {
		this.fighter = fighter;
	}

	public IFighter getFighter() {
		return fighter;
	}

	@Override
	public IHatred getLastActiveAttackTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHatred getHatredest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addActiveHatred(IFighter target, int val) {
		//通知宠物进行攻击目标
		IFighter role = fighter.getOwner();
		
		if(role != null){
			role.getHatredManager().addPassiveHatred(target, val);
			fighter.getStateManager().add(new AiFightState());
		}
		
	}

	@Override
	public void addPassiveHatred(IFighter target, int val) {
		//通知宠物进行攻击目标
		IFighter role = fighter.getOwner();
		
		if(role != null){
			role.getHatredManager().addPassiveHatred(target, val);
			fighter.getStateManager().add(new AiFightState());
		}
	}

	@Override
	public void refreshHatred() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deadHandle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addInsideHatred(Integer hatredVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(IHatredManagerListener listener) {
		listenerHelper.addListener(listener);
	}

	@Override
	public boolean checkTargetHatred(IFighter target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearHatred(long targetId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Long> getAllHaters() {
		// TODO Auto-generated method stub
		return null;
	}


}
