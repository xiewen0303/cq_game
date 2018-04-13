package com.junyou.bus.guild.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_guild_info")
public class RoleGuildInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("gift_state")
	private Integer giftState;

	@Column("update_time")
	private Long updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getGiftState(){
		return giftState;
	}

	public  void setGiftState(Integer giftState){
		this.giftState = giftState;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleGuildInfo copy(){
		RoleGuildInfo result = new RoleGuildInfo();
		result.setUserRoleId(getUserRoleId());
		result.setGiftState(getGiftState());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
