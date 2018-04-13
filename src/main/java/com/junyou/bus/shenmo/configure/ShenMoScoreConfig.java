package com.junyou.bus.shenmo.configure;
/**
 * @author LiuYu
 * 2015-9-24 下午2:14:42
 */
public class ShenMoScoreConfig {
	private String id;
	private int type;
	private int teamId;
	private int killScore;
	private int gsTime;
	private int gsScore;
	private String buffId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getKillScore() {
		return killScore;
	}
	public void setKillScore(int killScore) {
		this.killScore = killScore;
	}
	public int getGsTime() {
		return gsTime;
	}
	public void setGsTime(int gsTime) {
		this.gsTime = gsTime;
	}
	public int getGsScore() {
		return gsScore;
	}
	public void setGsScore(int gsScore) {
		this.gsScore = gsScore;
	}
	public String getBuffId() {
		return buffId;
	}
	public void setBuffId(String buffId) {
		this.buffId = buffId;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
}
