/**
 * 
 */
package com.junyou.stage.model.stage.aoi;

import java.util.Collection;

import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;

/**
 * 特殊地图
 * @author DaoZheng Yuan
 * 2014-8-5 下午2:48:20
 */
public class AoiSpecialStage extends AoiStage {
	

	/**
	 * 
	 */
	public AoiSpecialStage(String id, Integer mapId, Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig,StageType stageType) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,stageType);
	}
	
	
	@Override
	public Collection<IStageElement> getAroundEnemies(IStageElement fighter) {
		return null;
	}
	
	
	public Collection<IStageElement> getAroundRedRoles(IStageElement fighter) {
		return null;
	}
	
}
