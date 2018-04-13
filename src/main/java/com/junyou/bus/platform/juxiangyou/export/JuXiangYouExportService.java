package com.junyou.bus.platform.juxiangyou.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.juxiangyou.service.JuXiangYouService;

/**
 * 聚享游玩家第一次成创角进入游戏
 * @author lxn
 *
 */
@Service
public class JuXiangYouExportService {

	@Autowired
	private JuXiangYouService JuXiangYouService;
	
	/**
	 *  聚享游玩家第一次创角进入游戏
	 * @param userRoleId
	 * @return
	 */
	public void onlineHandle(Long userRoleId) {
		JuXiangYouService.onlineHandle(userRoleId);
	}
 
}
