package com.junyou.bus.fuben.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("fuben")
public class Fuben extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("fuben_id")
	private Integer fubenId;

	@Column("type")
	private Integer type;

	@Column("update_time")
	private Long updateTime;

	@Column("count")
	private Integer count;


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

	public Integer getFubenId(){
		return fubenId;
	}

	public  void setFubenId(Integer fubenId){
		this.fubenId = fubenId;
	}

	public Integer getType(){
		return type;
	}

	public  void setType(Integer type){
		this.type = type;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public Fuben copy(){
		Fuben result = new Fuben();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setFubenId(getFubenId());
		result.setType(getType());
		result.setUpdateTime(getUpdateTime());
		result.setCount(getCount());
		return result;
	}
}
