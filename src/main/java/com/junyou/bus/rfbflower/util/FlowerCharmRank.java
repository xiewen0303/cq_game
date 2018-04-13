package com.junyou.bus.rfbflower.util;

public class FlowerCharmRank {

	private long userRoleId;
	private String userName;
	private int  job;
	private int rank;
	private long charmValue;//魅力值
	private long shangBangTime;//上榜时间
	
	
	public Object[] toArray(){
		
		return new Object[]{userRoleId,userName,job,rank,charmValue};
	}
	public long getShangBangTime() {
		return shangBangTime;
	}
	public void setShangBangTime(long shangBangTime) {
		this.shangBangTime = shangBangTime;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getJob() {
		return job;
	}
	public void setJob(int job) {
		this.job = job;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public long getCharmValue() {
		return charmValue;
	}
	public void setCharmValue(long charmValue) {
		this.charmValue = charmValue;
	}
	
	
}
