package com.junyou.gameconfig.publicconfig.configure.export;

public class WenquanPublicConfig extends AdapterPublicConfig {
	/**
	 * 元宝聚灵 消耗元宝数
	 */
	private int needgold;
	/**
	 * 元宝聚灵 获得真气
	 */
	private int zhenqi;
	/**
	 * 元宝聚灵 获得经验
	 */
	private int jingyan;
	/**
	 * 活动期间，丢肥皂和搓搓背累计次数
	 */
	private int cishu;
	/**
	 * 丢肥皂或者搓搓背获得真气（等级*基础值）
	 */
	private int zhenqi1;
	/**
	 * 丢肥皂或者搓搓背获得经验（等级*基础值）
	 */
	private int jingyan1;
	/**
	 * 花费元宝进入更高倍区
	 */
	private int needgold1;
	/**
	 * 普通区域奖励倍数
	 */
	private int beishu;
	/**
	 * 高级区域奖励倍数
	 */
	private int beishu1;
	/**
	 * 排行榜显示前多少名玩家
	 */
	private int paim;
	/**
	 * 进入更高倍区坐标
	 */
	private int[] zuobiao;

	public int getNeedgold() {
		return needgold;
	}

	public void setNeedgold(int needgold) {
		this.needgold = needgold;
	}

	public int getZhenqi() {
		return zhenqi;
	}

	public void setZhenqi(int zhenqi) {
		this.zhenqi = zhenqi;
	}

	public int getJingyan() {
		return jingyan;
	}

	public void setJingyan(int jingyan) {
		this.jingyan = jingyan;
	}

	public int getCishu() {
		return cishu;
	}

	public void setCishu(int cishu) {
		this.cishu = cishu;
	}

	public int getZhenqi1() {
		return zhenqi1;
	}

	public void setZhenqi1(int zhenqi1) {
		this.zhenqi1 = zhenqi1;
	}

	public int getJingyan1() {
		return jingyan1;
	}

	public void setJingyan1(int jingyan1) {
		this.jingyan1 = jingyan1;
	}

	public int getNeedgold1() {
		return needgold1;
	}

	public void setNeedgold1(int needgold1) {
		this.needgold1 = needgold1;
	}

	public int getBeishu() {
		return beishu;
	}

	public void setBeishu(int beishu) {
		this.beishu = beishu;
	}

	public int getBeishu1() {
		return beishu1;
	}

	public void setBeishu1(int beishu1) {
		this.beishu1 = beishu1;
	}

	public int getPaim() {
		return paim;
	}

	public void setPaim(int paim) {
		this.paim = paim;
	}

	public int[] getZuobiao() {
		return zuobiao;
	}

	public void setZuobiao(int[] zuobiao) {
		this.zuobiao = zuobiao;
	}

}