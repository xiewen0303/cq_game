package com.junyou.io.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.friend.entity.Friend;
import com.junyou.bus.friend.export.FriendExportService;
import com.junyou.bus.jinfeng.export.JinFengExportService;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ChatListenerFor37Event;
import com.junyou.event.ChatLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.io.ChatOutputWrapper;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.stage.export.TeamExportService;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.codec.AmfUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 聊天
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-31 下午4:24:24
 */
@Service
public class ChatService{
	
	@Autowired
	private JinFengExportService jinFengExportService;
	@Autowired 
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private TeamExportService teamExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private FriendExportService friendExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private QqExportService qqExportService;
	/**
	 * 私聊
	 * @param userRoleId
	 * @param receiverId
	 * @param sendTime
	 * @param publicMsgQueue
	 */
	public void chatPrivate(Long sendRoleId, Long receiverId,Object[] otherData) {
		
			//禁言处理
			if(jinFengExportService.isShutUp(sendRoleId)){
				return ;
			}
			
			if(otherData == null){
				return;
			}
			String chatMsg =  (String)otherData[0];
			
//			if(chatMsg == null ){
//				sendOneIoMsg( ClientCmdType.CHAT_PRIVATE, sendRoleId, AppErrorCode.MSG_NULL);
//				return ;
//			}
			
			if(chatMsg!= null && chatMsg.length() > GameConstants.CHAT_MSG_LEN){  
				sendOneIoMsg( ClientCmdType.CHAT_PRIVATE, sendRoleId, AppErrorCode.CHAT_TOO_LEN);
				return ;
			}
			
			if(!publicRoleStateExportService.isPublicOnline(receiverId)){
				return;//目标玩家不在线
			}
			
			RoleWrapper receiveRole = roleExportService.getLoginRole(receiverId);
			if(receiveRole == null){ 
				sendOneIoMsg( ClientCmdType.CHAT_PRIVATE, sendRoleId, AppErrorCode.ROLE_NOT_LOGIN);
				return ;
			}
			
			 
			RoleWrapper sendRole = roleExportService.getLoginRole(sendRoleId);
			if (null == receiveRole || null == sendRole ) {
				ChuanQiLog.error("聊天模块获取玩家在线信息失败\t sendRoleId"+sendRoleId+"\t receiveRoleId:"+receiverId);
				return ;
			}
			
			
			
			
//			VipWrapper receiveVip = vipInfoExportService.getVipInfo(receiveRole.getId());
//			VipWrapper sendVip = vipInfoExportService.getVipInfo(sendRoleId);
			
			if (sendRoleId.equals(receiveRole.getId())) {
				sendOneIoMsg( ClientCmdType.CHAT_PRIVATE, sendRoleId, AppErrorCode.NO_CHAT_SELF);
				return ;
			}
			 
			
			//黑名单不能聊天
			//1:黑名单中有接受者
			
			Friend friend = friendExportService.getFriend(sendRoleId);
			if(friend.getBlackIds()!=null && !"".equals(friend.getBlackIds())){
				if(friend.getBlackIds().contains(receiverId+GameConstants.F_SPLIT_CHAR)){
					sendOneIoMsg( ClientCmdType.CHAT_PRIVATE, sendRoleId, AppErrorCode.BLACKIDS_CONTAIN);
					return ;
				}
			}
			
			//判断是否可以私聊了
			boolean isPrivate = friendExportService.isPrivate(sendRoleId,receiverId);
			
//			//推送给发送者  
//			Object reChatVO = getChatVO(receiverId);
			long sendTime = GameSystemTime.getSystemMillTime();
			
			Object[] data = new Object[]{otherData[0],otherData[1],sendTime};
			
//			Object[] chatResult = new Object[]{1,reChatVO,data};
//			sendOneIoMsg(ClientCmdType.CHAT_PRIVATE, sendRoleId, chatResult);
			
			if(isPrivate){
				Object sendChatVO = getChatVO(sendRoleId);
				//推送给接收者  
				Object[] recResult = new Object[]{ sendChatVO,data};
				sendOneIoMsg( ClientCmdType.CHAT_PRIVATE_REPLY, receiverId, recResult);
				
				//自动回复
				autoReply(receiverId,sendRoleId);
			}
			
			//打印日志
			String logChatMsg = receiveRole.getName() + ":" + chatMsg;
			GamePublishEvent.publishEvent(new ChatLogEvent(sendRole.getName(),sendRole.getUserId(),sendRole.getId(),sendRole.getServerId(), logChatMsg,LogPrintHandle.CHAT_PRIVATE));
			if(PlatformConstants.is37()){//37平台增加聊天监控
				GamePublishEvent.publishEvent(new ChatListenerFor37Event(sendRole.getUserId(),sendRole.getName(),sendRole.getId(),receiveRole.getUserId(),receiveRole.getName(),chatMsg,sendRole.getLastLoginIp(),ChuanQiConfigUtil.getPlatformServerId()));
			}
 	}
	
	/**
	 * 自动回复
	 * @param sendId
	 * @param replyId
	 */
	private void autoReply(long sendId,long receiverId){
		Friend friend = friendExportService.getFriend(sendId);
		int replyState = friend.getReplyState().intValue();
		if( replyState != GameConstants.F_NO_HUIFU){
			sendOneIoMsg( ClientCmdType.CHAT_PRIVATE_AUTO, receiverId,new Object[]{sendId,new Object[]{replyState,GameSystemTime.getSystemMillTime()}});
		}
	}
	
	
	private Object[] getChatVO(long userRoleId){
		RoleWrapper role = getRoleWrapper(userRoleId);
		RoleVipWrapper vip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		String guildName = guildExportService.getGuildName(userRoleId);
		return new Object[]{role.getConfigId(),role.getId(),role.getName(),role.getLevel(),vip.getVipLevel(),guildName,role.getIsGm()};
	} 
 
	/**
	 * 世界
	 * @param roleId
	 * @param chatMsg
	 * @param logType {@link LogPrintHandle}
	 */
 	public void chat2Public(Long roleId,Object[] chatMsg) {
 		String chatMsgStr = (String)chatMsg[0];
 		if(chatMsgStr!= null && chatMsgStr.length() > GameConstants.CHAT_MSG_LEN){  
 			return ;
 		}	
		
 		Object[] msgRoleVO = getMsgRoleVo(roleId);
 		Object result =  ChatOutputWrapper.getMessage2Public(chatMsg, msgRoleVO);
 		//被禁言
 		if(jinFengExportService.isShutUp(roleId)){
 			//被禁言后，处理
 			shutUpSendSelf(roleId,ClientCmdType.CHAT_WORLD_MSG, result);
 			return ;
 		}
 		
 		sendAllIoMsg(ClientCmdType.CHAT_WORLD_MSG, result,roleId);
 		try {
 			BusMsgSender.send2BusInner(roleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.WORLD_CHAT, null});
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
 		RoleWrapper role = getRoleWrapper(roleId);
 		Object msgContent = (String)((Object[])chatMsg)[0]; 
 		//打印日志
 		GamePublishEvent.publishEvent(new ChatLogEvent(role.getName(),role.getUserId(),role.getId(),role.getServerId(), msgContent.toString(),LogPrintHandle.CHAT_WORLD));
 		if(PlatformConstants.is37()){//37平台增加聊天监控
 			GamePublishEvent.publishEvent(new ChatListenerFor37Event(role.getUserId(),role.getName(),role.getId(),"","",msgContent.toString(),role.getLastLoginIp(),ChuanQiConfigUtil.getPlatformServerId()));
 		}
	} 
 	/**
 	 * 主播世界聊天
 	 * @param roleId
 	 * @param chatMsg
 	 * @param logType {@link LogPrintHandle}
 	 */
 	public int taiWanZhuBoLiaoTian(String zbUserId,String zbName,String userId,String serverId,String type,String chatMsgStr) {
		
 		if(chatMsgStr!= null && chatMsgStr.length() > 100){  
 			return GameConstants.ZHUBO_CONTENT_MAX;
 		}	
 		//被禁言
 		/*if(jinFengExportService.isShutUp(roleId)){
 			return GameConstants.ZHUBO_BEI_JINYAN;
 		}*/
 		
 		if(GameConstants.ZHUBO_TYPE_SHIJIE.equals(type)){
 			
 			//发送给所有客户端
 			BusMsgSender.send2All(ClientCmdType.TAIWAN_ZHUBO_XIAOXI,new Object[]{zbName,type,chatMsgStr});
 		
 		}else if(GameConstants.ZHUBO_TYPE_SILIAO.equals(type)){
 			
 			UserRole userRole = roleExportService.getRoleFromDb(userId, serverId);
 			if(userRole == null){
 				return GameConstants.ZHUBO_NOT_ROLE;
 			}
 			Long roleId = userRole.getId();
 			//发送给单个客户端
 			BusMsgSender.send2One(roleId, ClientCmdType.TAIWAN_ZHUBO_XIAOXI,new Object[]{zbName,type,chatMsgStr});
 		}
 		return GameConstants.ZHUBO_SEND_SUCCESS;
 	} 
 	
 	/**
 	 * 被禁言后，消息只发给自己
 	 * @param roleId
 	 * @param cmd
 	 * @param result
 	 */
 	private void shutUpSendSelf(Long roleId,short cmd,Object result){
 		byte[] bytes = AmfUtil.convertMsg2Bytes(cmd,result);
 		
 		GameSession session = GameSessionManager.getInstance().getSession4RoleId(roleId);
 		if(session != null){
 			session.sendMsg(bytes);
 		}
 	}
 	
 	/**
 	 * 被禁言后，消息会再发给被禁言玩家的同IP的人(包含自己)[暂时没用上]
 	 * @param roleId
 	 * @param chatMsg
 	 */
 	protected void shutUpSendSameIpMsg(Long roleId,short cmd,Object result){
 		RoleWrapper role = getRoleWrapper(roleId);
 		if(role != null){
 			byte[] bytes = AmfUtil.convertMsg2Bytes(cmd,result);
 			Map<Long, Object> ipMap = GameSessionManager.getInstance().getRoleIdsByIp(role.getLastLoginIp());
 			if(ipMap != null && ipMap.size() > 0){
 				
 				for (long tmpRoleId : ipMap.keySet()) {
 					GameSession session = GameSessionManager.getInstance().getSession4RoleId(tmpRoleId);
 					if(session != null){
 						session.sendMsg(bytes);
 					}
				}
 			}
 		}
 	}
 
 	
 	public RoleWrapper getRoleWrapper(long userRoleId){
 		return roleExportService.getLoginRole(userRoleId);
 	}
 	
 	/**
	 * 喇叭信息
	 */
	public void hornMsg(Object[] msg,long userRoleId) {
		
		if(jinFengExportService.isShutUp(userRoleId)){
			return ;
		}
		
		Object[] errorCode = roleBagExportService.checkRemoveBagItemByGoodsType(GameConstants.GOODS_LABA, 1, userRoleId);
		if(errorCode!=null) {
			return ;	
		}
		//扣除喇叭
		roleBagExportService.removeBagItemByGoodsType(GameConstants.GOODS_LABA, 1, userRoleId, GoodsSource.USE_LABA, true, true);
		 
		
		PublicMsgSender.send2All(ClientCmdType.CHAT_HORN_MSG, msg);
		
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		
		if(role != null){
			String content = (String)msg[3]; 
	 		//打印日志
			GamePublishEvent.publishEvent(new ChatLogEvent(role.getName(),role.getUserId(),role.getId(),role.getServerId(), content,LogPrintHandle.CHAT_LABA));
			if(PlatformConstants.is37()){//37平台增加聊天监控
				GamePublishEvent.publishEvent(new ChatListenerFor37Event(role.getUserId(),role.getName(),role.getId(),"","",content,role.getLastLoginIp(),ChuanQiConfigUtil.getPlatformServerId()));
			}
 		}
	}

	/**
	 * 通过userRoleId获得对应的MsgRoleVo数据
	 * @param roleId
	 * @return
	 */
	public Object[] getMsgRoleVo(Long roleId) {
		Object[] result =new Object[7];
		
		int vip = 0;
		RoleVipWrapper roleVipWrapper = roleVipInfoExportService.getRoleVipInfo(roleId);
		if(roleVipWrapper != null) {
			vip = roleVipWrapper.getVipLevel();
		}
		 
		RoleWrapper role = roleExportService.getLoginRoleCheckGmType(roleId);
		if(role == null){
			ChuanQiLog.error("在线玩家数据没获取到,userRoleId:"+roleId);
			return result;
		}
		
		result[0] = roleId;
		result[1] = role.getName();
		result[2] = vip;  
		result[3] = role.getConfigId();
		result[4] = role.getLevel();
		result[5] = role.getIsGm();
		result[6] = qqExportService.getRoleQQPlatformInfo(roleId,false);
		return result;
	}
	
	/**
	 * 发送信息
	 * @param cmd
	 * @param members
	 * @param result
	 */
	public void sendOneIoMsg(short cmd,long userRoleId,Object result){
		byte[] bytes = AmfUtil.convertMsg2Bytes(cmd,result);  
		GameSession session = GameSessionManager.getInstance().getSession4RoleId(userRoleId);
		if(null != session){
			sesesionWriteFuture(bytes, session);
		}  
	}
	
	
	private void sesesionWriteFuture(byte[] bytes,GameSession session){
		session.sendMsg(bytes);
	}
	
	
	/**
	 * 发送信息
	 * @param cmd
	 * @param members
	 * @param result
	 */
	public void sendManyIoMsg(short cmd,Collection<Long> members,Object result){
		if(members == null ){
			return;
		}
		byte[] bytes = AmfUtil.convertMsg2Bytes(cmd,result);
		
		for(Long tmp : members){
			GameSession session = GameSessionManager.getInstance().getSession4RoleId(tmp);
			if(null != session){
				sesesionWriteFuture(bytes, session);
			}
		}
	}
	
	/**
	 * 发送信息
	 * @param cmd
	 * @param members
	 * @param result
	 */
	public void sendAllIoMsg(short cmd,Object result,long sendRoleId){
		Collection<GameSession> ioSessions = GameSessionManager.getInstance().getRoleListSession();
		
		byte[] bytes = AmfUtil.convertMsg2Bytes(cmd,result);
		for(GameSession session : ioSessions){
			if(null != session){
				sesesionWriteFuture(bytes, session);
			}
		}
	}

	
	/**
 	 * 队伍聊天
 	 * @param roleId
 	 * @param data
 	 * @param msgQueue
 	 */
 	public void teamChat(long roleId, Object[] data) {
		//禁言处理
		if(jinFengExportService.isShutUp(roleId)){
			return;
		}
		String chatMsgStr = (String)data[0];
		if(chatMsgStr!= null && chatMsgStr.length() > GameConstants.CHAT_MSG_LEN){  
			return ;
		}
		
		Long[]  memberIds = teamExportService.getTeamMemberIds(roleId);
		if(memberIds!= null){
			Object obj2= getMsgRoleVo(roleId);
			Object result = ChatOutputWrapper.getMessage2Public(data,obj2);
			
			for (Long memberId : memberIds) {
				sendOneIoMsg(ClientCmdType.CHAT_TEAM_MSG, memberId, result);
			}
			
			RoleWrapper roleWrapper = roleExportService.getLoginRole(roleId);
			if(roleWrapper != null){
		 		//打印日志 
				GamePublishEvent.publishEvent(new ChatLogEvent(roleWrapper.getName(),roleWrapper.getUserId(),roleId,roleWrapper.getServerId(), chatMsgStr,LogPrintHandle.CHAT_TEAM));
				if(PlatformConstants.is37()){//37平台增加聊天监控
					GamePublishEvent.publishEvent(new ChatListenerFor37Event(roleWrapper.getUserId(),roleWrapper.getName(),roleWrapper.getId(),"","",chatMsgStr,roleWrapper.getLastLoginIp(),ChuanQiConfigUtil.getPlatformServerId()));
				}
 			}
		}
	}

}
