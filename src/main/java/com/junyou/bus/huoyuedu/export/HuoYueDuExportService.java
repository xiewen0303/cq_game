package com.junyou.bus.huoyuedu.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.service.HuoYueDuService;

@Service
public class HuoYueDuExportService {

	@Autowired 
	private HuoYueDuService huoYueDuService;
	
	/**任务总数只有一次
	 * 活动触发事件处理逻辑,或者注入对应的模块
	 */
	public void completeActivity(Long userRoleId, ActivityEnum acEnum) {
		completeActivity(userRoleId, acEnum,1);
	}
	
	/**任务总数大于1 一件扫荡什么的
	 * 活动触发事件处理逻辑,或者注入对应的模块
	 */
	public void completeActivity(Long userRoleId, ActivityEnum acEnum,Integer num) {
		huoYueDuService.completeActivity(userRoleId, acEnum,num);
	}
	 
	/**
	 * 上线后调用逻辑
	 * 
	 * @param userRoleId
	 * @throws InterruptedException
	 */
	public void onlineHandle(Long userRoleId) {

		this.huoYueDuService.onlineHandle(userRoleId);

	}
	
}
