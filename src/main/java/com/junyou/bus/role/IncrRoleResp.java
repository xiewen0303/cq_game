package com.junyou.bus.role;

/**
 * 增加后对象反馈
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-27 下午2:58:01
 */
public class IncrRoleResp {
	
	private Long incrExp;//实际增加经验
	private boolean isUpgrade;//是否升级（true:升级，false:不升级）
	
	public IncrRoleResp(Long incrExp, boolean isUpgrade){
		this.incrExp = incrExp;
		this.isUpgrade = isUpgrade;
	}

	public Long getIncrExp() {
		return incrExp;
	}

	public boolean isUpgrade() {
		return isUpgrade;
	}
}