package com.junyou.stage.shenmo.entity;

import com.junyou.stage.model.element.role.Role;

/**
 * @author LiuYu 2015-9-23 下午7:08:12
 */
public class ShenMoRole implements Comparable<ShenMoRole> {
	private long userRoleId;
	private String serverId;
	private int jing;// 占领晶岩数量
	private int kill;
	private int assists;// 助攻
	private int dead;
	private int team;
	private int score;// 积分
	private Role role;
	private boolean exit;
	private Object[] scoreInfo;
	
	@Override
	public int compareTo(ShenMoRole o) {
		if (this.score > o.score) {
			return -1;
		} else {
			return 1;
		}
	}

	public ShenMoRole(Role role, int team) {
		this.role = role;
		userRoleId = role.getId();
		jing = 0;
		kill = 0;
		assists = 0;
		dead = 0;
		score = 0;
		this.team = team;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getJing() {
		return jing;
	}

	public void setJing(int jing) {
		this.jing = jing;
	}

	public int getKill() {
		return kill;
	}

	public void setKill(int kill) {
		this.kill = kill;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getDead() {
		return dead;
	}

	public void setDead(int dead) {
		this.dead = dead;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public Role getRole() {
		return role;
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
		if (scoreInfo != null) {
			scoreInfo[2] = exit;
		}
	}

	public Object[] getScoreInfo() {
		if (scoreInfo == null) {
			scoreInfo = new Object[] { role.getName(), score, exit };
		} else {
			scoreInfo[1] = score;
		}
		return scoreInfo;
	}

}
