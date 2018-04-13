package com.junyou.stage.model.core.state;

import java.util.ArrayList;
import java.util.List;

public class StateManagerListenerHelper implements IStateManagerListener {

	private List<IStateManagerListener> listeners;
	
	public void addListener(IStateManagerListener stateManagerListener){
		if( listeners == null ){
			listeners = new ArrayList<IStateManagerListener>();
		}
		listeners.add(stateManagerListener);
	}
	
	@Override
	public void addState(IState state) {
		if( listeners != null ){
			for( IStateManagerListener listener : listeners ){
				listener.addState(state);
			}
		}
	}

	@Override
	public void removeState(IState state) {
		if( listeners != null ){
			for( IStateManagerListener listener : listeners ){
				listener.removeState(state);
			}
		}
	}

	@Override
	public void replaceState(IState state) {
		if( listeners != null ){
			for( IStateManagerListener listener : listeners ){
				listener.replaceState(state);
			}
		}
	}

	@Override
	public void clearState(IState state) {
		if( listeners != null ){
			for( IStateManagerListener listener : listeners ){
				listener.clearState(state);
			}
		}
	}

}
