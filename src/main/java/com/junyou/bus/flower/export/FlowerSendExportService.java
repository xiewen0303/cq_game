package com.junyou.bus.flower.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.flower.entity.RoleSendFlower;
import com.junyou.bus.flower.service.FlowerService;

@Service
public class FlowerSendExportService {

	@Autowired
	private FlowerService flowerService;

	/**
	 * 初始化数据
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleSendFlower> initData(long userRoleId) {
		return flowerService.initData(userRoleId);
	}

	//  登陆时候调用
	public void onlineHandle(Long userRoleId) {
		flowerService.handleUserLogin(userRoleId);

	}

	// 离线的时候调用
	public void offlineHandle(Long userRoleId) {
		flowerService.handleOffline(userRoleId);
	}
}
