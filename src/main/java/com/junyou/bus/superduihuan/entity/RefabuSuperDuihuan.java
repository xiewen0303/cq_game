package com.junyou.bus.superduihuan.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refabu_super_duihuan")
public class RefabuSuperDuihuan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("sub_id")
	private Integer subId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("config_id")
	private Integer configId;

	@Column("times_count")
	private Integer timesCount;

	@Column("update_time")
	private Long updateTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Integer getTimesCount(){
		return timesCount;
	}

	public  void setTimesCount(Integer timesCount){
		this.timesCount = timesCount;
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

	public RefabuSuperDuihuan copy(){
		RefabuSuperDuihuan result = new RefabuSuperDuihuan();
		result.setId(getId());
		result.setSubId(getSubId());
		result.setUserRoleId(getUserRoleId());
		result.setConfigId(getConfigId());
		result.setTimesCount(getTimesCount());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
