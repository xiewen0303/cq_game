package com.junyou.bus.activityboss.manage;

/**
 * boss伤害排行数据
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-25 下午2:54:14
 */
public class BossHurtRank {
	private long userRoleId;
	private String roleName;
	private long hurt; //伤害值
	private int rank;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public long getHurt() {
		return hurt;
	}
	public void setHurt(long hurt) {
		this.hurt = hurt;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
