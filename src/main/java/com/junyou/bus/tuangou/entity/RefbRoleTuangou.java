package com.junyou.bus.tuangou.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refb_role_tuangou")
public class RefbRoleTuangou extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("dian_shu")
	private Integer dianShu;

	@Column("create_time")
	private Timestamp createTime;

	@Column("sub_id")
	private Integer subId;


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

	public Integer getDianShu(){
		return dianShu;
	}

	public  void setDianShu(Integer dianShu){
		this.dianShu = dianShu;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefbRoleTuangou copy(){
		RefbRoleTuangou result = new RefbRoleTuangou();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setDianShu(getDianShu());
		result.setCreateTime(getCreateTime());
		result.setSubId(getSubId());
		return result;
	}
}
