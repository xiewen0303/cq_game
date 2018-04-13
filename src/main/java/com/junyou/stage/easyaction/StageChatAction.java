package com.junyou.stage.easyaction;

import com.kernel.pool.executor.Message;

/**
 * @description 聊天
 * @author ShiJie Chi
 * @date 2012-3-13 下午4:44:07 
 */
//@Controller
//@EasyWorker
public class StageChatAction {
	
	/**
	 * 聊天
	 */
//	@Autowired
//	private StageChatService chatService;
//	@Autowired
//	private IJinYanExportService jinYanExportService;
	
	/**
	 * 附近消息
	 * @param  [0:fk(发送者key),1:fn(发送者名字),2:m(发送的信息)]
	 * @return [0:fk(发送者key),1:fn(发送者名字),2:m(发送的信息)]
	 */
//	@EasyMapping(mapping = StageCommands.CHAT_NEARBY)
	public void nearbyMsg(Message inMsg){
		
//		Long roleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId();
		
		//被禁言
//		if(jinYanExportService.isShutUp(roleId)){
//			return;
//		}
		
//		chatService.nearbyMsg(roleId,stageId,(Object[])inMsg.getData());
		
	}
	
	
	/**
	 * 世界
	 * @param inMsg
	 */
//	@EasyMapping(mapping = StageCommands.WORLD_MSG)
	public void worldChat(Message inMsg) {
//		Long roleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId();
		
		//被禁言
//		if(jinYanExportService.isShutUp(roleId)){
//			return;
//		}
		
//		Object chatMsg = inMsg.getData();
//		boolean bln = chatService.worldChat(roleId, stageId,chatMsg);
//		if (bln) {
//			StageMsgSender.send2All(StageCommands.WORLD_MSG, chatMsg);
//		}
	}	
	
	
}
