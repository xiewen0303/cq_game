package com.junyou.bus.platform._360.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform._360.service._360VplanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 360V计划
 * @author lxn
 *
 */
@Component
@EasyWorker(moduleName = GameModType.PLATFORM_360_V_PLAN)
public class _360VplanAction {

	@Autowired
	private _360VplanService _360VplanService;
	/**
	 * 每日礼包领取
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_VPLAN_DAY_LB)
	public void get360GiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = _360VplanService.getDayReward(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_VPLAN_DAY_LB, result);
	}
	/**
	 * 等级礼包领取
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_VPLAN_UP_LB)
	public void getUpgradeReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int level  = (int)inMsg.getData();
		Object[]  result = _360VplanService.getUpgradeReward(userRoleId,level);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_VPLAN_UP_LB, result);
	}

	/**
	 * 等级礼包领取状态
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_VPLAN_UP_STATE)
	public void getUpgradeRewardState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = _360VplanService.getUpgradeRewardState(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_VPLAN_UP_STATE, result);
	}
	/**
	 *  开通礼包领取状态
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_VPLAN_KAITONG_STATE)
	public void getKtGigtState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = _360VplanService.getKtGigtState(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_VPLAN_KAITONG_STATE, result);
	}
	/**
	 * 领取 开通礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_VPLAN_KAITONG)
	public void getKtGigt(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = _360VplanService.getKtGigt(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_VPLAN_KAITONG, result);
	}
	/**
	 * 消费礼包状态
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_XF_LB_STATE)
	public void getXfGigtState(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = _360VplanService.getXfGigtState(userRoleId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_XF_LB_STATE, result);
		}
	}
	/**
	 *消费礼包消费金额
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_XF_LB_GOLD)
	public void getXfGigtGold(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = _360VplanService.getXfGigtConsume(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_XF_LB_GOLD, result);
	}
	/**
	 * 领取消费礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_360_XF_LB)
	public void getXfGigt(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] result = _360VplanService.getConsumeReward(userRoleId,id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_XF_LB, result);
	}
	/**
	 * 请求360V计划用户的信息
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_360_VPLAN_INFO)
	public void get360VplanInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		_360VplanService.init360VplanInfo(userRoleId);
	}
	
	
	/**
	 * 模拟消耗元宝
	 * @param inMsg
	
	@EasyMapping(mapping = ClientCmdType.RECHARGE_TEST)
	public void testRecharge(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long money  =Long.parseLong(inMsg.getData()+""); //一人民币等于10元宝
		_360VplanService.sendRechargeToClient(userRoleId,money*10);
	}
	 **/
}
