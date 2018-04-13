package com.junyou.bus.platform.qq.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("renwujishi")
public class Renwujishi extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("userRoleId")
	private Long userroleid;

	@Column("lingqu_status")
	private String lingquStatus;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;


	public Long getUserroleid(){
		return userroleid;
	}

	public  void setUserroleid(Long userroleid){
		this.userroleid = userroleid;
	}

	public String getLingquStatus(){
		return lingquStatus;
	}

	public  void setLingquStatus(String lingquStatus){
		this.lingquStatus = lingquStatus;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userroleid";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userroleid;
	}

	public Renwujishi copy(){
		Renwujishi result = new Renwujishi();
		result.setUserroleid(getUserroleid());
		result.setLingquStatus(getLingquStatus());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
