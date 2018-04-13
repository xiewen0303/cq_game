package com.junyou.bus.platform._37.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform._37.service._37VplanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 37V计划
 * @author lxn
 *
 */
@Component
@EasyWorker(moduleName = GameModType.PLATFORM_37WAN_GIFT)
public class _37VplanAction {
	
	@Autowired
	private _37VplanService _37VplanService;
	/**
	 * V计划一键领取每日礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_DAY_LB)
	public void getDayGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = _37VplanService.getDayGift(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_DAY_LB, result);
	}

	/**
	 * V计划升级礼包领取状态
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_UPGRADE_STATE)
	public void getUpgradeGiftState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int  result = _37VplanService.getUpgradeGiftState(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_UPGRADE_STATE, result);
	}
	/**
	 * V计划升级礼包领取
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_UPGRADE_LB)
	public void getUpgradeGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int level  = inMsg.getData();
		Object[]  result = _37VplanService.getUpgradeGift(userRoleId,level);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_UPGRADE_LB, result);
	}
	/**
	 * 请求V计划消费礼包的状态
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_CONSUME_STATE)
	public void getConsumeGiftState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = _37VplanService.getConsumeRewardState(userRoleId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_CONSUME_STATE, result);
		}
	}
	/**
	 *请求领取消费礼包奖励
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_CONSUM_LB)
	public void getConsumeGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int id  = inMsg.getData();
		Object[]  result = _37VplanService.getConsumeReward(userRoleId,id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_CONSUM_LB, result);
	}
	/**
	 *请求本轮消费金额
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_37WAN_CONSUM_VALUE)
	public void getConsumeGoldValue(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = _37VplanService.getXfGigtConsume(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_CONSUM_VALUE, result);
	}
	
	
	/**
	 * 模拟消费礼包消耗元宝
	 *  
	@EasyMapping(mapping = 20016)
	public void test(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		long value  =LongUtils.obj2long(inMsg.getData());
	    _37VplanService.sendRechargeToClient(userRoleId,value);
	}
  */
}
