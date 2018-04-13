package com.junyou.bus.platform._37.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.common.service.PtCommonService;

@Service
public class _37SuperVipService{
	
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
	 * 会员充值后调用逻辑 充值金额满足条件推送信息给客户端
	 * 满足条件客户端就显示qq
	 * @param userRoleId
	 * @return
	 */
	@Deprecated
	public void sendRechargeToClient(Long userRoleId, Long addYb) {
		ptCommonService.sendRechargeToClient(userRoleId, addYb);
	}
}
