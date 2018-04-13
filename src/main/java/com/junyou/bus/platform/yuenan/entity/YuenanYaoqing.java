package com.junyou.bus.platform.yuenan.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("yuenan_yaoqing")
public class YuenanYaoqing extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("haoyou_count")
	private Integer haoyouCount;

	@Column("lingqu_count")
	private Integer lingquCount;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("yaoqing_id")
	private String yaoqingId;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getHaoyouCount(){
		return haoyouCount;
	}

	public  void setHaoyouCount(Integer haoyouCount){
		this.haoyouCount = haoyouCount;
	}

	public Integer getLingquCount(){
		return lingquCount;
	}

	public  void setLingquCount(Integer lingquCount){
		this.lingquCount = lingquCount;
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

	public String getYaoqingId(){
		return yaoqingId;
	}

	public  void setYaoqingId(String yaoqingId){
		this.yaoqingId = yaoqingId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public YuenanYaoqing copy(){
		YuenanYaoqing result = new YuenanYaoqing();
		result.setUserRoleId(getUserRoleId());
		result.setHaoyouCount(getHaoyouCount());
		result.setLingquCount(getLingquCount());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setYaoqingId(getYaoqingId());
		return result;
	}
}
