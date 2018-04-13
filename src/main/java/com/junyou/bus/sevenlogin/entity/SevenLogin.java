package com.junyou.bus.sevenlogin.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("seven_login")
public class SevenLogin extends AbsVersion implements Serializable,IEntity {

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

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public SevenLogin copy(){
		SevenLogin result = new SevenLogin();
		result.setUserRoleId(getUserRoleId());
		result.setLoginDays(getLoginDays());
		result.setRewardDays(getRewardDays());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
