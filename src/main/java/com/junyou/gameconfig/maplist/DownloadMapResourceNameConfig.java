package com.junyou.gameconfig.maplist;


/**
 * 地图需要下载的资源配置对象
 *@author  DaoZheng Yuan
 *@created 2013-1-4下午6:47:53
 */
public class DownloadMapResourceNameConfig {

	private String id;
	
	private String mapId;
	
	private String mapResourceName;
	
	private String pathResourceName;
	private String monsterResourceName;
	
	private String itemResourceName;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMapId() {
		return mapId;
	}
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	public String getMapResourceName() {
		return mapResourceName;
	}
	public void setMapResourceName(String mapResourceName) {
		this.mapResourceName = mapResourceName;
	}
	public String getPathResourceName() {
		return pathResourceName;
	}
	public void setPathResourceName(String pathResourceName) {
		this.pathResourceName = pathResourceName;
	}
	public String getMonsterResourceName() {
		return monsterResourceName;
	}
	public void setMonsterResourceName(String monsterResourceName) {
		this.monsterResourceName = monsterResourceName;
	}
	public String getItemResourceName() {
		return itemResourceName;
	}
	public void setItemResourceName(String itemResourceName) {
		this.itemResourceName = itemResourceName;
	}
	
}
