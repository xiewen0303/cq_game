package com.junyou.bus.guild.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_guild_duihuan")
public class RoleGuildDuihuan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("item_id")
	private String itemId;

	@Column("count")
	private Integer count;

	@Column("update_time")
	private Long updateTime;


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

	public String getItemId(){
		return itemId;
	}

	public  void setItemId(String itemId){
		this.itemId = itemId;
	}

	public Integer getCount(){
		return count;
	}

	public  void setCount(Integer count){
		this.count = count;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleGuildDuihuan copy(){
		RoleGuildDuihuan result = new RoleGuildDuihuan();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setItemId(getItemId());
		result.setCount(getCount());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
