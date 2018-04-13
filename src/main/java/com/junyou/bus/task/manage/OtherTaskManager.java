package com.junyou.bus.task.manage;

/**
 * 完成其他任务的任务管理器
 * @author LiuYu
 * @date 2014-12-24 上午10:06:05
 */
public class OtherTaskManager extends TaskManager{
	private OtherTaskManager(){};
	private static OtherTaskManager manager = new OtherTaskManager();
	public static OtherTaskManager getManager(){return manager;};
	
}
