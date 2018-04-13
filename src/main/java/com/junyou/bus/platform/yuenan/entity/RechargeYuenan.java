package com.junyou.bus.platform.yuenan.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("recharge_yuenan")
public class RechargeYuenan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_id")
	private String userId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("rmb")
	private Double rmb;

	@Column("yb")
	private Long yb;

	@Column("platform_type")
	private String platformType;

	@Column("server_id")
	private String serverId;

	@Column("re_state")
	private Integer reState;

	@Column("order_id")
	private String orderId;

	@Column("create_time")
	private Timestamp createTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public String getUserId(){
		return userId;
	}

	public  void setUserId(String userId){
		this.userId = userId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Double getRmb(){
		return rmb;
	}

	public  void setRmb(Double rmb){
		this.rmb = rmb;
	}

	public Long getYb(){
		return yb;
	}

	public  void setYb(Long yb){
		this.yb = yb;
	}

	public String getPlatformType(){
		return platformType;
	}

	public  void setPlatformType(String platformType){
		this.platformType = platformType;
	}

	public String getServerId(){
		return serverId;
	}

	public  void setServerId(String serverId){
		this.serverId = serverId;
	}

	public Integer getReState(){
		return reState;
	}

	public  void setReState(Integer reState){
		this.reState = reState;
	}

	public String getOrderId(){
		return orderId;
	}

	public  void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RechargeYuenan copy(){
		RechargeYuenan result = new RechargeYuenan();
		result.setId(getId());
		result.setUserId(getUserId());
		result.setUserRoleId(getUserRoleId());
		result.setRmb(getRmb());
		result.setYb(getYb());
		result.setPlatformType(getPlatformType());
		result.setServerId(getServerId());
		result.setReState(getReState());
		result.setOrderId(getOrderId());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
