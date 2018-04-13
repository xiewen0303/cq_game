package com.junyou.bus.platform._37.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform._37.service._37SuperVipService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 37wan超级会员
 * @author lxn
 *
 */
@Component
@EasyWorker(moduleName = GameModType.PLATFORM_37WAN_SUPER_VIP)
public class _37SuperVipAction {
	
	@Autowired
	private _37SuperVipService _37SuperVipService;
	/**
	 * 点击37wan超级会员按钮请求信息
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_SUPER_VIP_INFO)
	public void get37WanGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = _37SuperVipService.getSuperVipInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_SUPER_VIP_INFO, result);
	}
	/**
	 * 超级会员活动是否关闭
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_SUPER_VIP_CLOSED)
	public void isCloseActivity(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = _37SuperVipService.isCloseActivity();
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_SUPER_VIP_CLOSED, result);
	}
}
