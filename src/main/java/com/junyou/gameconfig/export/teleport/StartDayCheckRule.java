package com.junyou.gameconfig.export.teleport;

/**
 * 开始日期验证
 *@author  LiuYu
 *@created 2014-5-15下午2:47:25
 */
public class StartDayCheckRule implements ICheckRule {

	private String startDay;
	
	
	public StartDayCheckRule(String startDay) {
		this.startDay = startDay;
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.startDayCheck(startDay);
		
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

