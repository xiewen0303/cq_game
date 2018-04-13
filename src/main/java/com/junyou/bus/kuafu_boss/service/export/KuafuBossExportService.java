package com.junyou.bus.kuafu_boss.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafu_boss.service.KuafuBossService;

@Service
public class KuafuBossExportService {
	@Autowired
	private KuafuBossService kuafuBossService;
	
	public void onlineHandle(Long userRoleId){
		kuafuBossService.onlineHandle(userRoleId);
	}

}
