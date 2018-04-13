package com.junyou.public_.guild.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.gameconfig.constants.EffectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.daytask.export.TaskDayExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.guild.dao.RoleGuildDuihuanDao;
import com.junyou.bus.guild.entity.RoleGuildDuihuan;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.err.HttpErrorCode;
import com.junyou.event.GuildChangePositionLogEvent;
import com.junyou.event.GuildDuihuanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GuildPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.io.swap.IoMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.configure.GuildConfigService;
import com.junyou.public_.guild.configure.GuildDuihuanConfigService;
import com.junyou.public_.guild.configure.GuildGeLouConfigService;
import com.junyou.public_.guild.configure.GuildQuanXianConfigService;
import com.junyou.public_.guild.configure.GuildZhaoShouConfigService;
import com.junyou.public_.guild.dao.GuildApplyDao;
import com.junyou.public_.guild.dao.GuildDao;
import com.junyou.public_.guild.dao.GuildLogDao;
import com.junyou.public_.guild.dao.GuildMemberDao;
import com.junyou.public_.guild.entity.Guild;
import com.junyou.public_.guild.entity.GuildApply;
import com.junyou.public_.guild.entity.GuildConfig;
import com.junyou.public_.guild.entity.GuildDuihuanConfig;
import com.junyou.public_.guild.entity.GuildGeLouConfig;
import com.junyou.public_.guild.entity.GuildLog;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.entity.GuildQuanXianConfig;
import com.junyou.public_.guild.entity.GuildZhaoShouConfig;
import com.junyou.public_.guild.manager.GuildLogManager;
import com.junyou.public_.guild.manager.GuildManager;
import com.junyou.public_.guild.manager.GuildMemberManager;
import com.junyou.public_.guild.manager.SortMemberComparator;
import com.junyou.public_.guild.util.GuildOutPutUtil;
import com.junyou.public_.guild.util.GuildUtil;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.public_.tunnel.PublicMsgQueue;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.CodeUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.name.RoleFieldCheck;
import com.junyou.utils.name.YYTerminator;
import com.junyou.utils.number.LongUtils;
import com.kernel.data.dao.QueryParamMap;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class GuildService {

	@Autowired
	private GuildDao guildDao;
	@Autowired
	private GuildMemberDao guildMemberDao;
	@Autowired
	private GuildLogDao guildLogDao;
	@Autowired
	private GuildApplyDao guildApplyDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private GuildQuanXianConfigService guildQuanXianConfigService;
	@Autowired
	private GuildConfigService guildConfigService;
	@Autowired
	private GuildZhaoShouConfigService guildZhaoShouConfigService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleGuildDuihuanDao roleGuildDuihuanDao;
	@Autowired
	private GuildGeLouConfigService guildGeLouConfigService;
	@Autowired 
	private TaskDayExportService taskDayExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private GuildDuihuanConfigService guildDuihuanConfigService;
	
	@Autowired 
	private TerritoryExportService territoryExportService;
	@Autowired 
	private HcZhengBaSaiExportService hcZhengBaSaiExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	
	private SortMemberComparator sortMemberComparator =  new SortMemberComparator();
	
	private boolean canGongGao = false;//当前是否可以发送公告
	private long canGongGaoTime;//可以发送公告的时间
	private Object[] gongGaoCode;
	
	public void init(){
		if(KuafuConfigPropUtil.isKuafuServer()){//跨服服务器无需初始化
			return;
		}
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		List<Guild> guildList = guildDao.getRecords(queryParams);
		List<GuildMember> memberList = guildMemberDao.initGuildMember(queryParams);
		GuildManager.getManager().init(guildList, memberList,guildLogDao,guildConfigService);
		Long guildId = hcZhengBaSaiExportService.getHcZbsWinerGuildId();
		Guild guild = GuildManager.getManager().getGuild(guildId);
		if(guild != null){
			guild.setHcZbsWinner(true);
		}
	}
	/**
	 * 当前是否可修改公告
	 * @return
	 */
	private boolean isCanGongGao(){
		if(canGongGao){
			return true;
		}
		if(canGongGaoTime <= 0){
			GuildPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_GUILD);
			int day = ServerInfoServiceManager.getInstance().getKaifuDays();
			int need = config.getDay() - day;
			if(need < 0){
				canGongGao = true;
				return canGongGao;
			}
			canGongGaoTime = DatetimeUtil.getCalaDay24Time(need);
			gongGaoCode = new Object[]{0,CodeUtil.getCode(AppErrorCode.GUILD_KAIFU_DAYS_CHANGE_GONGGAO, config.getDay())};
		}
		canGongGao = GameSystemTime.getSystemMillTime() > canGongGaoTime;
		return canGongGao;
	}
	
	
	/**
	 * 更新在线成员数据
	 * @param guildMember
	 * @param roleWrapper
	 * @param roleBusinessInfoWrapper
	 * @param roleVipWrapper
	 */
	private void refreshGuildMember(GuildMember guildMember){
		Long userRoleId = guildMember.getUserRoleId();
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		RoleBusinessInfoWrapper roleBusinessInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
		guildMember.setLevel(role.getLevel());
		guildMember.setZplus(roleBusinessInfoWrapper.getCurFighter());
		RoleVipWrapper roleVipWrapper = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		if(roleVipWrapper != null){
			guildMember.setVip(roleVipWrapper.getVipLevel());
		}
		guildMemberDao.cacheUpdate(guildMember, userRoleId);
	}
	
	/**
	 * 创建公会
	 * @param userRoleId
	 * @param guildName
	 * @return
	 */
	public Object[] createGuild(Long userRoleId,String guildName,String notice){
		if(!ActiveUtil.isCanChangeGuild()){
			return AppErrorCode.ATCIVE_CANNOT_CHANGE_GUILD;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_NOT_EXIST;// 角色不存在
		}
		GuildPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_GUILD);
		if(role.getLevel() < config.getLevel()){
			return AppErrorCode.ROLE_LEVEL_ERROR;//等级不足
		}
		//验证VIP等级
		if(roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_GUILD_CREATE) < 1){
			return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;//VIP等级不足
		}
		
		if(hasGuild(userRoleId)){
			return AppErrorCode.ROLE_HAS_GUILD;// 已有公会
		}
		
		Object[] reslut = checkGuildName(guildName);
		if(reslut != null){
			return reslut;
		}
		
		//刷YY嫌疑名称
		if(YYTerminator.checkYYTrue(guildName)){
			return AppErrorCode.NAME_S_YY;
		}
		
		if(!isCanGongGao()){//开服前N天不得发放公告
			notice = null;
		}
		if(notice != null && notice.length() > GameConstants.GUILD_NOTICE_MAX_LEN){
			return AppErrorCode.GUILD_GONGGAO_ERROR;// 公会名长度不合法
		}
		//字符是否合法
		if(notice != null && !"".equals(notice) && !RoleFieldCheck.checkInput(notice)){
			return AppErrorCode.GUILD_GONGGAO_ERROR;// 公告不合法
		}
		//删除所有申请
		guildApplyDao.delAllApply(userRoleId);
		//创建公会
		Guild guild = createGuild(guildName,notice);
		//公会信息入库
		guildDao.cacheInsert(guild, guild.getId());
		GuildMember leader = createGuildMember(role, GameConstants.GUILD_LEADER_POSTION, true);
		leader.setGuildId(guild.getId());
		//成员信息入库
		guildMemberDao.cacheInsert(leader, userRoleId);
		//初始化成员管理器
		GuildMemberManager manager = new GuildMemberManager(leader);
		guild.setGuildMemberManager(manager);
		//放入公会管理器中
		GuildManager.getManager().addGuild(guild);
		
		//将会长添加到公会聊天对列中去 
 		IoMsgSender.send2IoInner(userRoleId, InnerCmdType.GUILD_MEMBER_ADD_CHAT, new Object[]{guild.getId(),userRoleId}); 
 		//通知场景角色公会变更
 		notifyStageRoleGuildChange(userRoleId, guild);
		
 		//添加有公会日常的任务
		taskDayExportService.addGuildDayTask(userRoleId);
 		
		//公会日志
		addGuildLog(guild, GameConstants.GUILD_LOG_CREATE_GUILD, leader);
		
		//腾讯平台创建公会不发全服广播
		if(!PlatformConstants.isQQ()){
			//发送全服公告
			BusMsgSender.send2All(ClientCmdType.GUILD_CREATE_NOTICE, new Object[]{guild.getId(),guildName,role.getName()});
		}
		
		//支线
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_COUNT, new Object[]{BranchEnum.B13,1});
		
		Object[] guildVo = GuildOutPutUtil.getMyGuildVo(guild);
		guildVo[8] = leader.getPostion();
		guildVo[9] = 0;
		guildVo[10] = null;
		return new Object[]{1,guildVo};//创建成功
	}
	private Object[] checkGuildName(String guildName) {
		if(guildName == null || guildName.getBytes().length < GameConstants.GUILD_NAME_MIN_LEN || guildName.getBytes().length > GameConstants.GUILD_NAME_MAX_LEN){
			return AppErrorCode.GUILD_NAME_LENGTH_ERROR;// 公会名长度不合法
		}
		//字符是否合法
		if(!RoleFieldCheck.checkInput(guildName)){
			return AppErrorCode.GUILD_NAME_ERROR;// 名字不合法
		}
		if(guildNameRepeat(guildName)){
			return AppErrorCode.GUILD_NAME_REPEAT;// 公会名重复
		}
		return null;
	}
	/**
	 * 公会名称是否重复
	 * @param guildName
	 * @return true:重复
	 */
	private boolean guildNameRepeat(String guildName){
		List<Guild> guilds = guildDao.queryGuildByGuildName(guildName);
		if(guilds == null || guilds.size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 创建成员信息
	 * @param role
	 * @param postion
	 * @param isOnline
	 * @return	未赋值公会id 
	 */
	private GuildMember createGuildMember(RoleWrapper role,int postion,boolean isOnline){
		GuildMember guildMember = new GuildMember();
		guildMember.setUserRoleId(role.getId());
		guildMember.setName(role.getName());
		guildMember.setConfigId(role.getConfigId());
		guildMember.setLevel(role.getLevel());
		guildMember.setOnline(isOnline);
		guildMember.setOfflineTime(role.getOfflineTime());
		guildMember.setPostion(postion);
		guildMember.setBoxState(0);
		guildMember.setCurGongxian(0l);
		guildMember.setTotalGongxian(0l);
		guildMember.setTodayGongxian(0l);
		Long now = GameSystemTime.getSystemMillTime();
		guildMember.setEnterTime(now);
		
		RoleVipWrapper vip = null;
		RoleBusinessInfoWrapper roleBusinessInfoWrapper = null;
		if(isOnline){
			roleBusinessInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(role.getId());
			vip = roleVipInfoExportService.getRoleVipInfo(role.getId());
		}else{
			roleBusinessInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoForDB(role.getId());
			vip = roleVipInfoExportService.getRoleVipInfoFromDB(role.getId());
		}
		//VIP等级
		guildMember.setVip(vip.getVipLevel());
		//战力
		guildMember.setZplus(roleBusinessInfoWrapper.getCurFighter());
		guildMember.setUpdateTime(now);
		return guildMember;
	}
	/**
	 * 创建申请公会
	 * @param userRoleId
	 * @param guildId
	 * @return
	 */
	private GuildApply createGuildApply(Long userRoleId,Long guildId){
		GuildApply apply = new GuildApply();
		apply.setId(IdFactory.getInstance().generateId(ServerIdType.GUILD));
		apply.setUserRoleId(userRoleId);
		apply.setGuildId(guildId);
		apply.setApplyTime(GameSystemTime.getSystemMillTime());
		return apply;
	}
	/**
	 * 创建公会
	 * @param guildName
	 * @return
	 */
	private Guild createGuild(String guildName,String notice){
		Guild guild = new Guild();
		guild.setId(IdFactory.getInstance().generateId(ServerIdType.GUILD));
		guild.setName(guildName);
		guild.setNotice(notice);
		guild.setGold(0l);
		guild.setItem1(0);
		guild.setItem2(0);
		guild.setItem3(0);
		guild.setItem4(0);
		guild.setApplyType(GameConstants.GUILD_ZHAOSHOU_TYPE_DEFAULT);
		guild.setLevel(1);
		guild.setCreateTime(GameSystemTime.getSystemMillTime());
		GuildLogManager guildLogManager = new GuildLogManager();
		guild.setGuildLogManager(guildLogManager);
		GuildConfig config = guildConfigService.getGuildConfig(1);
		guild.setMaxCount(config.getMaxCount());
		guild.setSchool(1);
		return guild;
	}
	
	/**
	 * 是否有公会
	 * @param userRoleId
	 * @return
	 */
	public boolean hasGuild(Long userRoleId){
		return GuildManager.getManager().hasGuild(userRoleId);
	}
	/**
	 * 获取公会
	 * @param guildId
	 * @return
	 */
	public Guild getGuild(Long guildId){
		return GuildManager.getManager().getGuild(guildId);
	}
	/**
	 * 获取成员信息
	 * @param userRoleId
	 * @return
	 */
	public GuildMember getGuildMember(Long userRoleId){
		GuildMember guildMember = GuildManager.getManager().getGuildMember(userRoleId);
		calDayGongxian(guildMember);
		return guildMember;
	}
	/**
	 * 检测是否跨天，清空今日贡献
	 * @param guildMember
	 */
	private void calDayGongxian(GuildMember guildMember){
		if(guildMember != null && !DatetimeUtil.dayIsToday(guildMember.getUpdateTime())){
			guildMember.setTodayGongxian(0l);
			guildMember.setUpdateTime(GameSystemTime.getSystemMillTime());
			updateGuildMember(guildMember);
		}
	}
	/**
	 * 获取日志
	 * @param userRoleId
	 * @param start
	 * @return
	 */
	public Object[] getLogs(Long userRoleId,int start){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return null;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return null;//公会不存在
		}
		return guild.getGuildLogManager().getLogs(start, 10);
	}
	/**
	 * 修改公告
	 * @param userRoleId
	 * @param notice
	 * @return
	 */
	public Object[] modifyNotice(Long userRoleId,String notice){
		if(!isCanGongGao()){
			return gongGaoCode;//开服N天内不得修改公告
		}
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		//验证修改公告权限
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(!config.isSetNotice()){
			return AppErrorCode.GUILD_NO_QUANXIAN;//权限不足
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		if(notice != null && notice.length() > GameConstants.GUILD_NOTICE_MAX_LEN){
			return AppErrorCode.GUILD_GONGGAO_ERROR;// 公会名长度不合法
		}
		//字符是否合法
		if(!RoleFieldCheck.checkInput(notice)){
			return AppErrorCode.GUILD_GONGGAO_ERROR;//名字不合法
		}
		guild.setNotice(notice);
		//修改入库
		guildDao.cacheUpdate(guild, guild.getId());
		return new Object[]{1,notice};
	}
	/**
	 * 申请入会
	 * @return
	 */
	public Object[] applyGuild(Long userRoleId,Long guildId,PublicMsgQueue publicMsgQueue){
//		if(!ActiveUtil.isCanChangeGuild()){
//			return AppErrorCode.ATCIVE_CANNOT_CHANGE_GUILD;
//		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_NOT_EXIST;//角色不存在
		}
		GuildPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_GUILD);
		if(role.getLevel() < config.getLevel()){
			return AppErrorCode.ROLE_LEVEL_ERROR;//等级不足
		}
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember != null){
			return AppErrorCode.ROLE_HAS_GUILD;//已有公会
		}
		Guild guild = getGuild(guildId);
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		//公会信息
		Object[] guildVo = GuildOutPutUtil.getMyGuildVo(guild);
		guildVo[8] = GameConstants.GUILD_MEMBER_POSTION;
		guildVo[9] = 0;
		guildVo[10] = territoryExportService.getTerritoryMapByGuildId(guildId);
		GuildZhaoShouConfig zhaoShouConfig = guildZhaoShouConfigService.getGuildZhaoShouConfig(guild.getApplyType());
		if(zhaoShouConfig.getType() == GameConstants.GUILD_ZHAOSHOU_SHENPI){
			if(guildApplyDao.getRoleGuildApply(userRoleId, guildId) != null){
				return AppErrorCode.OK;//已申请过此公会,返回申请成功
			}
			GuildApply guildApply = createGuildApply(userRoleId, guildId);
			guildApplyDao.cacheInsert(guildApply, userRoleId);
			for (GuildMember member : guild.getAllMembers()) {
				if(member.isOnline()){
					GuildQuanXianConfig quanxian = guildQuanXianConfigService.getGuildQuanXianConfig(member.getPostion());
					if(quanxian.isRecruit()){
						publicMsgQueue.addMsg(member.getUserRoleId(), ClientCmdType.GUILD_NEW_APPLY, null);
					}
				}
			}
		}else{
			if(guild.isFull()){
				return AppErrorCode.GUILD_IS_FULL;//公会已满
			}
			if(zhaoShouConfig.getType() == GameConstants.GUILD_ZHAOSHOU_LEVEL){
				if(role.getLevel() < zhaoShouConfig.getValue()){
					return AppErrorCode.GUILD_ZHAOSHOU_NOT;//不满足公会招收条件
				}
			}else if(zhaoShouConfig.getType() == GameConstants.GUILD_ZHAOSHOU_VIP){
				//VIP验证
				RoleVipWrapper roleVipWrapper = roleVipInfoExportService.getRoleVipInfo(userRoleId);
				if(roleVipWrapper.getVipLevel() < zhaoShouConfig.getValue()){
					return AppErrorCode.GUILD_ZHAOSHOU_NOT;//不满足公会招收条件
				}
			}else{
				return AppErrorCode.GUILD_ZHAOSHOU_NOT;//公会收人
			}
			guildMember = createGuildMember(role, GameConstants.GUILD_MEMBER_POSTION, true);
			guildMember.setGuildId(guild.getId());
			if(!guild.addMember(guildMember)){
				return AppErrorCode.GUILD_IS_FULL;//公会已满
			}
			GuildManager.getManager().addGuildMember(guildMember);
			guildMemberDao.cacheInsert(guildMember, userRoleId);
			guildApplyDao.delAllApply(userRoleId);
			publicMsgQueue.addMsg(userRoleId, ClientCmdType.GET_MY_GUILD, guildVo);
			publicMsgQueue.addMsg(guild.getRoleIds(), ClientCmdType.GUILD_ADD_NEW_MEMBER, new Object[]{guildMember.getMemberVo()});
			//通知场景角色公会变更
	 		notifyStageRoleGuildChange(userRoleId, guild);
	 		 
			//添加公会内可收到聊天信息成员
			IoMsgSender.send2IoInner(userRoleId, InnerCmdType.GUILD_MEMBER_ADD_CHAT, new Object[]{guildMember.getGuildId(),userRoleId}); 
			
	 		//添加有公会日常的任务
			taskDayExportService.addGuildDayTask(userRoleId);
			//公会日志
			addGuildLog(guild, GameConstants.GUILD_LOG_ENTER_GUILD, guildMember);
			
			//支线
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_COUNT, new Object[]{BranchEnum.B13,1});
		}
		return AppErrorCode.OK;
	}
	
	/**
	 * 获取申请列表
	 * @param userRoleId
	 * @return
	 */
	public Object[] getApplyList(Long userRoleId){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return null;//没有公会
		}
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(!config.isRecruit()){
			return null;//权限不足
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return null;//公会不存在
		}
		List<GuildApply> list = guildApplyDao.getGuildApplyList(guild.getId());
		if(list != null && list.size() > 0){
			List<Object[]> out = new ArrayList<>();
			for (GuildApply guildApply : list) {
				out.add(GuildOutPutUtil.getApplyVo(guildApply));
			}
			return out.toArray();
		}
		return null;
	}
	/**
	 * 审批申请
	 * @param userRoleId
	 * @param targetRoleId
	 * @param agree
	 * @return
	 */
	public Object[] shenpi(Long userRoleId,Object[] ratgetRoleIds,boolean agree,PublicMsgQueue publicMsgQueue){
		if(!ActiveUtil.isCanChangeGuild()){
			return AppErrorCode.ATCIVE_CANNOT_CHANGE_GUILD;
		}
		if(ratgetRoleIds == null || ratgetRoleIds.length < 1){
			return AppErrorCode.GUILD_NO_SHENHE_TARGET;//没有选择审核目标
		}
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(!config.isRecruit()){
			return AppErrorCode.GUILD_NO_QUANXIAN;//权限不足
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		StringBuilder roleIds = new StringBuilder();
		List<Long> successRoleIds = new ArrayList<>();
		if(agree){
			List<Object[]> newMembers = new ArrayList<>();
			Object[] noticeGuildData = new Object[]{guild.getId(),guild.getName()};
			for (Object object : ratgetRoleIds) {
				Long targetRoleId = LongUtils.obj2long(object);
				GuildMember targetGuildMember = getGuildMember(targetRoleId);
				if(targetGuildMember != null){
					continue;//对方已有公会
				}
				GuildApply apply = guildApplyDao.getRoleGuildApply(targetRoleId, guild.getId());
				if(apply == null){
					continue;//未找到申请记录
				}
				if(guild.isFull()){
					break;//公会已满
				}
				RoleWrapper role = null;
				boolean online = false;
				if(publicRoleStateExportService.isPublicOnline(targetRoleId)){
					online = true;
					role = roleExportService.getLoginRole(targetRoleId);
				}else{
					role = roleExportService.getUserRoleFromDb(targetRoleId);
				}
				if(role == null){
					continue;//角色不存在
				}
				targetGuildMember = createGuildMember(role, GameConstants.GUILD_MEMBER_POSTION, online);
				targetGuildMember.setGuildId(guild.getId());
				if(!guild.addMember(targetGuildMember)){
					break;//公会已满
				}
				GuildManager.getManager().addGuildMember(targetGuildMember);
				guildMemberDao.cacheInsert(targetGuildMember, targetRoleId);
				guildApplyDao.delAllApply(targetRoleId);
				roleIds.append(",").append(targetRoleId);
				successRoleIds.add(targetRoleId);
				
				newMembers.add(targetGuildMember.getMemberVo());
				if(online){
					//添加公会内可收到聊天信息成员
					IoMsgSender.send2IoInner(userRoleId, InnerCmdType.GUILD_MEMBER_ADD_CHAT, new Object[]{guildMember.getGuildId(),targetRoleId}); 
					//通知场景角色公会变更
					notifyStageRoleGuildChange(targetRoleId, noticeGuildData,publicMsgQueue);
					notifyStageRoleGuildLevelChange(targetRoleId, guild.getLevel(), publicMsgQueue);
					//添加有公会日常的任务
					taskDayExportService.addGuildDayTask(userRoleId);
					
					//支线
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_COUNT, new Object[]{BranchEnum.B13,1});
				}
				
				
				//公会日志
				addGuildLog(guild, GameConstants.GUILD_LOG_ENTER_GUILD, targetGuildMember);
			}
			//通知公会成员有人加入
			PublicMsgSender.send2Many(guild.getRoleIds(), ClientCmdType.GUILD_ADD_NEW_MEMBER, newMembers.toArray());
			
			
			if(roleIds.length() > 1){
				guildApplyDao.delGuildApply(roleIds.substring(1), null);
			}
		}else{
			for (Object object : ratgetRoleIds) {
				Long targetRoleId = LongUtils.obj2long(object);
				roleIds.append(",").append(targetRoleId);
				successRoleIds.add(targetRoleId);
			}
			guildApplyDao.delGuildApply(roleIds.substring(1), guild.getId());
		}
		return GuildOutPutUtil.shenpiSuccess(agree, successRoleIds);
	}
	
	/**
	 * 获取公会信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getGuildInfo(Long userRoleId){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return null;//没有公会 返回null
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return null;//公会不存在 返回null
		}
		Object[] guildVo = GuildOutPutUtil.getMyGuildVo(guild);
		guildVo[8] = guildMember.getPostion();
		guildVo[9] = guildMember.getCurGongxian();
		guildVo[10] = territoryExportService.getTerritoryMapByGuildId(guild.getId());
		return guildVo;
	}
	
	/**
	 * 检测是否可拔旗，并消耗帮贡
	 * @param userRoleId
	 * @return
	 */
	public Object[] flagCheck(Long userRoleId,int need){
		GuildMember guildMember = getGuildMember(userRoleId);
		if (guildMember == null) {
			return AppErrorCode.TERRITORY_NO_GUILD;
		}
		// 校验帮派职位
		if (!GuildUtil.isLeaderOrViceLeader(guildMember.getPostion())) {
			return AppErrorCode.TERRITORY_POSITION_LIMIT;
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.TERRITORY_NO_GUILD;//公会不存在
		}
		if(guild.getGold() < need){
			return AppErrorCode.TERRITORY_NO_GOLD;
		}
		return null;
	}
	/**
	 * 检测是否可拔旗，并消耗帮贡
	 * @param userRoleId
	 * @return
	 */
	public Object[] flagCost(Long userRoleId,int need){
		GuildMember guildMember = getGuildMember(userRoleId);
		if (guildMember == null) {
			return AppErrorCode.TERRITORY_NO_GUILD;
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.TERRITORY_NO_GUILD;//公会不存在
		}
		if(guild.getGold() < need){
			return AppErrorCode.TERRITORY_NO_GOLD;
		}
		synchronized (guild) {
			guild.setGold(guild.getGold() - need);
		}
		guildDao.cacheUpdate(guild, guild.getId());
		return null;
	}
	
	/**
	 * 获取全部成员
	 * @param userRoleId
	 * @return
	 */
	public Object[] getAllMembers(Long userRoleId){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		List<Object[]> members = new ArrayList<>();
		for (GuildMember member : guild.getAllMembers()) {
			members.add(member.getMemberVo());
		}
		return new Object[]{1,members.toArray()};
	}
	
	/**
	 * 获取公会列表
	 * @return
	 */
	public Object[] getGuildList(){
		Object[] ret =  GuildManager.getManager().getGuildList();
		if(ret !=null){
			for(Object e:ret){
				Object[]  objArray = (Object[] )e;
				Long guildId = (Long)objArray[0];
				objArray[6] = territoryExportService.getTerritoryMapByGuildId(guildId);
			}
		}
		return ret;
	}
	/**
	 * 退出公会
	 * @param userRoleId
	 * @return
	 */
	public Object[] exitGuild(Long userRoleId){
		if(!ActiveUtil.isCanChangeGuild()){
			return AppErrorCode.ATCIVE_CANNOT_CHANGE_GUILD;
		}
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		
 		
		if(guildMember.getPostion() == GameConstants.GUILD_LEADER_POSTION){
			//会长退会视为解散
			if(!guild.beginDissol()){
				return AppErrorCode.GUILD_NOT_ONLY_LEADER;//不能解散
			}
			
			//移除公会日常
	 		taskDayExportService.removeGuildDayTask(userRoleId);
	 		
			//解散公会通知聊天体系
			IoMsgSender.send2IoInner(userRoleId, InnerCmdType.GUILD_MEMBER_CLEAR, guildMember.getGuildId());
			
			return dissolGuild(userRoleId, guildMember.getGuildId());
		}
		GuildManager.getManager().removeGuildMember(userRoleId);
		guild.removeMember(userRoleId);
		guildMemberDao.cacheDelete(userRoleId, userRoleId);
		
		//通知公会成员有玩家退出公会
		PublicMsgSender.send2Many(guild.getRoleIds(), ClientCmdType.GUILD_EXIT, new Object[]{1,0,new Object[]{userRoleId}});
		
		//清除公会内可收到聊天信息成员
 		IoMsgSender.send2IoInner(userRoleId, InnerCmdType.GUILD_MEMBER_REMOVE_CHAT, new Object[]{guildMember.getGuildId(),userRoleId}); 
		//通知场景角色公会变更
 		notifyStageRoleGuildChange(userRoleId,null);
 		//移除公会日常
 		taskDayExportService.removeGuildDayTask(userRoleId);
 		
		//公会日志
		addGuildLog(guild, GameConstants.GUILD_LOG_EXIT_GUILD, guildMember);
		return AppErrorCode.GUILD_EXIT_SUCCESS;
	}
	
	/**
	 * 踢出公会
	 * @param userRoleId
	 * @return
	 */
	public Object[] kickGuild(Long userRoleId,Object[] ratgetRoleIds,PublicMsgQueue publicMsgQueue){
		if(!ActiveUtil.isCanChangeGuild()){
			return AppErrorCode.ATCIVE_CANNOT_CHANGE_GUILD;
		}
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(!config.isExpel()){
			return AppErrorCode.GUILD_NO_QUANXIAN;//权限不足
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		List<Long> roleIds = new ArrayList<>();
		for (Object object : ratgetRoleIds) {
			Long targetRoleId = LongUtils.obj2long(object);
			GuildMember targetMember = getGuildMember(targetRoleId);
			if(targetMember == null || !targetMember.getGuildId().equals(guildMember.getGuildId())){
				continue;//不在同一公会
			}
			if(targetMember.getPostion() <= guildMember.getPostion()){
				continue;//权限不足
			}
			GuildManager.getManager().removeGuildMember(targetRoleId);
			guild.removeMember(targetRoleId);
			guildMemberDao.cacheDelete(targetRoleId, targetRoleId);
			roleIds.add(targetRoleId);
			
			//公会成员下线内部聊天指令
			IoMsgSender.send2IoInner( userRoleId, InnerCmdType.GUILD_MEMBER_REMOVE_CHAT, new Object[]{guildMember.getGuildId(),targetRoleId});
			
			if(publicRoleStateExportService.isPublicOnline(targetRoleId)){
				//通知被T出公会
				publicMsgQueue.addMsg(targetRoleId, ClientCmdType.GUILD_EXIT, new Object[]{1,1,new Object[]{targetRoleId}});
				//通知场景玩家公会变更
				notifyStageRoleGuildChange(targetRoleId, null,publicMsgQueue);
				notifyStageRoleGuildLevelChange(targetRoleId, 0, publicMsgQueue);
				
				//移除公会日常
				taskDayExportService.removeGuildDayTask(targetRoleId);
			}
			//公会日志
			addGuildLog(guild, GameConstants.GUILD_LOG_LET_LEAVE_GUILD,guildMember,targetMember);
		}
		
		if(roleIds.size() > 0){
			//通知公会成员有玩家退出公会
			publicMsgQueue.addMsg(guild.getRoleIds(), ClientCmdType.GUILD_EXIT, new Object[]{1,1,roleIds.toArray()});
			return AppErrorCode.OK;
		}else{
			return AppErrorCode.GUILD_OPERATE_FAIL;
		}
	}
	
	/**
	 * 任命职位
	 * @param userRoleId
	 * @param targetRoleId
	 * @param postion
	 * @return
	 */
	public Object[] changePostion(Long userRoleId,Long targetRoleId,int postion,PublicMsgQueue publicMsgQueue){
		if(!ActiveUtil.isCanChangeGuild()){
			return AppErrorCode.ATCIVE_CANNOT_CHANGE_GUILD;
		}
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		GuildMember targetGuildMember = getGuildMember(targetRoleId);
		if(targetGuildMember == null){
			return AppErrorCode.GUILD_NOT_SAME_GUILD;//对方没有公会
		}
		if(!targetGuildMember.getGuildId().equals(guildMember.getGuildId())){
			return AppErrorCode.GUILD_NOT_SAME_GUILD;//不在同一公会
		}
		if(postion == GameConstants.GUILD_LEADER_POSTION){
			//转让会长
			return changeLeader(guildMember, targetGuildMember, guild);
		}
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(!config.isAppoint() || postion <= guildMember.getPostion()){
			return AppErrorCode.GUILD_NO_QUANXIAN;//权限不足
		}
		int oldPostion = targetGuildMember.getPostion();
		if(oldPostion <= guildMember.getPostion()){
			return AppErrorCode.GUILD_NO_QUANXIAN;//权限不足
		}
		GuildQuanXianConfig nowPostion = guildQuanXianConfigService.getGuildQuanXianConfig(postion);
		int postionCount = guild.getPostionCount(postion);
		if(nowPostion.getPostMaxCount() > 0 && postionCount >= nowPostion.getPostMaxCount()){
			return AppErrorCode.GUILD_MAX_COUNT;//职位已达到最大人数
		}
		targetGuildMember.setPostion(postion);
		if(GuildUtil.isLeaderOrViceLeader(oldPostion) || GuildUtil.isLeaderOrViceLeader(postion)){
			//公会管理层有变动
			guild.changeManager();
		}
		Object[] result = new Object[]{1,targetRoleId,postion};
		publicMsgQueue.addMsg(targetRoleId, ClientCmdType.GUILD_CHANGE_POSTION,result);
		//同步入数据库
		updateGuildMember(targetGuildMember);
		//公会日志
		addGuildLog(guild, GameConstants.GUILD_LOG_CHANGE_POSITION,new Object[]{targetGuildMember.getName(),targetGuildMember.getUserRoleId()},new Object[]{oldPostion,postion});
		GamePublishEvent.publishEvent(new GuildChangePositionLogEvent(0,targetGuildMember.getUserRoleId(),
				guild.getId(),guild.getName(),oldPostion,postion,guildMember.getUserRoleId()));
		return result;
	}
	/**
	 * 转让会长
	 * @param guildMember
	 * @param targetGuildMember
	 * @param guild
	 * @return
	 */
	private Object[] changeLeader(GuildMember guildMember,GuildMember targetGuildMember,Guild guild){
		if(guildMember.getPostion() != GameConstants.GUILD_LEADER_POSTION){
			return AppErrorCode.GUILD_NO_QUANXIAN;//权限不足
		}
		Long roleId = targetGuildMember.getUserRoleId();
		boolean hasTequan = false;
		if(publicRoleStateExportService.isPublicOnline(roleId)){
			hasTequan = roleVipInfoExportService.getVipTequan(roleId, GameConstants.VIP_GUILD_CREATE) > 0;
		}else{
			hasTequan = roleVipInfoExportService.getVipTequanFromDb(roleId, GameConstants.VIP_GUILD_CREATE) > 0;
		}
		//验证VIP等级
		if(!hasTequan){
			return AppErrorCode.VIP_TARGET_NOT_ENOUGH_LEVEL;//VIP等级不足
		}
		int oldPosition = targetGuildMember.getPostion();
		guildMember.setPostion(GameConstants.GUILD_MEMBER_POSTION);
		targetGuildMember.setPostion(GameConstants.GUILD_LEADER_POSTION);
		guild.changeLeader(targetGuildMember);
		
		updateGuildMember(guildMember);
		updateGuildMember(targetGuildMember);
		
		//公会日志
		addGuildLog(guild, GameConstants.GUILD_LOG_ZHUANRANG_LEADER,guildMember,targetGuildMember);
		GamePublishEvent.publishEvent(new GuildChangePositionLogEvent(1,targetGuildMember.getUserRoleId(),guild.getId(),guild.getName(),oldPosition,GameConstants.GUILD_LEADER_POSTION,guildMember.getUserRoleId()));
		PublicMsgSender.send2One(roleId, ClientCmdType.GUILD_CHANGE_POSTION, new Object[]{1,roleId,GameConstants.GUILD_LEADER_POSTION});
		PublicMsgSender.send2One(roleId, ClientCmdType.GUILD_CHANGE_POSTION, new Object[]{1,guildMember.getUserRoleId(),GameConstants.GUILD_MEMBER_POSTION});
		
		return new Object[]{2,roleId,GameConstants.GUILD_MEMBER_POSTION};
	}
	
	private void addGuildLog(Guild guild,int logType,GuildMember role){
		Long now = GameSystemTime.getSystemMillTime();
		try{
			GuildLog log = new GuildLog(now, logType,new Object[]{role.getName(),role.getUserRoleId()},null);
			guild.getGuildLogManager().addLog(log);
			guildLogDao.writeLogFile(guild);
		}catch (Exception e) {
			ChuanQiLog.error("", e);
		}
	}
	
	private void addGuildLog(Guild guild,int logType,GuildMember role,GuildMember targeRole){
		Long now = GameSystemTime.getSystemMillTime();
		try{
			GuildLog log = new GuildLog(now, logType,new Object[]{role.getName(),role.getUserRoleId()},new Object[]{targeRole.getName(),targeRole.getUserRoleId()});
			guild.getGuildLogManager().addLog(log);
			guildLogDao.writeLogFile(guild);
		}catch (Exception e) {
			ChuanQiLog.error("", e);
		}
	}
	
	private void addGuildLog(Guild guild,int logType,Object[] role,Object[] data){
		Long now = GameSystemTime.getSystemMillTime();
		try{
			GuildLog log = new GuildLog(now, logType,role,data);
			guild.getGuildLogManager().addLog(log);
			guildLogDao.writeLogFile(guild);
		}catch (Exception e) {
			ChuanQiLog.error("", e);
		}
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		GuildMember guildMember = GuildManager.getManager().getGuildMember(userRoleId);
		if(guildMember == null){
			return;
		}
		Guild guild = GuildManager.getManager().getGuild(guildMember.getGuildId());
		if(guild == null){
			return;
		}
		guild.onlineHandle(userRoleId);
		
 		//公会成员上线内部聊天指令
 		IoMsgSender.send2IoInner(userRoleId, InnerCmdType.GUILD_MEMBER_ADD_CHAT, new Object[]{guildMember.getGuildId(),userRoleId}); 
 		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_SYN_GUILD_NAME, new Object[]{guild.getId(),guild.getName()});
	}
	public void zhuanzhi(Long userRoleId,int configId){
		GuildMember guildMember = GuildManager.getManager().getGuildMember(userRoleId);
		if(guildMember == null){
			return;
		}
		Guild guild = GuildManager.getManager().getGuild(guildMember.getGuildId());
		if(guild == null){
			return;
		}
		guild.zhuanzhi(userRoleId, configId);
	}
	
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		GuildMember guildMember = GuildManager.getManager().getGuildMember(userRoleId);
		if(guildMember == null){
			return;
		}
		Guild guild = GuildManager.getManager().getGuild(guildMember.getGuildId());
		if(guild == null){
			return;
		}
		guild.offlineHandle(userRoleId);
		refreshGuildMember(guildMember);
		
		//公会成员下线内部聊天指令
		IoMsgSender.send2IoInner( userRoleId, InnerCmdType.GUILD_MEMBER_REMOVE_CHAT, new Object[]{guildMember.getGuildId(),userRoleId});
	}
	/**
	 * 修改招收条件
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public Object[] changeZhaoshouType(Long userRoleId,int type){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(!config.isReviseterm()){
			return AppErrorCode.GUILD_NO_QUANXIAN;//权限不足
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		GuildZhaoShouConfig zhaoshouConfig = guildZhaoShouConfigService.getGuildZhaoShouConfig(type);
		if(zhaoshouConfig == null){
			return AppErrorCode.GUILD_TYPE_NO_EXIT;//条件不存在
		}
		guild.setApplyType(type);
		guildDao.cacheUpdate(guild, guild.getId());
		return new Object[]{1,type};
	}
	/**
	 * 解散公会
	 * @param userRoleId
	 * @param guildId
	 * @return
	 */
	private Object[] dissolGuild(Long userRoleId,Long guildId){
		GuildManager.getManager().removeGuild(guildId);
		GuildManager.getManager().removeGuildMember(userRoleId);
		guildDao.cacheDelete(guildId, guildId);
		guildMemberDao.cacheDelete(userRoleId, userRoleId);
		//通知场景角色公会变更
 		notifyStageRoleGuildChange(userRoleId,null);
 		//移除公会日常
 		taskDayExportService.removeGuildDayTask(userRoleId);
 		territoryExportService.resetTerritory(guildId);
 		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TERRITORY_LEADER_OFF, null);
		return AppErrorCode.GUILD_EXIT_SUCCESS;
	}
	/**
	 * 拉取公会日志
	 * @param userRoleId
	 * @param start
	 * @param size
	 */
	public Object[] getGuildLogs(Long userRoleId,int start,int size){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return null;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return null;//公会不存在
		}
		
		return guild.getGuildLogManager().getLogs(start, size);
	}
	/**
	 * 公会捐献
	 * @param userRoleId
	 * @param type
	 * @param value
	 * @return
	 */
	public Object[] guildJuanxian(Long userRoleId,int type,int value,PublicMsgQueue publicMsgQueue){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//公会不存在
		}
		if(value < 1){
			return AppErrorCode.NUMBER_ERROR;
		}
		int gongxian = 0;
		long guildGold = 0;
		GuildPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_GUILD);
		if(publicConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		String jxId = null;
		if(type == GameConstants.GUILD_JUANXIAN_TYPE_YINLIANG){
			int times = value / publicConfig.getMoneyRate();
			if(times < 1){
				return AppErrorCode.NUMBER_ERROR;
			}
			value = times * publicConfig.getMoneyRate();
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, value, userRoleId, LogPrintHandle.CONSUME_GUILD, true, LogPrintHandle.CBZ_GUILD_JUANXIAN);
			if(result != null){
				return result;
			}
			gongxian = times * publicConfig.getMoneyGong();;
			guildGold = times * publicConfig.getMoneyGuildGet();
			jxId = ModulePropIdConstant.MONEY_GOODS_ID;
		}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_GOLD){
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, value, userRoleId, LogPrintHandle.CONSUME_GUILD, true, LogPrintHandle.CBZ_GUILD_JUANXIAN);
			if(result != null){
				return result;
			}
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,value,LogPrintHandle.CONSUME_GUILD,QQXiaoFeiType.CONSUME_GUILD,1});
			}
			gongxian = value * publicConfig.getGoldGong();
			guildGold = value * publicConfig.getGoldGuildGet();
			jxId = ModulePropIdConstant.GOLD_GOODS_ID;
		}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_BGOLD){
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.BGOLD, value, userRoleId, LogPrintHandle.CONSUME_GUILD, true, LogPrintHandle.CBZ_GUILD_JUANXIAN);
			if(result != null){
				return result;
			}
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,value,LogPrintHandle.CONSUME_GUILD,QQXiaoFeiType.CONSUME_GUILD,1});
			}
			gongxian = value * publicConfig.getbGoldGong();
			guildGold = value * publicConfig.getbGoldGuildGet();
			jxId = ModulePropIdConstant.BIND_GOLD_GOODS_ID;
		}else{
			int need = value;
			Map<String,Integer> tempResources = new HashMap<>();
			String id1 = null;
			if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM1){
				id1 = ModulePropIdConstant.GUILD_ITEM1_ID;
			}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM2){
				id1 = ModulePropIdConstant.GUILD_ITEM2_ID;
			}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM3){
				id1 = ModulePropIdConstant.GUILD_ITEM3_ID;
			}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM4){
				id1 = ModulePropIdConstant.GUILD_ITEM4_ID;
			}
			
			if(ObjectUtil.strIsEmpty(id1)){
				return AppErrorCode.CONFIG_ERROR;
			}
			
			List<String> needGoodsIds = goodsConfigExportService.loadIdsById1(id1);
			for (String goodsId : needGoodsIds) {
				int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
				jxId = goodsId;
				if(owerCount >= need){
					tempResources.put(goodsId, need);
					need = 0;
					break;
				}
				need = need - owerCount;
				tempResources.put(goodsId, owerCount);
			}
			if(need > 0){
				return AppErrorCode.GOODS_NOT_ENOUGH;
			}
			
			roleBagExportService.removeBagItemByGoods(tempResources, userRoleId, GoodsSource.GUILD_JUANXIAN, true, true);
			
			gongxian = value * publicConfig.getItemGong();
		}
		guildMember.addGongxian(gongxian);
		updateGuildMember(guildMember);
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.GUILD_GONGXIAN_VALUE, gongxian});
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
		
		synchronized (guild) {
			//增加资金
			if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM1){
				guild.setItem1(guild.getItem1() + value);
			}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM2){
				guild.setItem2(guild.getItem2() + value);
			}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM3){
				guild.setItem3(guild.getItem3() + value);
			}else if(type == GameConstants.GUILD_JUANXIAN_TYPE_ITEM4){
				guild.setItem4(guild.getItem4() + value);
			}
			guild.setGold(guild.getGold() + guildGold);
		}
		guildDao.cacheUpdate(guild, guild.getId());

		//日志
		addGuildLog(guild, GameConstants.GUILD_LOG_GUILD_JUANXIAN, new Object[]{guildMember.getName(),guildMember.getUserRoleId()}, new Object[]{type,value,gongxian});
		//公告
		GoodsConfig config = goodsConfigExportService.loadById(jxId);
		if(config != null){
			Object[] notice = new Object[]{GameConstants.GUILD_JUANXIAN_NOTICE,new Object[]{guildMember.getName(),new Object[]{3,config.getId(),value},gongxian}};
			for (Long roleId : guild.getOnlineRoleIds()) {
				publicMsgQueue.addMsg(roleId, ClientCmdType.NOTIFY_CLIENT_ALERT4, notice);
			}
		}
		return AppErrorCode.OK;
	}
	private void updateGuildMember(GuildMember guildMember){
		guildMemberDao.cacheUpdate(guildMember, guildMember.getUserRoleId());
	}
	
	
	/**
	 * 通知场景角色公会变化
	 * @param userRoleId
	 * @param publicMsgQueue
	 */
	private void notifyStageRoleGuildChange(Long userRoleId,Object[] guildData,PublicMsgQueue publicMsgQueue){
		publicMsgQueue.addStageMsg(userRoleId, InnerCmdType.GUILD_CHANGE, guildData);
 		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_SYN_GUILD_NAME, guildData);
	}
	/**
	 * 通知场景角色公会等级变化
	 * @param userRoleId
	 * @param publicMsgQueue
	 */
	private void notifyStageRoleGuildLevelChange(Long userRoleId,Integer level,PublicMsgQueue publicMsgQueue){
		publicMsgQueue.addStageMsg(userRoleId, InnerCmdType.GUILD_LEVEL_CHANGE, level);
	}
	/**
	 * 通知场景角色公会变化
	 * @param userRoleId
	 * @param publicMsgQueue
	 */
	private void notifyStageRoleGuildChange(Long userRoleId,Guild guild){
		Object[] data = null;
		Integer level = 0;
		if(guild != null){
			data = new Object[]{guild.getId(),guild.getName()};
			level = guild.getLevel();
		}
		PublicMsgSender.send2Stage(userRoleId, InnerCmdType.GUILD_CHANGE, data);
		PublicMsgSender.send2Stage(userRoleId, InnerCmdType.GUILD_LEVEL_CHANGE, level);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.GUILD_SYN_GUILD_NAME, data);
	}
	
	public void addGongxian(Long userRoleId,int gx){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember != null){
			guildMember.addGongxian(gx);
			guildMemberDao.cacheUpdate(guildMember, userRoleId);
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.GUILD_GONGXIAN_VALUE, gx});
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}
	}
	
	/**
	 * 刷新所有在线成员数据
	 */
	public void refreshOnlineMembers(){
		List<Long> onlineIds = publicRoleStateExportService.getAllOnlineRoleids();
		for (Long userRoleId : onlineIds) {
			try{
				GuildMember guildMember = GuildManager.getManager().getGuildMember(userRoleId);
				if(guildMember == null){
					continue;
				}
				refreshGuildMember(guildMember);
			}catch (Exception e) {
				ChuanQiLog.error("玩家{}在刷新成员战力时出错",userRoleId);
			}
		}
		
		GuildManager.getManager().refreshAllGuild();
	}
	
	public Object[] guildLevelUp(Long userRoleId,PublicMsgQueue publicMsgQueue){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//没有公会
		}
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(!config.isGuildUp()){
			return AppErrorCode.GUILD_NO_QUANXIAN;
		}
		synchronized (guild) {
			GuildConfig guildConfig = guildConfigService.getGuildConfig(guild.getLevel() + 1);
			if(guildConfig == null){
				return AppErrorCode.GUILD_IS_MAX_LEVEL;//已经满级
			}
			long lastMoney = guild.getGold() - guildConfig.getNeedMoney();
			if(lastMoney < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_MONEY;//门派银两不足
			}
			int item1 = guild.getItem1() - guildConfig.getNeedItem1();
			if(item1 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM1;//门派令牌1不足
			}
			int item2 = guild.getItem2() - guildConfig.getNeedItem2();
			if(item2 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM2;//门派令牌2不足
			}
			int item3 = guild.getItem3() - guildConfig.getNeedItem3();
			if(item3 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM3;//门派令牌3不足
			}
			int item4 = guild.getItem4() - guildConfig.getNeedItem4();
			if(item4 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM4;//门派令牌4不足
			}
			guild.setGold(lastMoney);
			guild.setItem1(item1);
			guild.setItem2(item2);
			guild.setItem3(item3);
			guild.setItem4(item4);
			guild.setLevel(guildConfig.getLevel());
			guild.setMaxCount(guildConfig.getMaxCount());
		}
		guildDao.cacheUpdate(guild, guild.getId());
		//通知在线成员门派升级
		for (Long roleId : guild.getOnlineRoleIds()) {
			notifyStageRoleGuildLevelChange(roleId, guild.getLevel(), publicMsgQueue);
		}
		//TODO	门派升级日志
		//公告
		if(config != null){
			Object[] notice = new Object[]{GameConstants.GUILD_LEVEL_UP_NOTICE,new Object[]{guildMember.getName(),guild.getLevel()}};
			try{
				for (Long roleId : publicRoleStateExportService.getAllOnlineRoleids()) {
					publicMsgQueue.addMsg(roleId, ClientCmdType.NOTIFY_CLIENT_ALERT4, notice);
				}
			}catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		return new Object[]{1,guild.getLevel(),guild.getGold(),guild.getItem1(),guild.getItem2(),guild.getItem3(),guild.getItem4()};
	}
	
	public Object[] schoolLevelUp(Long userRoleId,PublicMsgQueue publicMsgQueue){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//没有公会
		}
		GuildQuanXianConfig config = guildQuanXianConfigService.getGuildQuanXianConfig(guildMember.getPostion());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(!config.isSchoolUp()){
			return AppErrorCode.GUILD_NO_QUANXIAN;
		}
		synchronized (guild) {
			GuildGeLouConfig guildConfig = guildGeLouConfigService.getConfigByLevel(guild.getSchool() + 1);
			if(guildConfig == null){
				return AppErrorCode.GUILD_IS_MAX_LEVEL;//已经满级
			}
			if(guild.getLevel() < guildConfig.getNeedGuildLevel()){
				return AppErrorCode.GUILD_NO_ENOUGH_LEVEL;//门派等级不足
			}
			long lastMoney = guild.getGold() - guildConfig.getNeedMoney();
			if(lastMoney < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_MONEY;//门派银两不足
			}
			int item1 = guild.getItem1() - guildConfig.getNeedItem1();
			if(item1 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM1;//门派令牌1不足
			}
			int item2 = guild.getItem2() - guildConfig.getNeedItem2();
			if(item2 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM2;//门派令牌2不足
			}
			int item3 = guild.getItem3() - guildConfig.getNeedItem3();
			if(item3 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM3;//门派令牌3不足
			}
			int item4 = guild.getItem4() - guildConfig.getNeedItem4();
			if(item4 < 0){
				return AppErrorCode.GUILD_NO_ENOUGH_ITEM4;//门派令牌4不足
			}
			guild.setGold(lastMoney);
			guild.setItem1(item1);
			guild.setItem2(item2);
			guild.setItem3(item3);
			guild.setItem4(item4);
			guild.setSchool(guildConfig.getLevel());
		}
		guildDao.cacheUpdate(guild, guild.getId());
		//通知在线成员阁楼升级
		for (Long roleId : guild.getOnlineRoleIds()) {
			publicMsgQueue.addMsg(roleId, ClientCmdType.GET_GUILD_GELOU_LEVEL, guild.getSchool());
		}
		//TODO	门派阁楼升级日志
		//公告
		if(config != null){
			Object[] notice = new Object[]{GameConstants.GUILD_GELOU_UP_NOTICE,new Object[]{guildMember.getName(),guild.getSchool()}};
			for (Long roleId : guild.getOnlineRoleIds()) {
				publicMsgQueue.addMsg(roleId, ClientCmdType.NOTIFY_CLIENT_ALERT4, notice);
			}
		}
		return new Object[]{1,guild.getSchool(),guild.getGold(),guild.getItem1(),guild.getItem2(),guild.getItem3(),guild.getItem4()};
	}
	
	public Integer getGuildGeLouLevel(Long userRoleId){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return null;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return null;//没有公会
		}
		return guild.getSchool();
	}
	
	public Object[] getGuildJuanXianInfo(Long userRoleId){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return null;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return null;//没有公会
		}
		return new Object[]{guildMember.getCurGongxian(),guild.getGold(),guild.getItem1(),guild.getItem2(),guild.getItem3(),guild.getItem4()};
	}
	
	public Object[] getGuildDuihuanInfos(Long userRoleId){
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return null;//没有公会
		}
		List<RoleGuildDuihuan> duihuanList = roleGuildDuihuanDao.cacheAsynLoadAll(userRoleId);
		if(duihuanList == null || duihuanList.size() == 0){
			return null;
		}
		List<Object[]> out = new ArrayList<>();
		for (RoleGuildDuihuan roleGuildDuihuan : duihuanList) {
			calDuihuan(roleGuildDuihuan);
			if(roleGuildDuihuan.getCount() > 0){
				out.add(new Object[]{roleGuildDuihuan.getItemId(),roleGuildDuihuan.getCount()});
			}
		}
		if(out.size() > 0){
			return out.toArray();
		}
		return null;
	}
	
	private void calDuihuan(RoleGuildDuihuan duihuan){
		if(!DatetimeUtil.dayIsToday(duihuan.getUpdateTime())){
			duihuan.setCount(0);
			duihuan.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleGuildDuihuanDao.cacheUpdate(duihuan, duihuan.getUserRoleId());
		}
	}
	
	public Object[] duihuan(Long userRoleId,String goodsId,Integer count){
		if(count == null || count < 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		GuildMember guildMember = getGuildMember(userRoleId);
		if(guildMember == null){
			return AppErrorCode.ROLE_NO_GUILD;//没有公会
		}
		Guild guild = getGuild(guildMember.getGuildId());
		if(guild == null){
			return AppErrorCode.GUILD_IS_NOT_EXIST;//没有公会
		}
		GuildDuihuanConfig config = guildDuihuanConfigService.getGuildDuihuanConfig(goodsId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(guild.getLevel() < config.getNeedLevel()){
			return AppErrorCode.GUILD_NO_ENOUGH_LEVEL;//门派等级不足
		}
		List<RoleGuildDuihuan> duihuanList = roleGuildDuihuanDao.cacheAsynLoadAll(userRoleId);
		RoleGuildDuihuan duihuan = null;
		for (RoleGuildDuihuan roleGuildDuihuan : duihuanList) {
			if(roleGuildDuihuan.getItemId().equals(goodsId)){
				duihuan = roleGuildDuihuan;
				calDuihuan(duihuan);
				break;
			}
		}
		int needCount = duihuan == null ? count : duihuan.getCount() + count;
		if(needCount > config.getMaxCount()){
			return AppErrorCode.GUILD_NO_DUIHUAN_COUNT;//今日兑换次数已满
		}
		long needGx = count * 1l * config.getNeedGong();
		if(guildMember.getCurGongxian() < needGx){
			return AppErrorCode.GUILD_NO_ENOUGH_GONG;//贡献不足
		}
		 Object[] result = roleBagExportService.checkPutInBag(goodsId, count, userRoleId);
		 if(result != null){
			 return result;
		 }
		if(duihuan == null){
			duihuan = new RoleGuildDuihuan();
			duihuan.setId(IdFactory.getInstance().generateId(ServerIdType.GUILD));
			duihuan.setUserRoleId(userRoleId);
			duihuan.setItemId(goodsId);
			duihuan.setCount(count);
			duihuan.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleGuildDuihuanDao.cacheInsert(duihuan, userRoleId);
		}else{
			duihuan.setCount(duihuan.getCount() + count);
			roleGuildDuihuanDao.cacheUpdate(duihuan, userRoleId);
		}
		guildMember.setCurGongxian(guildMember.getCurGongxian() - needGx);
		guildMemberDao.cacheUpdate(guildMember, userRoleId);
		
		Map<String,Integer> goods = new HashMap<>();
		goods.put(goodsId, count);
		roleBagExportService.putInBag(goods, userRoleId, GoodsSource.GOODS_GET_GUILD_DUIHUAN, true);
		
		//兑换日志
		GamePublishEvent.publishEvent(new GuildDuihuanLogEvent(userRoleId, guildMember.getName(), goodsId, count, needGx));
		
		return new Object[]{1,goodsId,count};
	}
	
	/**
	 * 弹劾掌门
	 * @param guildId
	 * @return
	 */
	public Object[] impeachLeader(Long userRoleId){
		GuildPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_GUILD);
		if(config==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		 if(ActiveUtil.isHcZBS()){
			 return AppErrorCode.GUILD_NO_IMPEACH_3; 
		 }
		 if(ActiveUtil.isTerritory()){
			 return AppErrorCode.GUILD_NO_IMPEACH_4; 
		 }
		 if(ActiveUtil.isKfYunGong()){
		     return AppErrorCode.GUILD_NO_IMPEACH_5; 
		 }
		 GuildMember guildMember = this.getGuildMember(userRoleId); //弹劾人
		 Guild guild = this.getGuild(guildMember.getGuildId()); 
		 if(guild==null){
				return AppErrorCode.ROLE_NO_GUILD; 
		 }
		int day = config.getLeaderNoLoginDay(); //多少天门主未登陆可以弹劾
		String impeachItemId = config.getImpeachItemId(); //弹劾道具
		if(roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_GUILD_CREATE)<1){
			return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;//VIP等级不足
		}
		if(!publicRoleStateExportService.isPublicOnline(userRoleId)){
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		//弹劾需要的道具
		 List<RoleItem> impeachItems = roleBagExportService.getBagItemByGoodId(impeachItemId, userRoleId);
		 if(impeachItems==null||impeachItems.size()<1){
			 return AppErrorCode.GUILD_NO_IMPEACH_ITEM; 
		 }
		 RoleWrapper leaderRoleWrapper  = null;
		 
		 GuildMember currentLeaderMember = guild.getLeader();//当前门主
		 Guild currentGuild = this.getGuild(currentLeaderMember.getGuildId());//当前帮派
		 if(publicRoleStateExportService.isPublicOnline(currentLeaderMember.getUserRoleId())){
			 //在线你呀的都敢弹劾
			 return AppErrorCode.GUILD_NO_IMPEACH_1; 
		 }else{
			 leaderRoleWrapper = roleExportService.getUserRoleFromDb(currentLeaderMember.getUserRoleId());
			 long a = GameSystemTime.getSystemMillTime()-leaderRoleWrapper.getOfflineTime();
			 double b = (double)a/GameConstants.DAY_TIME;
			 int delayDay =(int)Math.floor(b);
			 if(delayDay<day){
				 return AppErrorCode.GUILD_NO_IMPEACH_2;  
			 }
		 }
		GuildMember newLeader  = null;
		List<GuildMember> leaders = this.getAllLeader(guild.getId());
		List<GuildMember> prepareLeaders =null; //目标对象
		List<GuildMember> fuMenZhuList = new ArrayList<>(); //副门主
		List<GuildMember> zhangLaoList = new ArrayList<>(); //长老
		//寻找可能的新的门主
		for (int i = 0; i < leaders.size(); i++) {
			GuildMember gm = leaders.get(i);
			if(publicRoleStateExportService.isPublicOnline(gm.getUserRoleId())){
			  if(roleVipInfoExportService.getVipTequan(gm.getUserRoleId(), GameConstants.VIP_GUILD_CREATE)>0){
				//副掌门
				if(gm.getPostion()==GameConstants.GUILD_VIC_LEADER_POSTION){
						fuMenZhuList.add(gm);
				  }
				//长老
				if(gm.getPostion()==GameConstants.GUILD_ZHANGLAO_LEADER_POSTION){
						 zhangLaoList.add(gm);
				   }
				}
			}
		}
		prepareLeaders  = fuMenZhuList.size()==0?zhangLaoList:fuMenZhuList;
		//检查弹劾者是否在官职人员里面
		if(prepareLeaders.size()>0){
			for (GuildMember guildMember3 : prepareLeaders) {
			 if(userRoleId==guildMember3.getUserRoleId().longValue()){
					newLeader  = guildMember3; //弹劾者在官职人员里面
					break;
				}
			}
		}
		if(newLeader==null){
			 if(prepareLeaders.size()>0){
				 Collections.sort(prepareLeaders, sortMemberComparator);
				 newLeader  = prepareLeaders.get(0);
				 }else {
					 newLeader  = guildMember;//弹劾者就是门主了	
				}
		 }
		
		if(newLeader==null){
			//非正常结束
			ChuanQiLog.error("弹劾失败，没有找到新的接任人，请检查代码逻辑");
			return  AppErrorCode.GUILD_IMPEACH_ERROR;
		}
		//门主变更
		Object[] ret = changeLeader(currentLeaderMember,newLeader,currentGuild);
		if((int)ret[0]==0){
			return ret;
		}
		//消耗道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(impeachItems.get(0).getId(), 1, userRoleId, GoodsSource.GOODS_IMPEACH_ITEM, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		//邮件通知
		String title = EmailUtil.getCodeEmail(AppErrorCode.GUILD_IMPEACH_SUCCESS_EMAIL_TITLE);
		String content = EmailUtil.getCodeEmail(AppErrorCode.GUILD_IMPEACH_SUCCESS_EMAIL, 
				currentLeaderMember.getName(),day+"", guildMember.getName(),newLeader.getName());
		List<Long> membersIds = new ArrayList<>();
		for (GuildMember member : guild.getAllMembers()) {
			membersIds.add(member.getUserRoleId());
		}
		sendRewardEmail(membersIds,title, content);
		return AppErrorCode.OK;
	}
	private void sendRewardEmail(List<Long> roleIds,String title, String content) {
		emailExportService.sendEmailToMany(roleIds,title, content, GameConstants.EMAIL_TYPE_SINGLE, null);
	}
	public int[] getGuildMapExpZhenqiAdd(Long guildId){
		Guild guild = getGuild(guildId);
		if(guild == null){
			return null;
		}
		GuildConfig config = guildConfigService.getGuildConfig(guild.getLevel());
		if(config == null){
			return null;
		}
		return new int[]{config.getAddExp(),config.getAddZhenqi()};
	}
	/**
	 * 获取所有官职人员
	 */
	public List<GuildMember> getAllLeader(long guildId){
		Guild guild = getGuild(guildId);
		if(guild == null){
			return null;//公会不存在
		}
		List<GuildMember> members = new ArrayList<GuildMember>();
		for (GuildMember member : guild.getAllMembers()) {
			if(member.getPostion()!=4){
				members.add(member);
			}
		}
		return members;
	}
	public void handleUserModifyNameEvent(Long userRoleId, String afterName){
		GuildMember member = GuildManager.getManager().getGuildMember(userRoleId);
		if(member!=null){
			member.setName(afterName);
		}
	}
	
	//**********************后台使用方法*************************
	public String gmChangeGuildName(Long guildId,String guildName){
		Guild guild = GuildManager.getManager().getGuild(guildId);
		if(guild == null){
			return HttpErrorCode.GUILD_IS_NULL;
		}
		if(checkGuildName(guildName) != null){
			return HttpErrorCode.GUILD_NAME_ERROR;
		}
		guild.setName(guildName);
		GuildManager.getManager().needSort();
		guildDao.cacheUpdate(guild, guildId);

		return HttpErrorCode.SUCCESS;
	}
	
	public String gmChangeGuildNotice(Long guildId,String notice){
		Guild guild = GuildManager.getManager().getGuild(guildId);
		if(guild == null){
			return HttpErrorCode.GUILD_IS_NULL;
		}
		if(notice != null && notice.length() > GameConstants.GUILD_NOTICE_MAX_LEN){
			return HttpErrorCode.GUILD_NOTICE_ERROR;
		}
		//字符是否合法
		if(notice != null && !"".equals(notice) && !RoleFieldCheck.checkInput(notice)){
			return HttpErrorCode.GUILD_NOTICE_ERROR;
		}
		guild.setNotice(notice);
		guildDao.cacheUpdate(guild, guildId);
		
		return HttpErrorCode.SUCCESS;
	}
	
	public String gmChangeLeader(Long guildId,Long newLeader){
		GuildMember member = GuildManager.getManager().getGuildMember(newLeader);
		if(member == null){
			return HttpErrorCode.GUILD_MEMBER_IS_NULL;
		}
		if(!member.getGuildId().equals(guildId)){
			return HttpErrorCode.GUILD_MEMBER_IS_NOT_IN_THIS_GUILD;
		}
		Guild guild = GuildManager.getManager().getGuild(guildId);
		if(guild == null){
			return HttpErrorCode.GUILD_IS_NULL;
		}
		GuildMember oldLeader = guild.getLeader();
		if(oldLeader.getUserRoleId().equals(newLeader)){
			return HttpErrorCode.SUCCESS;
		}
		
		oldLeader.setPostion(GameConstants.GUILD_MEMBER_POSTION);
		member.setPostion(GameConstants.GUILD_LEADER_POSTION);
		guild.changeLeader(member);
		updateGuildMember(oldLeader);
		updateGuildMember(member);
		
		return HttpErrorCode.SUCCESS;
	}
	
	public String gmKickMember(Long guildId,Long userRoleId){
		GuildMember member = GuildManager.getManager().getGuildMember(userRoleId);
		if(member == null){
			return HttpErrorCode.GUILD_MEMBER_IS_NULL;
		}
		if(!member.getGuildId().equals(guildId)){
			return HttpErrorCode.GUILD_MEMBER_IS_NOT_IN_THIS_GUILD;
		}
		Guild guild = GuildManager.getManager().getGuild(guildId);
		if(guild == null){
			return HttpErrorCode.GUILD_IS_NULL;
		}
		if(member.getPostion() == GameConstants.GUILD_LEADER_POSTION){
			return HttpErrorCode.GUILD_CANNOT_KICK_LEADER;
		}
		GuildManager.getManager().removeGuildMember(userRoleId);
		guild.removeMember(userRoleId);
		guildMemberDao.cacheDelete(userRoleId, userRoleId);
		//通知公会成员有玩家退出公会
		PublicMsgSender.send2Many(guild.getRoleIds(), ClientCmdType.GUILD_EXIT, new Object[]{1,0,new Object[]{userRoleId}});
		if(sessionManagerExportService.isOnline(userRoleId)){
			//清除公会内可收到聊天信息成员
			IoMsgSender.send2IoInner(userRoleId, InnerCmdType.GUILD_MEMBER_REMOVE_CHAT, new Object[]{guildId,userRoleId}); 
			//通知场景角色公会变更
			notifyStageRoleGuildChange(userRoleId,null);
			//移除公会日常
			taskDayExportService.removeGuildDayTask(userRoleId);
		}
		return HttpErrorCode.SUCCESS;
	}
}
