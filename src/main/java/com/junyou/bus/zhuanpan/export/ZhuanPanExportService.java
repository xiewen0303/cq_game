package com.junyou.bus.zhuanpan.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.zhuanpan.entity.RefabuZhuanpan;
import com.junyou.bus.zhuanpan.server.ZhuanPanService;



@Service
public class ZhuanPanExportService {

	@Autowired
	private ZhuanPanService zhuanPanService;
	
	
	public List<RefabuZhuanpan> initRefabuZhuanpan(Long userRoleId){
		return zhuanPanService.initRefabuZhuanpan(userRoleId);
	}
	
	public Object[] getRefbZPInfo(Long userRoleId, Integer subId){
		return zhuanPanService.getRefbInfo(userRoleId, subId);
	} 
	
}
