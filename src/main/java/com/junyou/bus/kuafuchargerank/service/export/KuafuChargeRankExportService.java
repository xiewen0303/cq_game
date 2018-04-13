package com.junyou.bus.kuafuchargerank.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuchargerank.service.KuafuChargeRankService;

@Service
public class KuafuChargeRankExportService {
	@Autowired
	private KuafuChargeRankService kuafuChargeRankService;

	public Object[] getRfbInfo(Long userRoleId, Integer subId) {
		return kuafuChargeRankService.getInfo(userRoleId, subId);
	}

	public Object[] getKuafuChargeRankStates(Long userRoleId, Integer subId) {
		return kuafuChargeRankService.getRankInfo(false, userRoleId, 0, subId,
				0, 10);
	}
	public void kuafuChargeRankDayJob() {
		kuafuChargeRankService.kuafuChargeRankDayJob();
	}
}
