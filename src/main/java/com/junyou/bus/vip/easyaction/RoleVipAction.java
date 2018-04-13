package com.junyou.bus.vip.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.service.RoleVipInfoService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-5-4 下午8:52:10
 */
@Component
@EasyWorker(moduleName = GameModType.VIP_MODULE)
public class RoleVipAction {
	@Autowired
	private RoleVipInfoService roleVipInfoService;
	
	/**
	 * 请求VIP奖励状态
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ASK_VIP_GIFT_STATE)
	public void askVipGiftState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = roleVipInfoService.getRoleVipGiftState(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ASK_VIP_GIFT_STATE, result);
	}
	
	/**
	 * 请求领取VIP周奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECIVE_VIP_WEEK_GIFT)
	public void reciveVipWeekGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = roleVipInfoService.reciveRoleVipWeekGift(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RECIVE_VIP_WEEK_GIFT, result);
	}
	
	/**
	 * 请求领取VIP等级奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECIVE_VIP_LEVEL_GIFT)
	public void reciveVipLevelGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] result = roleVipInfoService.reciveRoleVipLevelGift(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RECIVE_VIP_LEVEL_GIFT, result);
	}
	
	/**
	 * VIP到期
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.VIP_TIME_OUT)
	public void vipTimeOut(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		roleVipInfoService.vipTimeOut(userRoleId);
	}
}
