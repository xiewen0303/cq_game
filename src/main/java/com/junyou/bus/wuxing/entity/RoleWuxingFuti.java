package com.junyou.bus.wuxing.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_wuxing_futi")
public class RoleWuxingFuti extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("wuxing_id")
	private Integer wuxingId;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getWuxingId(){
		return wuxingId;
	}

	public  void setWuxingId(Integer wuxingId){
		this.wuxingId = wuxingId;
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

	public RoleWuxingFuti copy(){
		RoleWuxingFuti result = new RoleWuxingFuti();
		result.setUserRoleId(getUserRoleId());
		result.setWuxingId(getWuxingId());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
