package com.junyou.gameconfig.publicconfig.configure.export;


public class KuafuArena1v1PublicConfig extends AdapterPublicConfig {
	/**
	 * 小连胜
	 */
	private int smallWin;
	/**
	 * 大连胜
	 */
	private int bigWin;
	/**
	 * 小连败
	 */
	private int smallLose;
	/**
	 * 大连败
	 */
	private int bigLose;
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
	/**
	 * 功能开启等级
	 */
	private int openLevel;
	/**
	 * 第1阶段匹配时间
	 */
	private int matchJie1;
	/**
	 * 第2阶段匹配时间
	 */
	private int matchJie2;
	/**
	 * 第三阶段匹配时间
	 */
	private int matchJie3;
	/**
	 * 匹配到玩家进入地图倒计时
	 */
	private int daojishi1;
	/**
	 * 进入地图后倒计时
	 */
	private int daojishi2;
	/**
	 * 战斗结束面板多长时间自动关闭并退出
	 */
	private int exitDaojishi;
	/**
	 * 每相差一个段位减少或者增加的积分
	 */
	private int jifenP;

	private int mapId;

	private int[] zuobiao1;

	private int[] zuobiao2;

	private int initJifen;

	private int endtime;

	private int zongtime;

	private int paiming;

	private int paijifen;

	private long qingjifenTime;

	public int getSmallWin() {
		return smallWin;
	}

	public void setSmallWin(int smallWin) {
		this.smallWin = smallWin;
	}

	public int getBigWin() {
		return bigWin;
	}

	public void setBigWin(int bigWin) {
		this.bigWin = bigWin;
	}

	public int getSmallLose() {
		return smallLose;
	}

	public void setSmallLose(int smallLose) {
		this.smallLose = smallLose;
	}

	public int getBigLose() {
		return bigLose;
	}

	public void setBigLose(int bigLose) {
		this.bigLose = bigLose;
	}

	public int getDayGongxunTimes() {
		return dayGongxunTimes;
	}

	public void setDayGongxunTimes(int dayGongxunTimes) {
		this.dayGongxunTimes = dayGongxunTimes;
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

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public int getMatchJie1() {
		return matchJie1;
	}

	public void setMatchJie1(int matchJie1) {
		this.matchJie1 = matchJie1;
	}

	public int getMatchJie2() {
		return matchJie2;
	}

	public void setMatchJie2(int matchJie2) {
		this.matchJie2 = matchJie2;
	}

	public int getMatchJie3() {
		return matchJie3;
	}

	public void setMatchJie3(int matchJie3) {
		this.matchJie3 = matchJie3;
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

	public int getExitDaojishi() {
		return exitDaojishi;
	}

	public void setExitDaojishi(int exitDaojishi) {
		this.exitDaojishi = exitDaojishi;
	}

	public int getJifenP() {
		return jifenP;
	}

	public void setJifenP(int jifenP) {
		this.jifenP = jifenP;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int[] getZuobiao1() {
		return zuobiao1;
	}

	public void setZuobiao1(int[] zuobiao1) {
		this.zuobiao1 = zuobiao1;
	}

	public int[] getZuobiao2() {
		return zuobiao2;
	}

	public void setZuobiao2(int[] zuobiao2) {
		this.zuobiao2 = zuobiao2;
	}

	public int getInitJifen() {
		return initJifen;
	}

	public void setInitJifen(int initJifen) {
		this.initJifen = initJifen;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public int getZongtime() {
		return zongtime;
	}

	public void setZongtime(int zongtime) {
		this.zongtime = zongtime;
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

}
