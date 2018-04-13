package com.junyou.bus.fuben.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("more_fuben")
public class MoreFuben extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("more_fuben_id")
	private Integer moreFubenId;

	@Column("update_time")
	private Long updateTime;

	@Column("count")
	private Integer count;

	@Column("status")
	private Integer status;


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

	public Integer getMoreFubenId(){
		return moreFubenId;
	}

	public  void setMoreFubenId(Integer moreFubenId){
		this.moreFubenId = moreFubenId;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Integer getCount(){
		return count;
	}

	public  void setCount(Integer count){
		this.count = count;
	}

	public Integer getStatus(){
		return status;
	}

	public  void setStatus(Integer status){
		this.status = status;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public MoreFuben copy(){
		MoreFuben result = new MoreFuben();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setMoreFubenId(getMoreFubenId());
		result.setUpdateTime(getUpdateTime());
		result.setCount(getCount());
		result.setStatus(getStatus());
		return result;
	}
}
