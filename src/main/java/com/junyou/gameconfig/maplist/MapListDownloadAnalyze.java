package com.junyou.gameconfig.maplist;

import java.io.File;
import java.util.Map;

import com.junyou.gameconfig.utils.GameConfigUtil;


public class MapListDownloadAnalyze{
	
	public final static String MAPLIST_FILE_DIR = "config/game/sys_config/";
	
	/**
	 * 配置名
	 */
	private static String configureName = "maplist.bin";
	
	@SuppressWarnings("unchecked")
	public static void analyze() {
		File file = new File(ClassLoader.getSystemResource("").getFile()+MAPLIST_FILE_DIR,configureName);
		
		Object[] maplistData = GameConfigUtil.getResource(file);
		
		for(Object obj : maplistData){
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			createDownloadMapResourceNameConfig(tmp);
		}
		
	}
	
	
	private static void createDownloadMapResourceNameConfig(Map<Object, Object> tmp){
		DownloadMapResourceNameConfig config = new DownloadMapResourceNameConfig(); 
		
		String id = (String)tmp.get("id");//id
		String mapId = (String)tmp.get("mapid");//mapid
		String monster = (String)tmp.get("monster");//monster
		String path = (String)tmp.get("path");//path
		
		config.setId(id);
		config.setMapId(mapId);
		config.setMonsterResourceName(monster);
		config.setPathResourceName(path);
		
		DownloadMapListManage.addDownloadMapResourceNameConfig(config);
	}

}
