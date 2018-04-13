package com.junyou.stage.model.state;

import com.junyou.stage.model.core.state.IState;

public abstract class AbsState implements IState {

	private Object data;
	
	@SuppressWarnings("unchecked")
	public <T> T getData(){
		return (T)data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
