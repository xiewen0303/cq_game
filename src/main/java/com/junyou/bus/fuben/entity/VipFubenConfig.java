package com.junyou.bus.fuben.entity;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;


public class VipFubenConfig extends AbsFubenConfig{
	private int vip;
	private int mapId;
	private int count;
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	@Override
	public short getExitCmd() {
		return InnerCmdType.B_EXIT_FUBEN;
	}
	@Override
	public int getMapType() {
		return MapType.FUBEN_MAP;
	}
	@Override
	public boolean isAutoProduct() {
		return true;
	}
	@Override
	public int getFubenType() {
		return GameConstants.FUBEN_TYPE_VIP;
	}

}
