package com.junyou.bus.personal_boss.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.personal_boss.entity.RolePersonalBoss;
import com.junyou.bus.personal_boss.service.RolePersonalBossService;

@Service
public class RolePersonalBossExportService {
	@Autowired
	private RolePersonalBossService service;

	public List<RolePersonalBoss> initRolePersonalBoss(Long userRoleId) {
		return service.initRolePersonalBoss(userRoleId);
	}
	
	public Object[] info(long userRoleId) {
		return service.info(userRoleId);
	}
}
