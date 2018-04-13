package com.junyou.gameconfig.export.teleport;

public class VipCheckRule implements ICheckRule {

	private int vipLevel;
	
	public VipCheckRule(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.vipCheck(vipLevel);
		if(!pass){
			callback.vipError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		
	}

}
