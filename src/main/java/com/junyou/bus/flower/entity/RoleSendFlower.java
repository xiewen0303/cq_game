package com.junyou.bus.flower.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_send_flower")
public class RoleSendFlower extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("charm_value")
	private Long charmValue;

	@Column("upate_time")
	private Long upateTime;

	@Column("create_time")
	private Timestamp createTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getCharmValue(){
		return charmValue;
	}

	public  void setCharmValue(Long charmValue){
		this.charmValue = charmValue;
	}

	public Long getUpateTime(){
		return upateTime;
	}

	public  void setUpateTime(Long upateTime){
		this.upateTime = upateTime;
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

	public RoleSendFlower copy(){
		RoleSendFlower result = new RoleSendFlower();
		result.setUserRoleId(getUserRoleId());
		result.setCharmValue(getCharmValue());
		result.setUpateTime(getUpateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
