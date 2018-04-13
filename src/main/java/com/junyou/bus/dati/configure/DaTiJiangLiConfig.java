package com.junyou.bus.dati.configure;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.common.CovertObjectUtil;

public class DaTiJiangLiConfig {
	
	private int minLevel;
	private int maxLevel;
	private int exp;
	private int zhenqi;
	private int jifen;
	private int exp1;
	private int zhenqi1;
	private int miao;
/*	private int time;
	private int time1;*/
	private Map<String,Integer> jiangItem;
	private int needGold;
	private String diyi;
	private String dier;
	private String disan;
	private String pu;


	
	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getZhenqi() {
		return zhenqi;
	}

	public void setZhenqi(int zhenqi) {
		this.zhenqi = zhenqi;
	}

	public int getJifen() {
		return jifen;
	}

	public void setJifen(int jifen) {
		this.jifen = jifen;
	}

	public int getMiao() {
		return miao;
	}

	public void setMiao(int miao) {
		this.miao = miao;
	}

/*	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime1() {
		return time1;
	}

	public void setTime1(int time1) {
		this.time1 = time1;
	}
*/
	
	public String getDiyi() {
		return diyi;
	}

	public Map<String, Integer> getJiangItem() {
		return jiangItem;
	}

	public void setJiangItem(String jiangItem) {
		if(this.jiangItem==null){
			this.jiangItem=new HashMap<String, Integer>();
		}
		for (String item : jiangItem.split("\\|")) {
			String[] kv= item.split(":");
			this.jiangItem.put(kv[0].trim(),CovertObjectUtil.object2int(kv[1].trim()));
		}
		 
	}

	public void setJiangItem(Map<String, Integer> jiangItem) {
		this.jiangItem = jiangItem;
	}

	public void setDiyi(String diyi) {
		this.diyi = diyi;
	}

	public String getDier() {
		return dier;
	}

	public void setDier(String dier) {
		this.dier = dier;
	}

	public String getDisan() {
		return disan;
	}

	public void setDisan(String disan) {
		this.disan = disan;
	}

	public String getPu() {
		return pu;
	}

	public void setPu(String pu) {
		this.pu = pu;
	}

	public int getExp1() {
		return exp1;
	}

	public void setExp1(int exp1) {
		this.exp1 = exp1;
	}

	public int getZhenqi1() {
		return zhenqi1;
	}

	public void setZhenqi1(int zhenqi1) {
		this.zhenqi1 = zhenqi1;
	}

	public int getNeedGold() {
		return needGold;
	}

	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}

}
