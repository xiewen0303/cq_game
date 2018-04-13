package com.junyou.bus.huanhua.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.huanhua.entity.RoleHuanhua;
import com.junyou.bus.huanhua.service.HuanHuaService;

@Service
public class HuanhuaExportService {
	@Autowired
	private HuanHuaService huanhuaService;
	

	public List<RoleHuanhua> initRoleHuanhua(Long userRoleId) {
		return huanhuaService.initRoleHuanhua(userRoleId);
	}
	
	public List<Integer> getRoleHuanhuaConfigList(Long userRoleId, final Integer type){
		return huanhuaService.getRoleHuanhuaConfigList(userRoleId, type);
	}

}
