package com.junyou.stage.model.stage.kuafuquanxianyan;
/**
 * @author zhongdian
 * 跨服群仙宴排行榜
 * 2016-2-22 下午6:08:50
 */
public class KuaFuQunXianYanRank {

	private Long userRoleId;
	private String name;
	private Integer score;//积分
	private Integer mingci;//名次
	private Integer deadCount;//死亡次数
	private String serverId;//原服ID
	
	
	
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getMingci() {
		return mingci;
	}
	public void setMingci(Integer mingci) {
		this.mingci = mingci;
	}
	public Integer getDeadCount() {
		return deadCount;
	}
	public void setDeadCount(Integer deadCount) {
		this.deadCount = deadCount;
	}
	
	
}
