package com.junyou.bus.shenmo.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.shenmo.service.KuafuArena4v4MatchService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.utils.KuafuConfigPropUtil;

@Service
public class KuafuArena4v4MatchExportService {
	@Autowired
	private KuafuArena4v4MatchService kuafuArenaMatchService;

	public void init() {
		if (KuafuConfigPropUtil.isKuafuServer() && KuafuManager.isMatchServer()) {
			kuafuArenaMatchService.startMatchPartnerThread();
			kuafuArenaMatchService.startMatchOpponentThread();
		}

	}
}
