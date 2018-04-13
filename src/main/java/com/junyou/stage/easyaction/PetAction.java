package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.service.PetService;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 技能招换的宠物ACTION
 * @author DaoZheng Yuan
 * 2013-11-5 下午3:51:55
 */
@Controller
@EasyWorker
public class PetAction {

	@Autowired
	private PetService petService;
	
	/**
	 * 心跳处理
	 * 来源:
	 * 1、自然心跳
	 * 2、主人移动，伙伴如果在巡逻状态则快速进入心跳
	 * 3、主人进入战斗状态，快速心跳
	 * @param message
	 */
	@TokenCheck(component = GameConstants.COMPONENT_AI_HEARTBEAT)
	@EasyMapping(mapping = InnerCmdType.AI_PET_HANDLE)
	public void heartBeatHandle(Message inMsg){
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		Long petId = (Long)data[1];
		
		petService.heartBeatHandle(petId, stageId);
	}
	
	
	/**
	 * 清除宠物内存数据
	 * @param inMsg
	 */
//	@EasyMapping(mapping = PetCommands.PET_CLEAR_HANDLE)
	public void clearPetHandle(Message inMsg){
		Object[] data = inMsg.getData();
		
		Long roleId = (Long)data[0];
		String stageId = (String)data[1];
		Long petId = (Long)data[2];
		
		petService.clearPetHandle(roleId,stageId,petId);
	}
}
