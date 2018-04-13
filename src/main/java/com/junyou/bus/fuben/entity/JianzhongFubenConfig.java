package com.junyou.bus.fuben.entity;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.utils.collection.ReadOnlyMap;

public class JianzhongFubenConfig extends AbsFubenConfig {
	private int mapId;
	private int openLevel;
	private ReadOnlyMap<String, Integer> jiangItem; //通关奖励
	
	public ReadOnlyMap<String, Integer> getJiangItem() {
		return jiangItem;
	}
	
	public int getOpenLevel() {
		return openLevel;
	}
	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
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

	 

	@Override
	public short getExitCmd() {
		return InnerCmdType.B_EXIT_JIANZHONG_FUBEN;
	}

	@Override
	public int getMapType() {
		return MapType.JIANZHONG_FUBEN_MAP;
	}

	@Override
	public boolean isAutoProduct() {
		return false;
	}

	@Override
	public int getFubenType() {
		return 0;
	}

}
