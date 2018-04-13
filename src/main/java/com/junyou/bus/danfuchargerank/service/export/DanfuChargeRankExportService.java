package com.junyou.bus.danfuchargerank.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.danfuchargerank.service.DanfuChargeRankService;

@Service
public class DanfuChargeRankExportService {
	@Autowired
	private DanfuChargeRankService danfuChargeRankService;

	public Object[] getRfbInfo(Long userRoleId, Integer subId) {
		return danfuChargeRankService.getInfo(userRoleId, subId);
	}

	public Object[] getDanfuChargeRankStates(Long userRoleId, Integer subId) {
		return danfuChargeRankService.getRankInfo(false, userRoleId, 0, subId);
	}
	public void danfuChargeRankDayJob() {
		danfuChargeRankService.danfuChargeRankDayJob();
	}
}
