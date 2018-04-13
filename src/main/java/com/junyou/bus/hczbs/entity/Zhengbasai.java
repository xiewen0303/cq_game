package com.junyou.bus.hczbs.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("zhengbasai")
public class Zhengbasai extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("guild_leader_id")
	private Long guildLeaderId;

	@Column("guild_name")
	private String guildName;

	@Column("guild_id")
	private Long guildId;

	@Column("update_time")
	private Long updateTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getGuildLeaderId(){
		return guildLeaderId;
	}

	public  void setGuildLeaderId(Long guildLeaderId){
		this.guildLeaderId = guildLeaderId;
	}

	public String getGuildName(){
		return guildName;
	}

	public  void setGuildName(String guildName){
		this.guildName = guildName;
	}

	public Long getGuildId(){
		return guildId;
	}

	public  void setGuildId(Long guildId){
		this.guildId = guildId;
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

	public Zhengbasai copy(){
		Zhengbasai result = new Zhengbasai();
		result.setId(getId());
		result.setGuildLeaderId(getGuildLeaderId());
		result.setGuildName(getGuildName());
		result.setGuildId(getGuildId());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
