package com.junyou.gameconfig.publicconfig.configure.export;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 * @author LiuYu
 * 2015-7-8 下午6:12:12
 */
public class ZiYuanPublicConfig extends AdapterPublicConfig{
	private float moneyRate;
	private float goldRate;
	public float getMoneyRate() {
		return moneyRate;
	}
	public void setMoneyRate(float moneyRate) {
		this.moneyRate = moneyRate;
	}
	public float getGoldRate() {
		return goldRate;
	}
	public void setGoldRate(float goldRate) {
		this.goldRate = goldRate;
	}
	
}
