package com.junyou.bus.hczbs.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.hczbs.service.HcZhengBaSaiService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.HcZhengBaSaiStageService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.pool.executor.Message;

/**
 * 皇城争霸赛 天宫之战
 * 
 * @author lxn
 * 
 */
@Controller
@EasyWorker(moduleName = GameModType.TERRITORY_MODULE, groupName = EasyGroup.BUS_CACHE)
public class HcZhengBaSaiAction {
	@Autowired
	private HcZhengBaSaiService hcZhengBaSaiService;
	@Autowired
	private HcZhengBaSaiStageService hcZhengBaSaiStageService;

	/**
	 * 请求进入皇城争霸
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HC_ZBS_ENTER)
	public void enterHcZBS(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = hcZhengBaSaiService.enterZBS(userRoleId);
		if(ret!=null){
		    StageMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_ENTER, ret);
		}
	}

	/**
	 * 皇城争霸领取奖励
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HC_ZBS_REWARD)
	public void getReward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = hcZhengBaSaiService.getReward(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_REWARD, ret);
	}

	/**
	 * 2131请求皇城占领信息
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HC_ZBS_WIN_INFO)
	public void getGuildLeaderMapId(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = hcZhengBaSaiService.getWinInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_WIN_INFO, ret);
		}
	}

	/**
	 * 皇城争霸活动结束
	 */
	@EasyMapping(mapping = InnerCmdType.HCZBS_ACTIVE_END)
	public void hcZbsActiveEnd(Message inMsg) {
		hcZhengBaSaiStageService.activeEnd();
	}

	/**
	 * 掌门让位
	 * BusMsgSender.send2BusInner(leader.getUserRoleId(), InnerCmdType.HCZBS_LEADER_OFF, guildMember.getGuildId());
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.HCZBS_LEADER_OFF, guildId);
	@EasyMapping(mapping = InnerCmdType.HCZBS_LEADER_OFF)
	public void hcZbsLeaderOff(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long guildId = LongUtils.obj2long(inMsg.getData());
		hcZhengBaSaiService.hcZbsLeaderOff(userRoleId, guildId);
	}
*/
	/**
	 * 新掌门上位
	 * BusMsgSender.send2BusInner(guildMember.getUserRoleId(), InnerCmdType.HCZBS_LEADER_ON, guildMember.getGuildId());
	
	@EasyMapping(mapping = InnerCmdType.HCZBS_LEADER_ON)
	public void hcZbsLeaderON(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long guildId = LongUtils.obj2long(inMsg.getData());
		hcZhengBaSaiService.hcZbsLeaderON(userRoleId, guildId);
	}
	 */
}
