package com.junyou.bus.task.entity;

import com.junyou.bus.task.service.ITaskService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.kernel.data.dao.AbsVersion;

public abstract class AbsTask extends AbsVersion {
	
	private ITaskService taskService;
	
	public abstract String getTargetId();

	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}
	
	public boolean handle(BusMsgQueue busMsgQueue){
		return taskService.handle(this,busMsgQueue);
	}
	
}
