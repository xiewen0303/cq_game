package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 完成主线任务日志
 * @author LiuYu
 * @date 2014-12-29 下午6:15:25
 */
public class FinishTaskLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	
	private long userRoleId;
	private String roleName;
	private int taskId;
	private long onlineTime;
	
	public FinishTaskLogEvent(long userRoleId,String roleName,int taskId, long onlineTime){
		super(LogPrintHandle.ZHUXIAN_TASK);
		
		if(roleName == null) roleName = LogPrintHandle.LINE_CHAR;
		
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.taskId = taskId;
		this.onlineTime = onlineTime;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public int getTaskId() {
		return taskId;
	}

	public long getOnlineTime() {
		return onlineTime;
	}
	
}