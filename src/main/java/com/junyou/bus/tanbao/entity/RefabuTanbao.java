package com.junyou.bus.tanbao.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refabu_tanbao")
public class RefabuTanbao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("status")
	private Integer status;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;

	@Column("wangcheng_count")
	private Integer wangchengCount;

	@Column("sub_id")
	private Integer subId;


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

	public Integer getStatus(){
		return status;
	}

	public  void setStatus(Integer status){
		this.status = status;
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

	public Integer getWangchengCount(){
		return wangchengCount;
	}

	public  void setWangchengCount(Integer wangchengCount){
		this.wangchengCount = wangchengCount;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefabuTanbao copy(){
		RefabuTanbao result = new RefabuTanbao();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setStatus(getStatus());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setWangchengCount(getWangchengCount());
		result.setSubId(getSubId());
		return result;
	}
}
