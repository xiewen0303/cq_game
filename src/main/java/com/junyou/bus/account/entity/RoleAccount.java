package com.junyou.bus.account.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_account")
public class RoleAccount extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("re_yb")
	private Long reYb;

	@Column("no_re_yb")
	private Long noReYb;

	@Column("jb")
	private Long jb;

	@Column("bind_yb")
	private Long bindYb;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;

	@Column("user_type")
	private Integer userType;

	@Column("user_id")
	private String userId;

	@Column("server_id")
	private String serverId;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getReYb(){
		return reYb;
	}
	
	public Long getYb(){
		return reYb + noReYb;
	}

	public  void setReYb(Long reYb){
		this.reYb = reYb;
	}

	public Long getNoReYb(){
		return noReYb;
	}

	public  void setNoReYb(Long noReYb){
		this.noReYb = noReYb;
	}

	public Long getJb(){
		return jb;
	}

	public  void setJb(Long jb){
		this.jb = jb;
	}

	public Long getBindYb(){
		return bindYb;
	}

	public  void setBindYb(Long bindYb){
		this.bindYb = bindYb;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Integer getUserType(){
		return userType;
	}

	public  void setUserType(Integer userType){
		this.userType = userType;
	}

	public String getUserId(){
		return userId;
	}

	public  void setUserId(String userId){
		this.userId = userId;
	}

	public String getServerId(){
		return serverId;
	}

	public  void setServerId(String serverId){
		this.serverId = serverId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleAccount copy(){
		RoleAccount result = new RoleAccount();
		result.setUserRoleId(getUserRoleId());
		result.setReYb(getReYb());
		result.setNoReYb(getNoReYb());
		result.setJb(getJb());
		result.setBindYb(getBindYb());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setUserType(getUserType());
		result.setUserId(getUserId());
		result.setServerId(getServerId());
		return result;
	}
}
