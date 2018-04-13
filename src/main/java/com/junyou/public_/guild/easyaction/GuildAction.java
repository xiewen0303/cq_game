package com.junyou.public_.guild.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.public_.guild.service.GuildService;
import com.junyou.public_.tunnel.PublicMsgQueue;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.GUILD_MODULE,groupName=EasyGroup.PUBLIC)
public class GuildAction {
	@Autowired
	private GuildService guildService;
	
	/**
	 * 创建公会
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CREATE_GUILD)
	public void tequanTimeOut(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String guildName = (String)data[0];
		String notice = (String)data[1];
		Object[] result = guildService.createGuild(userRoleId, guildName,notice);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.CREATE_GUILD, result);
	}
	/**
	 * 申请加入公会
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_APPLY)
	public void applyGuild(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guildId = LongUtils.obj2long(inMsg.getData());
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		Object[] result = guildService.applyGuild(userRoleId, guildId, publicMsgQueue);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_APPLY, result);
		publicMsgQueue.flush();
	}
	/**
	 * 拉取公会信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_MY_GUILD)
	public void getMyGuild(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = guildService.getGuildInfo(userRoleId);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GET_MY_GUILD, result);
	}
	
	/**
	 * 拉取公会申请列表信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_APPLY_LIST)
	public void guildApplyList(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = guildService.getApplyList(userRoleId);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_APPLY_LIST, result);
	}
	
	/**
	 * 审核公会申请
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_APPLY_SHENPI)
	public void shenpiApply(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int agree = (Integer)data[0];
		Object[] userRoleIds = (Object[])data[1];
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		Object[] result = guildService.shenpi(userRoleId,userRoleIds,agree == 1,publicMsgQueue);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_APPLY_SHENPI, result);
		publicMsgQueue.flush();
	}
	
	/**
	 * 拉取公会成员列表
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_MEMBER_LIST)
	public void getMembers(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = guildService.getAllMembers(userRoleId);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_MEMBER_LIST, result);
	}
	
	
	/**
	 * 拉取公会列表
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_LIST)
	public void getGuilds(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = guildService.getGuildList();
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_LIST, result);
	}
	
	/**
	 * 退出公会
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_EXIT)
	public void exitGuilds(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = guildService.exitGuild(userRoleId);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_EXIT, result);
	}
	
	/**
	 * 修改公告
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_CHANGE_NOTICE)
	public void changeNotice(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String notice = inMsg.getData();
		
		Object[] result = guildService.modifyNotice(userRoleId,notice);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_CHANGE_NOTICE, result);
	}
	
	/**
	 * 任命职务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_CHANGE_POSTION)
	public void changePostion(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		long targetRoleId = LongUtils.obj2long(data[0]);
		int postion = (Integer)data[1];
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		Object[] result = guildService.changePostion(userRoleId, targetRoleId, postion,publicMsgQueue);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_CHANGE_POSTION, result);
		publicMsgQueue.flush();
	}
	/**
	 * 把某些人踢出公会
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_KICK_MEMBER)
	public void kickMember(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] userRoleIds = inMsg.getData();
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		Object[] result = guildService.kickGuild(userRoleId,userRoleIds,publicMsgQueue);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_KICK_MEMBER, result);
		publicMsgQueue.flush();
	}
	/**
	 * 修改招收条件
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_CHANGE_ZHAOSHOU_TYPE)
	public void changeZhaoshouType(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int type = inMsg.getData();
		
		Object[] result = guildService.changeZhaoshouType(userRoleId,type);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_CHANGE_ZHAOSHOU_TYPE, result);
	}
	/**
	 * 拉取门派日志
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_GET_LOGS)
	public void getGuildLogs(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int start = (Integer)data[0];
		int size = (Integer)data[1];
		
		Object[] result = guildService.getGuildLogs(userRoleId, start, size);
		if(result != null){
			PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_GET_LOGS, result);
		}
	}
	
	/**
	 * 获取公会捐献信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_JUANXIAN_INFO)
	public void getGuildJuanxin(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = guildService.getGuildJuanXianInfo(userRoleId);
		if(result != null){
			PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_JUANXIAN_INFO, result);
		}
	}
	
	/**
	 * 公会捐献
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_JUANXIAN)
	public void guildJuanxian(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int type = (Integer)data[0];
		int value = CovertObjectUtil.object2Integer(data[1]);
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue(); 
		Object[] result = guildService.guildJuanxian(userRoleId, type, value, publicMsgQueue);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_JUANXIAN, result);
		publicMsgQueue.flush();
	}
	
	/**
	 * 公会升级
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_LEVEL_UP)
	public void guildLevelUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue(); 
		Object[] result = guildService.guildLevelUp(userRoleId, publicMsgQueue);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_LEVEL_UP, result);
		publicMsgQueue.flush();
	}
	/**
	 * 公会阁楼等级
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_GUILD_GELOU_LEVEL)
	public void gelouLevel(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Integer level = guildService.getGuildGeLouLevel(userRoleId);
		if(level != null){
			PublicMsgSender.send2One(userRoleId, ClientCmdType.GET_GUILD_GELOU_LEVEL, level);
		}
	}
	
	/**
	 * 公会阁楼升级
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_GELOU_UP)
	public void gelouUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue(); 
		Object[] result = guildService.schoolLevelUp(userRoleId,publicMsgQueue);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_GELOU_UP, result);
		publicMsgQueue.flush();
	}
	
	/**
	 * 获取门派兑换信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_GUILD_DUIHUAN_INFO)
	public void getDuihuanInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = guildService.getGuildDuihuanInfos(userRoleId);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GET_GUILD_DUIHUAN_INFO, result);
	}
	
	/**
	 * 门派兑换
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_DUIHUAN)
	public void guildDuihuan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String goodsId = (String)data[0];
		Integer count = (Integer)data[1];
		Object[] result = guildService.duihuan(userRoleId, goodsId,count);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_DUIHUAN, result);
	}
	/**
	 * 弹劾掌门
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_IMPEACH_LEADER)
	public void impeachLeader(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = guildService.impeachLeader(userRoleId);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_IMPEACH_LEADER, result);
	}
	
	/**
	 * 处理玩家改名
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MODIFY_NAME_EVENT_3)
	public void modifyNameHandle(Message inMsg) {
		Object[] data =  inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String afterName = (String) data[2];
		guildService.handleUserModifyNameEvent(userRoleId, afterName);
	}
	
}
