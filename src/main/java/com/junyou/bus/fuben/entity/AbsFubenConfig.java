package com.junyou.bus.fuben.entity;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 刘愚
 * @author LiuYu
 * @date 2015-3-12 下午4:58:04
 */
public abstract class AbsFubenConfig {
	private int id;
	private int time;
	private ReadOnlyMap<String, Integer> wantedMap;
	private boolean fuhuo;
	
	public Integer getId() {
		return id;
	}
	public Integer getTime() {
		return time;
	}
	/**
	 * 击杀名单(全击杀则为null)
	 * @return
	 */
	public ReadOnlyMap<String, Integer> getWantedMap() {
		return wantedMap;
	}
	/**
	 * 副本地图类型
	 */
	public abstract int getMapType();
	/**
	 * 副本类型
	 */
	public abstract int getFubenType();
	/**
	 * 是否可以复活
	 * @return
	 */
	public boolean canFuhuo() {
		return fuhuo;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void setWantedMap(Map<String, Integer> wantedMap) {
		this.wantedMap = new ReadOnlyMap<>(wantedMap);
	}
	public void setFuhuo(boolean fuhuo) {
		this.fuhuo = fuhuo;
	}
	public abstract short getExitCmd();
	/**
	 * 是否自动生产怪物
	 * @return
	 */
	public abstract boolean isAutoProduct();
	
	
}
