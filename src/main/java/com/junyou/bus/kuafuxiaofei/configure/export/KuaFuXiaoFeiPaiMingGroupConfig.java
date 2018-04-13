package com.junyou.bus.kuafuxiaofei.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

public class KuaFuXiaoFeiPaiMingGroupConfig {
	private String bg;
	private String desc;
	private int maxRank = 0;
	private int minXiaoFei ;
	//第一名特殊奖励限制
	private int top1SpecilLimit;
	//特殊奖励描述
	private String specilDesc;
	//第一名特殊奖励
	private List<GoodsConfigureVo> specilItem = new ArrayList<GoodsConfigureVo>();
	private Map<String, GoodsConfigureVo> specilItemMap = new HashMap<String, GoodsConfigureVo>();
	private Object[] clientSpecilItem;
	
	private Map<Integer, KuaFuXiaoFeiPaiMingConfig> configMap = new HashMap<Integer, KuaFuXiaoFeiPaiMingConfig>();

	private Object[] vo;
	
	public Object[] getClientSpecilItem() {
		return clientSpecilItem;
	}

	public void setClientSpecilItem(Object[] clientSpecilItem) {
		this.clientSpecilItem = clientSpecilItem;
	}

	public int getTop1SpecilLimit() {
		return top1SpecilLimit;
	}

	public void setTop1SpecilLimit(int top1SpecilLimit) {
		this.top1SpecilLimit = top1SpecilLimit;
	}

	public String getSpecilDesc() {
		return specilDesc;
	}

	public void setSpecilDesc(String specilDesc) {
		this.specilDesc = specilDesc;
	}

	public List<GoodsConfigureVo> getSpecilItem() {
		return specilItem;
	}

	public void setSpecilItem(List<GoodsConfigureVo> specilItem) {
		this.specilItem = specilItem;
	}

	public Map<String, GoodsConfigureVo> getSpecilItemMap() {
		return specilItemMap;
	}

	public void setSpecilItemMap(Map<String, GoodsConfigureVo> specilItemMap) {
		this.specilItemMap = specilItemMap;
	}

	public int getMinXiaoFei() {
		return minXiaoFei;
	}

	public void setMinXiaoFei(int minXiaoFei) {
		this.minXiaoFei = minXiaoFei;
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

	public Map<Integer, KuaFuXiaoFeiPaiMingConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, KuaFuXiaoFeiPaiMingConfig> configMap) {
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

	public KuaFuXiaoFeiPaiMingConfig getConfig(Integer id) {
		return configMap.get(id);
	}

	public int getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	public KuaFuXiaoFeiPaiMingConfig getConfigByRank(int rank){
		return configMap.get(rank);
	}

}
