package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.Map;

public class KuafuQunXianYanPublicConfig extends AdapterPublicConfig {
	private int open;// 跨服群仙宴开启等级
	private int zplus;// 跨服群仙宴进入战力限制
	private int mapid;// 跨服群仙宴活动地图id
	private int exptime;// 每次获得经验时间（秒） 10
	private long jiangexp;// 次获得的经验值 100000
	private int maxpople;// 每个跨服场景承载人数上限
	private int killValue;//杀人掉落积分比例
	private int killValeMax;//杀人掉落积分上限
	private int[] zuobiao;// boss刷新坐标
	private Map<String, Integer> lastattack;// 最后一击奖励（道具id）
	private int bossdeathgg;// boss击杀公告code码
	private String jlcode;//活动结束邮件奖励内容
	private String startTime;//开始时间
	private String endTime;//结束时间
	private int rankRefresh;//排行榜刷新时间
	private String sxCode;//刷新公告code
	private String cjCode;//采集公告code
	
	public int getOpen() {
		return open;
	}
	public void setOpen(int open) {
		this.open = open;
	}
	public int getZplus() {
		return zplus;
	}
	public void setZplus(int zplus) {
		this.zplus = zplus;
	}
	public int getMapid() {
		return mapid;
	}
	public void setMapid(int mapid) {
		this.mapid = mapid;
	}
	public int getExptime() {
		return exptime;
	}
	public void setExptime(int exptime) {
		this.exptime = exptime;
	}
	public long getJiangexp() {
		return jiangexp;
	}
	public void setJiangexp(long jiangexp) {
		this.jiangexp = jiangexp;
	}
	public int getMaxpople() {
		return maxpople;
	}
	public void setMaxpople(int maxpople) {
		this.maxpople = maxpople;
	}
	public int getKillValue() {
		return killValue;
	}
	public void setKillValue(int killValue) {
		this.killValue = killValue;
	}
	public int getKillValeMax() {
		return killValeMax;
	}
	public void setKillValeMax(int killValeMax) {
		this.killValeMax = killValeMax;
	}
	public int[] getZuobiao() {
		return zuobiao;
	}
	public void setZuobiao(int[] zuobiao) {
		this.zuobiao = zuobiao;
	}
	public Map<String, Integer> getLastattack() {
		return lastattack;
	}
	public void setLastattack(Map<String, Integer> lastattack) {
		this.lastattack = lastattack;
	}
	public int getBossdeathgg() {
		return bossdeathgg;
	}
	public void setBossdeathgg(int bossdeathgg) {
		this.bossdeathgg = bossdeathgg;
	}
	public String getJlcode() {
		return jlcode;
	}
	public void setJlcode(String jlcode) {
		this.jlcode = jlcode;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getRankRefresh() {
		return rankRefresh;
	}
	public void setRankRefresh(int rankRefresh) {
		this.rankRefresh = rankRefresh;
	}
	public String getSxCode() {
		return sxCode;
	}
	public void setSxCode(String sxCode) {
		this.sxCode = sxCode;
	}
	public String getCjCode() {
		return cjCode;
	}
	public void setCjCode(String cjCode) {
		this.cjCode = cjCode;
	}

	
	

}
