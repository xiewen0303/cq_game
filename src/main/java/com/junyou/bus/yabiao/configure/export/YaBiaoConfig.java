package com.junyou.bus.yabiao.configure.export;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 
 * @description 押镖表配置 
 *
 * @author LiNing
 * @date 2015-03-13 13:51:43
 */
public class YaBiaoConfig {

	private Integer id;
	private String name;
	private Integer needmoney;
	private Integer level;
	private Integer speed;
	private Integer map;
	private String useitem;
	private ReadOnlyMap<String, Long> attributeMap;
	private boolean isGongg;
	private Integer xintiao;
	private int minHarm;
	private int rollHarm;
	/**
	 * 是否发送全服公告
	 * @return true:发送
	 */
	public boolean isGongg() {
		return isGongg;
	}

	public void setGongg(boolean isGongg) {
		this.isGongg = isGongg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 镖车Id
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 镖车传送所需的铜钱
	 * @return
	 */
	public Integer getNeedmoney() {
		return needmoney;
	}

	public void setNeedmoney(Integer needmoney) {
		this.needmoney = needmoney;
	}

	public int getMinHarm() {
		return minHarm;
	}

	public void setMinHarm(int minHarm) {
		this.minHarm = minHarm;
	}

	public int getRollHarm() {
		return rollHarm;
	}

	public void setRollHarm(int rollHarm) {
		this.rollHarm = rollHarm;
	}

	/**
	 * 镖车等级（类似于怪物的怪物等级）
	 * @return
	 */
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 移动频率（毫秒）
	 * @return
	 */
	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	/**
	 * 交付NPC所在地图
	 * @return
	 */
	public Integer getMap() {
		return map;
	}

	public void setMap(Integer map) {
		this.map = map;
	}
	/**
	 * 押镖所需道具（道具表中“id1”的id，不是"物品id"）
	 * @return
	 */
	public String getUseitem() {
		return useitem;
	}

	public void setUseitem(String useitem) {
		this.useitem = useitem;
	}
	
	public ReadOnlyMap<String, Long> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(ReadOnlyMap<String, Long> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public YaBiaoConfig copy(){
		return null;
	}

	public Integer getXintiao() {
		return xintiao;
	}

	public void setXintiao(Integer xintiao) {
		this.xintiao = xintiao;
	}
}