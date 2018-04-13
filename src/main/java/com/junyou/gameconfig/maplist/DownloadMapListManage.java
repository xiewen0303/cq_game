package com.junyou.gameconfig.maplist;

import java.util.HashMap;
import java.util.Map;

public class DownloadMapListManage {

	private static Map<String,DownloadMapResourceNameConfig> maplistResources = new HashMap<String, DownloadMapResourceNameConfig>();
	
	public static void addDownloadMapResourceNameConfig(DownloadMapResourceNameConfig config){
		maplistResources.put(config.getMapId(), config);
	}
	
	public static DownloadMapResourceNameConfig getDownloadMapResourceNameConfig(String mapId){
		return maplistResources.get(mapId);
	}
	
	public static Object[] getMapListMapIds(){
		return maplistResources.keySet().toArray();
	}
	
	public static void clearMaplistResources(){
		maplistResources.clear();
		
		maplistResources = null;
	}
}
