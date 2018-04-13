package com.junyou.bus.hefuSevenlogin.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.hefuSevenlogin.service.HefuSevenLoginService;

/**
 *合服七日登陆
 */
@Service
public class HefuSevenLoginExportService {

	@Autowired
	private HefuSevenLoginService sevenLoginService;
	/**
	 * 获取七登是否有奖励
	 * @param userRoleId（暂没用）
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的3次方
	 */
	public int getSevenLoginStateValue(Long userRoleId){
		return sevenLoginService.getSevenLoginStateValue(userRoleId);
	}
	/**
	 * 上线后调用逻辑
	 * @param userRoleId
	 * @throws InterruptedException
	 */
	public void onlineHandle(Long userRoleId) {

		this.sevenLoginService.onlineHandle(userRoleId);

	}
}
