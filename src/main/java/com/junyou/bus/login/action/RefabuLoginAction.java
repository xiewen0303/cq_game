package com.junyou.bus.login.action;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.login.service.RefabuSevenLoginService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 合服七日登陆奖励action 
 *
 */
@Component
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RefabuLoginAction {
	
	@Autowired
	private RefabuSevenLoginService refabuSevenLoginService;
	/**
	 * 	合服七日奖励初始化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_REFABU_LOGIN_INFO)
	public void getInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Object[] obj = refabuSevenLoginService.getSevenLoginInfo(userRoleId, version, subId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_REFABU_LOGIN_INFO, obj);
	}
	
	
	/**
	 * 在线领奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LINGQU_REFABU_LONG)
	public void onlineReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer id = (Integer) data[2];
		
		Object[] obj = refabuSevenLoginService.getSevenLoginReward(userRoleId, version, subId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LINGQU_REFABU_LONG, obj);
	}
	
}
