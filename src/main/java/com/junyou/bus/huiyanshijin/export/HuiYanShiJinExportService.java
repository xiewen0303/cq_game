package com.junyou.bus.huiyanshijin.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.huiyanshijin.service.HuiYanShiJingService;

/**
 * @author zhongdian
 * 2016-3-24 下午1:57:31
 */
@Service
public class HuiYanShiJinExportService {

	
	@Autowired
	private HuiYanShiJingService huiYanShiJingService;
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return huiYanShiJingService.getRefbInfo(userRoleId, subId);
	}
}
