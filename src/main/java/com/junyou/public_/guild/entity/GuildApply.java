package com.junyou.public_.guild.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("guild_apply")
public class GuildApply extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("guild_id")
	private Long guildId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("apply_time")
	private Long applyTime;
	
	@EntityField
	private int zplus;
	
	@EntityField
	private int configId;
	
	@EntityField
	private int level;
	
	@EntityField
	private int vip;
	
	@EntityField
	private String name;


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

	public Long getApplyTime(){
		return applyTime;
	}

	public  void setApplyTime(Long applyTime){
		this.applyTime = applyTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public int getZplus() {
		return zplus;
	}

	public void setZplus(int zplus) {
		this.zplus = zplus;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GuildApply copy(){
		GuildApply result = new GuildApply();
		result.setId(getId());
		result.setGuildId(getGuildId());
		result.setUserRoleId(getUserRoleId());
		result.setApplyTime(getApplyTime());
		result.setName(getName());
		result.setLevel(getLevel());
		result.setVip(getVip());
		result.setZplus(getZplus());
		result.setConfigId(getConfigId());
		return result;
	}
}
