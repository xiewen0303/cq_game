package com.junyou.bus.kaifuactivity.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("zhanli_bipin")
public class ZhanliBipin extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("lingqu_status")
	private Integer lingquStatus;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;

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

	public Integer getLingquStatus(){
		return lingquStatus;
	}

	public  void setLingquStatus(Integer lingquStatus){
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

	public ZhanliBipin copy(){
		ZhanliBipin result = new ZhanliBipin();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setLingquStatus(getLingquStatus());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setSubId(getSubId());
		return result;
	}
}
