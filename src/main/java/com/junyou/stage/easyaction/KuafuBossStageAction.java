package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.KuafuBossStageService;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_BOSS, groupName = EasyGroup.STAGE)
public class KuafuBossStageAction {

	@Autowired
	private KuafuBossStageService kuafuBossStageService;

	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_SEND_ROLE_DATA)
	public void kuafuBossSendRoleData(Message inMsg) {
		Object[] data = inMsg.getData();
		Integer activeId = (Integer) data[0];
		Long userRoleId = (Long) data[1];
		Object roleData = data[2];
		kuafuBossStageService.kuafuBossSendRoleData(activeId, userRoleId,
				roleData);
	}

	/**
	 * 跨服boss加经验
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_ADD_EXP_DINGSHI)
	public void kuafuBossAddExp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String stageId = (String) data[0];
		Long exp = (Long) data[1];
		kuafuBossStageService.addExpDingshi(stageId, userRoleId, exp);
	}

	/**
	 * 跨服boss加经验
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_EXIT)
	public void kuafuBossExit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuBossStageService.kuafuBossExit(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_BOSS_RANK)
	public void kuafuBossRank(Message inMsg) {
		String stageId = inMsg.getStageId();
		kuafuBossStageService.kuafuBossRank(stageId);
	}

	/**
	 * 跨服boss加经验
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_FORCE_KICK)
	public void kuafubossForceKick(Message inMsg) {
		String stageId = inMsg.getStageId();
		kuafuBossStageService.kuafubossForceKick(stageId);
	}

	/**
	 * 跨服boss死亡
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_DEAD)
	public void kuafubossDead(Message inMsg) {
		Long killerId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		kuafuBossStageService.kuafubossDead(stageId, killerId);
	}
	
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_FUHUO)
	public void kuafuBossFuhuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer mapId = inMsg.getData();
		String stageId = StageUtil.getStageId(mapId, 1);
		kuafuBossStageService.kuafuBossFuhuo( userRoleId,
				stageId);
	}
}
