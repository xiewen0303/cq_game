package com.junyou.bus.task.manage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.bus.task.entity.AbsTask;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.log.ChuanQiLog;

public class TaskManager {
	private ConcurrentMap<Long, List<AbsTask>> roleTasks = new ConcurrentHashMap<>();
	
    public void addTask(Long roleId, AbsTask absTask) {
        List<AbsTask> list = roleTasks.get(roleId);
        if (list == null) {
            list = new ArrayList<>();
            roleTasks.put(roleId, list);
        }
        synchronized (list) {
            list.add(absTask);
        }
    }
	
	public void handle(Long roleId,String targetId,BusMsgQueue busMsgQueue){
        try {
            List<AbsTask> list = roleTasks.get(roleId);
            if (list != null && list.size() > 0) {
                synchronized (list) {
                    Iterator<AbsTask> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        AbsTask task = iterator.next();
                        if (targetId.equals(task.getTargetId())) {
                            if (task.handle(busMsgQueue)) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ChuanQiLog.error("", e);
        }
	}
	
    public void cancelTask(Long roleId, AbsTask task) {
        List<AbsTask> list = roleTasks.get(roleId);
        if (list != null && list.size() > 0) {
            synchronized (list) {
                list.remove(task);
            }
        }
    }
	
	public void offline(Long roleId){
		roleTasks.remove(roleId);
	}
}
