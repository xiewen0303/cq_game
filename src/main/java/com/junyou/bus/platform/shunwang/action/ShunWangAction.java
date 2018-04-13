package com.junyou.bus.platform.shunwang.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.shunwang.service.ShunWangService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 顺网
 */
@Component
@EasyWorker(moduleName = GameModType.PLATFORM_SHUNWANG)
public class ShunWangAction {
	@Autowired
	private ShunWangService shunWangService;
	/**
	 * 超级会员按钮请求信息
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SHUNWANG_GIFT_STATE)
	public void get37WanGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = shunWangService.getSuperVipInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SHUNWANG_GIFT_STATE, result);
	}
	
	/**
	 * 超级会员活动是否关闭
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SHUNWANG_CLOSED)
	public void isCloseActivity(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = shunWangService.isCloseActivity();
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SHUNWANG_CLOSED, result);
	}
	/**
	 * VIP等级礼包领取状态
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SHUNWANG_STATE)
	public void getState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = shunWangService.getState(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SHUNWANG_STATE, result);
	}
	/**
	 * 领取VIP等级礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SHUNWANG_LB)
	public void getLb(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int level = (int)inMsg.getData();
		Object[] result = shunWangService.getReward(userRoleId,level);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SHUNWANG_LB, result);
	}
}
