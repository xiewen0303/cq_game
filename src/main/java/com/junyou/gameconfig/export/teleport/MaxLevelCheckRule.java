package com.junyou.gameconfig.export.teleport;

public class MaxLevelCheckRule implements ICheckRule {

	private int maxLevel;
	
	public MaxLevelCheckRule(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.maxLevelCheck(maxLevel);
		
		if(!pass){
			callback.maxlevelError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
