package com.junyou.stage.model.stage.other;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapLineConfig;
import com.junyou.stage.service.StageService;

public class OtherElementCreateHanle {
	
	public static DiTuConfigExportService diTuConfigExportService;

	@Autowired
	private StageService stageService;
	
	@Autowired
	public void setDiTuConfigExportService(
			DiTuConfigExportService diTuConfigExportService) {
		OtherElementCreateHanle.diTuConfigExportService = diTuConfigExportService;
	}



	public void execute(){
		
		List<MapLineConfig> maplineConfigs = diTuConfigExportService.loadMapLineAll();
		
//		for( MapLineConfig maplineConfig : maplineConfigs ){
//			String[] stageIds = stageControllExternalExportService.getStageIdsByMapId(maplineConfig.getMapId());
//			if( stageIds != null && stageIds.length > 0 ){
//				
//				for( String stageId : stageIds ){
//					stageService.otherElementCreate(stageId);
//				}
//				
//			}
//		}
		
	}
}
