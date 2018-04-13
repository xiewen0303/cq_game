package com.junyou.gameconfig.export;

import com.junyou.gameconfig.export.teleport.TeleportChecker;

public class WaypointTeleportConfig{

	private String id;
	
	private String mapId;
	
	private int x;
	
	private int y;
	
	private TeleportChecker teleportChecker;
	

	public String getId() {
		return id;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTeleportChecker(TeleportChecker checker) {
		this.teleportChecker = checker;
	}
	
	public TeleportChecker getTeleportChecker(){
		return this.teleportChecker;
	}

	

}
