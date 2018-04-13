package com.junyou.bus.cuxiao.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.cuxiao.entity.RoleCuxiao;
import com.junyou.bus.cuxiao.service.CuxiaoService;

@Service
public class CuxiaoExportService {

	@Autowired
	private CuxiaoService cuxiaoService;
	
	public List<RoleCuxiao> initRoleCuxiao(Long userRoleId) {
		
		return cuxiaoService.initRoleCuxiao(userRoleId);
	}
}
