package com.junyou.gameconfig.publicconfig.configure.export;



/**
 * 怒气
 * @author LiuYu
 * @date 2015-6-4 下午3:55:32
 */
public class NuqiPublicConfig extends AdapterPublicConfig{
	
	private int maxNq;//怒气上限
	
	private int timeAdd;//每5秒增加怒气
	
	private int killAdd;//杀怪增加怒气
	
	private int jiange;//大招攻击间隔(单位秒)
	
	private int boshu;//大招持续波数

	public int getMaxNq() {
		return maxNq;
	}

	public void setMaxNq(int maxNq) {
		this.maxNq = maxNq;
	}

	public int getTimeAdd() {
		return timeAdd;
	}

	public void setTimeAdd(int timeAdd) {
		this.timeAdd = timeAdd;
	}

	public int getKillAdd() {
		return killAdd;
	}

	public void setKillAdd(int killAdd) {
		this.killAdd = killAdd;
	}

	public int getJiange() {
		return jiange;
	}

	public void setJiange(int jiange) {
		this.jiange = jiange;
	}

	public int getBoshu() {
		return boshu;
	}

	public void setBoshu(int boshu) {
		this.boshu = boshu;
	}
	
	
}
