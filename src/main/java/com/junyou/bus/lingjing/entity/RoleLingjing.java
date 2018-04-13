package com.junyou.bus.lingjing.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_lingjing")
public class RoleLingjing extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("rank")
	private Integer rank;

	@Column("state")
	private Integer state;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getRank(){
		return rank;
	}

	public  void setRank(Integer rank){
		this.rank = rank;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleLingjing copy(){
		RoleLingjing result = new RoleLingjing();
		result.setUserRoleId(getUserRoleId());
		result.setRank(getRank());
		result.setState(getState());
		return result;
	}
}
