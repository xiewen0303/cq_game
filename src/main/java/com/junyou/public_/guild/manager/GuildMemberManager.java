package com.junyou.public_.guild.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.util.GuildOutPutUtil;
import com.junyou.public_.guild.util.GuildUtil;
import com.junyou.utils.datetime.GameSystemTime;

public class GuildMemberManager {
	private Map<Long,GuildMember> members = new HashMap<>();
	private GuildMember leader;
	private long allZplus;
	/**管理员列表(前端格式)*/
	private List<Object[]> managerList = new ArrayList<>();
	private boolean managerChange;

	public GuildMemberManager(){};
	/**
	 *	初始化管理器，带会长信息
	 * @author LiuYu
	 * @date 2015-1-23 下午6:27:38
	 */
	public GuildMemberManager(GuildMember leader){
		this.leader = leader;
		members.put(leader.getUserRoleId(), leader);
		managerList.add(GuildOutPutUtil.getManagerVo(leader));
		allZplus = leader.getZplus();
	}
	/**
	 * 获取会长
	 * @return
	 */
	public GuildMember getLeader() {
		return leader;
	}
	
	/**
	 * 获取当前公会总人数
	 * @return
	 */
	public int getSize(){
		return members.size();
	}
	/**
	 * 初始化成员列表
	 * @param list
	 */
	public void init(List<GuildMember> list){
		for (GuildMember guildMember : list) {
			members.put(guildMember.getUserRoleId(), guildMember);
			if(leader == null && GameConstants.GUILD_LEADER_POSTION == guildMember.getPostion()){
				leader = guildMember;
			}
			if(GuildUtil.isLeaderOrViceLeader(guildMember.getPostion())){
				managerList.add(GuildOutPutUtil.getManagerVo(guildMember));
			}
			allZplus += guildMember.getZplus();
		}
	}
	/**
	 * 新增成员
	 * @param guildMember
	 */
	public void addMember(GuildMember guildMember){
		members.put(guildMember.getUserRoleId(), guildMember);
		allZplus += guildMember.getZplus();
	}
	/**
	 * 减少成员
	 * @param userRoleId
	 */
	public void removeMember(long userRoleId){
		GuildMember guildMember = members.remove(userRoleId);
		allZplus -= guildMember.getZplus();
		if(GuildUtil.isLeaderOrViceLeader(guildMember.getPostion())){
			managerChange = true;
		}
	}
	/**
	 * 成员上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(long userRoleId){
		GuildMember guildMember = members.get(userRoleId);
		if(guildMember != null){
			guildMember.setOnline(true);
		}
	}
	/**转职变职业*/
	public void zhuanzhi(long userRoleId,int configId){
		GuildMember guildMember = members.get(userRoleId);
		if(guildMember != null){
			guildMember.setConfigId(configId);
		}
	}
	/**
	 * 成员下线业务
	 * @param userRoleId
	 * @param level
	 * @param zplus
	 */
	public void offlineHandle(long userRoleId){
		GuildMember guildMember = members.get(userRoleId);
		if(guildMember != null){
			guildMember.setOfflineTime(GameSystemTime.getSystemMillTime());
			guildMember.setOnline(false);
		}
	}
	/**
	 * 获取全部成员列表
	 * @return
	 */
	public List<GuildMember> getAllMembers(){
		return new ArrayList<>(members.values());
	}
	
	/**
	 * 获取所有在线成员id数组
	 * @return
	 */
	public Long[] getRoleIds(){
		List<Long> roleIds = getOnlineRoleIds();
		return roleIds.toArray(new Long[roleIds.size()]);
	}
	/**
	 * 获取所有在线成员id
	 * @return
	 */
	public List<Long> getOnlineRoleIds(){
		List<Long> roleIds = new ArrayList<>();
		for (GuildMember member : members.values()) {
			if(member.isOnline()){
				roleIds.add(member.getUserRoleId());
			}
		}
		return roleIds;
	}
	/**
	 * 获取该职位人数
	 * @param postion 职位id
	 * @return
	 */
	public int getPostionCount(int postion){
		int count = 0;
		for (GuildMember guildMember : members.values()) {
			if(guildMember.getPostion() == postion)count++;
		}
		return count;
	}
	/**
	 * 变更会长
	 * @param guildMember
	 */
	public void changeLeader(GuildMember guildMember){
		BusMsgSender.send2BusInner(leader.getUserRoleId(), InnerCmdType.TERRITORY_LEADER_OFF, null);
		BusMsgSender.send2BusInner(guildMember.getUserRoleId(), InnerCmdType.TERRITORY_LEADER_ON, guildMember.getGuildId());
		leader = guildMember;
		managerChange = true;
	}

	public long getAllZplus() {
		return allZplus;
	}
	/**
	 * 重新计算总战力
	 */
	public void reCalZplus(){
		allZplus = 0;
		for (GuildMember guildMember : members.values()) {
			allZplus += guildMember.getZplus();
		}
	}
	/**
	 * 管理层变动
	 */
	public void changeManager(){
		managerChange = true;
	}
	/**
	 * 获取管理层列表
	 * @return
	 */
	public List<Object[]> getManagerList() {
		if(managerChange){
			managerChange = false;
			managerList.clear();
			for (GuildMember guildMember : members.values()) {
				if(GuildUtil.isLeaderOrViceLeader(guildMember.getPostion())){
					managerList.add(GuildOutPutUtil.getManagerVo(guildMember));
				}
			}
		}
		return managerList;
	}
}
