package com.junyou.bus.kuafu_boss.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.kuafu_boss.constants.KuafubossConstants;
import com.junyou.bus.kuafu_boss.service.KuafuBossService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_BOSS, groupName = EasyGroup.BUS_CACHE)
public class KuafuBossAction {

	@Autowired
	private KuafuBossService kuafuBossService;

	@EasyMapping(mapping = InnerCmdType.DINGSHI_KUAFUBOSS_ACTIVE_START)
	public void kuafuBossActiveStart(Message inMsg) {
		Integer id = inMsg.getData();
		kuafuBossService.activeStart(id);
	}

	@EasyMapping(mapping = InnerCmdType.DINGSHI_KUAFUBOSS_ACTIVE_END)
	public void kuafuBossActiveEnd(Message inMsg) {
		Integer id = inMsg.getData();
		kuafuBossService.activeEnd(id);
	}

	@EasyMapping(mapping = ClientCmdType.ENTER_KUAFU_BOSS)
	public void enterKuafuBoss(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuBossService.enterKuafuBoss(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_BOSS,
					ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.EXIT_KUAFU_BOSS)
	public void exitKuafuBoss(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuBossService.exitKuafuBoss(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.EXIT_KUAFU_BOSS,
					ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.KUAFU_BOSS_FUHUO)
	public void kuafuBossFuhuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuBossService.kuafuBossFuhuo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_BOSS_FUHUO,
					ret);
		}
	}
	
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_LEAVE_FB, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void leaveFb(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		 kuafuBossService.leaveFb(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_ENTER_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterFail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer reason = inMsg.getData();
		if (reason.intValue() == KuafubossConstants.ENTER_FAIL_REASON_1) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_BOSS,
					AppErrorCode.KUAFU_ENTER_FAIL);
		} else if (reason.intValue() == KuafubossConstants.ENTER_FAIL_REASON_2) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_BOSS,
					AppErrorCode.KUAFU_BOSS_ACTIVE_FULL);
		}
	}

	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_ENTER_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterXiaoheiwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuBossService.enterXiaoheiwu(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_REWARD_MAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void rewardMail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer rank = inMsg.getData();
		kuafuBossService.sendRewardMail(userRoleId, rank);
	}
}
