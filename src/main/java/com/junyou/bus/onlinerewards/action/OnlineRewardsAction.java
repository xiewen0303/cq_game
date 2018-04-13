package com.junyou.bus.onlinerewards.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.onlinerewards.service.OnlineRewardsService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布在线奖励
 * @author lxn
 *
 */
@Controller
@EasyWorker(moduleName = GameModType.ONLINE_REWARDS , groupName = EasyGroup.BUS_CACHE)
public class OnlineRewardsAction {

	@Autowired
	private  OnlineRewardsService onlineRewardsService;
	/**
	 * 领取在线奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RFB_ONLINE_REWARDS_GET)
	public void getRewards(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer id = (Integer) data[2];
		
		Object[] result = onlineRewardsService.getRewards(userRoleId, subId,version,id);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.RFB_ONLINE_REWARDS_GET, result);
		}
	}
}
