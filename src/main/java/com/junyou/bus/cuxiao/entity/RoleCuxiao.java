package com.junyou.bus.cuxiao.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_cuxiao")
public class RoleCuxiao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("task_id")
	private Integer taskId;

	@Column("state")
	private Integer state;

	@Column("config_id")
	private Integer configId;

	@Column("create_time")
	private Timestamp createTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

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

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
	}

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleCuxiao copy(){
		RoleCuxiao result = new RoleCuxiao();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setTaskId(getTaskId());
		result.setState(getState());
		result.setConfigId(getConfigId());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
