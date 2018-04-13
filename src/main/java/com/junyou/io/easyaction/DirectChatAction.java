package com.junyou.io.easyaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.jinfeng.export.JinFengExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.setting.export.RoleSettingExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.event.ChatListenerFor37Event;
import com.junyou.event.ChatLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.ChatPublicConfig;
import com.junyou.io.ChatOutputWrapper;
import com.junyou.io.GameSession;
import com.junyou.io.Message.IoMessage;
import com.junyou.io.global.GameSessionManager;
import com.junyou.io.service.ChatService;
import com.junyou.log.LogPrintHandle;
import com.junyou.module.GameModType;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.help.GongGongServiceHelper;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.spring.container.DataContainer;

@Controller
@EasyWorker(moduleName = GameModType.CHAT_MODULE)
public class DirectChatAction {
	
	
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private ChatService chatService;
	@Autowired
	private JinFengExportService jinFengExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired 
	private GuildExportService guildExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RoleSettingExportService roleSettingExportService;
	
	/**
	 * 修改用户场景状态为非场景中
	 * @param message
	 */
	@EasyMapping(mapping=InnerCmdType.leaveStage_cmd)
	public void leaveStage(IoMessage message){
		
		GameSession session = GameSessionManager.getInstance().getSession4RoleId(message.getRoleId());
		if(null != session){
			synchronized (session) {
				session.setInStage(false);
			}
		}
	}
	
	/**
	 * 公会聊天<br>
	 * 广播给公会在线人员
	 * @param [roleId,roleName,msg]
	 */
	@EasyMapping(mapping = ClientCmdType.CHAT_GUILD_MSG)
	public void guildChat(IoMessage message){
		
		Object[] tmps = (Object[]) message.getMsgSource();
		Object cmd = tmps[0];
		Object[] data = (Object[])tmps[1];
		
		Long roleId = message.getRoleId();
		String msg = (String)data[0];
		
		//在聊天CD中,不处理消息
		if(isChatCding(roleId,false)){
			return;
		}
		
		//禁言处理
		if(jinFengExportService.isShutUp(roleId)){
			return;
		}
		 
		if(msg!= null && msg.length() > GameConstants.CHAT_MSG_LEN){  
			return ;
		}
		
		
		List<Long> onlineMembers = dataContainer.getData(GameConstants.GUILD_CHAT_ROLE_GUILD_MEMBER_MAP, roleId.toString());
		if(null != onlineMembers && onlineMembers.size() > 0){
			Object obj2 = chatService.getMsgRoleVo(roleId);
			Object result =  ChatOutputWrapper.getMessage2Public(data, obj2);
			chatService.sendManyIoMsg((short)cmd, onlineMembers, result);
		}
		 
		//打印日志
		RoleWrapper role = roleExportService.getLoginRole(roleId);
		String logMsg = guildExportService.getGuildName(roleId)+":"+msg;
		
		GamePublishEvent.publishEvent(new ChatLogEvent(role.getName(),role.getUserId(),role.getId(),role.getServerId(), logMsg,LogPrintHandle.CHAT_GUILD));
		if(PlatformConstants.is37()){//37平台增加聊天监控
			GamePublishEvent.publishEvent(new ChatListenerFor37Event(role.getUserId(),role.getName(),role.getId(),"","",msg,role.getLastLoginIp(),ChuanQiConfigUtil.getPlatformServerId()));
		}
	}
	 
	
	/**
	 * 公会成员上线  
	 * @param [guidId,memberId]
	 */
	@EasyMapping(mapping = InnerCmdType.GUILD_MEMBER_ADD_CHAT)
	public void guildMemberOnline(IoMessage message){
		Object[] tmps = (Object[]) message.getMsgSource();
		Object[] data = (Object[])tmps[1];
		
		Long guildId = (Long)data[0];
		Long memberId = (Long)data[1];
		
		List<Long> onlineMembers = dataContainer.getData(GameConstants.GUILD_CHAT_COMPONENT_NAME, guildId.toString());
		if(null == onlineMembers){
			onlineMembers = new ArrayList<Long>();
			dataContainer.putData(GameConstants.GUILD_CHAT_COMPONENT_NAME, guildId.toString(), onlineMembers);
		}
		
		if(!onlineMembers.contains(memberId)){
			onlineMembers.add(memberId);
		}
		
		//增加人和帮会成员索引
		dataContainer.putData(GameConstants.GUILD_CHAT_ROLE_GUILD_MEMBER_MAP, memberId.toString(), onlineMembers);
	}
	
	/**
	 * 解散公会
	 * @param [guidId,memberId]
	 */
	@EasyMapping(mapping = InnerCmdType.GUILD_MEMBER_CLEAR)
	public void guildMemberJS(IoMessage message){
		Long guildId = message.getData();
		
		List<Long> onlineMembers = dataContainer.getData(GameConstants.GUILD_CHAT_COMPONENT_NAME, guildId.toString());
		if(dataContainer !=null ){
			dataContainer.removeData(GameConstants.GUILD_CHAT_COMPONENT_NAME, guildId.toString());
			for (Long memberId : onlineMembers) {
				dataContainer.removeData(GameConstants.GUILD_CHAT_ROLE_GUILD_MEMBER_MAP, memberId.toString());
			}
		}
	}
	
	/**
	 * 公会成员下线  
	 * @param [guidId,memberId]
	 */
	@EasyMapping(mapping = InnerCmdType.GUILD_MEMBER_REMOVE_CHAT)
	public void guildMemberOffline(IoMessage message){
		
		Object[] tmps = (Object[]) message.getMsgSource();
		Object[] data = (Object[])tmps[1];
		
		Long guildId = (Long)data[0];
		Long memberId = (Long)data[1];
		
		List<String> onlineMembers = dataContainer.getData(GameConstants.GUILD_CHAT_COMPONENT_NAME, guildId.toString());
		if(null != onlineMembers){
			onlineMembers.remove(memberId);
		}
		
		//清除人和帮会成员索引
		dataContainer.removeData(GameConstants.GUILD_CHAT_ROLE_GUILD_MEMBER_MAP, memberId.toString());
		removeChatCd(memberId);
	}
	
	/**
	 * 私聊频道
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHAT_PRIVATE)
	public void chatPrivate(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData(); 
		
		Long receiverId = LongUtils.obj2long(data[0]);
		
		Object[] dataOther = (Object[])data[1];
		
//		Long sendTime = LongUtils.obj2long(data[2]);
		if(!publicRoleStateExportService.isPublicOnline(receiverId)){
			return;
		}
		//对方是否拒绝私聊
		if(roleSettingExportService.isSetting(receiverId, GameConstants.SYSTEM_SETTING_TYPE_CHAT)){
			return;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return;
		}
		//聊天等级判断
		ChatPublicConfig chatPublicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_CHAT);
		if(chatPublicConfig != null && chatPublicConfig.getLevel() > role.getLevel()){
			return;
		}
		
		//在聊天CD中,不处理消息
//		if(isChatCding(userRoleId,false)){
//			return;
//		}
		
		chatService.chatPrivate(userRoleId,receiverId, dataOther);
 
	}
	
	 
	/**
	 * 世界频道
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHAT_WORLD_MSG)
	public void worldChat(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		 
		Object[] chatMsg =(Object[])inMsg.getData();
		
		//在聊天CD中,不处理消息
		if(isChatCding(roleId,true)){
			return;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(roleId);
		if(role == null){
			return;
		}
		//聊天等级判断
		ChatPublicConfig chatPublicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_CHAT);
		if(!role.isNoCheckChatLevel() && chatPublicConfig != null && chatPublicConfig.getLevel() > role.getLevel()){
			return;
		}
		
		chatService.chat2Public(roleId, chatMsg);
	}
	
	/**
	 * 队伍频道
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHAT_TEAM_MSG)
	public void teamChat(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		
		//在聊天CD中,不处理消息
		if(isChatCding(roleId,false)){
			return;
		}
		
		Object[] data = inMsg.getData();
		
		chatService.teamChat(roleId,data);
	}
	
	/**
	 * 喇叭
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHAT_HORN_MSG)
	public void hornChat(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		Object[] msg = inMsg.getData(); 
		
		chatService.hornMsg(msg,userRoleId);
	}
	
	
	/**
	 * 是否在聊天cd中
	 * @param userRoleId
	 * @param world	
	 * @return true:不可以聊天，false：可以聊天
	 */
	private boolean isChatCding(Long userRoleId,boolean world){
		String type = GameConstants.CHAT_NO_PUBLIC_STR;
		if(world){
			type = GameConstants.CHAT_PUBLIC_STR;
		}
		Long dataTime = dataContainer.getData(type, userRoleId.toString());
		if(dataTime == null){
			Long expireTime = GameSystemTime.getSystemMillTime() + (GameConstants.CHAT_PUBLIC_CD * 1000L); 
			dataContainer.putData(type, userRoleId.toString(), expireTime);
			return false;
		}else{
			//验证是否在CD时间内
			if(GameSystemTime.getSystemMillTime() >= dataTime){
				Long expireTime = GameSystemTime.getSystemMillTime() + (GameConstants.CHAT_PUBLIC_CD * 1000L); 
				dataContainer.putData(type, userRoleId.toString(), expireTime);
				return false;
			}else{
				
				/*
				 * 非法聊天业务
				 */
				Integer errTimes = dataContainer.getData(GameConstants.CHAT_PUBLIC_TIMES, userRoleId.toString());
				if(errTimes == null){
					errTimes = 0;
				}
//				ChuanQiLog.debug("roleId:{},times:{}",userRoleId,errTimes);
				
				//错误次数加1
				errTimes = errTimes + 1;
				if(errTimes >= 20){
					//达到20次容错次数，直接禁言半小时
					jinFengExportService.shutUp(userRoleId, GameConstants.CHAT_SHUT_UP_SECOND);
				}else if(errTimes >= 10){
					//达到10次容错次数，直接禁言5分钟
					jinFengExportService.shutUp(userRoleId, GameConstants.CHAT_SHUT_UP_FIRST);
				}
				dataContainer.putData(GameConstants.CHAT_PUBLIC_TIMES, userRoleId.toString(), errTimes);
				
				return true;
			}
		}
	}
	
	
	/**
	 * 删除聊天CD数据
	 * @param userRoleId
	 */
	private void removeChatCd(Long userRoleId){
		dataContainer.removeData(GameConstants.CHAT_PUBLIC_STR, userRoleId.toString());
		dataContainer.removeData(GameConstants.CHAT_NO_PUBLIC_STR, userRoleId.toString());
		dataContainer.removeData(GameConstants.CHAT_PUBLIC_TIMES, userRoleId.toString());
	}
	
}
