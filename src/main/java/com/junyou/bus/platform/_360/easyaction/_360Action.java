package com.junyou.bus.platform._360.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform._360.service._360Service;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 360加速球礼品 + 360游戏大厅礼包领取
 * 
 * @author lxn
 */
@Component
@EasyWorker(moduleName = GameModType.PLATFORM_360_GIFT)
public class _360Action {

	@Autowired
	private _360Service _360Service;
	/**
	 * 360礼包领取情况
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_GIFT_STATE)
	public void get360GiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int  result = _360Service.getStateByRoleId(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_GIFT_STATE, result);
	}
	/**
	 * 获取360加速球礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_BALL_GIFT)
	public void get360BallGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]   result = _360Service.getReward(userRoleId,1);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_BALL_GIFT, result);
	}
	/**
	 * 获取360游戏大厅的奖品
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_DATING_GIFT)
	public void get360DaTingGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]   result = _360Service.getReward(userRoleId,2);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_DATING_GIFT, result);
	}
	/**
	 * 获取特权等级领取状态
	 * 
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_TEQUAN_STATES)
	public void getTequanStates(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]   result = _360Service.getTequanStateByRoleId(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_TEQUAN_STATES, result);
	}
	/**
	 * 领取特权等级礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_TEQUAN_GIFT)
	public void getTequanLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int tequanNum = (Integer)inMsg.getData();
		Object[]   result = _360Service.getTequanReward(userRoleId,tequanNum);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_TEQUAN_GIFT, result);
	}
	/**
	 * 领取特权分享礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_TEQUAN_SHARE_LB)
	public void getTequanShareGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]   result = _360Service.getTequanShareGift(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_TEQUAN_SHARE_LB, result);
	}
}
