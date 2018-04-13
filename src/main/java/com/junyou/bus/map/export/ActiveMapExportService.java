package com.junyou.bus.map.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.map.configure.service.ActiveMapService;
import com.junyou.stage.model.core.stage.IStage;

/**
 * @author LiuYu
 * 2015-8-5 下午9:00:20
 */
@Service
public class ActiveMapExportService {
	@Autowired
	private ActiveMapService activeMapService;
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return activeMapService.getRefbInfo(userRoleId, subId);
	}
	
	public IStage createStage(String stageId,Integer subId){
		return activeMapService.createStage(stageId, subId);
	}
}
