package com.junyou.stage.collect.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.collect.service.CollectBoxService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.DINGSHI_ACTIVE_MODULE,groupName=EasyGroup.STAGE)
public class CollectBoxAction {
	@Autowired
	private CollectBoxService collectBoxService;
	
	@EasyMapping(mapping = InnerCmdType.COLLECT_BOX_START)
	public void collectBoxActivityStart(Message inMsg) {
		int activityId=inMsg.getData();
		collectBoxService.CollectBoxActivityStart(activityId);
		
	}
	
	@EasyMapping(mapping = InnerCmdType.COLLECT_BOX_STOP)
	public void collectBoxActivityStop(Message inMsg) {
		collectBoxService.collectBoxActivityEnd();
		
	}
	@EasyMapping(mapping = InnerCmdType.DINGSHI_FLUSH_BOX)
	public void flushBox(Message inMsg){
		String stageId = inMsg.getData();
		collectBoxService.flushBox(stageId);
		
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_CLEAR_ALL_BOX)
	public void clearAllBox(Message inMsg){
		String stageId = inMsg.getData();
		collectBoxService.clearAllBox(stageId);
	}
	
	
	
	@EasyMapping(mapping = ClientCmdType.START_COLLECT_BOX,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void startCollect(Message message){
		Long roleId = message.getRoleId();
		String stageId = message.getStageId();
		Number guid = message.getData();
		Object[] result = collectBoxService.startCollect(stageId, roleId, guid.longValue());
		if(result != null){
			StageMsgSender.send2One(roleId, ClientCmdType.START_COLLECT_BOX, result);
		}
		
	}
	
	@EasyMapping(mapping = ClientCmdType.END_COLLECT_BOX,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void endCollect(Message message){
		String stageId = message.getStageId();
		Long roleId = message.getRoleId();
		Object[] result =collectBoxService.completeCollect(stageId, roleId);
		if(result != null){
			StageMsgSender.send2One(roleId, ClientCmdType.END_COLLECT_BOX, result);
		}
	}
}
