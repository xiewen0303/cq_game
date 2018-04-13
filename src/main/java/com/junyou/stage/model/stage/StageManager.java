/**
 * 
 */
package com.junyou.stage.model.stage;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapLineConfig;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.aoi.AoiStageFactory;
import com.junyou.utils.KuafuConfigPropUtil;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-25下午1:48:29
 */
public class StageManager {
	
	private static ConcurrentMap<String,IStage> stageMap = new ConcurrentHashMap<>();
	
	//皇城场景ID
	private static String KING_STAGE_ID;
	
	private static DiTuConfigExportService diTuConfigExportService;
	private static MapConfigExportService mapConfigExportService;
	private static AoiStageFactory aoiStageFactory;
	
	
	@Autowired
	public void setDiTuConfigExportService(
			DiTuConfigExportService diTuConfigExportService) {
		StageManager.diTuConfigExportService = diTuConfigExportService;
	}
	
	@Autowired
	public void setMapConfigExportService(
			MapConfigExportService mapConfigExportService) {
		StageManager.mapConfigExportService = mapConfigExportService;
	}

	@Autowired
	public void setAoiStageFactory(AoiStageFactory aoiStageFactory) {
		StageManager.aoiStageFactory = aoiStageFactory;
	}

	/**
	 * @param
	 * @return 
	 */
	public static IStage getStage(String stageId) {
		IStage stage = stageMap.get(stageId);
		
		return stage;
	}
	
	
	/**
	 * 获取皇城场景
	 * @return
	 */
	public static IStage getKingStage(){
		return stageMap.get(KING_STAGE_ID);
	}
	
	/**
	 * 场景是否存在
	 */
	public static boolean exist(String stageId){
		return null != getStage(stageId);
		
	}
	
	/**
	 * 初始化静态地图
	 * @param
	 */
	public void init(){
		
		List<MapLineConfig> maplineConfigs = diTuConfigExportService.loadMapLineAll();
		
		for(MapLineConfig config : maplineConfigs){
			
			Integer mapline = config.getDefaultLine();
			
			for(int i = 1 ; i <= mapline ; i++){
				
				String stageId = StageUtil.getStageId(config.getMapId(), i);
				Integer lineNo = i;
				MapConfig mapConfig = mapConfigExportService.load(config.getMapId());
				
				if(mapConfig != null && !isBootNoInit(config.getMapId())){
					IStage stage = aoiStageFactory.create(stageId, lineNo, mapConfig);
					stageMap.put(stage.getId(), stage);
					stage.getStageProduceManager().produceAll();
				}
			}
			
		}
	}

	
	/**
	 * 是否要启动不初始化地图 (true:是)
	 * @param diTuConfig
	 */
	private boolean isBootNoInit(int mapId){
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(mapId);
		
		if(KuafuConfigPropUtil.isKuafuServer()){
			//跨服服务器上,非跨服地图就不创建了
			if(!DiTuConfigUtil.isKuaFuMap(diTuConfig.getType())){
				return true;
			}
		}else{
			//非跨服服务器上,跨服地图就不创建了
			if(DiTuConfigUtil.isKuaFuMap(diTuConfig.getType())){
				return true;
			}
		}
		boolean isNoInit = diTuConfig.isBootNoInit();
		
		if(GameServerContext.getGameAppConfig().isDebug()){
			if(isNoInit){
                ChuanQiLog.error("Warning map no init mapName={},mapId={}", diTuConfig.getName(), mapId);
			}
		}
		return isNoInit;
	}
	
	
	
	/**
	 * 创建场景
	 * @throws Exception 场景无法创建
	 */
	public static boolean checkAndCreateStage(String stageId,Integer mapId, Integer lineNo) throws Exception {
		
		
		synchronized(stageMap){
			
			IStage stage = stageMap.get(stageId);
			if(null == stage){
				
				stage = aoiStageFactory.create(stageId,lineNo,mapConfigExportService.load(mapId));
				if(stage == null){
					throw new Exception();
				}
				stageMap.put(stage.getId(), stage);
				
				stage.getStageProduceManager().produceAll();
				
				return true;
			}
			
		}
		
		return false;
		
	}

	public static void addStageCopy(IStage copy) {
		stageMap.put(copy.getId(), copy);
	}

	public static void removeCopy(IStage stage) {
		stageMap.remove(stage.getId());
	}
	
	public static Collection<IStage> getAllStage(){
		return stageMap.values();
	}
	
}
