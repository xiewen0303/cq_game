package com.junyou.bus.lianyuboss.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("guild_boss_lianyu")
public class GuildBossLianyu extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("guild_id")
	private Long guildId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("config_id")
	private Integer configId;

	@Column("tong_guan_time")
	private Integer tongGuanTime;

	@Column("reward_state")
	private Integer rewardState;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getGuildId(){
		return guildId;
	}

	public  void setGuildId(Long guildId){
		this.guildId = guildId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Integer getTongGuanTime(){
		return tongGuanTime;
	}

	public  void setTongGuanTime(Integer tongGuanTime){
		this.tongGuanTime = tongGuanTime;
	}

	public Integer getRewardState(){
		return rewardState;
	}

	public  void setRewardState(Integer rewardState){
		this.rewardState = rewardState;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
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

	public GuildBossLianyu copy(){
		GuildBossLianyu result = new GuildBossLianyu();
		result.setId(getId());
		result.setGuildId(getGuildId());
		result.setUserRoleId(getUserRoleId());
		result.setConfigId(getConfigId());
		result.setTongGuanTime(getTongGuanTime());
		result.setRewardState(getRewardState());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
