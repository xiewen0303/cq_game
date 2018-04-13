package com.junyou.bus.chongwu.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_chongwu")
public class RoleChongwu extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("config_id")
	private Integer configId;

	@Column("level")
	private Integer level;

	@Column("level_exp")
	private Long levelExp;

	@Column("jie")
	private Integer jie;

	@Column("ceng")
	private Integer ceng;

	@Column("jie_exp")
	private Long jieExp;

	@Column("status")
	private Integer status;

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

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Integer getLevel(){
		return level;
	}

	public  void setLevel(Integer level){
		this.level = level;
	}

	public Long getLevelExp(){
		return levelExp;
	}

	public  void setLevelExp(Long levelExp){
		this.levelExp = levelExp;
	}

	public Integer getJie(){
		return jie;
	}

	public  void setJie(Integer jie){
		this.jie = jie;
	}

	public Integer getCeng(){
		return ceng;
	}

	public  void setCeng(Integer ceng){
		this.ceng = ceng;
	}

	public Long getJieExp(){
		return jieExp;
	}

	public  void setJieExp(Long jieExp){
		this.jieExp = jieExp;
	}

	public Integer getStatus(){
		return status;
	}

	public  void setStatus(Integer status){
		this.status = status;
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

	public RoleChongwu copy(){
		RoleChongwu result = new RoleChongwu();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setConfigId(getConfigId());
		result.setLevel(getLevel());
		result.setLevelExp(getLevelExp());
		result.setJie(getJie());
		result.setCeng(getCeng());
		result.setJieExp(getJieExp());
		result.setStatus(getStatus());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
	
}
