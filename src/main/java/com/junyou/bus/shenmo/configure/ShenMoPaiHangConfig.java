package com.junyou.bus.shenmo.configure;

import com.kernel.data.dao.AbsVersion;

public class ShenMoPaiHangConfig extends AbsVersion {
	private int duan;
	private String name;
	private int jifen;
	private int winExp;
	private int wingx;
	private int winjf;
	private int loseExp;
	private int losejf;
	private int jiangcs;
	private int gongxun;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getJifen() {
		return jifen;
	}

	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}

	public Integer getWingx() {
		return wingx;
	}

	public void setWingx(Integer wingx) {
		this.wingx = wingx;
	}

	public Integer getWinjf() {
		return winjf;
	}

	public void setWinjf(Integer winjf) {
		this.winjf = winjf;
	}

	public Integer getJiangcs() {
		return jiangcs;
	}

	public void setJiangcs(Integer jiangcs) {
		this.jiangcs = jiangcs;
	}

	public Integer getGongxun() {
		return gongxun;
	}

	public void setGongxun(Integer gongxun) {
		this.gongxun = gongxun;
	}

	public Integer getLosejf() {
		return losejf;
	}

	public void setLosejf(Integer losejf) {
		this.losejf = losejf;
	}

	public Integer getDuan() {
		return duan;
	}

	public void setDuan(Integer duan) {
		this.duan = duan;
	}

	public int getWinExp() {
		return winExp;
	}

	public void setWinExp(int winExp) {
		this.winExp = winExp;
	}

	public int getLoseExp() {
		return loseExp;
	}

	public void setLoseExp(int loseExp) {
		this.loseExp = loseExp;
	}

}
