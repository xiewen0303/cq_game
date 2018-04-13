package com.junyou.bus.task.service;

import com.junyou.bus.task.entity.AbsTask;
import com.junyou.bus.tunnel.BusMsgQueue;

public interface ITaskService {
	public boolean handle(AbsTask absTask,BusMsgQueue busMsgQueue);
}
