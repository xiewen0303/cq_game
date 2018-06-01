package com.kernel.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.kernel.pool.executor.ThreadNameFactory;

/**
 * @description 定时任务执行器 
 * @author hehj
 * 2010-7-2 下午04:37:38
 */
public class ScheduleExecutor {

	private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadNameFactory("sche-exc-"));
	
	/**
	 * 在指定延迟后执行一个任务
	 * @param taskCategary 任务分类标识
	 * @param command 需要执行的任务
	 * @param delay 延迟的时间
	 * @param unit 时间单位
	 * @return 执行结果的回调对象
	 */
	public ScheduledFuture<?> schedule(Runnable command,long delay,TimeUnit unit){
		return getService().schedule(command, delay, unit);
	}
	
	/**
	 * 以固定的执行间隔执行指定的任务
	 * @param taskCategary 任务分类标识
	 * @param command 需要执行的任务
	 * @param initialDelay 延迟的时间
	 * @param period 执行间隔
	 * @param unit 时间单位
	 * @return 执行结果的回调对象
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,long initialDelay,long period,TimeUnit unit){
		return getService().scheduleWithFixedDelay(command, initialDelay, period, unit);
	}

	/**
	 * 以固定的周期执行指定的任务
	 * @param taskCategary 任务分类标识
	 * @param command 需要执行的任务
	 * @param initialDelay 延迟的时间
	 * @param period 执行周期
	 * @param unit 时间单位
	 * @return 执行结果的回调对象
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,long initialDelay,long period,TimeUnit unit){
		return getService().scheduleAtFixedRate(command, initialDelay, period, unit);
	}
	
	private ScheduledExecutorService getService(){
		return executorService;
	}
}
