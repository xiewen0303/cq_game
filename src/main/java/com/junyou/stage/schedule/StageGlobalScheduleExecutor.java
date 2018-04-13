package com.junyou.stage.schedule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.kernel.schedule.ScheduleExecutor;
import com.kernel.token.TokenManager;

/**
 * @description 场景定时调度
 * 
 * @author ShiJie Chi
 * @date 2012-3-5 下午5:19:39 
 */
public class StageGlobalScheduleExecutor {
	
	private static TokenManager tokenManager;
	
	private static ScheduleExecutor scheduleExecutor;
	
	/**
	 * key:component [key:identity value: scheduledFuture]
	 */
	static ConcurrentMap<String,ConcurrentMap<String, ScheduledFuture>> taskFutureMap = new ConcurrentHashMap<String, ConcurrentMap<String, ScheduledFuture>>(); 

	public void setScheduleExecutor(ScheduleExecutor scheduleExecutor) {
		this.scheduleExecutor = scheduleExecutor;
	}
	
	public static ScheduledFuture<?> schedule(Runnable command,long delay,TimeUnit unit){
		
		return scheduleExecutor.schedule(command, delay, unit);
	}
	
	public void setTokenManager(TokenManager _tokenManager) {
		tokenManager = _tokenManager;
	}

	public static ScheduleExecutor getScheduleExecutor() {
		return scheduleExecutor;
	}

	public static TokenManager getTokenManager() {
		return tokenManager;
	}
	
	
}
