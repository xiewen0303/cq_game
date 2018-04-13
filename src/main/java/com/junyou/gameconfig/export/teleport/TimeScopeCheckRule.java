package com.junyou.gameconfig.export.teleport;

import com.junyou.configure.ClientTimeScope;

public class TimeScopeCheckRule implements ICheckRule {

	private ClientTimeScope clientTimeScope;
	
	public TimeScopeCheckRule(ClientTimeScope clientTimeScope) {
		this.clientTimeScope = clientTimeScope;
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.activeTimeCheck(clientTimeScope);
		
		if(!pass){
			callback.timeError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
