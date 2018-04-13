package com.junyou.gameconfig.publicconfig.configure.export;

public class YijibossPublicConfig extends AdapterPublicConfig {
	/**
	 * 古神遗迹boss开启等级
	 */
	private int open;
	/**
	 * 最后一击奖励（道具id）
	 */
	private String lastattack;
	/**
	 * 怪物id
	 */
	private String monsterId;

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public String getLastattack() {
		return lastattack;
	}

	public void setLastattack(String lastattack) {
		this.lastattack = lastattack;
	}

	public String getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}

}