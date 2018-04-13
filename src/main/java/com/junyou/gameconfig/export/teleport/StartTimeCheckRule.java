package com.junyou.gameconfig.export.teleport;

/**
 * 开始时间验证
 *@author  DaoZheng Yuan
 *@created 2013-10-25下午2:34:25
 */
public class StartTimeCheckRule implements ICheckRule {

	private long startTime;
	
	
	
	public StartTimeCheckRule(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.startTimeCheck(startTime);
		
		if(!pass){
			callback.startTimeError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
