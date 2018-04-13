package com.junyou.gameconfig.map.configure.export;


public class ChuanSongConfig{

	private String wayPointId;
	
	private int curMapId;
	private int curX;
	private int curY;
	
	private int targetMapId;
	private int targetX;
	private int targetY;
	
	public String getWayPointId() {
		return wayPointId;
	}

	public void setWayPointId(String wayPointId) {
		this.wayPointId = wayPointId;
	}

	public int getCurMapId() {
		return curMapId;
	}

	public void setCurMapId(Integer curMapId) {
		this.curMapId = curMapId;
	}

	public int getCurX() {
		return curX;
	}

	public void setCurX(int curX) {
		this.curX = curX;
	}

	public int getCurY() {
		return curY;
	}

	public void setCurY(int curY) {
		this.curY = curY;
	}

	public Integer getTargetMapId() {
		return targetMapId;
	}

	public void setTargetMapId(Integer targetMapId) {
		this.targetMapId = targetMapId;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}
}
