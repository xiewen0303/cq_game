package com.junyou.bus.branchtask.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("task_branch")
public class TaskBranch extends  AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;
	
	@Column("id")
	private Long id; //主键ID
	
	@Column("user_role_id")
	private Long userRoleId;

	@Column("task_id")
	private Integer taskId;

	@Column("progress")
	private Integer progress;

	@Column("update_time")
	private Long updateTime;

	@Column("status")
	private Integer status;//任务完成状态  0 激活， 1已完成  2已领奖
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
 

	public TaskBranch copy(){
		TaskBranch result = new TaskBranch();
		result.setId(getId());
		result.setProgress(getProgress());
		result.setStatus(getStatus());
		result.setTaskId(getTaskId());
		result.setUpdateTime(getUpdateTime());
		result.setUserRoleId(getUserRoleId());
		return result;
	}
	

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 0:激活，1：已完成  2:已领取
	 * @return
	 */
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override	
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId();
	}

}
