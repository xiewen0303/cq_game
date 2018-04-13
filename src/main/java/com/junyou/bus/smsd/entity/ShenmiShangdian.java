package com.junyou.bus.smsd.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("shenmi_shangdian")
public class ShenmiShangdian extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_id")
	private Integer subId;

	@Column("shuaxi_time")
	private Long shuaxiTime;

	@Column("buy_id")
	private String buyId;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;


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

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Long getShuaxiTime(){
		return shuaxiTime;
	}

	public  void setShuaxiTime(Long shuaxiTime){
		this.shuaxiTime = shuaxiTime;
	}

	public String getBuyId(){
		return buyId;
	}

	public  void setBuyId(String buyId){
		this.buyId = buyId;
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
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public ShenmiShangdian copy(){
		ShenmiShangdian result = new ShenmiShangdian();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setShuaxiTime(getShuaxiTime());
		result.setBuyId(getBuyId());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
