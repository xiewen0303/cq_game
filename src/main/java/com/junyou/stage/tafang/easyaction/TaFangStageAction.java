package com.junyou.stage.tafang.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.tafang.service.TaFangStageService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-10-12 下午6:49:57
 */
@Controller
@EasyWorker(moduleName = GameModType.TAFANG,groupName = EasyGroup.STAGE)
public class TaFangStageAction {
	
	@Autowired
	private TaFangStageService taFangStageService;
	
	@EasyMapping(mapping = InnerCmdType.TAFANG_SHUAXIN_GUAIWU)
	public void createMonsters(Message inMsg) {
		String stageId = inMsg.getStageId();
		Object[] data = inMsg.getData();
		int level = CovertObjectUtil.object2int(data[0]);
		int state = CovertObjectUtil.object2int(data[1]);
		
		taFangStageService.createMonsters(stageId, level, state);
	}
	
	@EasyMapping(mapping = ClientCmdType.TAFANG_START)
	public void start(Message inMsg) {
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] result = taFangStageService.startTafang(userRoleId, stageId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAFANG_START, result);
	}
}
