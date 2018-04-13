package com.kernel.memory;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import com.junyou.log.ChuanQiLog;


/**
 * 内存监控(复制的其它项目组的)
 * @author DaoZheng Yuan
 * 2014年11月19日 下午2:04:50
 */
public class MemoryManager {
	
	private static MemoryManager instance;

	public static MemoryManager getInstance() {
		if (null == instance) {
			instance = new MemoryManager();
		}
		return instance;
	}

	/** 60s后启动 */
	private final static int TIME_DELAY = 60000;
	/** 每120s统计,太短的设置可能会导致玩家登陆不了 */
	private final static int TIME_INTERVAL = 120000;

	public void initialize() {
		this.init(TIME_DELAY, TIME_INTERVAL);
	}

	public void init(int delayTime, int intervalTime) {
		final Timer timer = new Timer("Memory Monitor", true);
		timer.schedule(new StatTimerTask(), delayTime, intervalTime);
		
		ChuanQiLog.memInfo("内存监控线程将在"+(delayTime / 1000)+"秒后启动，采样间隔为interval="+intervalTime / 1000+" 秒");
		
		ChuanQiLog.deployInfo("================内存监控线程将在"+(delayTime / 1000)+"秒后启动，采样间隔为interval="+intervalTime / 1000+" 秒");
	}

	static class StatTimerTask extends TimerTask {
		@Override
		public void run() {
			try {
				Class<?> c = Class.forName("java.nio.Bits");
				Field maxMemory = c.getDeclaredField("maxMemory");
				maxMemory.setAccessible(true);
				Field reserverdMemory = c.getDeclaredField("reservedMemory");
				reserverdMemory.setAccessible(true);
				Long maxMemoryValue = (Long) maxMemory.get(null);
				float reserverdMemoryValue = (Long) reserverdMemory.get(null);
				
				long totalHeadMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
				long totalHeaduseMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
				long directMaxMem = maxMemoryValue / (1024 * 1024);
				float directUseMem = reserverdMemoryValue / (1024 * 1024);
				
				ChuanQiLog.memInfo("服务器堆内存总共 "+totalHeadMem+" M,占用堆内存 "+totalHeaduseMem+" M,直接内存总共 "+directMaxMem+" M,占用直接内存 "+directUseMem+" M");
			} catch (Exception e) {
				ChuanQiLog.memInfo(e.getMessage(), e);
			}
		}
	}
}
