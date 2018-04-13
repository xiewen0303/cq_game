package com.junyou.bus.platform.qq.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("tencent_weiduan")
public class TencentWeiduan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("weiduan_status")
	private Integer weiduanStatus;

	@Column("tgp_status")
	private Integer tgpStatus;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("dtgp_status")
	private Integer dtgpStatus;

	@Column("tgp_level_status")
	private Integer tgpLevelStatus;
	
	@Column("baozi_status")
	private Integer baoziStatus;


	public Integer getBaoziStatus() {
		return baoziStatus;
	}

	public void setBaoziStatus(Integer baoziStatus) {
		this.baoziStatus = baoziStatus;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getWeiduanStatus(){
		return weiduanStatus;
	}

	public  void setWeiduanStatus(Integer weiduanStatus){
		this.weiduanStatus = weiduanStatus;
	}

	public Integer getTgpStatus(){
		return tgpStatus;
	}

	public  void setTgpStatus(Integer tgpStatus){
		this.tgpStatus = tgpStatus;
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

	public Integer getDtgpStatus(){
		return dtgpStatus;
	}

	public  void setDtgpStatus(Integer dtgpStatus){
		this.dtgpStatus = dtgpStatus;
	}

	public Integer getTgpLevelStatus(){
		return tgpLevelStatus;
	}

	public  void setTgpLevelStatus(Integer tgpLevelStatus){
		this.tgpLevelStatus = tgpLevelStatus;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public TencentWeiduan copy(){
		TencentWeiduan result = new TencentWeiduan();
		result.setUserRoleId(getUserRoleId());
		result.setWeiduanStatus(getWeiduanStatus());
		result.setTgpStatus(getTgpStatus());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setDtgpStatus(getDtgpStatus());
		result.setTgpLevelStatus(getTgpLevelStatus());
		result.setBaoziStatus(getBaoziStatus());
		return result;
	}
}
