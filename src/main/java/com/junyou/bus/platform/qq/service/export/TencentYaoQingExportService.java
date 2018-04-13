package com.junyou.bus.platform.qq.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.entity.TencentYaoqingLingqu;
import com.junyou.bus.platform.qq.service.TencentYaoQingService;

/**
 * @author zhongdian
 * 2015-12-30 下午3:16:10
 */
@Service
public class TencentYaoQingExportService {

	@Autowired
	private TencentYaoQingService tencentYaoQingService;
	
	public void insertTencentYaoQing(String userId,String iopenId){
		tencentYaoQingService.insertYaoQing(userId, iopenId);
	}
	
	public List<TencentYaoqingLingqu> initTencentYaoqingLingqus(Long userRoleId){
		return tencentYaoQingService.initTencentYaoqingLingqus(userRoleId);
	}
}
