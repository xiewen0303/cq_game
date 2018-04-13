package com.junyou.bus.hczbs.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("zhengbasai_day_reward")
public class ZhengbasaiDayReward extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("position")
	private Integer position;

	@Column("guild_id")
	private Long guildId;

	@Column("state")
	private Integer state;

	@Column("update_time")
	private Long updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getPosition(){
		return position;
	}

	public  void setPosition(Integer position){
		this.position = position;
	}

	public Long getGuildId(){
		return guildId;
	}

	public  void setGuildId(Long guildId){
		this.guildId = guildId;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
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

	public ZhengbasaiDayReward copy(){
		ZhengbasaiDayReward result = new ZhengbasaiDayReward();
		result.setUserRoleId(getUserRoleId());
		result.setPosition(getPosition());
		result.setGuildId(getGuildId());
		result.setState(getState());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
