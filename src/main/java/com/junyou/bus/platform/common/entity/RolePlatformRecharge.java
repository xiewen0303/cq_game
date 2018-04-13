package com.junyou.bus.platform.common.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_platform_recharge")
public class RolePlatformRecharge extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("recharge_month")
	private Long rechargeMonth;

	@Column("recharge_once_max")
	private Long rechargeOnceMax;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
		this.updateTime = updateTime;
	}

	public Long getRechargeMonth(){
		return rechargeMonth;
	}

	public  void setRechargeMonth(Long rechargeMonth){
		this.rechargeMonth = rechargeMonth;
	}

	public Long getRechargeOnceMax(){
		return rechargeOnceMax;
	}

	public  void setRechargeOnceMax(Long rechargeOnceMax){
		this.rechargeOnceMax = rechargeOnceMax;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RolePlatformRecharge copy(){
		RolePlatformRecharge result = new RolePlatformRecharge();
		result.setUserRoleId(getUserRoleId());
		result.setUpdateTime(getUpdateTime());
		result.setRechargeMonth(getRechargeMonth());
		result.setRechargeOnceMax(getRechargeOnceMax());
		return result;
	}
}
