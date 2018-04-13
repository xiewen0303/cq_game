package com.junyou.bus.hefuSevenlogin.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("hefu_seven_login")
public class HefuSevenLogin extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("login_days")
	private Integer loginDays;

	@Column("reward_days")
	private Integer rewardDays;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getLoginDays(){
		return loginDays;
	}

	public  void setLoginDays(Integer loginDays){
		this.loginDays = loginDays;
	}

	public Integer getRewardDays(){
		return rewardDays;
	}

	public  void setRewardDays(Integer rewardDays){
		this.rewardDays = rewardDays;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
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
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public HefuSevenLogin copy(){
		HefuSevenLogin result = new HefuSevenLogin();
		result.setUserRoleId(getUserRoleId());
		result.setLoginDays(getLoginDays());
		result.setRewardDays(getRewardDays());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
