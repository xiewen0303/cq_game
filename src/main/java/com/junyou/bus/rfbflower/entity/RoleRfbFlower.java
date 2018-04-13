package com.junyou.bus.rfbflower.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_rfb_flower")
public class RoleRfbFlower extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("charm_value")
	private Integer charmValue;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;

	@Column("sub_id")
	private Integer subId;


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

	public Integer getCharmValue(){
		return charmValue;
	}

	public  void setCharmValue(Integer charmValue){
		this.charmValue = charmValue;
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

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleRfbFlower copy(){
		RoleRfbFlower result = new RoleRfbFlower();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setCharmValue(getCharmValue());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setSubId(getSubId());
		return result;
	}
}
