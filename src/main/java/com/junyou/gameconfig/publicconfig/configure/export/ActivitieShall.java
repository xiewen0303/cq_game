package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * 新圣剑
 */
public class ActivitieShall extends AdapterPublicConfig{
	
	private long pvprefreshcd; //竞技场手动刷新cd

	public long getPvprefreshcd() {
		return pvprefreshcd;
	}

	public void setPvprefreshcd(long pvprefreshcd) {
		this.pvprefreshcd = pvprefreshcd;
	}
}
