package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.Map;

import com.junyou.bus.fuben.entity.JianzhongFubenConfig;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 魂魄
 * 
 * @author lxn
 * 
 */
public class YaoshenHunpoPublicConfig extends AdapterPublicConfig {
	private int open;
	private int jingQi; // 精气数量
	private Map<String, Integer> item;
	private int mapId;
	private int time; // 副本持续时间（秒）
	private ReadOnlyMap<String, Integer> jiangItem; // 通过奖励

	private JianzhongFubenConfig jianzhongFubenConfig;
	
	
	public JianzhongFubenConfig getJianzhongFubenConfig() {
		return jianzhongFubenConfig;
	}

	public void setJianzhongFubenConfig(JianzhongFubenConfig jianzhongFubenConfig) {
		this.jianzhongFubenConfig = jianzhongFubenConfig;
	}

	public ReadOnlyMap<String, Integer> getJiangItem() {
		return jiangItem;
	}

	public void setJiangItem(ReadOnlyMap<String, Integer> jiangItem) {
		this.jiangItem = jiangItem;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Map<String, Integer> getItem() {
		return item;
	}

	public void setItem(Map<String, Integer> item) {
		this.item = item;
	}

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public int getJingQi() {
		return jingQi;
	}

	public void setJingQi(int jingQi) {
		this.jingQi = jingQi;
	}

}