package com.junyou.stage.model.core.skill;

import java.util.ArrayList;
import java.util.List;

public class BuffManagerListenerHelper implements IBuffManagerListener {
	
	private List<IBuffManagerListener> listeners;
	
	public void addListener(IBuffManagerListener buffListener){
		if( listeners == null ){
			listeners = new ArrayList<IBuffManagerListener>();
		}
		listeners.add(buffListener); 
	}
	
	@Override
	public void addBuff(IBuff buff) {
		if( listeners != null ){
			for( IBuffManagerListener listener : listeners ){
				listener.addBuff(buff);
			}
		}
	}

	@Override
	public void removeBuff(IBuff buff) {
		if( listeners != null ){
			for( IBuffManagerListener listener : listeners ){
				listener.removeBuff(buff);
			}
		}
	}

}
