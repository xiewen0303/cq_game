package com.junyou.stage.model.state;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;

public class DaZuoState extends AbsState implements IState {

	@Override
	public StateType getType() {
		return StateType.DAZUO;
	}

}
