package com.junyou.io.global;

import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;

public class PingVo {
	
	
	
	private int interval;
	
	private long starttime;
	
	private String msg = "";
	
	private int ljTimes = 0;
	
	public boolean checkInterval() {
		
		long realInterval = (GameSystemTime.getSystemMillTime() - starttime);
		
		//最大累积10次,判断为异常断线
		boolean isNoMaxTimes = ljTimes < 6;
		//时间是否正常
		boolean isNomal = realInterval >= interval * (1 - 0.1);
		if(!isNomal){
			addLjTimes();
		}
		
		//异常打印日志
		boolean isSuccess = isNoMaxTimes || isNomal;
		if(!isSuccess){
			msg = "ljTimes:"+ljTimes+",realInterval:"+realInterval+","+interval;
			ChuanQiLog.error("ljTimes:"+ljTimes+",isNoMaxTimes:"+isNoMaxTimes+"-isNomal:"+isNomal);
		}
		return isSuccess;
	}

	public Object getErrorCode() {
		return 999999;
	}
	
	public String getCalcMsg(){
		return msg;
	}

	/**
	 * 增加异常次数
	 */
	public void addLjTimes(){
		++ljTimes;
	}
	
	public Integer genInterval() {
		
		this.interval = 2000 + Lottery.roll(2000);
		this.starttime = GameSystemTime.getSystemMillTime();
		
		
		return interval;
	}
	
	public long getStartTime(){
		return starttime;
	}

}
