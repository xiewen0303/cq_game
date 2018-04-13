package com.junyou.bus.stagecontroll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapLineConfig;
import com.kernel.spring.container.DataContainer;

@Component
public class StageStatisticInit {
	
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	
	public void init(){
		
		List<MapLineConfig> maplineConfigs =  diTuConfigExportService.loadMapLineAll();
		
		for(MapLineConfig config : maplineConfigs){
			
			if(config.getDefaultLine() >= 1){
				StageStatistic stageStatistic = new StageStatistic(config);
				dataContainer.putData(GameConstants.DATA_CONTAINER_STAGELINE, config.getMapId().toString(), stageStatistic);
			}
		}
	}

}
