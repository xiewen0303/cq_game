package com.junyou.bus.lj.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.junyou.bus.lj.entity.RoleLj;
import com.junyou.bus.lj.service.LJService;

@Service
public class LJExportService {
	
	@Autowired
	private LJService lJService;
	
	public RoleLj initRoleLJ(Long userRoleId){
		return lJService.initRoleLJ(userRoleId);
	}

	public Object[] getRoleLjInfo(Long userRoleId) {
		return lJService.getInfo(userRoleId);
	}
}
