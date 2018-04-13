package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.ArrayList;
import java.util.List;


/**
 * PK相关配置
 *@author  LiNing
 *@created 2013-10-24上午6:22:26
 */
public class PataPublicConfig extends AdapterPublicConfig{
	
	private int mapId;
	private int freeTime;
	private List<Integer[]> position = new ArrayList<>();
	private int buyGold;
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public List<Integer[]> getPosition() {
		return new ArrayList<>(position);
	}
	public void setPosition(Integer[] position) {
		this.position.add(position);
	}
	public int getFreeTime() {
		return freeTime;
	}
	public void setFreeTime(int freeTime) {
		this.freeTime = freeTime;
	}
	public int getBuyGold() {
		return buyGold;
	}
	public void setBuyGold(int buyGold) {
		this.buyGold = buyGold;
	}
	
}
