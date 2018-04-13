package com.junyou.bus.extremeRecharge.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.extremeRecharge.entity.RfbExtremeRecharge;
import com.junyou.bus.extremeRecharge.service.RfbExtremeRechargeService;

@Service
public class RfbExtremeRechargeExportService {
	@Autowired
	private RfbExtremeRechargeService rfbExtremeRechargeService;
	
	public List<RfbExtremeRecharge> initRfbExtremeRecharge(Long userRoleId) {
		return rfbExtremeRechargeService.initRfbExtremeRecharge(userRoleId);
	}

	public Object getRefbInfo(Long userRoleId, Integer subId) {
		return rfbExtremeRechargeService. getRefbInfo( userRoleId,  subId);
	}

	public Object getRFbExtremeRechargeStates(Long userRoleId, int subId) {
		return rfbExtremeRechargeService.getRFbExtremeRechargeStates(userRoleId, subId);
	}
}
