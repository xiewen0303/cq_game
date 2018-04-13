package com.junyou.bus.kuafuchargerank.vo;

public class KuafuChargeRankVo implements Comparable<KuafuChargeRankVo> {
	private Long userRoleId;
	private int rank;
	private String name;
	private int yb;
	private long updateTime;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYb() {
		return yb;
	}

	public void setYb(int yb) {
		this.yb = yb;
	}

	public void addYb(int yb) {
		this.yb = this.yb + yb;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		if (this.updateTime < updateTime) {
			this.updateTime = updateTime;
		}
	}

	@Override
	public int compareTo(KuafuChargeRankVo o) {
		if (this.yb > o.yb) {
			return -1;
		} else if (this.yb == o.yb) {
			if (this.updateTime > o.updateTime) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 1;
		}

	}
}
