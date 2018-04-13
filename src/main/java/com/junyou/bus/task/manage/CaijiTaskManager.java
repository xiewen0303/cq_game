package com.junyou.bus.task.manage;

/**
 * 采集任务管理器
 * @author LiuYu
 * @date 2014-12-24 上午10:06:05
 */
public class CaijiTaskManager extends TaskManager{
	private CaijiTaskManager(){};
	private static CaijiTaskManager manager = new CaijiTaskManager();
	public static CaijiTaskManager getManager(){return manager;};
}
