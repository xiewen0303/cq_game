package com.junyou.bus.xianqi.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xianqi_juexing")
public class RoleXianqiJuexing extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("xianqi_type")
	private Integer xianqiType;

	@Column("juexing_lvl")
	private Integer juexingLvl;

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

	public Integer getXianqiType(){
		return xianqiType;
	}

	public  void setXianqiType(Integer xianqiType){
		this.xianqiType = xianqiType;
	}

	public Integer getJuexingLvl(){
		return juexingLvl;
	}

	public  void setJuexingLvl(Integer juexingLvl){
		this.juexingLvl = juexingLvl;
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

	public RoleXianqiJuexing copy(){
		RoleXianqiJuexing result = new RoleXianqiJuexing();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setXianqiType(getXianqiType());
		result.setJuexingLvl(getJuexingLvl());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
