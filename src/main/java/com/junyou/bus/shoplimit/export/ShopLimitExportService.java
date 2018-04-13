package com.junyou.bus.shoplimit.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.shoplimit.entity.ShopLimitInfo;
import com.junyou.bus.shoplimit.service.ShopLimitService;

@Service
public class ShopLimitExportService {
	@Autowired
	private ShopLimitService shopLimitService;
	
	public void rechargeYb(Long userRoleId, Long addVal, Map<String, Long[]> rechargeData) {
		shopLimitService.rechargeYb(userRoleId, addVal,rechargeData);
	}

	public void onlineHandle(Long userRoleId) {
		shopLimitService.onlineHandle(userRoleId);
	}

	public ShopLimitInfo initLimitShopInfo(Long userRoleId) {
		return shopLimitService.initLimitShopInfo(userRoleId);
	}
}
