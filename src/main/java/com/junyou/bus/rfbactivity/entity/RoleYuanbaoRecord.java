package com.junyou.bus.rfbactivity.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_yuanbao_record")
public class RoleYuanbaoRecord extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("cz_value")
	private Integer czValue;

	@Column("xf_value")
	private Integer xfValue;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getCzValue(){
		return czValue;
	}

	public  void setCzValue(Integer czValue){
		this.czValue = czValue;
	}

	public Integer getXfValue(){
		return xfValue;
	}

	public  void setXfValue(Integer xfValue){
		this.xfValue = xfValue;
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
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleYuanbaoRecord copy(){
		RoleYuanbaoRecord result = new RoleYuanbaoRecord();
		result.setUserRoleId(getUserRoleId());
		result.setCzValue(getCzValue());
		result.setXfValue(getXfValue());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
