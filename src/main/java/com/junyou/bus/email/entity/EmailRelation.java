package com.junyou.bus.email.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("email_relation")
public class EmailRelation extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("email_id")
	private Long emailId;

	@Column("is_delete")
	private Integer isDelete;
	
	@Column("status")
	private Integer status;


	
	
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

	public Long getEmailId(){
		return emailId;
	}

	public  void setEmailId(Long emailId){
		this.emailId = emailId;
	}

	public Integer getIsDelete(){
		return isDelete;
	}

	public  void setIsDelete(Integer isDelete){
		this.isDelete = isDelete;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public EmailRelation copy(){
		EmailRelation result = new EmailRelation();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setEmailId(getEmailId());
		result.setIsDelete(getIsDelete());
		result.setStatus(getStatus());
		return result;
	}
}
