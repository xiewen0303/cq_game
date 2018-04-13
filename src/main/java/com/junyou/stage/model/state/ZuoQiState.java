package com.junyou.stage.model.state;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;

public class ZuoQiState extends AbsState implements IState {
	
	private IRole role;
	
	public ZuoQiState(IRole role) {
		this.role = role;
	}

	@Override
	public StateType getType() {
		return StateType.ZUOQI;
	}

}
