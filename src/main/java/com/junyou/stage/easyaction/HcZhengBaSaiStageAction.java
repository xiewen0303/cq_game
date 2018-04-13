package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.HcZhengBaSaiStageService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.HCZHENGBASAI_MODULE)
public class HcZhengBaSaiStageAction {
	@Autowired
	private HcZhengBaSaiStageService hcZhengBaSaiStageService;

	/**
	 * 皇城争霸赛活动开始
	 */
	@EasyMapping(mapping = InnerCmdType.ZHENGBASAI_START)
	public void hcZbsStart(Message inMsg) {
		Object[] data = inMsg.getData();
		Integer hdId = (Integer) data[0];
		hcZhengBaSaiStageService.activeStart(hdId);
	}

	/**
	 * 皇城争霸赛活动中获得经验，真气
	 */
	@TokenCheck(component = GameConstants.COMPONENT_HCZBS_EXP)
	@EasyMapping(mapping = InnerCmdType.HCZBS_ADD_EXP_ZHENQI)
	public void hcZbsAddExpZhenqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		hcZhengBaSaiStageService.addExpZhenqi(userRoleId, stageId);
	}

	/**
	 * 皇城争霸赛夺旗开始
	 */
	@EasyMapping(mapping = ClientCmdType.HC_ZBS_FETCH_FLAG_BEGIN)
	public void hcZbsfetchFlagBegin(Message inMsg) {
		Long flagGuid = LongUtils.obj2long(inMsg.getData());
		Object[] ret = hcZhengBaSaiStageService.fetchFlagBegin(inMsg.getRoleId(), flagGuid);
		if (ret != null) {
			StageMsgSender.send2One(inMsg.getRoleId(), ClientCmdType.HC_ZBS_FETCH_FLAG_BEGIN, ret);
		}
	}

	/**
	 * 皇城争霸赛旗帜被抢
	 */
	@EasyMapping(mapping = ClientCmdType.HC_ZBS_FETCH_FLAG_END)
	public void hcZbsfetchFlagEnd(Message inMsg) {
		Object[] ret = hcZhengBaSaiStageService.fetchFlagEnd(inMsg.getRoleId());
		if (ret != null) {
			StageMsgSender.send2One(inMsg.getRoleId(), ClientCmdType.HC_ZBS_FETCH_FLAG_END, ret);
		}
	}

	/**
	 * 皇城争霸赛扛旗者死亡
	 */
	@EasyMapping(mapping = InnerCmdType.HCZBS_FLAG_OWNER_DEAD)
	public void hcZbsFlagOwnerDead(Message inMsg) {
		hcZhengBaSaiStageService.flagOwnerDead(inMsg.getStageId());
	}

	/**
	 * 皇城争霸赛怪物死亡
	 */
	@EasyMapping(mapping = InnerCmdType.HCZBS_MONSTER_DEAD)
	public void hcZbsMonsterDead(Message inMsg) {
		hcZhengBaSaiStageService.monsterDead(inMsg.getStageId());
	}

	/**
	 * 皇城争霸赛坚持三十分钟
	 */
	@TokenCheck(component = GameConstants.COMPONENT_HCZBS_HAS_WINNER)
	@EasyMapping(mapping = InnerCmdType.HCZBS_HAS_WINNER)
	public void hcZbsHasWinner(Message inMsg) {
		hcZhengBaSaiStageService.hasWinner(inMsg.getStageId());
	}

	/**
	 * 同步旗子位置
	 * 
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_HCZBS_FLAG_SYN)
	@EasyMapping(mapping = InnerCmdType.HCZBS_SYN_FLAG)
	public void synFlag(Message inMsg) {
		String stageId = inMsg.getStageId();
		hcZhengBaSaiStageService.synFlag(stageId);
	}
	/**
	 * 通知门主切换buff+外显
	 */
	@EasyMapping(mapping = InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES)
	public void leaderChangeClothes(Message inMsg) {
		long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] data  = inMsg.getData();
		boolean isShow = (Boolean) data[0];
		int roleType = (Integer)data[1];
		hcZhengBaSaiStageService.updateClothes(roleId,stageId,isShow,roleType);
	}

	
	/**
	 * 退出皇城争霸地图
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HC_ZBS_EXIT)
	public void exitZBS(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = hcZhengBaSaiStageService.exitZhengBaSai(userRoleId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_EXIT, ret);
	}
}
