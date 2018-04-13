package com.junyou.bus.role.export;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.role.IncrRoleResp;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.service.UserCopyRoleService;
import com.junyou.bus.role.service.UserRoleService;
import com.junyou.bus.role.vo.LevelRankVo;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.vip.entity.VipConfig;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.export.VipConfigExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.export.ExpConfig;
import com.junyou.gameconfig.export.ExpConfigExportService;
import com.junyou.gameconfig.export.teleport.ICheckCallback;
import com.junyou.gameconfig.export.teleport.TeleportChecker;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.rank.export.ILevelRankExportService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.spring.container.DataContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 玩家角色Export
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午4:55:02
 */
@Service
public class RoleExportService implements ILevelRankExportService<LevelRankVo> ,IFightVal {

	public long getZplus(long userRoleId, int fightPowerType) {
		if(fightPowerType == FightPowerType.ROLE_LEVEL) {
			UserRole userRole = userRoleService.getUserRole(userRoleId);
			int level = userRole.getLevel();
			ExpConfig config = expConfigExportService.loadById(level);
			if(config == null) {
				ChuanQiLog.error("ExpConfig is null,level="+level);
				return 0;
			}
			return CovertObjectUtil.getZplus(config.getAttribute());
		} else if(fightPowerType == FightPowerType.ROLE_VIP){
			RoleVipWrapper roleVipWrapper = roleVipInfoExportService.getRoleVipInfo(userRoleId);
			int vipLevel = roleVipWrapper.getVipLevel();
			VipConfig vipConfig = vipConfigExportService.getVipConfigByLevel(vipLevel);
			if(vipConfig == null) {
				ChuanQiLog.error("VipConfig is null,level="+vipLevel);
				return 0;
			}
			return vipConfig.getZplus();
		}else{
			ChuanQiLog.error("not exist UserRoleService,fightPowerType="+fightPowerType);
		}
		return 0;
	}


	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private UserCopyRoleService userCopyRoleService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private ExpConfigExportService expConfigExportService;
	@Autowired
	private VipConfigExportService vipConfigExportService;
	
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public UserRole initUserRole(Long userRoleId){
		return userRoleService.initUserRole(userRoleId);
	}
	
	public int reChenmi(Long userRoleId){
		return userRoleService.reChenmi(userRoleId);
	}
	/**
	 * 玩家上线初始化（包括防沉迷，设置角色状态为在线）
	 * @param userRoleId
	 * @param ip
	 */
	public void logLoginTime(Long userRoleId, String ip){
		//玩家上线初始化
		userRoleService.logLoginTime(userRoleId, ip);
		//防沉迷
		userRoleService.onlineChenmi(userRoleId);
		//删除聊天CD数据
		removeChatCd(userRoleId);
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
	
	/**
	 * 下线业务处理（包括防沉迷，设置角色状态为离线）
	 * @param userRoleId
	 */
	public void logOfflineTime(Long userRoleId){
		//玩家下线处理
		userRoleService.logOfflineTime(userRoleId);
		//防沉迷
		userRoleService.offlineChenmi(userRoleId);
	}
	
	/**
	 * 创建角色直接访问库
	 * @param userId
	 * @param name
	 * @param configId
	 * @param isChenmi
	 * @param serverId
	 * @param ip
	 * @return
	 */
	public Object[] createRoleFromDb(String userId, String name, Integer configId,boolean isChenmi,String serverId,String ip,boolean isAutoCreate){
		return userRoleService.createRoleFromDb(userId, name, configId, isChenmi, serverId, ip,isAutoCreate,null);//false则不防沉迷
	}
	
	/**
	 * 创建角色直接访问库
	 * @param userId
	 * @param name
	 * @param configId
	 * @param isChenmi
	 * @param serverId
	 * @param ip
	 * @return
	 */
	public Object[] createRoleFromDb(String userId, String name, Integer configId,boolean isChenmi,String serverId,String ip,boolean isAutoCreate,Map<Short,Object> otherMsg){
		return userRoleService.createRoleFromDb(userId, name, configId, isChenmi, serverId, ip,isAutoCreate,otherMsg);//false则不防沉迷
	}
	
	/**
	 * 获取该账号玩家的所有的角色直接访问库
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public UserRole getRoleFromDb(String userId, String serverId){
		return userRoleService.getRoleFromDb(userId, serverId);
	}
	
	/**
	 * 获取已登陆用户信息
	 * @param userRoleId
	 * @return 用户如果不在线，则返回空
	 */
	public RoleWrapper getLoginRole(Long userRoleId){
		return userRoleService.getLoginRole(userRoleId);
	}
	/**
	 * 获取已登陆用户信息(检测用户Gm类型是否已失效)
	 * @param userRoleId
	 * @return 用户如果不在线，则返回空
	 */
	public RoleWrapper getLoginRoleCheckGmType(Long userRoleId){
		return userRoleService.getLoginRoleCheckGmType(userRoleId);
	}
	/**
	 * 获取已登陆用户信息
	 * @param userRoleId
	 * @param isChenmi
	 * @return 用户如果不在线，则返回空
	 */
	public RoleWrapper getLoginRole(Long userRoleId,boolean isChenmi){
		return userRoleService.getLoginRole(userRoleId,isChenmi);
	}
	/**
	 * 从数据库获取用户信息
	 * @param userRoleId
	 * @return
	 */
	public RoleWrapper getUserRoleFromDb(Long userRoleId){
		return userRoleService.getUserRoleFromDb(userRoleId);
	}
	/**
	 * 判别是否为本服玩家 
	 * @param userRoleId
	 * @return
	 */
	public boolean isLocalServerRole(Long userRoleId){
	    return null != userRoleService.getUserRoleFromDb(userRoleId);
	}
	
	
	/**
	 * 玩家传送验证
	 * @param userRoleId
	 * @param checker
	 * @return
	 */
	public ICheckCallback teleportCheck(Long userRoleId, TeleportChecker checker) {
		return userRoleService.teleportCheck(userRoleId, checker);
	}
	
	/**
	 * 新增经验(已推送升级和增加经验)包括防沉迷
	 * @param userRoleId
	 * @param exp
	 * @return null则没有升级成功
	 */
	public IncrRoleResp incrExp(Long userRoleId, Long exp){
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		IncrRoleResp incrRoleResp = userRoleService.incrExp(userRoleId, exp, busMsgQueue);
		if(incrRoleResp != null){
			busMsgQueue.flush();
			return incrRoleResp;
		}
		return null;
	}
	/**
	 * 消耗经验
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public Object[] costExp(Long userRoleId,long value){
		return userRoleService.costExp(userRoleId, value);
	}
	/**
	 * 直升等级级
	 * @param userRoleId
	 * @param exp
	 */
	public void addLevel(Long userRoleId,int level){
		if(level < 1){
			return;
		}
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		userRoleService.addLevel(userRoleId, level, busMsgQueue);
		busMsgQueue.flush();
	}
	/**
	 * GM修改角色等级
	 * @param userRoleId
	 * @param level
	 */
	public void gmChangeLevel(Long userRoleId,int level){
		userRoleService.gmChangeLevel(userRoleId, level);
	}
	
	/**
	 * 判定防沉迷收益
	 * @param userRoleId
	 * @return 0:收益全无，0.5收益减半，1:正常
	 */
	public double getChenmiIncomeRate(Long userRoleId) {
		return userRoleService.getChenmiIncomeRate(userRoleId);
	}
	
	/**
	 * 是否是首次充值
	 * @param userRoleId
	 * @return
	 */
	public boolean isFirstRecharge(Long userRoleId){
		return userRoleService.isFirstRecharge(userRoleId);
	}


	@Override
	public List<LevelRankVo> getLevelRankVo(int limit) {
		
		return userRoleService.getLevelRankVo(limit);
	}
	
	/**
	 * 真气是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughZhenqi(long userRoleId,long value){
		return userRoleService.isEnoughZhenqi(userRoleId, value);
	}
	/**
	 * 消耗真气
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costZhenqi(long userRoleId, long value){
		return userRoleService.costZhenqi(userRoleId, value);
	}
	/**
	 * 增加真气
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public void addZhenqi(long userRoleId, long value){
		userRoleService.addZhenqi(userRoleId, value);
	}
	
	/**
	 * 是否领取微端奖励
	 * @param userRoleId
	 * @return  true:领取  false:未领取
	 */
	public boolean isWeiDuanJL(Long userRoleId){
		return userRoleService.isWeiDuanJL(userRoleId);
	}

	/**
	 * 是否领取微端奖励
	 * @param userRoleId
	 * @return  true:领取  false:未领取
	 */
	public boolean isWeiDuanJL2(Long userRoleId){
		return userRoleService.isWeiDuanJL2(userRoleId);
	}
	/**
	 * 更新绑定手机奖励信息
	 * @param userRoleId
	 * @param status
	 */
	public void updatePhoneReward(Long userRoleId,Integer status){
		userRoleService.updatePhoneReward(userRoleId, status);
	}
	
	public UserRole getUserRole(Long userRoleId){
		return userRoleService.getUserRole(userRoleId);
	}
	
	/**
	 * 处理pps每日用户激活和活跃数据
	 * @param serverId
	 */
	public void getPPsDayRoleDatas(){
		String serverId = GameServerContext.getGameAppConfig().getServerId();
		
		//激活用户
		userRoleService.getPPsJHRoles(serverId);
		//活跃用户
		userRoleService.getPPsHYRoles(serverId);
	}
	
	/**
	 * 修改用户gm类型
	 * @param userId
	 * @param serverId
	 * @param gmType
	 * @param keepTime
	 * @return
	 */
	public String changeGmType(String userId,String serverId,Integer gmType,Long keepTime){
		return userRoleService.changeGmType(userId, serverId, gmType, keepTime);
	}
	
	/**
	 * 发送上小时创号信息
	 */
	public void sendCreateInfoJob(){
		//TODO wind 这个地方不知道在哪里接收数据
		//userRoleService.sendHourCreateInfo();
	}
	/**
	 * 越南版 根据一个号负责多个号
	 * 只需要以下功能：
	 * 妖神霸体 + 装备（包括强化等级）+神器+人物等级+御剑+翅膀+糖宝+天工+天裳+器灵+宠物+时装 +技能
	 */
	public Map<String, String> copyRoleByRole(Map<String, Object> param){
		return userCopyRoleService.copyRoleByRole(param);
	}
	
	/**
	 * 查询角色在全服的等级排名
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public long getUserServerRank(String userId,String serverId){
		return userRoleService.getUserServerRank(userId, serverId);
	}
	
	/**
	 * 获取总注册人数
	 * @return
	 */
	public int getTotalRegistCount(){
		return userRoleService.getTotalRegistCount();
	}
	
	/**
	 * 获取时间区间内注册人数
	 * @return
	 */
	public int getTimeRegistCount(long startTime,long endTime){
		return userRoleService.getTimeRegistCount(startTime, endTime);
	}
}