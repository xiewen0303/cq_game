package com.junyou.bus.chengjiu.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_chengjiu")
public class RoleChengjiu extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("chengjiu_id")
	private String chengjiuId;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;

	@Column("receive_id")
	private String receiveId;
	
	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getChengjiuId(){
		return chengjiuId;
	}

	public  void setChengjiuId(String chengjiuId){
		this.chengjiuId = chengjiuId;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleChengjiu copy(){
		RoleChengjiu result = new RoleChengjiu();
		result.setUserRoleId(getUserRoleId());
		result.setChengjiuId(getChengjiuId());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setReceiveId(getReceiveId());
		return result;
	}
}
