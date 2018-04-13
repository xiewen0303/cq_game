package com.junyou.bus.hongbao.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_hongbao")
public class RoleHongbao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("send_role_id")
	private Long sendRoleId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("state")
	private Integer state;

	@Column("create_time")
	private Timestamp createTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getSendRoleId(){
		return sendRoleId;
	}

	public  void setSendRoleId(Long sendRoleId){
		this.sendRoleId = sendRoleId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
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

	public RoleHongbao copy(){
		RoleHongbao result = new RoleHongbao();
		result.setId(getId());
		result.setSendRoleId(getSendRoleId());
		result.setUserRoleId(getUserRoleId());
		result.setState(getState());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
