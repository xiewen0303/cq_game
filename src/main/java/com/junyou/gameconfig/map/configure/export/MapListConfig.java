/**
 * 
 */
package com.junyou.gameconfig.map.configure.export;


/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-9下午2:56:47
 */
public class MapListConfig{
	
	private int mapId;
	
	private String monsterPath;
	
	private String collectPath;
	
	private String pathInfoPath;
	
	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getMonsterPath() {
		return monsterPath;
	}

	public void setMonsterPath(String monsterPath) {
		this.monsterPath = monsterPath;
	}

	public String getCollectPath() {
		return collectPath;
	}

	public void setCollectPath(String collectPath) {
		this.collectPath = collectPath;
	}

	public String getPathInfoPath() {
		return pathInfoPath;
	}

	public void setPathInfoPath(String pathInfoPath) {
		this.pathInfoPath = pathInfoPath;
	}

}
