package com.junyou.gameconfig.publicconfig.configure.export;



/**
 * 混沌战场
 * @author LiuYu
 * @date 2015-9-7 上午9:53:46
 */
public class HundunPublicConfig extends AdapterPublicConfig{
	private float shengyu;
	private int monsterJifen;
	private int roleJifen;
	private int enterTime;
	private float resetRate;
	public float getShengyu() {
		return shengyu;
	}
	public void setShengyu(float sunshi) {
		this.shengyu = 1 - sunshi;
	}
	public int getMonsterJifen() {
		return monsterJifen;
	}
	public void setMonsterJifen(int monsterJifen) {
		this.monsterJifen = monsterJifen;
	}
	public int getRoleJifen() {
		return roleJifen;
	}
	public void setRoleJifen(int roleJifen) {
		this.roleJifen = roleJifen;
	}
	public int getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(int enterTime) {
		this.enterTime = enterTime;
	}
	public float getResetRate() {
		return resetRate;
	}
	public void setResetRate(float resetRate) {
		this.resetRate = resetRate;
	}
	
}