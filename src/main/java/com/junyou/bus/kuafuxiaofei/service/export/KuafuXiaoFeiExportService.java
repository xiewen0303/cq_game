package com.junyou.bus.kuafuxiaofei.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuxiaofei.service.KuafuXiaoFeiRankService;

@Service
public class KuafuXiaoFeiExportService {
	@Autowired
	private KuafuXiaoFeiRankService kuafuXiaoFeiService;

	public Object[] getRfbInfo(Long userRoleId, Integer subId) {
		return kuafuXiaoFeiService.getInfo(userRoleId, subId);
	}

	public Object[] getKuafuXiaoFeiRankStates(Long userRoleId, Integer subId) {
		return kuafuXiaoFeiService.getRankInfo(false, userRoleId, 0, subId,0, 10);
	}
	public void kuafuXiaoFeiRankDayJob() {
		kuafuXiaoFeiService.kuafuXiaoFeiDayJob();
	}
}
