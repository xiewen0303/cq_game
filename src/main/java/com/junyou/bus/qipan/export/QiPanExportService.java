package com.junyou.bus.qipan.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.qipan.entity.Qipan;
import com.junyou.bus.qipan.server.QiPanService;



@Service
public class QiPanExportService {

	@Autowired
	private QiPanService qiPanService;
	
	
	public List<Qipan> initQiPan(Long userRoleId){
		return qiPanService.initQiPan(userRoleId);
	}
	
	public Object[] getRefbQiPanInfo(Long userRoleId, Integer subId){
		return qiPanService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbQiPanLingQuStatus(Long userRoleId,Integer subId){
		return qiPanService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	
	public void rechargeYb(Long userRoleId,Long addVal){
		qiPanService.rechargeYb(userRoleId, addVal);
	}
}
