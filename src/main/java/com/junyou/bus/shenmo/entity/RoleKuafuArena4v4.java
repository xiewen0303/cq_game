package com.junyou.bus.shenmo.entity;

import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_kuafu_arena_4v4")
public class RoleKuafuArena4v4 extends AbsVersion implements Serializable,
		IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("arena_times")
	private Integer arenaTimes;

	@Column("win_times")
	private Integer winTimes;

	@Column("last_arena_time")
	private Long lastArenaTime;
	
	@Column("last_arena_times")
	private Integer lastArenaTimes;
	
	@Column("last_escape_time")
	private Long lastEscapeTime;
	
	@Column("escape_times")
	private Integer escapeTimes;
	
	@Column("last_duan")
	private Integer lastDuan;

	@Column("gongxun_status")
	private Integer gongxunStatus;

	@Column("jifen")
	private Integer jifen;

	@Column("jifen_update_time")
	private Long jifenUpdateTime;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getArenaTimes() {
		return arenaTimes;
	}

	public void setArenaTimes(Integer arenaTimes) {
		this.arenaTimes = arenaTimes;
	}

	public Long getLastArenaTime() {
		return lastArenaTime;
	}

	public void setLastArenaTime(Long lastArenaTime) {
		this.lastArenaTime = lastArenaTime;
	}

	public Integer getGongxunStatus() {
		return gongxunStatus;
	}

	public void setGongxunStatus(Integer gongxunStatus) {
		this.gongxunStatus = gongxunStatus;
	}

	public Integer getJifen() {
		return jifen;
	}

	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}

	public Long getJifenUpdateTime() {
		return jifenUpdateTime;
	}

	public void setJifenUpdateTime(Long jifenUpdateTime) {
		this.jifenUpdateTime = jifenUpdateTime;
	}

	public Integer getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(Integer winTimes) {
		this.winTimes = winTimes;
	}

	public Integer getLastDuan() {
		return lastDuan;
	}

	public void setLastDuan(Integer lastDuan) {
		this.lastDuan = lastDuan;
	}

	public Integer getLastArenaTimes() {
		return lastArenaTimes;
	}

	public void setLastArenaTimes(Integer lastArenaTimes) {
		this.lastArenaTimes = lastArenaTimes;
	}

	public Long getLastEscapeTime() {
		return lastEscapeTime;
	}

	public void setLastEscapeTime(Long lastEscapeTime) {
		this.lastEscapeTime = lastEscapeTime;
	}

	public Integer getEscapeTimes() {
		return escapeTimes;
	}

	public void setEscapeTimes(Integer escapeTimes) {
		this.escapeTimes = escapeTimes;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleKuafuArena4v4 copy() {
		RoleKuafuArena4v4 result = new RoleKuafuArena4v4();
		result.setUserRoleId(getUserRoleId());
		result.setWinTimes(getWinTimes());
		result.setArenaTimes(getArenaTimes());
		result.setLastArenaTime(getLastArenaTime());
		result.setLastArenaTimes(getLastArenaTimes());
		result.setLastDuan(getLastDuan());
		result.setGongxunStatus(getGongxunStatus());
		result.setJifen(getJifen());
		result.setLastEscapeTime(getLastEscapeTime());
		result.setEscapeTimes(getEscapeTimes());
		result.setJifenUpdateTime(getJifenUpdateTime());
		return result;
	}
}
