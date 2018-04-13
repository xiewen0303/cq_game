package com.junyou.stage.kuafu.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.kuafu.service.KuafuStageService;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.KUAFU_MODULE,groupName = EasyGroup.STAGE)
public class KuafuStageAction{

	@Autowired
	private KuafuStageService kuafuStageService;
	
//	@EasyMapping(mapping = ClientCmdType.CLIENT_KF_CMD)
	public void applyEnterKF(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		kuafuStageService.applyEnterKF(roleId, stageId, 0);
	}
}
