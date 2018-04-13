package com.junyou.bus.task.manage;

/**
 * 杀怪任务管理器
 * @author LiuYu
 * @date 2014-12-24 上午10:06:05
 */
public class KillTaskManager extends TaskManager{
	private KillTaskManager(){};
	private static KillTaskManager manager = new KillTaskManager();
	public static KillTaskManager getManager(){return manager;};
}
