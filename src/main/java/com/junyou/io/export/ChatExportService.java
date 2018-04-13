package com.junyou.io.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.cmd.ClientCmdType; 
import com.junyou.io.service.ChatService;
import com.junyou.public_.tunnel.PublicMsgSender;

@Service
public class ChatExportService {
	@Autowired
	private ChatService chatService;
	/**
	 * 系统信息
	 */
	public void systemMsg(Object[] msg) { 
		PublicMsgSender.send2All(ClientCmdType.SYSTEM_MSG, msg);
	}
 
	/**
	 * 通过userRoleId获得对应的MsgRoleVo数据
	 * @param roleId
	 * @return
	 */
	public Object[] getMsgRoleVo(Long roleId) {
		return chatService.getMsgRoleVo(roleId);
	}
	
	public int taiWanZhuBoLiaoTian(String zbUserId,String zbName,String userId,String serverId,String type,String chatMsg){
		return chatService.taiWanZhuBoLiaoTian(zbUserId,zbName,userId, serverId, type,chatMsg);
	}
}
