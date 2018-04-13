/**
 * 
 */
package com.junyou.stage.model.stage.aoi;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.analysis.StageInfoConfig;
import com.junyou.gameconfig.export.ResourceRefreshConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.DefaultStageProduceManager;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.PointType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.core.stage.aoi.AoiFactory;
import com.junyou.stage.model.stage.MonsterProduceTeam;
import com.junyou.stage.service.TerritoryStageService;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-14下午2:41:11
 */
@Component
public class AoiStageFactory {
	
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private TerritoryStageService territoryStageService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
//	@Autowired
//	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

	public AoiStage create(String stageId,Integer lineNo,MapConfig mapConfig){
		//场景创建基本配置
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
												mapConfig.getPathInfoConfig().getXsWidth(), 
												mapConfig.getPathInfoConfig().getXsHeight(), 
												PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapConfig.getMapId());
		if(config == null || !config.isCanCreateAoiStage()){
			ChuanQiLog.error("野外场景创建失败，当前地图id:{}  config:{}  isCanCreateAoiStage{}",mapConfig.getMapId(),config,config.isCanCreateAoiStage());
			return null;//该地图不可创建
		}
		AoiStage aoiStage = new AoiStage(stageId,mapConfig.getMapId(),lineNo, aoiManager, mapConfig.getPathInfoConfig(),StageType.NOMAL);
		
		aoiStage.setCanPk(!config.isSafeMap());
		aoiStage.setAddPk(config.isAddPk());
		
		aoiStage.setStageProduceManager(new DefaultStageProduceManager(aoiStage));
		
		/*******怪物资源*********/
		List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
		if( refreshConfigs != null ){
			for( ResourceRefreshConfig refreshConfig : refreshConfigs ){
				MonsterConfig monsterConfig = monsterExportService.load(refreshConfig.getResourceId());
				if(monsterConfig != null && monsterConfig.getMonsterType() == 5 && lineNo > 1){
					continue;
				}
				
				boolean isXunlou = monsterConfig.isXunluo();
				//怪物不可移动也不可随机巡逻
				if(isXunlou && monsterConfig.isNoMove()){
					isXunlou = false;
				}
				
				MonsterProduceTeam monsterTeam = new MonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(),isXunlou);
				aoiStage.getStageProduceManager().addElementProduceTeam(monsterTeam);
			}
		}
		
		/*******采集资源*********/
//		ZiYuanMapConfig ziYuanMapConfig = ziYuanConfigExportService.loadByMapId(mapConfig.getMapId());
//		if(ziYuanMapConfig != null){
//			List<ZiYuanConfig> ziYuans = ziYuanMapConfig.getZiYuans() ;
//			
//			for (ZiYuanConfig ziYuanConfig : ziYuans) {
//				
//				//类型为1的是 需要在地图中,刷新出来的,其它不刷新
//				if(ziYuanConfig.getType() == 1){
//					CollectProduceTeam collectTeam = new CollectProduceTeam("ct"+ziYuanConfig.getId(), ElementType.COLLECT, ziYuanConfig.getZuobiao().size(), ziYuanConfig.getId().toString(), ziYuanConfig.getZuobiao(), ziYuanConfig.getRefreshTime());
//					aoiStage.getStageProduceManager().addElementProduceTeam(collectTeam);
//				}
//			}
//		}
		/*******初始化领地战旗子*********/
		territoryStageService.initQizi(aoiStage);
		return aoiStage;
	}

}
