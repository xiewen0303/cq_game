package com.junyou.gameconfig.export.teleport;

/**
 * 结束时间验证
 *@author  DaoZheng Yuan
 *@created 2013-10-25下午2:34:25
 */
public class EndTimeCheckRule implements ICheckRule {

	private long endTime;
	
	public EndTimeCheckRule(long endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.endTimeCheck(endTime);
		
		if(!pass){
			callback.endTimeError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		
	}

}
