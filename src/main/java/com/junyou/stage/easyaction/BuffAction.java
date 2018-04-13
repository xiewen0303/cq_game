package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.service.BuffService;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

@Controller
@EasyWorker
public class BuffAction {
	
	@Autowired
	private BuffService buffService;

	@TokenCheck(component = GameConstants.COMPONENT_BUFF_END)
	@EasyMapping(mapping = InnerCmdType.INNER_BUFF_END)
	public void buffEnd(Message inMsg){
		
		//buffCategory,id,ownerId,elementType,stageId
		Object[] data = inMsg.getData();
		
		String buffCategory = (String)data[0];
		Long id = LongUtils.obj2long(data[1]);
		Long ownerId = (Long)data[2];
		Integer elementType = (Integer)data[3];
		String stageId = (String)data[4];
		
		buffService.buffEnd(id,buffCategory,ownerId,elementType,stageId);
		
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_BUFF_TRIGGER)
	@EasyMapping(mapping = InnerCmdType.INNER_BUFF_TRIGGER)
	public void buffTrigger(Message inMsg){
		
		//buffCategory,id,ownerId,elementType,stageId
		//buffCategory,id,ownerId,elementType,stageId
		Object[] data = inMsg.getData();
		
		String buffCategory = (String)data[0];
		Long id = LongUtils.obj2long(data[1]);
		Long ownerId = (Long)data[2];
		Integer elementType = (Integer)data[3];
		String stageId = (String)data[4];
		
		buffService.specialEffectTrigger(id, buffCategory, ownerId, elementType, stageId);
		
	}
	
	/**
	 * 剧情BUFF开始
	 * @param inMsg
	 */
//	@EasyMapping(mapping = StageCommands.JUNQIN_START)
	public void junQinStart(Message inMsg){
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		//接到指令马上回复  （服务器收到指令）
//		StageMsgSender.send2One(roleId, StageCommands.JUNQIN_START, null);
		
		buffService.junQinStart(roleId, stageId);
	}
	
	/**
	 * 剧情BUFF开始
	 * @param inMsg
	 */
//	@EasyMapping(mapping = StageCommands.JUNQIN_END)
	public void junQinEnd(Message inMsg){
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		//接到指令马上回复  （服务器收到指令）
//		StageMsgSender.send2One(roleId, StageCommands.JUNQIN_END, null);
		
		buffService.junQinEnd(roleId, stageId);
	}
	
	
}
