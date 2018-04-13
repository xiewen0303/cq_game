package com.junyou.bus.onlinerewards.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_online_rewards")
public class RoleOnlineRewards extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("start_time")
	private Long startTime;

	@Column("today_online_time")
	private Long todayOnlineTime;

	@Column("update_time")
	private Long updateTime;

	@Column("sub_id")
	private Integer subId;

	@Column("state")
	private Integer state;

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

	public Long getStartTime(){
		return startTime;
	}

	public  void setStartTime(Long startTime){
		this.startTime = startTime;
	}

	public Long getTodayOnlineTime(){
		return todayOnlineTime;
	}

	public  void setTodayOnlineTime(Long todayOnlineTime){
		this.todayOnlineTime = todayOnlineTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
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

	public RoleOnlineRewards copy(){
		RoleOnlineRewards result = new RoleOnlineRewards();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setStartTime(getStartTime());
		result.setTodayOnlineTime(getTodayOnlineTime());
		result.setUpdateTime(getUpdateTime());
		result.setSubId(getSubId());
		result.setState(getState());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
