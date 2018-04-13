package com.junyou.gameconfig.export.teleport;

/**
 * 官职等级验证
 *@author  DaoZheng Yuan
 *@created 2013-10-25下午2:34:25
 */
public class GuanZhiLevelCheckRule implements ICheckRule {

	private int gzLevel;
	
	public GuanZhiLevelCheckRule(Integer gzLevel) {
		this.gzLevel = gzLevel;
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.guanZhiLevelCheck(gzLevel);
		
		if(!pass){
			callback.gzLevelError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
