package com.junyou.bus.kfjingji.configure;
/**
 * @author LiuYu
 * 2015-10-26 下午4:34:33
 */
public class KfJingjiConfig {
	private int cishu;
	private int cishuGold;
	private int fightCd;
	private int maxCd;
	private int cdGold;
	private int freeChange;
	private int changeGold;
	private int winExp;
	private int winShenhun;
	private int loseExp;
	private int loseShenhun;
	public int getCishu() {
		return cishu;
	}
	public void setCishu(int cishu) {
		this.cishu = cishu;
	}
	public int getCishuGold() {
		return cishuGold;
	}
	public void setCishuGold(int cishuGold) {
		this.cishuGold = cishuGold;
	}
	public int getFightCd() {
		return fightCd;
	}
	public void setFightCd(int fightCd) {
		this.fightCd = fightCd * 1000;
	}
	public int getMaxCd() {
		return maxCd;
	}
	public void setMaxCd(int maxCd) {
		this.maxCd = maxCd * 1000;
	}
	public int getCdGold() {
		return cdGold;
	}
	public void setCdGold(int cdGold) {
		this.cdGold = cdGold;
	}
	public int getWinExp() {
		return winExp;
	}
	public void setWinExp(int winExp) {
		this.winExp = winExp;
	}
	public int getWinShenhun() {
		return winShenhun;
	}
	public void setWinShenhun(int winShenhun) {
		this.winShenhun = winShenhun;
	}
	public int getLoseExp() {
		return loseExp;
	}
	public void setLoseExp(int loseExp) {
		this.loseExp = loseExp;
	}
	public int getLoseShenhun() {
		return loseShenhun;
	}
	public void setLoseShenhun(int loseShenhun) {
		this.loseShenhun = loseShenhun;
	}
	public int getFreeChange() {
		return freeChange;
	}
	public void setFreeChange(int freeChange) {
		this.freeChange = freeChange;
	}
	public int getChangeGold() {
		return changeGold;
	}
	public void setChangeGold(int changeGold) {
		this.changeGold = changeGold;
	}
	
}
