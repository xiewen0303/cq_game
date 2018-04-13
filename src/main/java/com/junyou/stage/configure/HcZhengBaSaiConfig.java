package com.junyou.stage.configure;

import java.util.Map;

public class HcZhengBaSaiConfig {
	private int map;
	private long exp;
	private int zhenqi;
	private int[] zuobiao;
	private Object[] gongfuhuo;//攻   二维数组 里面两个复活点随记取一个 new Random().nextInt(2)
	private Object[] shoufuhuo;//守   二维数组 里面两个复活点随记取一个
	private int[] fuhuo1;  
	private int[] fuhuo2;
	private float addexp;
	private float addzhenqi;
	private Map<String, Integer> jiangitem;
	private int needlevel;
	
	private Map<String, Long> attrs;  //给门主的奖励

	public Object[] getGongfuhuo() {
		return gongfuhuo;
	}
	public void setGongfuhuo(Object[] gongfuhuo) {
		this.gongfuhuo = gongfuhuo;
	}
	public Object[] getShoufuhuo() {
		return shoufuhuo;
	}
	public void setShoufuhuo(Object[] shoufuhuo) {
		this.shoufuhuo = shoufuhuo;
	}
	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getZhenqi() {
		return zhenqi;
	}

	public void setZhenqi(int zhenqi) {
		this.zhenqi = zhenqi;
	}

	public int[] getZuobiao() {
		return zuobiao;
	}

	public void setZuobiao(int[] zuobiao) {
		this.zuobiao = zuobiao;
	}

	public int[] getFuhuo1() {
		return fuhuo1;
	}

	public void setFuhuo1(int[] fuhuo1) {
		this.fuhuo1 = fuhuo1;
	}

	public int[] getFuhuo2() {
		return fuhuo2;
	}

	public void setFuhuo2(int[] fuhuo2) {
		this.fuhuo2 = fuhuo2;
	}

	public float getAddexp() {
		return addexp;
	}

	public void setAddexp(float addexp) {
		this.addexp = addexp;
	}

	public float getAddzhenqi() {
		return addzhenqi;
	}

	public void setAddzhenqi(float addzhenqi) {
		this.addzhenqi = addzhenqi;
	}

	public Map<String, Integer> getJiangitem() {
		return jiangitem;
	}

	public void setJiangitem(Map<String, Integer> jiangitem) {
		this.jiangitem = jiangitem;
	}

	public int getNeedlevel() {
		return needlevel;
	}

	public void setNeedlevel(int needlevel) {
		this.needlevel = needlevel;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

}
