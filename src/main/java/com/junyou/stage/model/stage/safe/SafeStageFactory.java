package com.junyou.stage.model.stage.safe;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.PointType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.core.stage.aoi.AoiFactory;
import com.junyou.stage.model.stage.aoi.AoiMsgListenerFactory;
import com.junyou.stage.model.stage.aoi.AoiStage;

/**
 * 安全地图创建工厂
 * @author LiuYu
 * @date 2015-3-21 下午6:56:19
 */
public class SafeStageFactory {
	
	public static IStage create(Long roleId, String stageId,MapConfig mapConfig,Integer mapType){
		//整个副本只有一个AOI
		AOIManager aoiManager = new AOIManager(
				mapConfig.getPathInfoConfig().getXsWidth(), 
				mapConfig.getPathInfoConfig().getXsHeight(),
				mapConfig.getPathInfoConfig().getXsWidth(), 
				mapConfig.getPathInfoConfig().getXsHeight(), 
				PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		

		StageType stageType = null;
		if(mapType.equals(MapType.JINGJI_SAFE_MAP)){//竞技场安全地图
			stageType = StageType.SAFE_STAGE;
		}else if(mapType.equals(MapType.KUAFU_SAFE_MAP)){//跨服安全地图
			stageType = StageType.KUAFU_SAFE_STAGE;
		}else if(mapType.equals(MapType.NEW_SAFE_MAP)){//跨服安全地图
			stageType = StageType.START_GAME_SAFE_STAGE;
		}else if(mapType.equals(MapType.KF_JINGJI_SAFE_MAP)){//跨服竞技安全地图
			stageType = StageType.KF_JINGJI_STAGE;
		}
		AoiStage aoiStage = new SafeStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(),stageType);
		aoiStage.setCanPk(false);
		aoiStage.setAddPk(false);
		return aoiStage;
	}
}