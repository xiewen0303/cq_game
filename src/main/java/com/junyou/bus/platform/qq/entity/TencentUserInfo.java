package com.junyou.bus.platform.qq.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("tencent_user_info")
public class TencentUserInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("via")
	private String via;

	@Column("create_time")
	private Timestamp createTime;

	@Column("pf")
	private String pf;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getVia(){
		return via;
	}

	public  void setVia(String via){
		this.via = via;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public String getPf(){
		return pf;
	}

	public  void setPf(String pf){
		this.pf = pf;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public TencentUserInfo copy(){
		TencentUserInfo result = new TencentUserInfo();
		result.setUserRoleId(getUserRoleId());
		result.setVia(getVia());
		result.setCreateTime(getCreateTime());
		result.setPf(getPf());
		return result;
	}
}
