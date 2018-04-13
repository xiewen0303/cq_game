package com.junyou.bus.shoplimit.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("shop_limit_info")
public class ShopLimitInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("config_id")
	private Integer configId;

	@Column("recharge_total")
	private Long rechargeTotal;

	@Column("state")
	private Integer state;

	@Column("open_time")
	private Long openTime;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public Long getRechargeTotal() {
		return rechargeTotal;
	}

	public void setRechargeTotal(Long rechargeTotal) {
		this.rechargeTotal = rechargeTotal;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Long openTime) {
		this.openTime = openTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public ShopLimitInfo copy(){
		ShopLimitInfo result = new ShopLimitInfo();
		result.setUserRoleId(getUserRoleId());
		result.setRechargeTotal(getRechargeTotal());
		result.setConfigId(getConfigId());
		result.setOpenTime(getOpenTime());
		result.setState(getState());
		return result;
	}
}
