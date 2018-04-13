package com.junyou.stage.model.element.monster.ai;

import java.util.concurrent.TimeUnit;

public interface IAi{
	
	/**
	 * 紧急反应时间
	 */
	public static final Integer CRITICAL_RESPONSE_TIME = 200;
	
	/**
	 * 正常反应时间
	 */
	public static final Integer NOMAL_RESPONSE_TIME = 2000;
	
	/**
	 * 巡逻间隔
	 */
	public static final Integer XUNLUO_PERIOD = 10000;

	/**
	 * 开启定时
	 */
	void schedule(int delay,TimeUnit unit);
	
	/**
	 * 中断并重启定时(强行关闭之前定时)
	 */
	void interruptSchedule(int delay,TimeUnit unit);

	/**
	 * 停止
	 */
	void stop();

	/**
	 * 心跳是否已停止
	 * @return
	 */
	public boolean isStop();
}
