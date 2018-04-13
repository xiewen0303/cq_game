package com.junyou.gameconfig.export.teleport;

/**
 * 结束日期验证
 *@author  LiuYu
 *@created 2014-5-15下午2:47:25
 */
public class EndDayCheckRule implements ICheckRule {

	private String endDay;
	
	
	public EndDayCheckRule(String endDay) {
		this.endDay = endDay.trim() + " 23:59:59";
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.endDayCheck(endDay);
		
		if(!pass){
			callback.endTimeError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		// TODO Auto-generated method stub
		
	}

}

