package com.junyou.bus.territory.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("territory")
public class Territory extends AbsVersion implements Serializable, IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("map_id")
	private Long mapId;

	@Column("guild_id")
	private Long guildId;

	@Column("update_time")
	private Long updateTime;

	public Long getMapId() {
		return mapId;
	}

	public void setMapId(Long mapId) {
		this.mapId = mapId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getGuildId() {
		return guildId;
	}

	public void setGuildId(Long guildId) {
		this.guildId = guildId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "mapId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return mapId.longValue();
	}

	public Territory copy() {
		Territory result = new Territory();
		result.setMapId(getMapId());
		result.setGuildId(getGuildId());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
