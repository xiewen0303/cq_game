package com.junyou.bus.offlineexp.configure;

import com.kernel.data.dao.AbsVersion;

/**
 * 离线经验奖励配置
 * @author jy
 *
 */
public class LiXianJingYanConfig extends AbsVersion {

	private int lvl;
	private int exp;
	public int getLvl() {
		return lvl;
	}
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	
}
