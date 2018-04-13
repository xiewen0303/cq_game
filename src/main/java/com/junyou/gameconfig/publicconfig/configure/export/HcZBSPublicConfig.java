package com.junyou.gameconfig.publicconfig.configure.export;



/**
 * 云宫之战（争霸赛）共配置表
 * @date 2015-4-21 下午2:20:59
 */
public class HcZBSPublicConfig extends AdapterPublicConfig{
	
	private int needGold;//拔旗所需帮派资金
	private int winBg;//胜方奖励帮贡
	private int winExp;//胜方奖励经验
	private int loseBg;//败方奖励帮贡
	private int loseExp;//败方奖励经验
	private String buff;//胜方BUFF
	private int needTime;//所需停留时间
	private int delay;//跳经验间隔
	private int time2;//需在活动地图停留超过一定时间获得奖励
	private int jingyan2;//活动结束后，停留超15分钟获得经验
	private int zhenqi2;//活动结束后，停留超15分钟获得真气
	private String kqbuff;//扛旗buff
	private int ziyuanid;//旗子资源id
	private String longt;//图腾id
//	private int jigong;//杀非本帮派的玩家获得帮贡奖励
//	private int jigongshu;//一次领地战最多获得奖励
	private int shanghai;//固定伤害值（每次攻击伤害为100点）
	
	public int getNeedGold() {
		return needGold;
	}
	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}
	public int getWinBg() {
		return winBg;
	}
	public void setWinBg(int winBg) {
		this.winBg = winBg;
	}
	public int getWinExp() {
		return winExp;
	}
	public void setWinExp(int winExp) {
		this.winExp = winExp;
	}
	public int getLoseBg() {
		return loseBg;
	}
	public void setLoseBg(int loseBg) {
		this.loseBg = loseBg;
	}
	public int getLoseExp() {
		return loseExp;
	}
	public void setLoseExp(int loseExp) {
		this.loseExp = loseExp;
	}
	public String getBuff() {
		return buff;
	}
	public void setBuff(String buff) {
		this.buff = buff;
	}
	public int getNeedTime() {
		return needTime;
	}
	public void setNeedTime(int needTime) {
		this.needTime = needTime;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public int getTime2() {
		return time2;
	}
	public void setTime2(int time2) {
		this.time2 = time2;
	}
	public int getJingyan2() {
		return jingyan2;
	}
	public void setJingyan2(int jingyan2) {
		this.jingyan2 = jingyan2;
	}
	public int getZhenqi2() {
		return zhenqi2;
	}
	public void setZhenqi2(int zhenqi2) {
		this.zhenqi2 = zhenqi2;
	}
	public String getKqbuff() {
		return kqbuff;
	}

	public void setKqbuff(String kqbuff) {
		this.kqbuff = kqbuff;
	}
	public String getLongt() {
		return longt;
	}
	public void setLongt(String longt) {
		this.longt = longt;
	}
	public int getZiyuanid() {
		return ziyuanid;
	}
	public void setZiyuanid(int ziyuanid) {
		this.ziyuanid = ziyuanid;
	}
	public int getShanghai() {
		return shanghai;
	}
	public void setShanghai(int shanghai) {
		this.shanghai = shanghai;
	}
	
}
