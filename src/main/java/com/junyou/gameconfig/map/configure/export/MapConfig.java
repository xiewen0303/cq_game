package com.junyou.gameconfig.map.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.export.PathInfoCopy;
import com.junyou.gameconfig.export.PathNodeCopy;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.gameconfig.export.ResourceRefreshConfig;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-8下午2:00:55
 */
public class MapConfig{
	
	private int mapId;
	
	private String mapName;
	
	private PathInfoConfig pathInfoConfig = new PathInfoConfig();
	
	private PathInfoCopy pathInfoCopy;
	
	/**
	 * 地图里的相关资源
	 */
	private List<ResourceRefreshConfig> resourceRefreshConfigs = new ArrayList<ResourceRefreshConfig>();
	
	
	/**
	 * 地图内所有怪物
	 */
	private Map<String,Integer> mapAllMonsters;
	
	
	
	
	/**
	 * 获取地图内所有怪物
	 * @return
	 */
	public Map<String, Integer> getMapAllMonsters() {
		return mapAllMonsters;
	}

	public void addMapAllMonsters(String monsterId, Integer count) {
		if(mapAllMonsters == null){
			mapAllMonsters = new HashMap<String, Integer>();
			
			mapAllMonsters.put(monsterId, count);
		}else{
			int tmpCount = count;
			if(mapAllMonsters.containsKey(monsterId)){
				tmpCount = tmpCount + mapAllMonsters.get(monsterId);
			}
			
			mapAllMonsters.put(monsterId, tmpCount);
		}
		
	}

	/**
	 * 地图里的相关资源
	 * @return
	 */
	public List<ResourceRefreshConfig> getResourceRefreshConfigs() {
		return resourceRefreshConfigs;
	}

	public void setResourceRefreshConfigs(
			List<ResourceRefreshConfig> resourceRefreshConfigs) {
		this.resourceRefreshConfigs = resourceRefreshConfigs;
	}

	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public PathInfoConfig getPathInfoConfig() {
		return pathInfoConfig;
	}

	public void setPathInfoConfig(PathInfoConfig pathInfoConfig) {
		this.pathInfoConfig = pathInfoConfig;
	}
	
	/**
	 * 是否可走点
	 * @param x
	 * @param y
	 * @return true:可走
	 */
	public boolean isCanUsePoint(int x,int y){
		if(pathInfoCopy == null){
			pathInfoCopy = new PathInfoCopy(pathInfoConfig);
		}
		
		PathNodeCopy pathNode = pathInfoCopy.getPathNode(x, y, PathNodeSize._1X);
		if(null == pathNode){
			return false;
		}
		
		return true;
	}

	public MapConfig copy() {
		MapConfig smc = new MapConfig();
		
		smc.setMapId(this.getMapId());
		smc.setResourceRefreshConfigs(getResourceRefreshConfigs());
		smc.setPathInfoConfig(this.getPathInfoConfig());
		smc.setMapName(this.getMapName());
		
		return smc;
	}

}
