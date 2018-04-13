package com.junyou.bus.hefuSevenlogin.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.hefuSevenlogin.service.HefuSevenLoginService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 合服七日登陆奖励action 
 *
 */
@Component
@EasyWorker(moduleName = GameModType.HEFU_SEVEN_LOGIN_REWARD)
public class HefuSevenLoginAction {
	
	@Autowired
	private HefuSevenLoginService sevenLoginService;
	/**
	 * 	合服七日奖励初始化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HEFU_SEVEN_REWARD_INIT)
	public void getInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = sevenLoginService.getSevenLoginInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.HEFU_SEVEN_REWARD_INIT, obj);
	}
	
	
	/**
	 * 在线领奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HEFU_SEVEN_REWARD)
	public void onlineReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		
		Object[] obj = sevenLoginService.getSevenLoginReward(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.HEFU_SEVEN_REWARD, obj);
	}
	
}
