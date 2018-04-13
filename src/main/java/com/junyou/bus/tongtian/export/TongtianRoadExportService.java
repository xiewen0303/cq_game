package com.junyou.bus.tongtian.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tongtian.entity.RoleTongtianRoad;
import com.junyou.bus.tongtian.service.TongtianRoadService;


@Service
public class TongtianRoadExportService {

	@Autowired
	private TongtianRoadService tongtianLoadService;
	
	/**
	 * 初始化数据
	 * @param userRoleId
	 * @return
	 */
	public RoleTongtianRoad initData(Long userRoleId) {
		return   tongtianLoadService.initData(userRoleId);
	}
	/**
	 * 初始化属性
	 */
	public Map<String, Long> initAttrMap(Long userRoleId){
		
		return tongtianLoadService.initAttrMap(userRoleId);
	}
}
