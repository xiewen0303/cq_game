package com.junyou.bus.active.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.active.service.DingShiActiveService;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;

@Service
public class DingShiActiveExportService {
	@Autowired
	private DingShiActiveService dingShiActiveService;
	
	public void initDayActive(){
		dingShiActiveService.initDayActive();
	}
	
	/**
	 * 获取活动（如果不在活动期间内，返回null）
	 * @param activeId
	 * @return
	 */
	public DingShiActiveConfig getActiveInTime(Integer activeId){
		return dingShiActiveService.getActiveInTime(activeId);
	}
}
