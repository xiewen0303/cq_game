package com.junyou.bus.maigu.entity;
import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_maigu")
public class RoleMaigu extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("fuben_id")
	private Integer fubenId;

	@Column("times")
	private Integer times;

	@Column("status")
	private Integer status;

	@Column("update_time")
	private Long updateTime;


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

	public Integer getFubenId(){
		return fubenId;
	}

	public  void setFubenId(Integer fubenId){
		this.fubenId = fubenId;
	}

	public Integer getTimes(){
		return times;
	}

	public  void setTimes(Integer times){
		this.times = times;
	}

	public Integer getStatus(){
		return status;
	}

	public  void setStatus(Integer status){
		this.status = status;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleMaigu copy(){
		RoleMaigu result = new RoleMaigu();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setFubenId(getFubenId());
		result.setTimes(getTimes());
		result.setStatus(getStatus());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
