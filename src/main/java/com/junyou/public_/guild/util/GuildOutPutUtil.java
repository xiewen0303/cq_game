package com.junyou.public_.guild.util;

import java.util.List;

import com.junyou.err.AppErrorCode;
import com.junyou.public_.guild.entity.Guild;
import com.junyou.public_.guild.entity.GuildApply;
import com.junyou.public_.guild.entity.GuildMember;

public class GuildOutPutUtil {
	public static Object[] getMemberVo(GuildMember member){
		/**
		 * 	0 Number(角色guid)
			1 String(角色名字)
			2 int(角色VIP等级，0为非VIP)
			3 int(角色等级)
			4 int(战斗力)
			5 int(角色职业)
			6 int(总贡献)
			7 int(职位 1:会长,2:副会长,3:长老,4:普通成员)
			8 Number(离线时间戳,0为当前在线)
		 */
		return new Object[]{
				member.getUserRoleId()
				,member.getName()
				,member.getVip()
				,member.getLevel()
				,member.getZplus()
				,member.getConfigId()
				,member.getTotalGongxian()
				,member.getPostion()
				,member.isOnline()?0:member.getOfflineTime()};
	}
	
	public static Object[] getMyGuildVo(Guild guild){
		/**
		 * 	0 Number(guid)
			1 String(门派名字)
			2 String(门派公告)
			3 String(掌门人名字)
			4 int(门派资金)
			5 int(门派人数)
			6 int(门派等级)
			7 int(入门派条件(入门派配置id))
			8 int(本人在门派中的职位id)
			9 Number(本人在门派中的贡献),
			10 Array(领地战占领的地图id数组,没有传null)
		 */
		return new Object[]{
				guild.getId()
				,guild.getName()
				,guild.getNotice()
				,guild.getLeader().getName()
				,guild.getGold()
				,guild.getSize()
				,guild.getLevel()
				,guild.getApplyType()
				,null
				,null
				,null
		};
	}
	
	public static Object[] getGuildListVo(Guild guild){
		
		/**
		 * 	0 Number(guid)
			1 String(门派名字)
			2 int(门派权利)
			3 int(门派等级)
			4 int(门派人数)
			5 Number(门派战斗力总和)
			6 Array[String(地图id),String(地图id),...String(地图id)]
			7 Number(建会时间(时间戳))
			8 Array[[0:int(职位),1:String(名字),2:int(等级),3:Boolean(是否在线)],[0:int(职位),1:String(名字),2:int(等级),3:Boolean(是否在线)]...]
			9 入门派条件(入门派配置id)
			10是否是皇城霸主
			11是否是跨服云宫之巅冠军公会
		 */
		return new Object[]{
				guild.getId()
				,guild.getName()
				,null	//权利
				,guild.getLevel()
				,guild.getSize()
				,guild.getAllZplus()
				,null	//占领地图
				,guild.getCreateTime()
				,guild.getManagerList().toArray()
				,guild.getApplyType()
				,guild.isHcZbsWinner()
				,guild.isKfYunGongWinner()
		};
	}
	
	public static Object[] getManagerVo(GuildMember member){
		return new Object[]{
			member.getPostion()
			,member.getName()
			,member.getLevel()
			,member.isOnline()
		};
	}
	
	public static Object[] getApplyVo(GuildApply guildApply){
		return new Object[]{
				guildApply.getUserRoleId()
				,guildApply.getName()
				,guildApply.getVip()
				,guildApply.getLevel()
				,guildApply.getZplus()
				,guildApply.getConfigId()
		};
	}
	
	/**
	 * 审核成功
	 * @param agree
	 * @param userRoleIds
	 * @return
	 */
	public static Object[] shenpiSuccess(boolean agree,List<Long> userRoleIds){
		if(userRoleIds.size() < 1){
			return AppErrorCode.GUILD_OPERATE_FAIL;
		}
		return new Object[]{1
				,agree ? 1 : 0
				,userRoleIds.toArray()};
	}
	
	
}
