package com.junyou.bus.daytask.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 
import com.junyou.bus.daytask.entity.TaskDay;
import com.junyou.bus.daytask.service.TaskDayService;

/**
 * 日常任务
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-3-18 下午6:48:42
 */
@Service
public class TaskDayExportService {
	@Autowired
	private TaskDayService taskDayService;
	
	public List<TaskDay> initTask(Long userRoleId) {
		return taskDayService.initAll(userRoleId);
	}
	/**
	 * 删除公会日常监听
	 * @param userRoleId
	 */
	public void removeGuildDayTask(long userRoleId){
		taskDayService.removeGuildDayTask(userRoleId);
	}
	
	/**
	 * 添加公会日常监听
	 * @param userRoleId
	 */
	public void addGuildDayTask(long userRoleId){
		taskDayService.addGuildDayTask(userRoleId);
	} 
	
	public void onlineHandle(Long userRoleId) {
		taskDayService.onlineHandle(userRoleId);
	}
	
	public TaskDay getTaskDay(long userRoleId,int type){
		return taskDayService.getTaskDay(userRoleId, type);
	}
//	public void offlineHandle(Long userRoleId){
//	taskDayService.offlineHandle(userRoleId);
//}
	/**
	 * 计算日常任务\公会日常资源情况
	 */
	public void calDayFubenResource(Map<String,Map<String,Integer>> dayTaskMap,Map<String,Map<String,Integer>> guildTaskMap,Long userRoleId){
		taskDayService.calDayFubenResource(dayTaskMap, guildTaskMap, userRoleId);
	}
}
