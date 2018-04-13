package com.junyou.bus.danfuchargerank.configure.export;

import java.util.HashMap;
import java.util.Map;

public class DanFuChongZhiPaiMingGroupConfig {
	private String bg;
	private String desc;
	private int maxRank = 0;
	private int minCharge =0;
	private int itemGold = 1;//充值达到多少元宝可领取物品
	private String goldItem; //充值达到条件领取的物品
	private int extraRank = 0;//排名前几的可以获得额外物品
	private Map<Integer, DanFuChongZhiPaiMingConfig> configMap = new HashMap<Integer, DanFuChongZhiPaiMingConfig>();
	private String itemtx;
	private String itemms;
	
	private Object[] vo;
	private Object[] txVo;
	
	
	public Object[] getTxVo() {
		return txVo;
	}

	public void setTxVo(Object[] txVo) {
		this.txVo = txVo;
	}

	public String getBg() {
		return bg;
	}

	public void setBg(String bg) {
		this.bg = bg;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Map<Integer, DanFuChongZhiPaiMingConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, DanFuChongZhiPaiMingConfig> configMap) {
		this.configMap = configMap;
	}

	private String md5Version;

	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Object[] getVo() {
		return vo;
	}

	public void setVo(Object[] vo) {
		this.vo = vo;
	}

	public DanFuChongZhiPaiMingConfig getConfig(Integer id) {
		return configMap.get(id);
	}

	public int getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	public DanFuChongZhiPaiMingConfig getConfigByRank(int rank){
		return configMap.get(rank);
	}

	public int getMinCharge() {
		return minCharge;
	}

	public void setMinCharge(int minCharge) {
		this.minCharge = minCharge;
	}

	public int getItemGold() {
		return itemGold;
	}

	public void setItemGold(int itemGold) {
		this.itemGold = itemGold;
	}

	public String getGoldItem() {
		return goldItem;
	}

	public void setGoldItem(String goldItem) {
		this.goldItem = goldItem;
	}

	public int getExtraRank() {
		return extraRank;
	}

	public void setExtraRank(int extraRank) {
		this.extraRank = extraRank;
	}

	public String getItemtx() {
		return itemtx;
	}

	public void setItemtx(String itemtx) {
		this.itemtx = itemtx;
	}

	public String getItemms() {
		return itemms;
	}

	public void setItemms(String itemms) {
		this.itemms = itemms;
	}

	
}
