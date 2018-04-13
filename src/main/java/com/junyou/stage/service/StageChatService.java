package com.junyou.stage.service;

import org.springframework.stereotype.Service;

import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;

@Service
public class StageChatService{
	

	
	public void nearbyMsg(Long roleId, String stageId, Object[] msg) {
		
		IStage stage = StageManager.getStage(stageId);
		IElement element = stage.getElement(roleId, ElementType.ROLE);
		Long[] roleIds = stage.getSurroundRoleIds(element.getPosition());
		
//		StageMsgSender.send2Many(roleIds, StageCommands.CHAT_NEARBY, msg);
	}

	
	public boolean worldChat(Long roleId, String stageId,Object chatMsg) {
		if(stageId == null || roleId == null){
			return false;
		}
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return false;
		}
		
		IRole role = (IRole) stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return false;
		}
		
		
		Object msgContent = ((Object[])chatMsg)[3];
		
		//日志
//		LogChuanQiUtil.gameChat(role.getName(),role.getId(),role.getBusinessData().getUserId(), role.getBusinessData().getServerId(),msgContent, LogChuanQiUtil.WORLD_CHAT);
		
		return true;
	}
}
