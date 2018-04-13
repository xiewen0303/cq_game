package com.junyou.bus.superduihuan.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.superduihuan.entity.RefabuSuperDuihuan;
import com.junyou.bus.superduihuan.service.RfbSuperDuihuanService;

@Service
public class RfbSuperDuihuanExportService {
	
	@Autowired
	private RfbSuperDuihuanService superDuihuanService;
	
	public List<RefabuSuperDuihuan> initRefabuSuperDuihuan(Long userRoleId) {
		return superDuihuanService.initRefabuSuperDuihuan(userRoleId);
	}
	
	
	public Object[] getRfbInfo(Long userRoleId,Integer subId){
		return superDuihuanService.getInfo(userRoleId, subId);
	}
	public Object[] getSuperDuihuanStates(Long userRoleId,Integer subId){
		return superDuihuanService.getStates(userRoleId, subId);
	}

}
