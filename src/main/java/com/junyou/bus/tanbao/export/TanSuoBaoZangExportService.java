package com.junyou.bus.tanbao.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tanbao.entity.RefabuTanbao;
import com.junyou.bus.tanbao.service.RFBTanBaoService;



@Service
public class TanSuoBaoZangExportService {

	@Autowired
	private RFBTanBaoService rFBTanBaoService;
	
	
	public List<RefabuTanbao> initRefabuTanbao(Long userRoleId) {
		return rFBTanBaoService.initRefabuTanbao(userRoleId);
	} 
	
	public Object[] getRefbTanBaoInfo(Long userRoleId, Integer subId){
		return rFBTanBaoService.getRefbInfo(userRoleId, subId);
	} 
	
}
