package com.junyou.bus.platform._37.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform._37.service._37GiftService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 37wanVIP等级礼包领取情况
 * @author lxn
 *
 */
@Component
@EasyWorker(moduleName = GameModType.PLATFORM_37WAN_GIFT)
public class _37GiftAction {
	
	@Autowired
	private _37GiftService _37GiftService;
	/**
	 * 获取玩家在37wan平台的礼包领取情况
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_GIFT_STATE)
	public void get37WanGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = _37GiftService.getStateByRoleId(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_GIFT_STATE, result);
	}
	/**
	 * 玩家领取平台奖励
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_GIFT)
	public void get37WanGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer level = inMsg.getData(); //会员等级
		Object[] result = (Object[])_37GiftService.getReward(userRoleId,level);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_GIFT, result);
	}
	/**
	 * 37令牌奖励领取状态
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_LINGPAI_STATE)
	public void get37LingpaiState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		boolean result = _37GiftService.getLingpaiRewardState(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_LINGPAI_STATE, result);
	}

	/**
	 * 领取 37令牌奖励
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_LINGPAI_GET)
	public void get37LingpaiGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = _37GiftService.getLingpaiReward(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_LINGPAI_GET, result);
	}
}
