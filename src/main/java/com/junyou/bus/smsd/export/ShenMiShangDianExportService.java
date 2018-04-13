package com.junyou.bus.smsd.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.smsd.entity.ShenmiShangdian;
import com.junyou.bus.smsd.server.ShenMiShangDianService;



@Service
public class ShenMiShangDianExportService {

	@Autowired
	private ShenMiShangDianService shenMiShangDianService;
	
	
	public List<ShenmiShangdian> initShenmiShangdian(Long userRoleId){
		return shenMiShangDianService.initShenmiShangdian(userRoleId);
	}
	
	public Object[] getRefbSMSDInfo(Long userRoleId, Integer subId){
		return shenMiShangDianService.getRefbInfo(userRoleId, subId);
	} 
	
}
