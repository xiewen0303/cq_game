package com.junyou.bus.platform.yuenan.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.yuenan.entity.YuenanYaoqing;
import com.junyou.bus.platform.yuenan.service.YuenanYaoqingService;

/**
 * @author zhongdian
 * 2015-12-3 上午11:24:49
 */
@Service
public class YuenanExportService {

	
	@Autowired
	private YuenanYaoqingService yuenanYaoqingService;
	
	public List<YuenanYaoqing> initYaoqings(Long userRoleId){
		return yuenanYaoqingService.initYuenanYaoqings(userRoleId);
	}
}
