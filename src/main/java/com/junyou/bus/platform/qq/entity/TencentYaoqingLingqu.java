package com.junyou.bus.platform.qq.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("tencent_yaoqing_lingqu")
public class TencentYaoqingLingqu extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("lingqu_id")
	private String lingquId;

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

	public String getLingquId(){
		return lingquId;
	}

	public  void setLingquId(String lingquId){
		this.lingquId = lingquId;
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

	public TencentYaoqingLingqu copy(){
		TencentYaoqingLingqu result = new TencentYaoqingLingqu();
		result.setUserRoleId(getUserRoleId());
		result.setLingquId(getLingquId());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
