package com.junyou.gameconfig.export.teleport;

public class MinLevelCheckRule implements ICheckRule {

	private int minLevel;
	
	public MinLevelCheckRule(Integer minLevel) {
		this.minLevel = minLevel;
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.minLevelCheck(minLevel);
		
		if(!pass){
			callback.minLevelError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
