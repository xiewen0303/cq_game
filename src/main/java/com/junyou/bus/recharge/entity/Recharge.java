package com.junyou.bus.recharge.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Primary;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;


@Table("recharge")
public class Recharge extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Primary
	@Column("id")
	private Long id;

	@Column("user_id")
	private String userId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("server_id")
	private String serverId;

	@Column("rmb")
	private Double rmb;

	@Column("yb")
	private Long yb;

	@Column("is_calc")
	private Integer isCalc;

	@Column("re_type")
	private Integer reType;

	@Column("platform_type")
	private String platformType;

	@Column("re_state")
	private Integer reState;

	@Column("order_id")
	private String orderId;

	@Column("create_time")
	private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public double getRmb() {
		return rmb;
	}

	public void setRmb(double rmb) {
		this.rmb = rmb;
	}
	public long getYb() {
		return yb;
	}

	public void setYb(long yb) {
		this.yb = yb;
	}
	public int getIsCalc() {
		return isCalc;
	}

	public void setIsCalc(int isCalc) {
		this.isCalc = isCalc;
	}
	public int getReType() {
		return reType;
	}

	public void setReType(int reType) {
		this.reType = reType;
	}
	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public int getReState() {
		return reState;
	}

	public void setReState(int reState) {
		this.reState = reState;
	}
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override	
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId();
	}
	
	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Recharge copy(){
		Recharge result = new Recharge();
		result.setId(getId());
		result.setUserId(getUserId());
		result.setServerId(getServerId());
		result.setRmb(getRmb());
		result.setYb(getYb());
		result.setIsCalc(getIsCalc());
		result.setReType(getReType());
		result.setPlatformType(getPlatformType());
		result.setReState(getReState());
		result.setOrderId(getOrderId());
		result.setCreateTime(getCreateTime());
		result.setUserRoleId(getUserRoleId());
		return result;
	}
}
