package com.junyou.bus.laowanjia.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refb_laowanjia")
public class RefbLaowanjia extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_id")
	private Integer subId;

	@Column("login_day")
	private Integer loginDay;

	@Column("recharge_val")
	private Integer rechargeVal;

	@Column("lingqu_status")
	private String lingquStatus;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;


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

	public Integer getLoginDay(){
		return loginDay;
	}

	public  void setLoginDay(Integer loginDay){
		this.loginDay = loginDay;
	}

	public Integer getRechargeVal(){
		return rechargeVal;
	}

	public  void setRechargeVal(Integer rechargeVal){
		this.rechargeVal = rechargeVal;
	}

	public String getLingquStatus(){
		return lingquStatus;
	}

	public  void setLingquStatus(String lingquStatus){
		this.lingquStatus = lingquStatus;
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

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefbLaowanjia copy(){
		RefbLaowanjia result = new RefbLaowanjia();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setLoginDay(getLoginDay());
		result.setRechargeVal(getRechargeVal());
		result.setLingquStatus(getLingquStatus());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
