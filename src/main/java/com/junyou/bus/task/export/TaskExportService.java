package com.junyou.bus.task.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.task.entity.Task;
import com.junyou.bus.task.service.TaskService;

@Service
public class TaskExportService {
	@Autowired
	private TaskService taskService;
	
	public List<Task> initTask(Long userRoleId) {
		return taskService.initTask(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		taskService.onlineHandle(userRoleId);
	}
	
	public void offlineHandle(Long userRoleId){
		taskService.offlineHandle(userRoleId);
	}
	
	public Object[] getTask(Long userRoleId){
		return taskService.getTask(userRoleId);
	}
	/**
	 * gm功能跳任务
	 * @param userRoleId
	 * @param taskId
	 */
	public void gmChangeTask(Long userRoleId,Integer taskId){
		taskService.gmChangeTask(userRoleId, taskId);
	}
}
