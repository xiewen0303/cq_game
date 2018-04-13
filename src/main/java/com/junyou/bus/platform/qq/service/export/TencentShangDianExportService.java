package com.junyou.bus.platform.qq.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.entity.TencentShangdian;
import com.junyou.bus.platform.qq.service.TencentShangDianService;

/**
 * @author zhongdian
 * 2015-12-17 上午10:40:55
 */
@Service
public class TencentShangDianExportService {

	
	@Autowired
	private TencentShangDianService tencentShangDianService;
	
	public List<TencentShangdian> initTencentShangdians(Long userRoleId){
		return tencentShangDianService.initTencentShangdians(userRoleId);
	}
	
}
