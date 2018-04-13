package com.junyou.stage.hundun.entity;
/**
 * 混沌战场排名
 * @author LiuYu
 * 2015-9-1 下午5:05:18
 */
public class HundunRank {
	private Long userRoleId;
	private String userName;
	private int jfVal;
	private long updateTime;
	private int rank;
	
	public Long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getJfVal() {
		return jfVal;
	}
	public void setJfVal(int jfVal) {
		this.jfVal = jfVal;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public Object[] getData(){
		return new Object[]{userRoleId, userName, jfVal};
	}
}
