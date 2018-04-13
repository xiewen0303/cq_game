package com.junyou.bus.sevenlogin.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.sevenlogin.service.SevenLoginService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 七日登陆奖励action 
 * @author jy
 *
 */
@Component
@EasyWorker(moduleName = GameModType.SEVEN_LOGIN_REWARD)
public class SevenLoginAction {
	
	@Autowired
	private SevenLoginService sevenLoginService;
	
	/**
	 * 七日奖励初始化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SEVEN_REWARD_INIT)
	public void onlineRewardInit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] obj = sevenLoginService.getSevenLoginInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SEVEN_REWARD_INIT, obj);
	}
	
	
	/**
	 * 在线领奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SEVEN_REWARD)
	public void onlineReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer rewardType = inMsg.getData();
		
		Object[] obj = sevenLoginService.getSevenLoginReward(userRoleId, rewardType);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SEVEN_REWARD, obj);
	}
	
}
