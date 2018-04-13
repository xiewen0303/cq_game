package com.junyou.bus.platform.supervip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.common.service.PtCommonService;

@Service
public class SuperVipCommonService {


	@Autowired
	private PtCommonService ptCommonService;
	/**
	 * 判断活动是否关闭
	 */
	public int isCloseActivity(){
		
		return ptCommonService.isCloseActivity();
	}
	/**
	 * 打开超级会员面板请求的信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getSuperVipInfo(Long userRoleId) {
		 
		return ptCommonService.getSuperVipInfo(userRoleId);
	}
	
	/**
	 * 模拟测试充值
	 * @param userRoleId
	 * @return
	 */
	public void sendRechargeToClient(Long userRoleId, Long addYb) {
		 
		  ptCommonService.sendRechargeToClient(userRoleId,addYb);
	}
	
	
	
}
