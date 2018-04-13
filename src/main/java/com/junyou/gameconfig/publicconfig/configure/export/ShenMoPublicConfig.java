package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.List;

import com.junyou.utils.collection.ReadOnlyList;

/**
 * 神魔战场公共配置
 * 
 * @author LiuYu
 * @date 2015-9-24 下午2:15:43
 */
public class ShenMoPublicConfig extends AdapterPublicConfig {

	private int level;
	private int piptime1;
	private int piptime2;
	private int piptime3;
	private int piptime4;
	private int piptime5;
	private int daojishi1;
	private int daojishi2;
	private int mianTime;
	private int hdTime;
	private int endTimel;
	private int zdfh;
	private int outCf;
	private int cfTime;
	private int winScore;
	private int killScore;
	private int assScore;
	private int map;
	private int paiming;
	private int paijifen;
	private long qingjifenTime;
	private List<int[]> jyzb;
	private int gongji;
	/**
	 * 每日胜利前10次获得功勋奖励
	 */
	private int dayGongxunTimes;
	/**
	 * 开始时间
	 */
	private String openBeginTime;
	/**
	 * 结束时间
	 */
	private String openEndTime;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPiptime1() {
		return piptime1;
	}

	public void setPiptime1(int piptime1) {
		this.piptime1 = piptime1;
	}

	public int getPiptime2() {
		return piptime2;
	}

	public void setPiptime2(int piptime2) {
		this.piptime2 = piptime2;
	}

	public int getPiptime3() {
		return piptime3;
	}

	public void setPiptime3(int piptime3) {
		this.piptime3 = piptime3;
	}

	public int getPiptime4() {
		return piptime4;
	}

	public void setPiptime4(int piptime4) {
		this.piptime4 = piptime4;
	}

	public int getPiptime5() {
		return piptime5;
	}

	public void setPiptime5(int piptime5) {
		this.piptime5 = piptime5;
	}

	public int getDaojishi1() {
		return daojishi1;
	}

	public void setDaojishi1(int daojishi1) {
		this.daojishi1 = daojishi1;
	}

	public int getDaojishi2() {
		return daojishi2;
	}

	public void setDaojishi2(int daojishi2) {
		this.daojishi2 = daojishi2;
	}

	public int getMianTime() {
		return mianTime;
	}

	public void setMianTime(int mianTime) {
		this.mianTime = mianTime;
	}

	public int getHdTime() {
		return hdTime;
	}

	public void setHdTime(int hdTime) {
		this.hdTime = hdTime;
	}

	public int getEndTimel() {
		return endTimel;
	}

	public void setEndTimel(int endTimel) {
		this.endTimel = endTimel;
	}

	public int getZdfh() {
		return zdfh;
	}

	public void setZdfh(int zdfh) {
		this.zdfh = zdfh;
	}

	public int getOutCf() {
		return outCf;
	}

	public void setOutCf(int outCf) {
		this.outCf = outCf;
	}

	public int getCfTime() {
		return cfTime;
	}

	public void setCfTime(int cfTime) {
		this.cfTime = cfTime;
	}

	public int getWinScore() {
		return winScore;
	}

	public void setWinScore(int winScore) {
		this.winScore = winScore;
	}

	public int getKillScore() {
		return killScore;
	}

	public void setKillScore(int killScore) {
		this.killScore = killScore;
	}

	public int getAssScore() {
		return assScore;
	}

	public void setAssScore(int assScore) {
		this.assScore = assScore;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public int getPaiming() {
		return paiming;
	}

	public void setPaiming(int paiming) {
		this.paiming = paiming;
	}

	public int getPaijifen() {
		return paijifen;
	}

	public void setPaijifen(int paijifen) {
		this.paijifen = paijifen;
	}

	public long getQingjifenTime() {
		return qingjifenTime;
	}

	public void setQingjifenTime(long qingjifenTime) {
		this.qingjifenTime = qingjifenTime;
	}

	public List<int[]> getJyzb() {
		return jyzb;
	}

	public void setJyzb(List<int[]> jyzb) {
		this.jyzb = new ReadOnlyList<>(jyzb);
	}

	public String getOpenBeginTime() {
		return openBeginTime;
	}

	public void setOpenBeginTime(String openBeginTime) {
		this.openBeginTime = openBeginTime;
	}

	public String getOpenEndTime() {
		return openEndTime;
	}

	public void setOpenEndTime(String openEndTime) {
		this.openEndTime = openEndTime;
	}

	public int getGongji() {
		return gongji;
	}

	public void setGongji(int gongji) {
		this.gongji = gongji;
	}

	public int getDayGongxunTimes() {
		return dayGongxunTimes;
	}

	public void setDayGongxunTimes(int dayGongxunTimes) {
		this.dayGongxunTimes = dayGongxunTimes;
	}

}
