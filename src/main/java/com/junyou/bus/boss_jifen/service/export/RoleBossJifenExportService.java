package com.junyou.bus.boss_jifen.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.boss_jifen.entity.RoleBossJifen;
import com.junyou.bus.boss_jifen.service.RoleBossJifenService;

@Service
public class RoleBossJifenExportService {
	@Autowired
	private RoleBossJifenService bossJifenService;
	
	public List<RoleBossJifen> initRoleBossJifen(Long userRoleId) {
		return bossJifenService.initRoleBossJifen(userRoleId);
	}

	public Object[] addJifen(Long userRoleId, long addJifen) {
		return bossJifenService.addJifen(userRoleId,addJifen);
	}

	public Map<String, Long> getBossJifenAttr(Long id) {
		return bossJifenService.getRoleBossJifenAttrs(id);
	}
	
}
