package com.junyou.bus.shenqi.componentlistener;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;

public class ShenqiListener  implements IStateManagerListener{
	
	private IRole role;
	 
	public ShenqiListener(IRole role) {
		super();
		this.role = role;
	}


	@Override
	public void removeState(IState state) {
		if(state.getType() == StateType.DEAD){
			role.checkShenqiAttackSchedule();
		}
	}

	public IRole getRole() {
		return role;
	}

	public void setRole(IRole role) {
		this.role = role;
	}

	@Override
	public void clearState(IState state) {
	}


	@Override
	public void addState(IState state) {
		
	}


	@Override
	public void replaceState(IState state) {
		
	} 
}
