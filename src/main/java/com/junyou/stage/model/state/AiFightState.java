package com.junyou.stage.model.state;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;

public class AiFightState extends AbsState implements IState {
	
	@Override
	public StateType getType() {
		return StateType.FIGHT;
	}


}
