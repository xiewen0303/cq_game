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
import com.junyou.stage.service.TerritoryStageService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.TERRITORY_MODULE)
public class TerritoryStageAction {
	@Autowired
	private TerritoryStageService territoryStageService;
	
	/**
	 * 领地战活动开始
	 */
	@EasyMapping(mapping = InnerCmdType.TERRITORY_START)
	public void territoryStart(Message inMsg){
		Object[] data = inMsg.getData();
		Integer hdId = (Integer) data[0];
		territoryStageService.activeStart(hdId);
	}
	/**
	 * 领地战活动中获得经验，真气
	 */
	@TokenCheck(component = GameConstants.COMPONENT_TERRITORY_EXP)
	@EasyMapping(mapping = InnerCmdType.TERRITORY_ADD_EXP_ZHENQI)
	public void territoryAddExpZhenqi(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		territoryStageService.addExpZhenqi(userRoleId, stageId);
	}
	/**
	 * 获得领地战帮派占领信息
	 */
	@EasyMapping(mapping = ClientCmdType.TERRITORY_GET_INFO)
	public void getInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] info = territoryStageService.getInfo(userRoleId);
		if(info != null){
			StageMsgSender.send2One(userRoleId, ClientCmdType.TERRITORY_GET_INFO, info);
		}
	}
	/**
	 * 领地战夺旗开始
	 */
	@EasyMapping(mapping = ClientCmdType.TERRITORY_FETCH_FLAG_BEGIN)
	public void territoryfetchFlagBegin(Message inMsg){
		Long flagGuid = LongUtils.obj2long(inMsg.getData());
		Object[] ret = territoryStageService.fetchFlagBegin(inMsg.getRoleId(), flagGuid);
		if (ret != null){
			StageMsgSender.send2One(inMsg.getRoleId(), ClientCmdType.TERRITORY_FETCH_FLAG_BEGIN, ret);
		}
	}
	/**
	 * 领地战夺旗结束
	 */
	@EasyMapping(mapping = ClientCmdType.TERRITORY_FETCH_FLAG_END)
	public void territoryfetchFlagEnd(Message inMsg){
		Object[] ret = territoryStageService.fetchFlagEnd(inMsg.getRoleId());
		if (ret != null){
			StageMsgSender.send2One(inMsg.getRoleId(), ClientCmdType.TERRITORY_FETCH_FLAG_END, ret);
		}
	}
	/**
	 * 领地战扛旗者死亡
	 */
	@EasyMapping(mapping = InnerCmdType.TERRITORY_FLAG_OWNER_DEAD)
	public void territoryFlagOwnerDead(Message inMsg){
		territoryStageService.flagOwnerDead(inMsg.getStageId());
	}
	/**
	 * 领地战怪物死亡
	 */
	@EasyMapping(mapping = InnerCmdType.TERRITORY_MONSTER_DEAD)
	public void territoryMonsterDead(Message inMsg){
		territoryStageService.monsterDead(inMsg.getStageId());
	}
	/**
	 * 领地战坚持三十分钟
	 */
	@TokenCheck(component = GameConstants.COMPONENT_TERRITORY_HAS_WINNER)
	@EasyMapping(mapping = InnerCmdType.TERRITORY_HAS_WINNER)
	public void territoryHasWinner(Message inMsg){
		territoryStageService.hasWinner(inMsg.getStageId());
	}
	/**
	 *同步旗子位置
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_TERRITORY_FLAG_SYN)
	@EasyMapping(mapping = InnerCmdType.TERRITORY_SYN_FLAG)
	public void synFlag(Message inMsg){
		String stageId = inMsg.getStageId();
		territoryStageService.synFlag(stageId);
	}
	
	/**
	 * 领地战杀人帮贡奖励
	 */
	@EasyMapping(mapping = InnerCmdType.TERRITORY_ADD_BANGGONG)
	public void territoryAddBanggong(Message inMsg){
		territoryStageService.addBanggong(inMsg.getRoleId());
	}
}
