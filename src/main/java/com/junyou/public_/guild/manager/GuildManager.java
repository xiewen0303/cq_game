package com.junyou.public_.guild.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.junyou.public_.guild.configure.GuildConfigService;
import com.junyou.public_.guild.dao.GuildLogDao;
import com.junyou.public_.guild.entity.Guild;
import com.junyou.public_.guild.entity.GuildConfig;
import com.junyou.public_.guild.entity.GuildMember;

public class GuildManager {
	private GuildManager(){}
	private static GuildManager manager = new GuildManager();
	public static GuildManager getManager(){return manager;}
	
	private List<Guild> guildOrders;
	private Map<Long,Guild> guildMap = new HashMap<>();
	private GuildComparator guildComparator = new GuildComparator();
	private boolean needSort = true;
	
	private Object[] guildList;
	
	private Map<Long,GuildMember> allMembers = new HashMap<>();
	
	private GuildLogDao guildLogDao;
	
	/**
	 * 初始化
	 * @param list
	 */
	public void init(List<Guild> list,List<GuildMember> memberList,GuildLogDao guildLogDao,GuildConfigService guildConfigService){
		this.guildLogDao = guildLogDao;
		
		initGuild(list,guildLogDao,guildConfigService);
		
		initGuildMember(memberList);
		
		sort();
	}
	/**
	 * 初始化公会列表
	 * @param list
	 */
	private void initGuild(List<Guild> list,GuildLogDao guildLogDao,GuildConfigService guildConfigService){
		guildOrders = list;
		for (Guild guild : list) {
			GuildConfig config = guildConfigService.getGuildConfig(guild.getLevel());
			guild.setMaxCount(config.getMaxCount());
			guildLogDao.initLogs(guild);
			guildMap.put(guild.getId(), guild);
			//容错之前数据
			if(guild.getSchool() == null){
				guild.setSchool(1);
			}
		}
	}
	
	/**
	 * 初始化成员列表
	 * @param list
	 */
	private void initGuildMember(List<GuildMember> list){
		long guildId = -1;
		List<GuildMember> memberList = null;
		for (GuildMember guildMember : list) {
			if(guildMember.getGuildId() != guildId){
				//初始化时成员列表是根据公会id排序的，所以公会id变更则表示上一个公会的所有成员已加载完毕，执行成员初始化
				Guild guild = guildMap.get(guildId);
				if(guild != null){//容错公会不存在
					GuildMemberManager guildMemberManager = new GuildMemberManager();
					guildMemberManager.init(memberList);
					guild.setGuildMemberManager(guildMemberManager);
				}
				//new对象，防止对象引用问题，导致公会成员列表出错
				memberList = new ArrayList<>();
				guildId = guildMember.getGuildId();
			}
			memberList.add(guildMember);
			allMembers.put(guildMember.getUserRoleId(), guildMember);
		}
		//最后一个公会不会在循环中初始化成员
		Guild guild = guildMap.get(guildId);
		if(guild != null){//容错公会不存在
			GuildMemberManager guildMemberManager = new GuildMemberManager();
			guildMemberManager.init(memberList);
			guild.setGuildMemberManager(guildMemberManager);
		}
		
		checkGuild();
	}
	/**
	 * 公会检测
	 */
	private void checkGuild(){
		Iterator<Guild> iterator= guildOrders.iterator();
		while(iterator.hasNext()){
			Guild guild = iterator.next();
			if(!guild.isInit()){
				guildMap.remove(guild.getId());
				iterator.remove();
			}
		}
	}
	/**
	 * 新增公会
	 * @param guild
	 */
	public void addGuild(Guild guild){
		guildOrders.add(guild);
		guildMap.put(guild.getId(), guild);
		GuildMember guildMember = guild.getLeader();
		allMembers.put(guildMember.getUserRoleId(), guildMember);
		needSort = true;
	}
	/**
	 * 新增成员
	 * @param guildMember
	 */
	public void addGuildMember(GuildMember guildMember){
		allMembers.put(guildMember.getUserRoleId(), guildMember);
	}
	/**
	 * 减少成员
	 * @param guildMember
	 */
	public void removeGuildMember(Long userRoleId){
		allMembers.remove(userRoleId);
	}
	
	/**
	 * 解散公会
	 * @param guildId	公会id
	 * @return	
	 */
	public void removeGuild(Long guildId){
		Guild guild = guildMap.remove(guildId);
		guildOrders.remove(guild);
		guildList = null;
	}
	
	/**
	 * 公会排序
	 */
	private void sort(){
		if(needSort){
			Collections.sort(guildOrders, guildComparator);
			needSort = false;
			guildList = null;
		}
	}
	/**
	 * 获取玩家公会信息
	 * @param guildId
	 * @return
	 */
	public Guild getGuild(Long guildId){
		return guildMap.get(guildId);
	}
	/**
	 * 获取玩家公会成员信息
	 * @param userRoleId
	 * @return
	 */
	public GuildMember getGuildMember(long userRoleId){
		return allMembers.get(userRoleId);
	}
	/**
	 * 判断是否有公会
	 * @param userRoleId
	 * @return
	 */
	public boolean hasGuild(Long userRoleId){
		return allMembers.containsKey(userRoleId);
	}
	
	public Object[] getGuildList(){
		sort();
		if(guildList == null){
			List<Object[]> list = new ArrayList<>();
			for (Guild guild : guildOrders) {
				list.add(guild.getGuildListVo());
			}
			guildList = list.toArray();
		}
		return guildList;
	}
	
	/**
	 * 停机处理
	 */
	public void stopHandle(){
		if(guildLogDao != null){
			for (Guild guild : guildOrders) {
				guild.finalWrite();
				guildLogDao.writeLogFile(guild);
			}
		}
	}
	
	/**
	 * 刷新所有公会战力
	 */
	public void refreshAllGuild(){
		if(guildOrders == null){
			return;
		}
		List<Guild> list = new ArrayList<>(guildOrders);
		for (Guild guild : list) {
			guild.reCalZplus();
		}
		needSort = true;
	}
	
	public void needSort(){
		needSort = true;
	}
}
