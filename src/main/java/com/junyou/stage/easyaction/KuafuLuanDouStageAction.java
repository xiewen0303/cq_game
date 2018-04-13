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
import com.junyou.stage.service.KuafuLuanDouStageService;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_DALUANDOU, groupName = EasyGroup.STAGE)
public class KuafuLuanDouStageAction {

	@Autowired
	private KuafuLuanDouStageService kuafuLuanDouStageService;

	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_SEND_ROLE_DATA)
	public void kuafuBossSendRoleData(Message inMsg) {
		Object[] data = inMsg.getData();
		Integer fangJianId = (Integer) data[0];//房间ID
		Long userRoleId = (Long) data[1];
		String name = (String) data[2];
		Object roleData = data[3];
		kuafuLuanDouStageService.kuafuDaLuanDouSendRoleData(fangJianId, userRoleId,name,roleData);
	}

	/**
	 * 跨服退出
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_EXIT)
	public void kuafuBossExit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuLuanDouStageService.kuafuLuanDouExit(userRoleId);
	}

	/**
	 * 刷新排行榜
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_LUANDOU_RANK)
	public void kuafuLuanDouRank(Message inMsg) {
		String stageId = inMsg.getStageId();
		kuafuLuanDouStageService.kuafuLuanDouRank(stageId);
	}

	/**
	 * 跨服结束强T人
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_FORCE_KICK)
	public void kuafubossForceKick(Message inMsg) {
		String stageId = inMsg.getStageId();
		kuafuLuanDouStageService.kuafudaluandouForceKick(stageId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_FUHUO)
	public void kuafuBossFuhuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuLuanDouStageService.kuafuDaLuanDouFuhuo(userRoleId);
	}
	/**
	 * 增加积分
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_ADD_JIFEN)
	public void kuafuBossAddJiFen(Message inMsg) {
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		kuafuLuanDouStageService.kuafuLuanDouAddJiFen(stageId, userRoleId);
	}
	
	
}
