package com.junyou.bus.territory.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.territory.service.TerritoryService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.TerritoryStageService;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.TERRITORY_MODULE, groupName = EasyGroup.BUS_CACHE)
public class TerritoryAction {
	@Autowired
	private TerritoryService territoryService;
	@Autowired
	private TerritoryStageService territoryStageService;

	/**
	 * 领地战领取奖励
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TERRITORY_GET_REWARD)
	public void getReward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer[] maps = new Integer[data.length];
		for(int i=0;i<data.length;i++){
			maps[i] = (Integer)data[i];
		}
		Object[] ret = territoryService.getReward(userRoleId, maps);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TERRITORY_GET_REWARD,ret);
	}

	/**
	 * 获得领地战基本信息
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TERRITORY_GET_GUILD_LEADER_MAP_ID)
	public void getGuildLeaderMapId(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer ret = territoryService.getGuildLeaderMapId(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.TERRITORY_GET_GUILD_LEADER_MAP_ID, ret);
		}
	}
	/**
	 * 领地战活动结束
	 */
	@EasyMapping(mapping = InnerCmdType.TERRITORY_ACTIVE_END)
	public void territoryActiveEnd(Message inMsg){
		territoryStageService.activeEnd();
	}
	
	/**
	 * 掌门让位
	 */
	@EasyMapping(mapping = InnerCmdType.TERRITORY_LEADER_OFF)
	public void territoryLeaderOff(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		territoryService.territoryLeaderOff(userRoleId);
	}
	/**
	 * 新掌门上位
	 */
	@EasyMapping(mapping = InnerCmdType.TERRITORY_LEADER_ON)
	public void territoryLeaderON(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guildId = LongUtils.obj2long(inMsg.getData());
		territoryService.territoryLeaderON(userRoleId,guildId);
	}
}
