package com.junyou.bus.lianyuboss.entity;

import java.sql.Timestamp;

/**
 * 门派个人boss公共数据对象
 * @author lxn
 *
 */
public class LianyuBossPublicVo {

	private long guildId;
	private int configId;
	private long userRoleId;
	private int countNum;//通关人数
	private int fastestTime;//最快通关时间
	private Timestamp cTime;
	
	public Timestamp getcTime() {
		return cTime;
	}
	public void setcTime(Timestamp cTime) {
		this.cTime = cTime;
	}
	public long getGuildId() {
		return guildId;
	}
	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	public int getFastestTime() {
		return fastestTime;
	}
	public void setFastestTime(int fastestTime) {
		this.fastestTime = fastestTime;
	}
	
	
}
