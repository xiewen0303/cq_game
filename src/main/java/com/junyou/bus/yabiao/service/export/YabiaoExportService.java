package com.junyou.bus.yabiao.service.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.yabiao.entity.Yabiao;
import com.junyou.bus.yabiao.service.YabiaoService;

@Service
public class YabiaoExportService {
	@Autowired
	private YabiaoService yabiaoService;

	public void offilineHandler(Long userRoleId) {
		yabiaoService.offilineHandler(userRoleId);
	}
	public Yabiao initYabiao(Long userRoleId){
		return yabiaoService.initYabiao(userRoleId);
	}
	public void calDayFubenResource(Map<String,Map<String,Integer>> map,Long userRoleId){
		yabiaoService.calDayFubenResource(map, userRoleId);
	}
	
	public Yabiao getYaBiao(Long userRoleId){
		return yabiaoService.getYabiao(userRoleId);
	}
}
