package com.junyou.bus.wuxing.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_wuxing")
public class RoleWuxing extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("wuxing_type")
	private Integer wuxingType;

	@Column("wuxing_level")
	private Integer wuxingLevel;

	@Column("zfz_val")
	private Integer zfzVal;

	@Column("last_sj_time")
	private Long lastSjTime;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;


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

	public Integer getWuxingType(){
		return wuxingType;
	}

	public  void setWuxingType(Integer wuxingType){
		this.wuxingType = wuxingType;
	}

	public Integer getWuxingLevel(){
		return wuxingLevel;
	}

	public  void setWuxingLevel(Integer wuxingLevel){
		this.wuxingLevel = wuxingLevel;
	}

	public Integer getZfzVal(){
		return zfzVal;
	}

	public  void setZfzVal(Integer zfzVal){
		this.zfzVal = zfzVal;
	}

	public Long getLastSjTime(){
		return lastSjTime;
	}

	public  void setLastSjTime(Long lastSjTime){
		this.lastSjTime = lastSjTime;
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
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleWuxing copy(){
		RoleWuxing result = new RoleWuxing();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setWuxingType(getWuxingType());
		result.setWuxingLevel(getWuxingLevel());
		result.setZfzVal(getZfzVal());
		result.setLastSjTime(getLastSjTime());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
