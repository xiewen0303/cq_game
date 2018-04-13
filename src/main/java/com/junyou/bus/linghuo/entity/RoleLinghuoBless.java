package com.junyou.bus.linghuo.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_linghuo_bless")
public class RoleLinghuoBless extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("linghuo_id")
	private Integer linghuoId;

	@Column("linghuo_slot")
	private Integer linghuoSlot;

	@Column("bless_value")
	private Integer blessValue;

	@Column("create_time")
	private Long createTime;

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

	public Integer getLinghuoId(){
		return linghuoId;
	}

	public  void setLinghuoId(Integer linghuoId){
		this.linghuoId = linghuoId;
	}

	public Integer getLinghuoSlot(){
		return linghuoSlot;
	}

	public  void setLinghuoSlot(Integer linghuoSlot){
		this.linghuoSlot = linghuoSlot;
	}

	public Integer getBlessValue(){
		return blessValue;
	}

	public  void setBlessValue(Integer blessValue){
		this.blessValue = blessValue;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
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

	public RoleLinghuoBless copy(){
		RoleLinghuoBless result = new RoleLinghuoBless();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setLinghuoId(getLinghuoId());
		result.setLinghuoSlot(getLinghuoSlot());
		result.setBlessValue(getBlessValue());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
