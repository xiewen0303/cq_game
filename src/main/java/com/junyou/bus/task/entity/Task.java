package com.junyou.bus.task.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.IEntity;

@Table("task")
public class Task extends AbsTask implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("task_id")
	private Integer taskId;

	@Column("progress")
	private Integer progress;

	@Column("state")
	private Integer state;

	@Column("update_time")
	private Long updateTime;
	
	@EntityField
	private Integer needCount;
	
	@EntityField
	private String targetId;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getTaskId(){
		return taskId;
	}

	public  void setTaskId(Integer taskId){
		this.taskId = taskId;
	}

	public Integer getProgress(){
		return progress;
	}

	public  void setProgress(Integer progress){
		this.progress = progress;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Task copy(){
		Task result = new Task();
		result.setUserRoleId(getUserRoleId());
		result.setTaskId(getTaskId());
		result.setProgress(getProgress());
		result.setState(getState());
		result.setUpdateTime(getUpdateTime());
		return result;
	}

	@Override
	public String getTargetId() {
		return targetId;
	}

	public Integer getNeedCount() {
		return needCount;
	}

	public void setNeedCount(Integer needCount) {
		this.needCount = needCount;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
}
