package com.junyou.bus.fuben.entity;

public class BaguaZhenmenInfo {
	
	public BaguaZhenmenInfo(int zhenmenId) {
		super();
		this.zhenmenId = zhenmenId;
	}
	private int zhenmenId;
	private boolean open;
	public int getZhenmenId() {
		return zhenmenId;
	}
	public void setZhenmenId(int zhenmenId) {
		this.zhenmenId = zhenmenId;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	
}
