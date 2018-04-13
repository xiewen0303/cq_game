package com.junyou.bus.online.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.online.service.RoleOnlineService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 在线奖励action 
 * @author jy
 *
 */
@Component
@EasyWorker(moduleName = GameModType.ONLINE_REWARD)
public class OnlineAction {
	
	@Autowired
	private RoleOnlineService roleOnlineService;
	
	/**
	 * 在线奖励初始化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ONLINE_REWARD_INIT)
	public void onlineRewardInit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = roleOnlineService.getOnlineRewardInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ONLINE_REWARD_INIT, obj);
	}
	
	
	/**
	 * 在线领奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ONLINE_REWARD)
	public void onlineReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = roleOnlineService.getOnlineReward(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ONLINE_REWARD, obj);
	}
	
	/**
	 * 获取限时在线信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.AWARD_ONLINE_INFO)
	public void awardOnlineInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = roleOnlineService.awardOnlineInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.AWARD_ONLINE_INFO, obj);
	}
	
	/**
	 * 请求领取某个奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LINGQU_ONLINE)
	public void lingquOnline(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int time = inMsg.getData();
		
		Object[] obj = roleOnlineService.lingquOnline(userRoleId,time);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LINGQU_ONLINE, obj);
	}
}
