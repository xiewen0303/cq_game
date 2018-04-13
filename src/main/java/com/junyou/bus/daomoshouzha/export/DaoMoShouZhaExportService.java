package com.junyou.bus.daomoshouzha.export;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.daomoshouzha.service.DaoMoShouZhaService;



@Service
public class DaoMoShouZhaExportService {

	@Autowired
	private DaoMoShouZhaService daoMoShouZhaService;
	
	
	
	public Object[] getRefbDaoMoInfo(Long userRoleId, Integer subId){
		return daoMoShouZhaService.getRefbInfo(userRoleId, subId);
	} 
	
}
