package com.junyou.bus.xiaofei.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refabu_xiaofei")
public class RefabuXiaofei extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("xf_gold")
	private Integer xfGold;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;

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

	public Integer getXfGold(){
		return xfGold;
	}

	public  void setXfGold(Integer xfGold){
		this.xfGold = xfGold;
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

	public RefabuXiaofei copy(){
		RefabuXiaofei result = new RefabuXiaofei();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setXfGold(getXfGold());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setSubId(getSubId());
		return result;
	}
}
