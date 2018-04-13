package com.junyou.bus.shouchong.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refb_role_shouchong")
public class RefbRoleShouchong extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_activity_id")
	private Integer subActivityId;

	@Column("lj_yb_val")
	private Integer ljYbVal;

	@Column("receive_state")
	private Integer receiveState;

	@Column("update_time")
	private Long updateTime;

	@Column("expire_time")
	private Long expireTime;


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

	public Integer getSubActivityId(){
		return subActivityId;
	}

	public  void setSubActivityId(Integer subActivityId){
		this.subActivityId = subActivityId;
	}

	public Integer getLjYbVal(){
		return ljYbVal;
	}

	public  void setLjYbVal(Integer ljYbVal){
		this.ljYbVal = ljYbVal;
	}

	public Integer getReceiveState(){
		return receiveState;
	}

	public  void setReceiveState(Integer receiveState){
		this.receiveState = receiveState;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Long getExpireTime(){
		return expireTime;
	}

	public  void setExpireTime(Long expireTime){
		this.expireTime = expireTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefbRoleShouchong copy(){
		RefbRoleShouchong result = new RefbRoleShouchong();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubActivityId(getSubActivityId());
		result.setLjYbVal(getLjYbVal());
		result.setReceiveState(getReceiveState());
		result.setUpdateTime(getUpdateTime());
		result.setExpireTime(getExpireTime());
		return result;
	}
}
