package com.junyou.stage.model.core.hatred;

import java.util.ArrayList;
import java.util.List;

import com.junyou.stage.model.core.element.IFighter;

public class HatredListenerHelper implements IHatredManagerListener {

	private List<IHatredManagerListener> listeners;
	
	@Override
	public void addHatred(IFighter fighter) {
		if( listeners != null ){
			
			for( IHatredManagerListener listener : listeners ){
				listener.addHatred(fighter);
			}
			
		}
	}

	public void addListener(IHatredManagerListener listener) {
		if( listeners == null ){
			listeners = new ArrayList<IHatredManagerListener>();
		}
		listeners.add(listener);
	}

	
	
}
