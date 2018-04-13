package com.junyou.bus.role.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.branchtask.configure.ConditionType;
import com.junyou.bus.marry.export.MarryExportService;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.service.export.QQShuJiaHuoDongExportService;
import com.junyou.bus.platform.qq.service.export.TencentLuoPanExportService;
import com.junyou.bus.platform.taiwan.export.TaiWanExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.IncrRoleResp;
import com.junyou.bus.role.UserRoleOutputWrapper;
import com.junyou.bus.role.dao.UserRoleDao;
import com.junyou.bus.role.data.ChenmiData;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.entity.XianRenManager;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.role.vo.LevelRankVo;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolestage.export.RoleStageExportService;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.export.RoleStateExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.shizhuang.export.RoleShiZhuangExportService;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.xinmo.export.XinmoExportService;
import com.junyou.bus.xinwen.export.XinwenExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenHunpoExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.configure.ClientTimeScope;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.err.HttpErrorCode;
import com.junyou.event.CreateRoleLogEvent;
import com.junyou.event.ModifyNameLogEvent;
import com.junyou.event.RoleInOrOutLogEvent;
import com.junyou.event.RoleUpLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.ExpConfig;
import com.junyou.gameconfig.export.ExpConfigExportService;
import com.junyou.gameconfig.export.teleport.ICheckCallback;
import com.junyou.gameconfig.export.teleport.TeleportChecker;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ModifyNamePublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.WeiDuanPublicConfig;
import com.junyou.gameconfig.role.configure.export.ZhuJueDzConfig;
import com.junyou.gameconfig.role.configure.export.ZhuJueDzConfigExportService;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.nodecontrol.export.NodeControlExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.math.BitOperationUtil;
import com.junyou.utils.name.RoleFieldCheck;
import com.junyou.utils.name.YYTerminator;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;
import com.kernel.spring.container.DataContainer;
import com.kernel.sync.annotation.Sync;

/**
 * 玩家角色Service
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午4:55:19
 */
@Service
public class UserRoleService  {
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleStateExportService roleStateExportService;
	@Autowired
	private RoleStageExportService roleStageExportService;
	@Autowired
	private ExpConfigExportService expConfigExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private ZhuJueDzConfigExportService zhuJueDzConfigExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private NodeControlExportService nodeControlExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private QQShuJiaHuoDongExportService qqShuJiaHuoDongExportService;
	@Autowired
	private TencentLuoPanExportService tencentLuoPanExportService;
	@Autowired
	private YaoshenExportService yaoshenExportService;
	@Autowired
	private YaoshenHunpoExportService yaoshenHunpoExportService;
	@Autowired
	private XinwenExportService xinwenExportService;
	@Autowired
	private TaiWanExportService taiWanExportService;
	@Autowired
	private PtCommonService ptCommonService;
	@Autowired
	private RoleShiZhuangExportService roleShiZhuangExportService;
	@Autowired
	private MarryExportService marryExportService;
	@Autowired
	private RoleSkillExportService roleSkillExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private XinmoExportService xinmoExportService;
	
	public UserRole getUserRole(Long userRoleId){
		return userRoleDao.cacheLoad(userRoleId, userRoleId);
	}
	
	/**
	 * 玩家自身初始化
	 * @param userId
	 * @param name
	 * @param configId
	 * @param isChenmi
	 * @param serverId
	 * @param ip
	 * @return
	 */
	private UserRole UserRoleInit(String userId, String name, Integer configId, boolean isChenmi,String serverId,String ip,boolean isAutoCreate){
		UserRole userRole = new UserRole();
		
		userRole.setName(name);
		userRole.setUserId(userId);
		userRole.setLastLoginIp(ip);
		userRole.setConfigId(configId);
		userRole.setExp(0l);
		userRole.setLevel(1);
		userRole.setZhenqi(0l);
		userRole.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		userRole.setId(IdFactory.getInstance().generateId(ServerIdType.ROLE));
		userRole.setServerId(serverId == null ? ChuanQiConfigUtil.getServerId() : serverId);
		userRole.setIsFangchenmi(isChenmi ? GameConstants.IS_FANGCHENMI : GameConstants.NOT_IS_FANGCHENMI);
		userRole.setOnceData(0);
		userRole.setUserType(GameConstants.USER_TYPE_PLAYER);
		userRole.setOnlineTime(userRole.getCreateTime().getTime());
		userRole.setOfflineTime(userRole.getCreateTime().getTime() / 1000 * 1000);
		userRole.setChenmiAddOnline(0);
		userRole.setChenmiAddOffline(0);
		userRole.setIsAutoCreate(isAutoCreate ? GameConstants.AUTO_CRAETE_ROLE : GameConstants.NO_AUTO_CRAETE_ROLE);
		
		userRoleDao.insertUserRoleFromDb(userRole);
		String via = null;
		String pf = null;
		if(PlatformConstants.isQQ()){
			Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, userId);	
			if(keyMap != null){
				via  = keyMap.get("via");
				pf  = keyMap.get("pf");
			}
		}
		if(PlatformConstants.isTaiWan()){
			Map<String, String> keyMap = ptCommonService.getRoleMapTW(userRole.getId(),PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL);;	
			if(keyMap != null){
				pf  = keyMap.get("pf");
			}
		}
		//创建玩家账号日志
		GamePublishEvent.publishEvent(new CreateRoleLogEvent(LogPrintHandle.CREATE_USER, userId, userRole.getId().toString(), name, ip, configId.toString(),via,pf));
		return userRole;
	}
	
	
	/**
	 * 判定防沉迷收益
	 * @param userRoleId
	 * @return 0:收益全无，0.5收益减半，1:正常
	 */
	public double getChenmiIncomeRate(Long userRoleId) {
		ChenmiData chenmiData = dataContainer.getData(GameConstants.COMPONENET_CHENMI, userRoleId.toString());
		if(chenmiData == null){
			//如果没有数据则不处理
			return GameConstants.CHENMI_STATUS_ACTIVE;
		}
		
		if (chenmiData.getChenmiState() == GameConstants.CHENMI_STATUS_HALF) {
			return 0.5d;
		} else if (chenmiData.getChenmiState() == GameConstants.CHENMI_STATUS_ACTIVE) {
			return GameConstants.CHENMI_STATUS_ACTIVE;
		}
		return chenmiData.getChenmiState();
	}
	
	public UserRole initUserRole(Long userRoleId){
		return userRoleDao.initUserRoleFromDb(userRoleId);
	}
	
	/**
	 * 创建角色直接访问库
	 * @param userId
	 * @param name
	 * @param configId
	 * @param isChenmi
	 * @param serverId
	 * @param ip
	 * @param isAutoCreate true:自动创号玩家，false:正常玩家(非自动创号玩家)
	 * @param otherMsg 
	 * @return
	 */
	public Object[] createRoleFromDb(String userId, String name, Integer configId, boolean isChenmi,String serverId,String ip,boolean isAutoCreate, Map<Short, Object> otherMsg){
		//玩家名称合法验证
		if(!RoleFieldCheck.checkInputName(name)){
			return AppErrorCode.ROLE_NAME_NO_ALLOW;
		}
		
		//名字长度最少是六个字节，最长20个字节
		if(PlatformConstants.isYueNan()){
//			if(name.getBytes().length <= GameConstants.ROLE_NAME_MIN_SIZE || name.length() > GameConstants.YUENAN_ROLE_NAME_MAX_SIZE){
//				return AppErrorCode.NAME_LENGTH_ERROR;
//			}
		}else{
			if(name.getBytes().length <= GameConstants.ROLE_NAME_MIN_SIZE || name.getBytes().length > GameConstants.ROLE_NAME_MAX_SIZE){
				return AppErrorCode.NAME_LENGTH_ERROR;
			}
		}
		
		//刷YY嫌疑名称
		if(YYTerminator.checkYYTrue(name) && !GameServerContext.getGameAppConfig().isDebug()){
			return AppErrorCode.NAME_S_YY;
		}
		
		//判断同一账户在同服务器上是否达到了最大的角色数
		List<UserRole> realRoles  = userRoleDao.getRolesFromDb(userId, serverId);
		if(realRoles.size() > 0){
			return AppErrorCode.CANNOT_CREATE_ROLE;
		}
		
		//判断主角配置
		ZhuJueDzConfig zuJueDzConfig = zhuJueDzConfigExportService.loadById(configId);
		if(zuJueDzConfig == null){
			return AppErrorCode.JOB_ERROR;
		}
		
		if(GameServerContext.getServerInfoConfig().isOpenRoleNamePrefix()){
			name = GameServerContext.getServerInfoConfig().getRoleNamePrefix()+name;
		}
		
		//角色名唯一验证，判断是否有重复的名称
		int existRoleSize = userRoleDao.getRolesFromDb(name);
		if(existRoleSize > 0){
			return AppErrorCode.ROLE_IS_EXIST;
		}
		
		//服务器Id
		if(serverId == null){ 
			serverId = ChuanQiConfigUtil.getServerId();
		}
		
		//这个地方可以优化一下，创建缓存数据
		try {
			//角色自身创建
			UserRole role = UserRoleInit(userId, name, configId, isChenmi, serverId, ip,isAutoCreate);
			long userRoleId = role.getId();
			//玩家角色货币类型信息创建
			accountExportService.createRoleAccount(userRoleId,userId,serverId);
			//角色创建初始化
			roleStageExportService.roleStageInit(userRoleId, configId,otherMsg);
			//创建VIP信息
			roleVipInfoExportService.createRoleVipInfo(userRoleId);
			//创建业务数据
			roleBusinessInfoExportService.createRoleBusinessInfo(userRoleId);
			//绑定session
			sessionManagerExportService.saveSessin4RoleBaseInfo(userRoleId, role.getName(), configId, role.getLevel(), role.getCreateTime().getTime());
			
			//腾讯创角罗盘数据
			if(PlatformConstants.isQQ()){
				//tencentLuoPanExportService.tencentLuoPanTongYong(userRoleId,QqConstants.REGISTER);
				BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_LUOPAN_TONGYONG, new Object[]{userRoleId,QqConstants.REGISTER});
				//tencentLuoPanExportService.tencentLuoPanLmZhuCe(userRoleId);
				BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_LUOPANLM_ZHUCE, userRoleId);
				BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_GAOQIAN_LOG, userRoleId);
				BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_LUOPAN_OSS_ZHUCE, userRoleId);
				//tencentLuoPanExportService.gaoqianLog(userRoleId);
				BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_VIA_USER, userRoleId);
				//暑假活动
				qqShuJiaHuoDongExportService.sendPlayzoneTask(userRoleId, "up");
			}
			//如果是台湾平台的绑定用户平台ID
			if(PlatformConstants.isTaiWan()){
				BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TAIWAN_PF_BANGDING, userRoleId);
			}
			int type = GameConstants.GAME_JINDU_TYPE_CREATE;
			if((userId.hashCode() & 1) == 1){
				type = GameConstants.GAME_JINDU_TYPE_CREATE_OTHER;
			}
			//游戏进度--创角
			PublicMsgSender.send2PublicInner(InnerCmdType.GAME_JINDU, GameConstants.DEFAULT_ROLE_ID, type);
			//标记聚享游玩家第一次创角
//			if (ChuanQiConfigUtil.getPlatfromId().equals(PlatformPublicConfigConstants.PPS_PLAT_NAME)) {
//				dataContainer.putData(GameConstants.JUXIANGYOU_ROLE_FIRST_LOGIN, String.valueOf(userRoleId), userRoleId);
//			}
			//进入游戏
			return new Object[]{1, UserRoleOutputWrapper.chooseRoleVO(role)};
		}catch (Exception e) {
			ChuanQiLog.error("",e);
			return AppErrorCode.CREATE_EXCEPTION;
		}
	}
	
	/**
	 * 玩家沉迷
	 * @param userRoleId
	 */
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public void setChenmi(Long userRoleId) {
		UserRole userRole = getUserRole(userRoleId);
		//该玩家不是防沉迷玩家则不处理
		if (!userRole.isFangchenmi()) {
			return;
		}
		
		ChenmiData chenmiData = dataContainer.getData(GameConstants.COMPONENET_CHENMI, userRoleId.toString());
		if (null == chenmiData) {
			chenmiData = new ChenmiData();
		}
		chenmiData.setChenmiState(GameConstants.CHENMI_STATUS);
	}
	
	/**
	 * 玩家半沉迷
	 * @param userRoleId
	 */
	public void setHalfChenmi(Long userRoleId) {
		UserRole userRole = getUserRole(userRoleId);
		//该玩家不是防沉迷玩家则不处理
		if(!userRole.isFangchenmi()) {
			return;
		}
		
		ChenmiData chenmiData = dataContainer.getData(GameConstants.COMPONENET_CHENMI, userRoleId.toString());
		if (null == chenmiData) {
			chenmiData = new ChenmiData();
		}
		chenmiData.setChenmiState(GameConstants.CHENMI_STATUS_HALF);
		int chenmiInterval = GameConstants.CHENMI_TIME - GameConstants.HALF_CHENMI_TIME;

		//启动定时（进入沉迷状态）
		scheduleExportService.schedule(userRoleId.toString(),
				GameConstants.COMPONENET_CHENMI, new BusTokenRunable(userRoleId, InnerCmdType.CHENMIN_CMD, userRoleId),
				chenmiInterval, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 玩家上线初始化
	 * @param roleId
	 * @param ip
	 */
	public void logLoginTime(Long userRoleId,String ip) {
		
		UserRole userRole = getUserRole(userRoleId);
		if(!DatetimeUtil.dayIsToday(userRole.getOnlineTime())){
			BusMsgSender.send2One(userRoleId, ClientCmdType.JIHE_ICON_TUISONG, 1);
		}
		
		userRole.setOnlineTime(GameSystemTime.getSystemMillTime());
		userRole.setLastLoginIp(ip);//最后登录Ip
		
		userRoleDao.cacheUpdate(userRole, userRoleId);
		roleStateExportService.change2online(userRoleId, userRole.getName());
		
		try{
			if(XianRenManager.getManager().needXianren()){
				if(XianRenManager.getManager().addXianren(userRoleId, ip)){
					BusMsgSender.send2One(userRoleId, ClientCmdType.SET_XIANREN, true);
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("添加线人出错。",e);
		}
	}
	
	/**
	 * 去除防沉迷
	 * @param userRoleId
	 */
	public int reChenmi(Long userRoleId){
		UserRole userRole = null;
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			userRole = getUserRole(userRoleId);
		}else{
			userRole = userRoleDao.load(userRoleId, userRoleId, AccessType.getDirectDbType());
		}
		//该玩家不是防沉迷玩家则不处理
		if (userRole == null || !userRole.isFangchenmi()) {
			return 1;
		}
		userRole.setChenmiAddOffline(0);
		userRole.setChenmiAddOnline(0);
		userRole.setIsFangchenmi(0);
		dataContainer.removeData(GameConstants.COMPONENET_CHENMI, userRoleId.toString());

		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			userRoleDao.cacheUpdate(userRole, userRoleId);
		}else{
			userRoleDao.updateUserRoleFromDb(userRole);
		}
		return 0;
		
	}
	
	/**
	 * 上线处理防沉迷
	 * @param userRoleId
	 */
	public void onlineChenmi(Long userRoleId){
		UserRole userRole = getUserRole(userRoleId);
		//该玩家不是防沉迷玩家则不处理
		if (!userRole.isFangchenmi()) {
			return;
		}
		
		//未成年沉迷（下线处理沉迷离线时间）
		ChenmiData chenmiData = dataContainer.getData(GameConstants.COMPONENET_CHENMI, userRoleId.toString());
		if (null == chenmiData) {
			chenmiData = new ChenmiData();
		}
		long sysTime = GameSystemTime.getSystemMillTime();
		chenmiData.setStartTime(sysTime);
		
		long addOfflineTime = sysTime - userRole.getOfflineTime();
		int offline = (int) (userRole.getChenmiAddOffline() + addOfflineTime);
		
		// 下线时间超过5小时，清除沉迷在线和离线时间
		if (offline >= GameConstants.CHENMI_TIME) {
			userRole.setChenmiAddOnline(0);
			userRole.setChenmiAddOffline(0);
		} else {
			userRole.setChenmiAddOffline(offline);
		}
		userRoleDao.cacheUpdate(userRole, userRoleId);

		long indulgeAddOnline = userRole.getChenmiAddOnline();
		//健康时间：沉迷在线时间<3小时，收益正常（经验，游戏币）
		if(indulgeAddOnline < GameConstants.HALF_CHENMI_TIME){
			chenmiData.setChenmiState(GameConstants.CHENMI_STATUS_ACTIVE);
			
			int chenmiInterval = (int) (GameConstants.HALF_CHENMI_TIME - userRole.getChenmiAddOnline());
			scheduleExportService.schedule(userRoleId.toString(),GameConstants.COMPONENET_CHENMI, new BusTokenRunable(userRoleId, InnerCmdType.HALF_CHENMIN_CMD,userRoleId), chenmiInterval,TimeUnit.MILLISECONDS);
		
		//疲劳时间：沉迷在线时间<5小时，并>=3小时，收益下降50%（经验，游戏币）
		}else if(indulgeAddOnline < GameConstants.CHENMI_TIME){
			chenmiData.setChenmiState(GameConstants.CHENMI_STATUS_HALF);
			
			int chenmiInterval = (int) (GameConstants.CHENMI_TIME - userRole.getChenmiAddOnline());
			scheduleExportService.schedule(userRoleId.toString(),GameConstants.COMPONENET_CHENMI, new BusTokenRunable(userRoleId, InnerCmdType.CHENMIN_CMD,userRoleId), chenmiInterval,TimeUnit.MILLISECONDS);
			
		//不健康时间：沉迷累积在线时间超过5小时，收益全无（经验，游戏币）
		}else{
			chenmiData.setChenmiState(GameConstants.CHENMI_STATUS);
		}
		dataContainer.putData(GameConstants.COMPONENET_CHENMI, userRoleId.toString(),chenmiData);
	}
	
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public void logOfflineTime(Long roleId) {
		
		UserRole userRole = userRoleDao.cacheLoad(roleId, roleId);
		if(userRole ==null){
			ChuanQiLog.error("roleCache is null,roleId="+roleId);
			return;
		}
		userRole.setOfflineTime(GameSystemTime.getSystemMillTime());
		
		userRoleDao.cacheUpdate(userRole, roleId);
		
		roleStateExportService.change2offline(roleId, userRole.getName());
		
		try{
			XianRenManager.getManager().offline(roleId);
		}catch (Exception e) {
			ChuanQiLog.error("线人下线出错。",e);
		}
	}
	
	/**
	 * 下线处理防沉迷，并打印角色下线日志
	 * @param userRoleId
	 */
	public void offlineChenmi(Long userRoleId){
		
		UserRole userRole = getUserRole(userRoleId);
		if(userRole == null){
			ChuanQiLog.error("offlineChenmi is null"+userRoleId);
			return;
		}
		//未成年沉迷（下线处理沉迷在线时间）
		ChenmiData chenmiData = dataContainer.getData(GameConstants.COMPONENET_CHENMI, userRoleId.toString());
		if(chenmiData != null){
			//如果没有数据则不处理
			if (userRole.isFangchenmi()) {
				long onlineTime = GameSystemTime.getSystemMillTime() - chenmiData.getStartTime();
				Long addOnlineTime =  (onlineTime + userRole.getChenmiAddOnline());
				userRole.setChenmiAddOnline(addOnlineTime.intValue());//沉迷在线时间 = （当前时间 - 开始沉迷时间） + 沉迷时间
				userRoleDao.cacheUpdate(userRole, userRoleId);
				
				scheduleExportService.cancelSchedule(userRoleId.toString(),GameConstants.COMPONENET_CHENMI);
			}
			dataContainer.removeData(GameConstants.COMPONENET_CHENMI, userRoleId.toString());
		}
		//打印角色下线日志
		String via = null;
		String pf = null;
		if(PlatformConstants.isQQ()){
			via  = tencentLuoPanExportService.getUserZhuCeVia(userRoleId);
			pf  = tencentLuoPanExportService.getUserZhuCePf(userRoleId);
		}
		if(PlatformConstants.isTaiWan()){
			pf  = taiWanExportService.getUserZhuCePf(userRoleId);
		}
		GamePublishEvent.publishEvent(new RoleInOrOutLogEvent(LogPrintHandle.ROLE_IN_OR_OUT, LogPrintHandle.ROLE_IO_OUT, userRole.getUserId(), userRoleId.toString(), userRole.getName(), userRole.getLastLoginIp(),via,pf));

	}
	
	/**
	 * 获取该账号玩家的角色直接访问库
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public UserRole getRoleFromDb(String userId, String serverId){
		List<UserRole> realRoles  = userRoleDao.getRolesFromDb(userId, serverId);
		if(realRoles != null && realRoles.size() > 0){
			return realRoles.get(0);
		}
		return null;
	}
	
	/**
	 * 获取已登陆用户信息
	 * @param userRoleId
	 * @return 用户如果不在线，则返回空
	 */
	public RoleWrapper getLoginRole(Long userRoleId){
		try {
			
			UserRole userRole = userRoleDao.cacheLoad(userRoleId, userRoleId);
			if(userRole != null){
				return new RoleWrapper(userRole);
			}else{
				return null;
			}
		} catch (Exception e) {
			ChuanQiLog.error("Role is not online", e);
			return null;
		}
	}
	
	/**
	 * 获取已登陆用户信息(检测用户Gm类型是否已失效)
	 * @param userRoleId
	 * @return 用户如果不在线，则返回空
	 */
	public RoleWrapper getLoginRoleCheckGmType(Long userRoleId){
		try {
			
			UserRole userRole = userRoleDao.cacheLoad(userRoleId, userRoleId);
			if(userRole != null){
				if(roleBusinessInfoExportService.checkKeepTime(userRoleId)){
					userRole.setIsGm(GameConstants.IS_NOT_GM);
					userRoleDao.cacheUpdate(userRole, userRoleId);
				}
				return new RoleWrapper(userRole);
			}else{
				return null;
			}
		} catch (Exception e) {
			ChuanQiLog.error("Role is not online", e);
			return null;
		}
	}
	
	/**
	 * 获取已登陆用户信息
	 * @param userRoleId
	 * @return 用户如果不在线，则返回空
	 */
	public RoleWrapper getLoginRole(Long userRoleId,boolean isChenmi){
		try {
			
			UserRole userRole = userRoleDao.cacheLoad(userRoleId, userRoleId);
			if(userRole != null){
				boolean update = false;
				if(userRole.isFangchenmi() != isChenmi){
					update = true;
					userRole.setIsFangchenmi(isChenmi ? GameConstants.IS_FANGCHENMI : GameConstants.NOT_IS_FANGCHENMI);
				}
				if(update){
					userRoleDao.cacheUpdate(userRole, userRoleId);
				}
				return new RoleWrapper(userRole);
			}else{
				return null;
			}
		} catch (Exception e) {
			ChuanQiLog.error("Role is not online", e);
			return null;
		}
	}
	
	/**
	 * 从数据库获取用户信息
	 * @param userRoleId
	 * @return 
	 */
	public RoleWrapper getUserRoleFromDb(Long userRoleId){
		UserRole userRole = userRoleDao.load(userRoleId, userRoleId, AccessType.getDirectDbType());
		if(userRole != null){
			return new RoleWrapper(userRole);
		}else{
			return null;
		}
	}
	
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public ICheckCallback teleportCheck(Long userRoleId, TeleportChecker checker) {
		
		TeleportCheckCallback callback = new TeleportCheckCallback(userRoleId);
		checker.check(callback);
		
		return callback;
	}
	
	/**
	 * 新增经验(已推送升级和增加经验)包括防沉迷
	 * @param userRoleId
	 * @param exp
	 * @return 当前增加的经验值
	 */
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public IncrRoleResp incrExp(Long userRoleId, Long exp, BusMsgQueue busMsgQueue){
		//处理防沉迷收益
		double chenmiRate = getChenmiIncomeRate(userRoleId);
		exp = (long) (exp * chenmiRate);
		
		//判定经验
		if(exp <= 0 ){
			return null;
		}
		
		UserRole userRole = userRoleDao.cacheLoad(userRoleId, userRoleId);
		//判定是否为满级
		/*if(userRole.getLevel() >= expConfigExportService.getRoleMaxLevel()){
			return null;
		}
		*/
		//获取经验配置
		long nowExp = userRole.getExp() + exp;
		//判定是否升级
		boolean isUpgrage = false;
		Long[] expInfo = new Long[]{userRole.getLevel()*1l,nowExp};
		ExpConfig expConfig = expConfigExportService.getNowExpConfig(expInfo);
		if(expConfig == null){
			return null;
		}
		
		int beforeLevel = userRole.getLevel();
		if(userRole.getLevel() != expConfig.getLevel()){
			userRole.setLevel(expConfig.getLevel());
			isUpgrage = true;//升级
			
			//升级成功，发送到场景（处理玩家升级后的属性）
			busMsgQueue.addStageMsg(userRoleId, InnerCmdType.LEVEL_UPGRADE, expConfig.getLevel());
			//抛出升级日志
			GamePublishEvent.publishEvent(new RoleUpLogEvent(userRoleId, userRole.getName(), beforeLevel, userRole.getLevel()));
			//成就推送
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_LEVEL, userRole.getLevel()});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_LEVEL, userRole.getLevel());
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			} 
			//腾讯暑假活动
			if(PlatformConstants.isQQ()){
				if(userRole.getLevel() == 30){
					qqShuJiaHuoDongExportService.sendPlayzoneTask(userRoleId, userRole.getLevel()+"");
				}else if(userRole.getLevel() == 40){
					qqShuJiaHuoDongExportService.sendPlayzoneTask(userRoleId, userRole.getLevel()+"");
				}else if(userRole.getLevel() == 50){
					qqShuJiaHuoDongExportService.sendPlayzoneTask(userRoleId, userRole.getLevel()+"");
				}
			}
			//一定等级激活妖神魔纹
			yaoshenExportService.roleUpLevel(userRoleId,userRole.getLevel());
			//激活魂魄
			yaoshenHunpoExportService.roleUpLevel(userRoleId, userRole.getLevel());
			//激活糖宝心纹
			xinwenExportService.roleUpLevel(userRoleId, userRole.getLevel());
			//一定等级激活心魔炼丹
			xinmoExportService.roleUpLevel(userRoleId);
			
			//推送支线任务激活
	      	BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_ACTIVITY, new Object[]{ConditionType.HERO_LEVEL,userRole.getLevel()});
	      	//推送等级解锁
	      	BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_ROLE_LEVEL_UP, new Object[]{userRole.getLevel(),userRole.getConfigId()});
		}
		nowExp = expInfo[1];
		if(userRole.getLevel() >= expConfigExportService.getRoleMaxLevel() && nowExp >= expConfig.getNeedexp()){
			nowExp = expConfig.getNeedexp() - 1;
		}
		if(userRole.getExp() != nowExp){
			//增加经验成功
			busMsgQueue.addMsg(userRoleId, ClientCmdType.EXP_CHANGE, new Object[]{nowExp,exp});
		}
		userRole.setExp(nowExp);
		userRoleDao.cacheUpdate(userRole, userRoleId);
		
		
		return new IncrRoleResp(exp, isUpgrage);
	}
	
	/**
	 * 直升等级(本级经验清空)
	 * @param userRoleId
	 * @param level
	 */
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public void addLevel(Long userRoleId, Integer level, BusMsgQueue busMsgQueue){
		UserRole userRole = userRoleDao.cacheLoad(userRoleId, userRoleId);
		if(userRole == null){
			return;
		}
		userRole.setExp(0l);
		int beforeLevel = userRole.getLevel();
		userRole.setLevel(userRole.getLevel() + level);
		userRoleDao.cacheUpdate(userRole, userRoleId);
		//升级成功，发送到场景（处理玩家升级后的属性）
		busMsgQueue.addStageMsg(userRoleId, InnerCmdType.LEVEL_UPGRADE, userRole.getLevel());
		//抛出升级日志
		GamePublishEvent.publishEvent(new RoleUpLogEvent(userRoleId, userRole.getName(), beforeLevel, userRole.getLevel()));
		//增加经验成功
		busMsgQueue.addMsg(userRoleId, ClientCmdType.EXP_CHANGE, new Object[]{0,0});
		//成就推送
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_LEVEL, userRole.getLevel()});
			//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_LEVEL, userRole.getLevel());
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
		//腾讯暑假活动
		if(PlatformConstants.isQQ()){
			if(userRole.getLevel() == 30){
				qqShuJiaHuoDongExportService.sendPlayzoneTask(userRoleId, userRole.getLevel()+"");
			}else if(userRole.getLevel() == 40){
				qqShuJiaHuoDongExportService.sendPlayzoneTask(userRoleId, userRole.getLevel()+"");
			}else if(userRole.getLevel() == 50){
				qqShuJiaHuoDongExportService.sendPlayzoneTask(userRoleId, userRole.getLevel()+"");
			}
		}
		//一定等级激活妖神魔纹
		yaoshenExportService.roleUpLevel(userRoleId,userRole.getLevel());
		//激活魂魄
		yaoshenHunpoExportService.roleUpLevel(userRoleId, userRole.getLevel());
		//激活糖宝心纹
		xinwenExportService.roleUpLevel(userRoleId, userRole.getLevel());
		//一定等级激活心魔炼丹
        xinmoExportService.roleUpLevel(userRoleId);
        //推送支线任务激活
      	BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_ACTIVITY, new Object[]{ConditionType.HERO_LEVEL,userRole.getLevel()});
      	//推送等级解锁
      	BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_ROLE_LEVEL_UP, new Object[]{userRole.getLevel(),userRole.getConfigId()});
	}
	
	/**
	 * 消耗经验
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public Object[] costExp(Long userRoleId,long value){
		UserRole userRole = userRoleDao.cacheLoad(userRoleId, userRoleId);
		if(value < 1){
			return AppErrorCode.NUMBER_ERROR;
		}
		if(userRole == null){
			return AppErrorCode.NO_ENOUGH_EXP;
		}
		if(userRole.getExp() < value){
			return AppErrorCode.NO_ENOUGH_EXP;
		}
		long nowExp = userRole.getExp() - value;
		userRole.setExp(nowExp);
		userRoleDao.cacheUpdate(userRole, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.EXP_CHANGE, new Object[]{nowExp,-value});
		return null;
	}
	
	/**
	 * GM修改角色等级
	 * @param userRoleId
	 * @param level
	 */
	public void gmChangeLevel(Long userRoleId,int level){
		if(level < 1){
			level = 1;
		}else if(level > expConfigExportService.getRoleMaxLevel()){
			level = expConfigExportService.getRoleMaxLevel();
		}
		UserRole userRole = getUserRole(userRoleId);
		userRole.setLevel(level);
		userRole.setExp(0l);
		userRoleDao.cacheUpdate(userRole, userRoleId);
        //推送支线任务激活
      	BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_ACTIVITY, new Object[]{ConditionType.HERO_LEVEL,userRole.getLevel()});
      	//推送等级解锁
      	BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_ROLE_LEVEL_UP, new Object[]{userRole.getLevel(),userRole.getConfigId()});
		//升级成功，发送到场景（处理玩家升级后的属性）
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.LEVEL_UPGRADE, level);
	}
	
	
	public List<LevelRankVo> getLevelRankVo(int limit){
		return userRoleDao.getLevelRankVo(limit);
	}
	public long getUserServerRank(String userId,String serverId){
		return userRoleDao.getUserServerRank(userId, serverId);
	}
	/**
	 * 是否是首次充值
	 * @param userRoleId
	 * @return
	 */
	public boolean isFirstRecharge(Long userRoleId){
		UserRole userRole = getUserRole(userRoleId);
		boolean first = BitOperationUtil.calState(userRole.getOnceData(), GameConstants.ROLE_HAS_RECHARGE);
		if(first){
			int onceData = BitOperationUtil.chanageState(userRole.getOnceData(), GameConstants.ROLE_HAS_RECHARGE);
			userRole.setOnceData(onceData);
			userRoleDao.cacheUpdate(userRole, userRoleId);
		}
		return first;
	}
	
	/**
	 * 是否领取微端奖励,true:领取  false:未领取
	 * @return
	 */
	public boolean isWeiDuanJL(Long userRoleId){
		UserRole role = getUserRole(userRoleId);
		if(role == null){
			return false;
		}
		
		if(!BitOperationUtil.calState(role.getOnceData(), GameConstants.RECIVE_WEIDUAN)){
			return true;
		}
		return false;
	}

	/**
	 * 是否领取微端奖励,true:领取  false:未领取
	 * @return
	 */
	public boolean isWeiDuanJL2(Long userRoleId){
		UserRole role = getUserRole(userRoleId);
		if(role == null){
			return false;
		}

		if(!BitOperationUtil.calState(role.getOnceData(), GameConstants.RECIVE_WEIDUAN2)){
			return true;
		}
		return false;
	}
	
	private class TeleportCheckCallback implements ICheckCallback{
		
		private Long roleId;
		
		private Object[] errorCode;
		
		public TeleportCheckCallback(Long roleId) {
			this.roleId = roleId;
		}

		@Override
		public void moneyError() {
			this.errorCode = AppErrorCode.TELEPORT_MONEY_ERROR;
		}
		@Override
		public void liquanError() {
			this.errorCode = AppErrorCode.TELEPORT_LIQUAN_ERROR;
		}

		@Override
		public void maxlevelError() {
			this.errorCode = AppErrorCode.TELEPORT_MAX_LEVEL_ERROR;
		}
		
		@Override
		public void gzLevelError() {
			this.errorCode = AppErrorCode.TELEPORT_GZ_ERROR;
		}

		public void startTimeError(){
			this.errorCode = AppErrorCode.TELEPORT_START_TIME_ERROR;
		}
		public void endTimeError(){
			this.errorCode = AppErrorCode.TELEPORT_END_TIME_ERROR;
		}
		
		@Override
		public void vipError() {
			this.errorCode = AppErrorCode.TELEPORT_VIP_ERROR;
		}
		
		@Override
		public void minLevelError() {
			this.errorCode = AppErrorCode.TELEPORT_MIN_LEVEL_ERROR;
		}
		
		public void noGuildError() {
			this.errorCode = AppErrorCode.TELEPORT_GUILD_ERROR;
		}
		
		@Override
		public void mustItemError() {
			this.errorCode = AppErrorCode.TELEPORT_MUST_ITEM_ERROR;
		}

		@Override
		public void useItemError() {
			this.errorCode = AppErrorCode.TELEPORT_NEED_ITEM_ERROR;
		}

		
		@Override
		public void timeError() {
			this.errorCode = AppErrorCode.TELEPORT_TIME_ERROR;
		}

		@Override
		public boolean isSuccess() {
			return null == errorCode;
		}

		@Override
		public Object[] getErrorCode() {
			return errorCode;
		}

		@Override
		public void useItemHandle(String goodsId, int goodsCount) {
		}

		@Override
		public boolean maxLevelCheck(int maxLevel) {
			RoleWrapper role = getLoginRole(roleId);
			return role.getLevel() <= maxLevel;
		}

		@Override
		public boolean minLevelCheck(int minLevel) {
			RoleWrapper role = getLoginRole(roleId);
			return role.getLevel() >= minLevel;
		}

		@Override
		public boolean haveGuild() {
			return false;
		}

		@Override
		public boolean guanZhiLevelCheck(int gzLevel) {
			return false;
		}

		@Override
		public boolean startDayCheck(String startDay) {
			return false;
		}

		@Override
		public boolean endDayCheck(String endDay) {
			return false;
		}
		@Override
		public boolean startTimeCheck(long startTime) {
			return GameSystemTime.getSystemMillTime() > startTime;
		}

		@Override
		public boolean endTimeCheck(long endTime) {
			return GameSystemTime.getSystemMillTime() < endTime;
		}
		
		@Override
		public boolean startDayTimeCheck(int hour, int minute) {
			return false;
		}

		@Override
		public boolean endDayTimeCheck(int hour, int minute) {
			return false;
		}

		@Override
		public boolean mustItemCheck(Map<String, Integer> items) {
			return false;
		}

		@Override
		public boolean useItemCheck(Map<String, Integer> items) {
			return false;
		}

		@Override
		public boolean vipCheck(Integer vipLevel) {
			return false;
		}

		@Override
		public boolean activeTimeCheck(ClientTimeScope timeScope) {
			return false;
		}

		@Override
		public void success() {
			errorCode = null;
		}
	}
	
	/**
	 * 增加真气
	 * @param userRoleId
	 * @param value
	 */
	public void addZhenqi(long userRoleId,long value){
		UserRole userRole = getUserRole(userRoleId);
		long zhenqi = userRole.getZhenqi() + value;
		userRole.setZhenqi(zhenqi);
		userRoleDao.cacheUpdate(userRole, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, zhenqi);
	}
	
	/**
	 * 真气是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughZhenqi(long userRoleId,long value){
		UserRole userRole = getUserRole(userRoleId);
		return userRole.getZhenqi() >= value;
	}
	/**
	 * 消耗真气
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costZhenqi(long userRoleId, long value){
		if(value < 0){
			return false;
		}
		UserRole userRole = getUserRole(userRoleId);
		long zhenqi = userRole.getZhenqi() - value;
		if(zhenqi < 0)return false;
		userRole.setZhenqi(zhenqi);
		userRoleDao.cacheUpdate(userRole, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, zhenqi);
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.XIAOHAO_ZHENQI, value});
		return true;
	}
	
	
	/**
	 * 领取微端奖励
	 * @param userRoleId
	 * @return 
	 */
	public Object[] lqWeiDuanAward(Long userRoleId) {
		UserRole userRole = getUserRole(userRoleId);
		if(userRole == null){
			return AppErrorCode.NUMBER_ERROR;
		}
		
		if(!nodeControlExportService.isWeiDuanLogin(userRoleId)){
			return AppErrorCode.NOT_WEIDUAN;
		}
		
		//已领取过
		int state = userRole.getOnceData();
		if(!BitOperationUtil.calState(state, GameConstants.RECIVE_WEIDUAN)){
			return AppErrorCode.YI_LQWEIDUAN;
		}
		//没有奖励配置
		WeiDuanPublicConfig weiDuanPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WEIDUAN);
		if(weiDuanPublicConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Map<String, Integer> goodsMap = weiDuanPublicConfig.getJiangitem();
		if(goodsMap != null && goodsMap.size() > 0){
			Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(goodsMap, userRoleId);
			//背包空间不足
			if(code != null){
				return code;
			}
			
			//修改领取状态，微端登录领取是第1位。
			state = BitOperationUtil.chanageState(state, GameConstants.RECIVE_WEIDUAN);
			userRole.setOnceData(state);
			userRoleDao.cacheUpdate(userRole, userRoleId);
			
			//发放奖励
			roleBagExportService.putGoodsAndNumberAttr(goodsMap, userRoleId, GoodsSource.WEIDUAN_GET, LogPrintHandle.GET_WEIDUAN_GET, LogPrintHandle.GBZ_WEIDUAN_GET, true);
		}
		
		return AppErrorCode.OK;
	}
	/**
	* 获得绑定手机奖励状态
	* @param userRoleId
	* @return
	*/
	public Integer getPhoneRewardStatus(Long userRoleId){
		return getUserRole(userRoleId).getPhoneRewardStatus();
	}
	/**
	* 更改绑定手机奖励状态
	* @param userRoleId
	* @return
	*/
	public void updatePhoneReward(Long userRoleId,Integer status){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			UserRole userRole = getUserRole(userRoleId);
			userRole.setPhoneRewardStatus(status);
			userRoleDao.cacheUpdate(userRole, userRoleId);
		}else{
			UserRole userRole = userRoleDao.load(userRoleId, userRoleId, AccessType.getDirectDbType());
			userRole.setPhoneRewardStatus(status);
			userRoleDao.update(userRole, userRoleId, AccessType.getDirectDbType());
		}
	}
	
	
	public static void main(String[] args) {
		boolean first = BitOperationUtil.calState(3, GameConstants.ROLE_HAS_RECHARGE);
		
		System.out.println(7&1);
		if(first){
			int onceData = BitOperationUtil.chanageState(0, GameConstants.ROLE_HAS_RECHARGE);
			System.out.println(onceData);
		}
		System.out.println(first);
	}
	
	
	/**
	 * pps单服激活玩家数据 (获取各个服务器24小时内的新创角人数，新创角的角色信息列表) 定时要在跨天后执行
	 * @param serverId
	 */
	public void getPPsJHRoles(String serverId){
		String dateStr = DatetimeUtil.formatTime(DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), -1), DatetimeUtil.FORMART2);
		List<Map<String, Object>> list = userRoleDao.getPPsJHRoles(serverId, dateStr);
		
		Map<String,Object> totalMap = getPPsMap();
		if(list != null && list.size() > 0){
			Map<String, Object> roleDatas = new HashMap<>();
			roleDatas.put("total", list.size());
			
			Map<String, Object> userList = new HashMap<>();
			for (Map<String, Object> map : list) {
				Map<String,Object> tmpRole = new HashMap<>();
				
				String userId = (String)map.get("user_id");
				tmpRole.put("roleName", map.get("name"));
				tmpRole.put("roleLevel",map.get("level"));
				//时间转换时间格式
				Timestamp createTime = (Timestamp)map.get("create_time");
				tmpRole.put("createTime",DatetimeUtil.formatTime(createTime.getTime(), DatetimeUtil.FORMART3));
				
				userList.put(userId, tmpRole);
			}
			roleDatas.put("userList", userList);
			totalMap.put("data", roleDatas);
		}
		
		String dataJsonStr = JSON.toJSONString(totalMap);
		//请求后台让后台先存成文件
		StringBuffer requestUrl = new StringBuffer();
		requestUrl.append(ChuanQiConfigUtil.getManageToolUrl());
		requestUrl.append("/gameRoleEveryDayData.do");
		
		//参数填充
		StringBuffer params = new StringBuffer();
		params.append("psid=").append(GameServerContext.getGameAppConfig().getPlatformServerId());
		params.append("&ptid=").append(GameServerContext.getGameAppConfig().getPlatformId());
		params.append("&type=").append("1");
		params.append("&dateStr=").append(dateStr);
		params.append("&data=").append(dataJsonStr);
		
		//发起后台请求
		HttpClientMocker.requestMockPost(requestUrl.toString(), params.toString());
	}

	
	/**
	 * pps单服活跃玩家数据 (获取各个服务器24小时内的进入游戏游玩的有效用户数) 定时要在跨天后执行
	 * @param serverId
	 */
	public void getPPsHYRoles(String serverId){
		String dateStr = DatetimeUtil.formatTime(DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), -1), DatetimeUtil.FORMART2);
		List<Map<String, Object>> list = userRoleDao.getPPsHYRoles(serverId, dateStr);
		Map<String,Object> totalMap = getPPsMap();
		
		if(list != null && list.size() > 0){
			Map<String, Object> roleDatas = new HashMap<>();
			roleDatas.put("total", list.size());
			
			Map<String, Object> userList = new HashMap<>();
			for (Map<String, Object> map : list) {
				Map<String,Object> tmpRole = new HashMap<>();
				
				String userId = (String)map.get("user_id");
				tmpRole.put("roleName", map.get("name"));
				tmpRole.put("roleLevel",map.get("level"));
				//时间转换时间格式
				Long createTime = (Long)map.get("online_time");
				tmpRole.put("createTime",DatetimeUtil.formatTime(createTime, DatetimeUtil.FORMART3));
				
				userList.put(userId, tmpRole);
			}
			roleDatas.put("userList", userList);
			totalMap.put("data", roleDatas);
		}
		
		String dataJsonStr = JSON.toJSONString(totalMap);
		//请求后台让后台先存成文件
		StringBuffer requestUrl = new StringBuffer();
		requestUrl.append(ChuanQiConfigUtil.getManageToolUrl());
		requestUrl.append("/gameRoleEveryDayData.do");
		
		//参数填充
		StringBuffer params = new StringBuffer();
		params.append("psid=").append(GameServerContext.getGameAppConfig().getPlatformServerId());
		params.append("&ptid=").append(GameServerContext.getGameAppConfig().getPlatformId());
		params.append("&type=").append("2");
		params.append("&dateStr=").append(dateStr);
		params.append("&data=").append(dataJsonStr);
		
		//发起后台请求
		HttpClientMocker.requestMockPost(requestUrl.toString(), params.toString());
	}
	
	public String changeGmType(String userId,String serverId,Integer gmType,Long keepTime){
		UserRole role = getRoleFromDb(userId, serverId);
		if(role == null){
			return HttpErrorCode.USER_NOT_EXIST;
		}
		role.setIsGm(gmType);
		boolean isOnline = publicRoleStateExportService.isPublicOnline(role.getId());
		if(keepTime != null && keepTime > 0){
			keepTime = keepTime * GameConstants.DAY_TIME;
			roleBusinessInfoExportService.changeKeepTime(role.getId(), keepTime, isOnline);
		}
		if(isOnline){
			userRoleDao.cacheUpdate(role, role.getId());
		}else{
			userRoleDao.updateUserRoleFromDb(role);
		}
		return HttpErrorCode.SUCCESS;
	}
	
	/**
	 * 获取数据map基本对象
	 * @return
	 */
	private Map<String,Object> getPPsMap(){
		Map<String,Object> totalMap = new HashMap<>();
		totalMap.put("success", true);
		totalMap.put("message", "ok");
		
		return totalMap;
	}
	
	public void sendHourCreateInfo(){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<>();
		queryParams.put("startTime", DatetimeUtil.formatTime(DatetimeUtil.getBeforeHourTimeStr(1), DatetimeUtil.FORMART3));
		queryParams.put("endTime", DatetimeUtil.formatTime(DatetimeUtil.getBeforeHourTimeStr(0), DatetimeUtil.FORMART3));
		@SuppressWarnings("unchecked")
		List<Integer> list = userRoleDao.query("selectHourCreateRoleCount", queryParams);
		try{
			if(list != null && list.size() > 0){
				StringBuffer requestUrl = new StringBuffer();
				requestUrl.append(ChuanQiConfigUtil.getManageToolUrl());
				requestUrl.append("/get_role_number.do");
				
				//参数填充
				StringBuffer params = new StringBuffer();
				params.append("serverId=").append(GameServerContext.getGameAppConfig().getServerId());
				params.append("&roleNumber=").append(list.get(0));
				params.append("&time=").append(DatetimeUtil.formatTime(DatetimeUtil.getBeforeHourTimeStr(1), DatetimeUtil.FORMART5));
				
				//发起后台请求
				HttpClientMocker.requestMockPost(requestUrl.toString(), params.toString());
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	public Object[] getModifyNameCd(Long userRoleId){
		UserRole userRole = getUserRole(userRoleId);
		Long lastModifyNameTime = userRole.getLastModifyNameTime();
		if(lastModifyNameTime==null){
			return new Object[]{-1};
		}else{
			ModifyNamePublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MODIFY_NAME);
			if(config == null){
				return AppErrorCode.CONFIG_ERROR;
			}
			long currentTime = GameSystemTime.getSystemMillTime();
			if(config.getCdTime()+lastModifyNameTime >currentTime){
				return new Object[]{config.getCdTime()+lastModifyNameTime-currentTime};
			}else{
				return new Object[]{-1};
			}
		}
	}
	
	public Object[] modifyName(Long userRoleId,String name){
		//玩家名称合法验证
		if(!RoleFieldCheck.checkInputName(name)){
			return AppErrorCode.ROLE_NAME_NO_ALLOW;
		}
		
		//名字长度最少是六个字节，最长20个字节
		if(name.getBytes().length <= GameConstants.ROLE_NAME_MIN_SIZE || name.getBytes().length > GameConstants.ROLE_NAME_MAX_SIZE){
			return AppErrorCode.NAME_LENGTH_ERROR;
		}
		
		//刷YY嫌疑名称
		if(YYTerminator.checkYYTrue(name)){
			return AppErrorCode.NAME_S_YY;
		}
		ModifyNamePublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MODIFY_NAME);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//校验道具
		int num = roleBagExportService.getBagItemCountByGoodsId(config.getGoodsId(), userRoleId);
		if(num <= 0){
			return AppErrorCode.MODIFY_NAME_ITEM_ERROR;
		}
		UserRole userRole = getUserRole(userRoleId);
		Long lastModifyNameTime = userRole.getLastModifyNameTime();
		if(lastModifyNameTime!=null){
			long currentTime = GameSystemTime.getSystemMillTime();
			if(currentTime < lastModifyNameTime+config.getCdTime()){
				return AppErrorCode.MODIFY_NAME_IN_CD;
			}
		}
		String beforeName = userRole.getName();
		int index = beforeName.indexOf(".");
		String afterName = null;
		if(index == -1){
			afterName = name;
		}else{
			String prefix = beforeName.substring(0, index+1);
			afterName = prefix+name;
		}
		//校验名字是否存在
		if(userRoleDao.isNameExist(afterName)){
			return AppErrorCode.ROLE_IS_EXIST;
		}
		//扣道具
		roleBagExportService.removeBagItemByGoodsId(config.getGoodsId(), 1, userRoleId, GoodsSource.GOODS_MODIFY_NAME, true, true);
		//改名
		userRole.setName(afterName);
		//修改改名状态
		int state = userRole.getOnceData();
		state = BitOperationUtil.chanageState(state, GameConstants.HAS_MODIFY_NAME);
		userRole.setOnceData(state);
		userRole.setLastModifyNameTime(GameSystemTime.getSystemMillTime());
		userRoleDao.cacheUpdate(userRole, userRoleId);
		//打日志
		GamePublishEvent.publishEvent(new ModifyNameLogEvent(userRoleId,beforeName, afterName));
		//发送事件
		BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.MODIFY_NAME_EVENT_1, new Object[]{userRoleId,beforeName,afterName});
		BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.MODIFY_NAME_EVENT_2, new Object[]{userRoleId,beforeName,afterName});
		BusMsgSender.send2Public(InnerCmdType.MODIFY_NAME_EVENT_3, new Object[]{userRoleId,beforeName,afterName});
		BusMsgSender.send2BusInner(userRoleId,InnerCmdType.MODIFY_NAME_EVENT_4, afterName);
		BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.MODIFY_NAME_EVENT_5, new Object[]{userRoleId,beforeName,afterName});
		BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.MODIFY_NAME_EVENT_6, new Object[]{userRoleId,beforeName,afterName});
		ChuanQiLog.info("userRoleId modify name from {} to {}", beforeName,afterName);
		return new Object[]{AppErrorCode.SUCCESS,afterName};
	}
	
	public Object[] modifyJob(Long userRoleId,Integer configId,String item){
		UserRole userRole = getUserRole(userRoleId);
		
		//判断主角配置
		ZhuJueDzConfig zuJueDzConfig = zhuJueDzConfigExportService.loadById(configId);
		ZhuJueDzConfig yuanDzConfig = zhuJueDzConfigExportService.loadById(userRole.getConfigId());
		if(zuJueDzConfig == null){
			return AppErrorCode.JOB_ERROR;
		}
		if(userRole.getConfigId().intValue() == configId.intValue()){
			return AppErrorCode.ZHUANZHI_IS_TONG_JOB;
		}
		//判断是否有道具
		int num = roleBagExportService.getBagItemCountByGoodsId(item, userRoleId);
		if(num <= 0){
			return AppErrorCode.MODIFY_NAME_ITEM_ERROR;
		}
		//如果是同性别，直接修改configId
		if(yuanDzConfig.getSex().intValue() == zuJueDzConfig.getSex().intValue()){
			userRole.setConfigId(configId);
			
			userRoleDao.cacheUpdate(userRole, userRoleId);
			
			//转技能
			/*//通知场景转职变化
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_ROLE_CHANGE_JOB, configId);*/
			
		}else{
			//转时装
			Object[] errorCode = roleShiZhuangExportService.zhuanZhiShiZhuangZhuanHuan(userRoleId);
			if(null != errorCode){
			    return errorCode;
			}
			//离婚
			Integer jhStat = marryExportService.getUserRoleJieHunStat(userRoleId);
			if(jhStat == GameConstants.MARRY_STATE_DING){//订婚
				marryExportService.zhuanZhiCancelDing(userRoleId);
			}else if(jhStat == GameConstants.MARRY_STATE_MARRY || jhStat == GameConstants.MARRY_STATE_DIVORCE){//结婚或者离婚状态
				marryExportService.zhuanZhiJieChuJieHun(userRoleId);
			}
		}
		//公会
		guildExportService.zhuanzhi(userRoleId, configId);
		//转技能
		roleSkillExportService.zhuanZhiJiNeng(userRoleId, configId);
		//消耗道具
		roleBagExportService.removeBagItemByGoodsId(item, 1, userRoleId, GoodsSource.ZHUANG_ZHI, true, true);
		//通知场景转职变化
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_ROLE_CHANGE_JOB, configId);
		return new Object[]{1,configId};
	}
	
	/**
	 * 获取总注册人数
	 * @return
	 */
	public int getTotalRegistCount(){
		return userRoleDao.getTotalRegistCount();
	}
	
	/**
	 * 获取时间区间内注册人数
	 * @return
	 */
	public int getTimeRegistCount(long startTime,long endTime){
		return userRoleDao.getTimeRegistCount(startTime, endTime);
	}




	/**
	 * 领取微端奖励
	 * @param userRoleId
	 * @return
	 */
	public Object[] lqWeiDuanAward2(Long userRoleId) {
		UserRole userRole = getUserRole(userRoleId);
		if(userRole == null){
			return AppErrorCode.NUMBER_ERROR;
		}

//		if(!nodeControlExportService.isWeiDuanLogin(userRoleId)){
//			return AppErrorCode.NOT_WEIDUAN;
//		}

		//已领取过
		int state = userRole.getOnceData();
		if(!BitOperationUtil.calState(state, GameConstants.RECIVE_WEIDUAN2)){
			return AppErrorCode.YI_LQWEIDUAN;
		}
		//没有奖励配置
		WeiDuanPublicConfig weiDuanPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_DESKSAVA);
		if(weiDuanPublicConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}

		Map<String, Integer> goodsMap = weiDuanPublicConfig.getJiangitem();
		if(goodsMap != null && goodsMap.size() > 0){
			Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(goodsMap, userRoleId);
			//背包空间不足
			if(code != null){
				return code;
			}

			//修改领取状态，微端登录领取是第1位。
			state = BitOperationUtil.chanageState(state, GameConstants.RECIVE_WEIDUAN2);
			userRole.setOnceData(state);
			userRoleDao.cacheUpdate(userRole, userRoleId);

			//发放奖励
			roleBagExportService.putGoodsAndNumberAttr(goodsMap, userRoleId, GoodsSource.WEIDUAN_GET, LogPrintHandle.GET_WEIDUAN_GET, LogPrintHandle.GBZ_WEIDUAN_GET, true);
		}

		return AppErrorCode.OK;
	}
}