package com.junyou.bus.kuafuarena1v1.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1MatchService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.utils.KuafuConfigPropUtil;

@Service
public class KuafuArena1v1MatchExportService {
	@Autowired
	private KuafuArena1v1MatchService kuafuArenaMatchService;

	public void init() {
		if (KuafuConfigPropUtil.isKuafuServer() && KuafuManager.isMatchServer()) {
			kuafuArenaMatchService.startMatchThread();
		}

	}
}
