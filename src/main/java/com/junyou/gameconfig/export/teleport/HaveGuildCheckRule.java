package com.junyou.gameconfig.export.teleport;

/**
 * 有公会
 * @author DaoZheng Yuan
 * 2013-11-13 下午2:16:12
 */
public class HaveGuildCheckRule implements ICheckRule {

	
	public HaveGuildCheckRule() {
	
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.haveGuild();
		
		if(!pass){
			callback.noGuildError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		
	}

}
