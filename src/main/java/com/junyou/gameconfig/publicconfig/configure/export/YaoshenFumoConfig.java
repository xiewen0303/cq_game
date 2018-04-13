package com.junyou.gameconfig.publicconfig.configure.export;

public class YaoshenFumoConfig extends AdapterPublicConfig {
	private int open; 
	private int needGold; //元宝
	private int needBgold; //绑定元宝
	
	public int getNeedBgold() {
		return needBgold;
	}
	public void setNeedBgold(int needBgold) {
		this.needBgold = needBgold;
	}
	public int getNeedGold() {
		return needGold;
	}
	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	 

}