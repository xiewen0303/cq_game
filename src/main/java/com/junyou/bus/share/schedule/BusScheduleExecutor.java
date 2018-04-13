package com.junyou.bus.share.schedule;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.kernel.schedule.ScheduleExecutor;

/**
 * @description 业务定时调度
 * 
 * @author ShiJie Chi
 * @date 2012-3-5 下午5:19:39 
 */
public class BusScheduleExecutor {
	
	private static ScheduleExecutor scheduleExecutor;

	public void setScheduleExecutor(ScheduleExecutor scheduleExecutor) {
		this.scheduleExecutor = scheduleExecutor;
	}
	
	public static ScheduledFuture<?> schedule(Runnable command,long delay,TimeUnit unit){
		
		return scheduleExecutor.schedule(command, delay, unit);
	}

}
