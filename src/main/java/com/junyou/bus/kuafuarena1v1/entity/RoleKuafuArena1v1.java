package com.junyou.bus.kuafuarena1v1.entity;

import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_kuafu_arena_1v1")
public class RoleKuafuArena1v1 extends AbsVersion implements Serializable,
		IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("gongxun")
	private Integer gongxun;

	@Column("arena_times")
	private Integer arenaTimes;

	@Column("win_times")
	private Integer winTimes;

	@Column("last_arena_time")
	private Long lastArenaTime;
	
	@Column("last_arena_times")
	private Integer lastArenaTimes;
	
	@Column("last_duan")
	private Integer lastDuan;

	@Column("gongxun_status")
	private Integer gongxunStatus;

	@Column("jifen")
	private Integer jifen;

	@Column("jifen_update_time")
	private Long jifenUpdateTime;

	@Column("lian_win")
	private Integer lianWin;

	@Column("lian_lose")
	private Integer lianLose;

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

	public Integer getLianWin() {
		return lianWin;
	}

	public void setLianWin(Integer lianWin) {
		this.lianWin = lianWin;
	}

	public Integer getLianLose() {
		return lianLose;
	}

	public void setLianLose(Integer lianLose) {
		this.lianLose = lianLose;
	}

	public Integer getGongxun() {
		return gongxun;
	}

	public void setGongxun(Integer gongxun) {
		this.gongxun = gongxun;
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

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleKuafuArena1v1 copy() {
		RoleKuafuArena1v1 result = new RoleKuafuArena1v1();
		result.setUserRoleId(getUserRoleId());
		result.setGongxun(getGongxun());
		result.setWinTimes(getWinTimes());
		result.setArenaTimes(getArenaTimes());
		result.setLastArenaTime(getLastArenaTime());
		result.setLastArenaTimes(getLastArenaTimes());
		result.setLastDuan(getLastDuan());
		result.setGongxunStatus(getGongxunStatus());
		result.setJifen(getJifen());
		result.setJifenUpdateTime(getJifenUpdateTime());
		result.setLianWin(getLianWin());
		result.setLianLose(getLianLose());
		return result;
	}
}
