package com.junyou.bus.jinfeng.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("feng_ip")
public class FengIp extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("ip")
	private String ip;

	@Column("server_id")
	private String serverId;

	@Column("reasons")
	private String reasons;

	@Column("expire_time")
	private Long expireTime;

	@Column("update_time")
	private Timestamp updateTime;
	

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}
	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return null;
	}

	@Override
	public Long getPrimaryKeyValue() {
		return null;
	}

	@Override
	public IEntity copy() {
		return null;
	}


}
