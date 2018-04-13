package com.junyou.bus.jingji.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.jingji.entity.RoleJingjiDuihuan;
import com.junyou.bus.jingji.service.JingjiService;

@Service
public class JingjiExportService {
	@Autowired
	private JingjiService jingjiService;
	
	public void init(){
		jingjiService.init();
	}
	
	public List<RoleJingjiDuihuan> initRoleJingjiDuihuan(Long userRoleId) {
		return jingjiService.initRoleJingjiDuihuan(userRoleId);
	}
}
