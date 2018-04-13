package com.junyou.bus.firstChargeRebate.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.firstChargeRebate.entity.RefabuFirstChargeRebate;
import com.junyou.bus.firstChargeRebate.server.RefabuFirstChargeRebateService;

/**
 * 
 *@Description 首冲返利对外访问处理  
 *@Author Yang Gao
 *@Since 2016-6-6
 *@Version 1.1.0
 */
@Service
public class RefabuFirstChargeRebateExportService {

	@Autowired
	private RefabuFirstChargeRebateService refabuFirstChargeRebateService;
	
	public List<RefabuFirstChargeRebate> initRefabuFirstChargeRebate(Long userRoleId){
		return refabuFirstChargeRebateService.initRefabuFirstChargeRebate(userRoleId);
	}
	
	public boolean isActivityValid(Long userRoleId, Integer subId){
	    return refabuFirstChargeRebateService.isIntoActivity(userRoleId, subId);
	}
	
	public Object[] getRefbFirstChargeRebateInfo(Long userRoleId, Integer subId){
		return refabuFirstChargeRebateService.getRefbInfo(userRoleId, subId);
	}
	
}
