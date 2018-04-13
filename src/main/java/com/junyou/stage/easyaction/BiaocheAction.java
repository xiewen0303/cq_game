package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.BiaocheService;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 镖车场景
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-16 下午2:45:18 
 */
@Controller
@EasyWorker(moduleName = GameModType.YABIAO_MODULE,groupName = EasyGroup.STAGE)
public class BiaocheAction {
	@Autowired
	private BiaocheService biaocheService;
	
	@EasyMapping(mapping = InnerCmdType.S_CREATE_BIAOCHE)
	public void createBiaoche(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Integer configId = inMsg.getData();
		
		biaocheService.createBiaoche(userRoleId, stageId, configId);
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_AI_HEARTBEAT)
	@EasyMapping(mapping = InnerCmdType.S_BIAOCHE_HEART)
	public void biaocheHeart(Message inMsg){
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		Long bcId = (Long)data[1];
		
		biaocheService.biaocheHeart(stageId, bcId);
	}
	
	@EasyMapping(mapping = InnerCmdType.S_BIAOCHE_CLEAN)
	public void biaocheClean(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String jbName = inMsg.getData();
		
		biaocheService.biaocheClean(userRoleId, jbName);
	}
}
