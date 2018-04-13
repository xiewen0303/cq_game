package com.junyou.public_.guild.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.configure.export.BagConfig;
import com.junyou.bus.bag.configure.export.BagConfigExportService;
import com.junyou.bus.bag.configure.export.StorageConfig;
import com.junyou.bus.bag.configure.export.StorageConfigExportService;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.manage.AbstractContainer;
import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.guild.configure.GuildConfigService;
import com.junyou.public_.guild.entity.Guild;
import com.junyou.public_.guild.entity.GuildConfig;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.service.GuildService;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class GuildExportService implements IFightVal {

	public long getZplus(long userRoleId, int fightPowerType) {
		if(fightPowerType == FightPowerType.GUILD_QZ){
			
			GuildMember guildMember = guildService.getGuildMember(userRoleId);
			if(guildMember == null){
				return 0;
			}
			
			Guild guild = guildService.getGuild(guildMember.getGuildId());
			if(guild == null){
				return 0;
			}
			GuildConfig guildConfig = guildConfigService.getGuildConfig(guild.getLevel());
			if(guildConfig == null){
				return 0;
			}
			return CovertObjectUtil.getZplus(guildConfig.getAttribute());
		}else if(fightPowerType == FightPowerType.CK_OPEN){
					AbstractContainer storageContainer = bagExportService.getContainer(ContainerType.STORAGEITEM, userRoleId);
					if(storageContainer != null){
						int sSlot = storageContainer.getEndSlot();
						StorageConfig sConfig=storageConfigExportService.getStorageConfigBySlot(sSlot);
						if(sConfig != null){
							Map<String, Long> sAttrs = sConfig.getAttrs();
							return CovertObjectUtil.getZplus(sAttrs);
						}
					}
		}else if(fightPowerType == FightPowerType.BAG_OPEN){
					AbstractContainer storageContainer = bagExportService.getContainer(ContainerType.BAGITEM, userRoleId);
					int bSlot = storageContainer.getEndSlot();


					BagConfig bConfig=bagConfigExportService.getBagConfigBySlot(bSlot);
					if(bConfig != null){
						Map<String,Long> bAttrs = bConfig.getAttrs();
						if(bAttrs != null){
							return  CovertObjectUtil.getZplus(bAttrs);
						}
					}
		}
	return 0;
	}

	@Autowired
	private GuildService guildService;

	@Autowired
	private RoleBagExportService bagExportService;

	@Autowired
	private GuildConfigService guildConfigService;
	@Autowired
	private StorageConfigExportService storageConfigExportService;
	@Autowired
	private BagConfigExportService bagConfigExportService;

	
	public void init(){
		guildService.init();
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		guildService.onlineHandle(userRoleId);
	}
	public void zhuanzhi(Long userRoleId,int configId){
		guildService.zhuanzhi(userRoleId, configId);
	}
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		try{
			guildService.offlineHandle(userRoleId);
		}catch (Exception e) {
			ChuanQiLog.error("{}下线时，公会下线业务异常:{}",userRoleId,e);
		}
	}
	/**
	 * 获取公会信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getGuildInfo(Long userRoleId){
		return guildService.getGuildInfo(userRoleId);
	}
	
	
	/**
	 * 获取公会名称
	 * @param guildId
	 * @return
	 */
	public String getGuildName(long userRoleId){
		GuildMember  guildMember = guildService.getGuildMember(userRoleId);
		if(guildMember == null){
			return null;
		}
		
		Guild  guild = guildService.getGuild(guildMember.getGuildId());
		if(guild == null ){
			return null;
		}
		
		return guild.getName();
	}
	/**
	 * 增加成员贡献度
	 * @param userRoleId
	 * @param gx
	 */
	public void addGongxian(Long userRoleId,int gx){
		guildService.addGongxian(userRoleId, gx);
	}
	/**
	 * 刷新所有在线成员数据
	 */
	public void refreshOnlineMembers(){
		guildService.refreshOnlineMembers();
	}
	
	public GuildMember getGuildMember(Long userRoleId){
		return guildService.getGuildMember(userRoleId);
	}
	
	public Object[] getGuildBaseInfo(Long guildId){
		Guild guild = guildService.getGuild(guildId);
		if(guild!=null){
			Object[] ret =new Object[4];
			ret[0] =  guild.getName();
			ret[1] = guild.getLeader().getUserRoleId();
			ret[2] = guild.getLeader().getName();
			ret[3] = guild.getLevel();
			return ret;
		}
		return null;
	}
	public int getGuildGeLouLevel(Long userRoleId){
		Integer gelou = guildService.getGuildGeLouLevel(userRoleId);
		if(gelou == null){
			gelou = 0;
		}
		return gelou;
	}
	
	/**
	 * 检测是否可拔旗
	 * @param userRoleId
	 * @return
	 */
	public Object[] flagCheck(Long userRoleId,int need){
		return guildService.flagCheck(userRoleId, need);
	}
	/**
	 * 拔旗消耗帮贡
	 * @param userRoleId
	 * @return
	 */
	public Object[] flagCost(Long userRoleId,int need){
		return guildService.flagCost(userRoleId, need);
	}
	
	/**
	 * 获取领地战地图增加经验、真气百分比
	 * @param guildId
	 * @return	int[]{0位经验加成，1位真气加成}
	 */
	public int[] getGuildMapExpZhenqiAdd(Long guildId){
		return guildService.getGuildMapExpZhenqiAdd(guildId);
	}
	/**
	 * 获取官职人员
	 */
	public List<GuildMember> getAllLeader(long guildId){
		return guildService.getAllLeader(guildId);
	}
	
	public String gmKickMember(Long guildId,Long userRoleId){
		return  guildService.gmKickMember(guildId,userRoleId);
	}
	
	public String gmChangeLeader(Long guildId,Long newLeader){
		return  guildService.gmChangeLeader(guildId,newLeader);
	}
	
	public String gmChangeGuildNotice(Long guildId,String notice){
		return  guildService.gmChangeGuildNotice(guildId,notice);
	}
	
	public String gmChangeGuildName(Long guildId,String guildName){
		return  guildService.gmChangeGuildName(guildId,guildName);
	}
}
