package com.junyou.bus.huajuan.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_huajuan")
public class RoleHuajuan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("huanjuan_id")
	private Integer huanjuanId;

	@Column("is_up")
	private Integer isUp;

	@Column("create_time")
	private Long createTime;

	@Column("level_id")
	private Integer levelId;

	@Column("exp")
	private Integer exp;

	@Column("sj_update_time")
	private Timestamp sjUpdateTime;


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

	public Integer getHuanjuanId(){
		return huanjuanId;
	}

	public  void setHuanjuanId(Integer huanjuanId){
		this.huanjuanId = huanjuanId;
	}

	public Integer getIsUp(){
		return isUp;
	}

	public  void setIsUp(Integer isUp){
		this.isUp = isUp;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Integer getLevelId(){
		return levelId;
	}

	public  void setLevelId(Integer levelId){
		this.levelId = levelId;
	}

	public Integer getExp(){
		return exp;
	}

	public  void setExp(Integer exp){
		this.exp = exp;
	}

	public Timestamp getSjUpdateTime(){
		return sjUpdateTime;
	}

	public  void setSjUpdateTime(Timestamp sjUpdateTime){
		this.sjUpdateTime = sjUpdateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleHuajuan copy(){
		RoleHuajuan result = new RoleHuajuan();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setHuanjuanId(getHuanjuanId());
		result.setIsUp(getIsUp());
		result.setCreateTime(getCreateTime());
		result.setLevelId(getLevelId());
		result.setExp(getExp());
		result.setSjUpdateTime(getSjUpdateTime());
		return result;
	}
}
