package com.junyou.bus.task;

import com.junyou.bus.task.entity.Task;

/**
 * 主线任务输出
 * @description 
 * @author Hanchun
 * @email han88316250@163.com
 * @date 2015-1-20 下午8:08:46
 */
public class TaskOutputWrapper {
	
	public static Object[] getTaskRunState(Task task){
		if(task == null){
			return null;
		}
		/**
		 *  taskid
			 state 任务状态 0:未完成,1:已完成未交付,2:已交付   
			 count
		 */
		int count = task.getProgress() == null ? 0 : task.getProgress();
		return new Object[]{task.getTaskId(),task.getState(),count};
	}
	
	public static Object[] receiveTaskSuccess(Integer taskId){
		return new Object[]{1,taskId};
	}
	
}
