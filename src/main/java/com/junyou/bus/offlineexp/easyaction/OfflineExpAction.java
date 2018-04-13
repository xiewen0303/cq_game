package com.junyou.bus.offlineexp.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.offlineexp.service.OfflineExpService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 离线经验初始化 
 * @author jy
 *
 */
@Component
@EasyWorker(moduleName = GameModType.OFFLINE_EXP)
public class OfflineExpAction {
	
	@Autowired
	private OfflineExpService offlineExpService;
	
	/**
	 * 离线经验初始化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.OFFLINE_EXP_INIT)
	public void offlineInit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = offlineExpService.getOfflineExpInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.OFFLINE_EXP_INIT, obj);
	}
	
	/**
	 * 离线经验领奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.OFFLINE_EXP_REWARD)
	public void assign(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer rewardType = inMsg.getData();
		Object[] obj = offlineExpService.getOfflineExpReward(userRoleId, rewardType);
		BusMsgSender.send2One(userRoleId, ClientCmdType.OFFLINE_EXP_REWARD, obj);
	}
	
}
