package com.junyou.bus.wenquan.vo;


public class WenquanRankVo implements Comparable<WenquanRankVo>{
	private int rank;
	private Long userRoleId;
	private String name;
	private int goldTimes;
	private long goldUpdateTime;

	public long getGoldUpdateTime() {
		return goldUpdateTime;
	}

	public void setGoldUpdateTime(long goldUpdateTime) {
		this.goldUpdateTime = goldUpdateTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
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

	public int getGoldTimes() {
		return goldTimes;
	}

	public void setGoldTimes(int goldTimes) {
		this.goldTimes = goldTimes;
	}

	@Override
	public int compareTo(WenquanRankVo o) {
		if (this.goldTimes > o.goldTimes){
			return -1;
		}else if (this.goldTimes == o.goldTimes){
			if(this.goldUpdateTime > o. goldUpdateTime){
				return 1;
			}else{
				return -1;
			}
		}else{
			return 1;
		}
	}
}
