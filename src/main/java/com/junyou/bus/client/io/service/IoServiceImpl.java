package com.junyou.bus.client.io.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.bus.jingji.service.FightPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.service.RoleBagService;
import com.junyou.bus.bagua.service.export.BaguaExportService;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.chenghao.service.export.ChenghaoExportService;
import com.junyou.bus.chengjiu.export.RoleChengJiuExportService;
import com.junyou.bus.chengshen.export.ChengShenExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.chongwu.service.export.ChongwuExportService;
import com.junyou.bus.daytask.export.TaskDayExportService;
import com.junyou.bus.email.export.EmailRelationExportService;
import com.junyou.bus.flower.export.FlowerSendExportService;
import com.junyou.bus.friend.export.FriendExportService;
import com.junyou.bus.fuben.export.FubenExportService;
import com.junyou.bus.fuben.export.MoreFubenExportService;
import com.junyou.bus.happycard.service.export.HappyCardExportService;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.hefuSevenlogin.export.HefuSevenLoginExportService;
import com.junyou.bus.hongbao.export.HongbaoExportService;
import com.junyou.bus.huajuan.service.export.HuajuanExportService;
import com.junyou.bus.huajuan2.export.Huajuan2ExportService;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.jinfeng.export.JinFengExportService;
import com.junyou.bus.kuafu_boss.service.export.KuafuBossExportService;
import com.junyou.bus.kuafuarena1v1.service.export.KuafuArena1v1SourceExportService;
import com.junyou.bus.laowanjia.export.RefbLaowanjiaExportService;
import com.junyou.bus.lj.export.LJExportService;
import com.junyou.bus.login.export.RefabuSevenLoginExportService;
import com.junyou.bus.maigu.service.export.MaiguExportService;
import com.junyou.bus.marry.export.MarryExportService;
import com.junyou.bus.notice.export.NoticeExportService;
import com.junyou.bus.offlineexp.export.OfflineExpExportService;
import com.junyou.bus.online.export.RoleOnlineExportService;
import com.junyou.bus.onlinerewards.export.OnlineRewardsExportService;
import com.junyou.bus.platform.export.PlatformExportService;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.qq.service.export.TencentLuoPanExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.recharge.export.RechargeExportService;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.role.export.TangbaoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.setting.export.RoleSettingExportService;
import com.junyou.bus.shenmo.service.export.KuafuArena4v4SourceExportService;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.shizhuang.export.RoleShiZhuangExportService;
import com.junyou.bus.shoplimit.export.ShopLimitExportService;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tafang.export.RoleTaFangExportService;
import com.junyou.bus.task.export.TaskExportService;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.touzi.export.RoleTouziExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.service.RoleVipInfoService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.wuxing.export.WuXingMoShenExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xinmo.export.XinmoExportService;
import com.junyou.bus.yabiao.service.export.YabiaoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zhuansheng.export.ZhuanshengExportService;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.nodecontrol.export.NodeControlExportService;
import com.junyou.public_.service.MsgFilterService;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.public_.trade.export.TradeExportService;
import com.junyou.stage.configure.export.impl.RoleBehaviourExportService;
import com.junyou.stage.export.TeamExportService;
import com.junyou.utils.zip.CompressConfigUtil;
import com.kernel.data.cache.CacheManager;

@Service
public class IoServiceImpl{

	/**
	 * 是否使用缓存
	 */
	private final boolean usecache = true;

	@Autowired
	private FightPowerService fightPowerService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private JinFengExportService jinFengExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private RoleBehaviourExportService roleBehaviourExportService;
	@Autowired
	private TaskExportService taskExportService;
	@Autowired
	private RechargeExportService rechargeExportService;
	@Autowired
	private EmailRelationExportService emailRelationExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private TeamExportService teamExportService;
	@Autowired
	private RoleSettingExportService roleSettingExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private FubenExportService fubenExportService;
	@Autowired
	private RoleSkillExportService roleSkillExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private FriendExportService friendExportService;
	@Autowired
	@Qualifier("roleCacheManager")
	private CacheManager roleCacheManager;
	@Autowired
	private RoleBagService roleBagService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private YaoshenExportService yaoshenExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired 
	private TradeExportService tradeExportService;
	@Autowired
	private RoleOnlineExportService roleOnlineExportService;
	@Autowired
	private OfflineExpExportService offlineExpExportService;
	@Autowired
	private TaskDayExportService taskDayExportService;
	@Autowired
	private MoreFubenExportService moreFubenExportService;
	@Autowired
	private BaguaExportService baguaExportService;
	@Autowired
	private MaiguExportService maiguExportService;
	@Autowired
	private ChiBangExportService chibangExportService;
	@Autowired
	private QiLingExportService qiLingExportService;
	@Autowired
	private TianYuExportService tianYuExportService;
	@Autowired
	private NoticeExportService noticeExportService;
	@Autowired
	private TangbaoExportService tangbaoExportService;
	@Autowired
	private YabiaoExportService yabiaoExportService;
	@Autowired
	private MsgFilterService msgFilterService;
	@Autowired
	private NodeControlExportService nodeControlExportService;
	@Autowired
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private ChengShenExportService chengShenExportService;
	@Autowired
	private HefuSevenLoginExportService hefuSevenLoginExportService;
	@Autowired
	private TencentLuoPanExportService tencentLuoPanExportService;
	@Autowired
	private ShenQiExportService shenqiExportService;
	@Autowired
	private ChenghaoExportService chenghaoExportService; 
	@Autowired
	private RoleTouziExportService roleTouziExportService;
	@Autowired
	private PlatformExportService platformExportService;
	@Autowired
	private OnlineRewardsExportService onlineRewardsExportService;
	@Autowired
	private FlowerSendExportService flowerSendExportService;
	@Autowired
	private QqExportService qqExportService;
	@Autowired
	private RefabuSevenLoginExportService refabuSevenLoginExportService;
	@Autowired
	private HongbaoExportService hongbaoExportService;
	@Autowired
	private RoleChengJiuExportService roleChengJiuExportService;
	@Autowired
	private TerritoryExportService territoryExportService;
	@Autowired
	private HcZhengBaSaiExportService hcZhengBaSaiExportService;
	@Autowired
	private ChongwuExportService chongwuExportService;
	@Autowired
	private MarryExportService marryExportService;
	@Autowired
	private HappyCardExportService happyCardExportService;
	@Autowired
	private RoleShiZhuangExportService roleShiZhuangExportService;
	@Autowired
	private KuafuArena1v1SourceExportService kuafuArena1v1SourceExportService;
	@Autowired
	private KuafuArena4v4SourceExportService kuafuArena4v4SourceExportService;
	@Autowired
	private RoleTaFangExportService roleTaFangExportService;
	@Autowired
	private ZhuanshengExportService zhuanshengExportService;
	@Autowired
	private KuafuBossExportService kuafuBossExportService;
	@Autowired
	private HuajuanExportService huajuanExportService;
	@Autowired
	private RefbLaowanjiaExportService refbLaowanjiaExportService;
	@Autowired
	private WuXingMoShenExportService wuXingMoShenExportService;
    @Autowired
    private XinmoExportService xinmoExportService;
    @Autowired
    private Huajuan2ExportService huajuan2ExportService;
    @Autowired
	private WuQiExportService wuQiExportService;
    @Autowired
    private TaskBranchService taskBranchService;
    @Autowired
    private PublicRoleStateService publicRoleStateService;
    @Autowired
    private RoleVipInfoService roleVipInfoService;
    @Autowired
    private LJExportService lJExportService;
	/**
	 * 角色登入节点
	 * @param roleid
	 * @param ip 
	 */
	public void roleIn(Long userRoleId,String ip) {
		if(usecache){
			roleCacheManager.activateRoleCache(userRoleId);
		}
		
		roleInHandle(userRoleId, ip);
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 * @param ip
	 */
	private void roleInHandle(Long userRoleId,String ip){
		//玩家上线初始化（包括防沉迷，设置角色状态为在线）
		roleExportService.logLoginTime(userRoleId, ip);
		//充值计算
		rechargeExportService.onlineCalRecharge(userRoleId);
		/**
		 * 推送给客服端背包数据
		 */
		roleBagService.loginHandle(userRoleId);
		//处理禁言上线业务
		jinFengExportService.onlineHandle(userRoleId);
		//处理邮件上线业务
		emailRelationExportService.onlineHandle(userRoleId);
		//成员上线业务
		guildExportService.onlineHandle(userRoleId);
		//副本上线业务
		fubenExportService.onlineHandle(userRoleId);
		//技能上线业务
		roleSkillExportService.onlineHandle(userRoleId);
		//VIP上线业务
		roleVipInfoExportService.onlineHandle(userRoleId);
		//任务上线业务
		taskExportService.onlineHandle(userRoleId);
		//日常任务上线业务
		taskDayExportService.onlineHandle(userRoleId);
		//多人副本上线补发奖励
		moreFubenExportService.onlineHandle(userRoleId);
		//八卦副本上线补发奖励
		baguaExportService.onlineHandle(userRoleId);
		//埋骨之地副本上线补发奖励
		maiguExportService.onlineHandle(userRoleId);
		//角色业务上线处理
		roleBusinessInfoExportService.onlineHandle(userRoleId);
		//上线设置在线奖励登陆时间
		roleOnlineExportService.loginHandler(userRoleId);
		//上线设置离线时间
		offlineExpExportService.loginHandler(userRoleId);
		//糖宝上线处理
		tangbaoExportService.onlineHandle(userRoleId);
		//佩戴神器推送
		shenqiExportService.onlineHandle(userRoleId);
		//坐骑潜能丹，成长丹使用数量推送
		zuoQiExportService.onlineHandle(userRoleId);
		//翅膀潜能丹，成长丹使用数量推送
		chiBangExportService.onlineHandle(userRoleId);
		//器灵潜能丹，成长丹使用数量推送
		qiLingExportService.onlineHandle(userRoleId);
		//天羽潜能丹，成长丹使用数量推送
		tianYuExportService.onlineHandle(userRoleId);
		//妖神状态推送
		yaoshenExportService.onlineHandle(userRoleId);
		//仙剑潜能丹，成长丹使用数量推送
		xianJianExportService.onlineHandle(userRoleId);
		//战甲潜能丹，成长丹使用数量推送
		zhanJiaExportService.onlineHandle(userRoleId);
		//投资计划
		roleTouziExportService.onlineTouzi(userRoleId);
		//活跃度
		huoYueDuExportService.onlineHandle(userRoleId);
		//推送神魂值to client
		chengShenExportService.onlineHandlerToClientSHZ(userRoleId);
		//平台业务
		platformExportService.onlineHandle(userRoleId);
		//成就点数
		roleChengJiuExportService.onlineHandle(userRoleId);
		//合服七天登陆
		hefuSevenLoginExportService.onlineHandle(userRoleId);
		//称号
		chenghaoExportService.onlineHandle(userRoleId);
		//聚享游玩家创角登陆游戏
//		juXiangYouExportService.onlineHandle(userRoleId);
		//掌门领地战勋章
		territoryExportService.onlineHandle(userRoleId);
		//争霸赛勋章
		hcZhengBaSaiExportService.onlineHandle(userRoleId);
		//出战宠物推送
		chongwuExportService.onlineHandle(userRoleId);
		//婚姻上线业务
		marryExportService.onlineHandle(userRoleId);
		//欢乐卡牌
		happyCardExportService.onlineHandle(userRoleId);
		//限时时装
		roleShiZhuangExportService.onlineHandle(userRoleId);
		//塔防上线业务
		roleTaFangExportService.onlineHandle(userRoleId);
		//热发布在线奖励
		onlineRewardsExportService.onlineHandle(userRoleId);
		//送花
		flowerSendExportService.onlineHandle(userRoleId);
		//登陆活动
		refabuSevenLoginExportService.onlineHandle(userRoleId);
		//跨服世界boss
		kuafuBossExportService.onlineHandle(userRoleId);
		//画卷
		huajuanExportService.onlineHandle(userRoleId);
		//老玩家回归
		refbLaowanjiaExportService.onlineHandle(userRoleId);
		//五行附体
		wuXingMoShenExportService.onlineTuiSong(userRoleId);
		//心魔炼丹上线:1:开启丹药生产定时器;2:推送剩余空格位
		xinmoExportService.onlineLiandanHandle(userRoleId);
		//心魔-魔神上线通知噬体魔神id信息
        xinmoExportService.onlineXinmoMoshenShitiHandle(userRoleId);
        //画卷2上限清除错误数据
        huajuan2ExportService.onlineHandle(userRoleId);
		//图片公告
		//picGongGaoExportService.onlineHandle(userRoleId);
		//腾讯罗盘数据
		if(PlatformConstants.isQQ()){
			//tencentLuoPanExportService.tencentLuoPanTongYong(userRoleId,QqConstants.LOGIN);
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_LUOPAN_TONGYONG, new Object[]{userRoleId,QqConstants.LOGIN});
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_LUOPAN_OSS_LOGIN, userRoleId);
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_LUOPANLM_LOGIN, userRoleId);//联盟登陆
		}
		
		//圣剑潜能丹，成长丹使用数量推送
		wuQiExportService.onlineHandle(userRoleId);
		
		shopLimitExportService.onlineHandle(userRoleId);
		
		taskBranchService.onlineHandle(userRoleId);
		
		/**
		 * 最后才能登录
		 */
		stageControllExportService.login(userRoleId);
		
		//打包数据
		compressDataAndSend2Client(userRoleId);
		
	}
	
	@Autowired
	private ShopLimitExportService shopLimitExportService;
	
	
	/**
	 * 登陆或者创建后打包数据给客户端
	 * @param userRoleId
	 */
	private void compressDataAndSend2Client(Long userRoleId){
		List<Object[]> data = new ArrayList<>();
		
//		//背包
//		data.add(new Object[]{ClientCmdType.GET_BAG_ITEMS, roleBagService.loginHandle(userRoleId)});
//		
		//货币
		RoleAccountWrapper roleAccountWrapper = accountExportService.getAccountWrapper(userRoleId);
		if(roleAccountWrapper != null){
			data.add(new Object[]{ClientCmdType.MONEY_CHANGE, roleAccountWrapper.getJb()});
			data.add(new Object[]{ClientCmdType.YUANBAO_CHANGE, roleAccountWrapper.getYb()});
			data.add(new Object[]{ClientCmdType.BANG_YB_CHANGE, roleAccountWrapper.getBindYb()});
		}
		//VIP信息
		RoleVipWrapper vip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		if(vip != null){
			data.add(new Object[]{ClientCmdType.VIP_LEVEL_CHANGE, vip.getVipLevel()});
			data.add(new Object[]{ClientCmdType.VIP_EXP_CHANGE, vip.getVipExp()});
		}
		
		//角色快捷栏设置 
		Object[] qbInfoData = roleSettingExportService.getQuickbarInfo(userRoleId);
		if(qbInfoData != null){
			data.add(new Object[]{ClientCmdType.GET_SETTINGINFO, qbInfoData});
		}
		//角色挂机设置
		Object gjInfoData = roleSettingExportService.getGuajiInfo(userRoleId);
		if(gjInfoData != null){
			data.add(new Object[]{ClientCmdType.GET_GUAJI_SET, gjInfoData});
		}
		
		//经验
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		data.add(new Object[]{ClientCmdType.EXP_CHANGE, new Object[]{roleWrapper.getExp(),0}});
		//真气
		data.add(new Object[]{ClientCmdType.ZQ_CHANGE, roleWrapper.getZhenqi()});
		
//		//主线任务
		Object[] taskState = taskExportService.getTask(userRoleId);
		data.add(new Object[]{ClientCmdType.SEND_TASK_STATE, taskState});
		
		
		RoleBusinessInfoWrapper roleBusinessInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
		//荣誉
		data.add(new Object[]{ClientCmdType.RONGYU_CHANGE, roleBusinessInfoWrapper.getRoleyu()});
		
		//公会
		Object[] guild = guildExportService.getGuildInfo(userRoleId);
		if(guild != null){
			data.add(new Object[]{ClientCmdType.GET_MY_GUILD, guild});
		}
		
		//坐骑
		ZuoQiInfo  zuoQiInfo = zuoQiExportService.getZuoQiInfo(userRoleId);
		if(zuoQiInfo != null){ 
			data.add(new Object[]{ClientCmdType.ZUOQI_JJ_LEVE,new Object[]{zuoQiInfo.getZuoqiLevel(),zuoQiInfo.getShowId(),zuoQiInfo.getIsGetOn()}});
		}
		
		//系统公告
		Object notice = noticeExportService.getNotice();
		if(null != notice){
			data.add(new Object[]{ClientCmdType.GET_NOTICE, notice});
		}
		
		//翅膀
		ChiBangInfo  chiBangInfo = chibangExportService.getChiBangInfo(userRoleId);
		if(chiBangInfo != null){ 
			data.add(new Object[]{ClientCmdType.CHIBANG_JJ_LEVE,new Object[]{chiBangInfo.getChibangLevel(),chiBangInfo.getShowId()}});
//			data.add(new Object[]{ClientCmdType.CHIBANG_JJ_LEVE,new Object[]{chiBangInfo.getChibangLevel(),chiBangInfo.getShowId(),chiBangInfo.getIsGetOn()}});
		}
		//器灵
		QiLingInfo  qiLingInfo = qiLingExportService.getQiLingInfo(userRoleId);
		if(qiLingInfo != null){ 
			data.add(new Object[]{ClientCmdType.QILING_JJ_LEVE,qiLingInfo.getShowId()});
//			data.add(new Object[]{ClientCmdType.CHIBANG_JJ_LEVE,new Object[]{chiBangInfo.getChibangLevel(),chiBangInfo.getShowId(),chiBangInfo.getIsGetOn()}});
		}
		//天羽
		TianYuInfo  tianyuInfo = tianYuExportService.getTianYuInfo(userRoleId);
		if(tianyuInfo != null){ 
			data.add(new Object[]{ClientCmdType.TIANYU_JJ_LEVE,tianyuInfo.getShowId()});
//			data.add(new Object[]{ClientCmdType.CHIBANG_JJ_LEVE,new Object[]{chiBangInfo.getChibangLevel(),chiBangInfo.getShowId(),chiBangInfo.getIsGetOn()}});
		}
		
		//黑名单
		Object[] friend = friendExportService.getMembers(userRoleId, GameConstants.F_HEI);
		if(friend != null){
			data.add(new Object[]{ClientCmdType.GET_MEMBERS,friend});
		}
		
		//静默禁言
		Object jfData = jinFengExportService.getJinYanData(userRoleId);
		if(jfData != null){
			data.add(new Object[]{ClientCmdType.JING_YAN_JM,jfData});
		}
		
		//500 服务器通知客户端临时关闭某个功能
		Collection<Integer>  modIds = msgFilterService.getCloseModelClientIds();
		if(modIds != null && modIds.size() > 0){
			for (Integer clientModId : modIds) {
				data.add(new Object[]{ClientCmdType.MODEL_CLOSE,clientModId});
			}
		}
		//是否领取微端登录奖励 true:领取  false:未领取
		boolean weiDuanJiangLiGet = roleExportService.isWeiDuanJL(userRoleId);
		data.add(new Object[]{ClientCmdType.ISLQ_WEIDUAN, weiDuanJiangLiGet});

		//是否领取微端2登录奖励 true:领取  false:未领取
		boolean weiDuanJiangLiGet2 = roleExportService.isWeiDuanJL2(userRoleId);
		data.add(new Object[]{ClientCmdType.ISLQ_WEIDUAN2, weiDuanJiangLiGet2});
		
		//转生
		Integer zhuansheng = zhuanshengExportService.getZhuanshengLevel(userRoleId);
		if(zhuansheng != null){
			data.add(new Object[]{ClientCmdType.ZHUANSHENG_LEVEL, zhuansheng});
		}
		data.add(new Object[]{ClientCmdType.XIUWEI_CHARGE, roleBusinessInfoWrapper.getXiuwei()});
		
		//新圣剑
		WuQiInfo  wuQiInfo = wuQiExportService.getWuQiInfo(userRoleId);
		if(wuQiInfo != null){ 
			data.add(new Object[]{ClientCmdType.WUQI_JJ_LEVE,new Object[]{wuQiInfo.getZuoqiLevel(),wuQiInfo.getShowId()}});
		}
		
		//创号时间
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		if(userRole != null){
			data.add(new Object[]{ClientCmdType.CREATE_TIME,userRole.getCreateTime().getTime()});	
		}
		
		//炼金
		Object[] ljInfo = lJExportService.getRoleLjInfo(userRoleId);
		if(ljInfo != null){
			data.add(new Object[]{ClientCmdType.LJ_SEND_LINFO,ljInfo});	
		}
		
//		RoleStage roleStage = roleStageExportService.getRoleStage(userRoleId);
//		Object[] object = null;
//		if(roleStage != null){
//			object = new Object[]{roleStage.getMapId(),roleStage.getMapX(),roleStage.getMapY()};
//		}
		
//		//引导信息
//		Object[]  yindaoInfo = roleBusinessInfoExportService.getYinDaoInfo(userRoleId);
//		if(yindaoInfo != null){ 
//			data.add(new Object[]{ClientCmdType.YIN_DAO_PROGRESS,yindaoInfo});
//		}
		
		Object[] result = roleVipInfoService.getRoleVipGiftState(userRoleId);
		if(result != null){
			data.add(new Object[]{ClientCmdType.ASK_VIP_GIFT_STATE,result});	
		}
		
		//通知客户端服务器数据初始完成
		BusMsgSender.send2One(userRoleId, ClientCmdType.SERVER_INIT_OK,null);//, object);
		
		//数据打包
		Object[] obj = CompressConfigUtil.compressAddCheckObject(data.toArray());
		BusMsgSender.send2One(userRoleId, (short)obj[0], obj[1]);
	}
	
	/**
	 * 角色退出节点
	 * @param roleid
	 */
	public void roleOut(Long roleid) {
		if(!publicRoleStateService.isPublicOnline(roleid)){
			ChuanQiLog.error("roleOut roleid check");
			return;
		}
		try{
			//业务下线
			roleOutHandle(roleid);
			//状态下线
			nodeControlExportService.change2offline(roleid);
		}catch (Exception e) {
			ChuanQiLog.error("role out error",e);
		}
		
		// 冻结缓存对象
		if(usecache){
			roleCacheManager.freezeRoleCache(roleid);
		}
		
	}

    private void roleOutHandle(Long userRoleId) {
        try {
			/** 处理下线业务 **/
			fightPowerService.offOnline(userRoleId);
            /** 处理下线业务 **/
            roleBehaviourExportService.offOnline(userRoleId);
            // 下线业务处理（包括防沉迷，设置角色状态为离线）
            roleExportService.logOfflineTime(userRoleId);
            // 处理禁言离线业务
            jinFengExportService.offlineHandle(userRoleId);
            // 下线背包manage缓存数据处理
            roleBagService.clearBagData(userRoleId);
            // 下线主线任务业务
            taskExportService.offlineHandle(userRoleId);
            // 公会下线业务
            guildExportService.offlineHandle(userRoleId);
            // 组队下线业务
            teamExportService.offlineHandle(userRoleId);
            // 副本下线业务
            fubenExportService.offlineHandle(userRoleId);
            // 下线背包manage缓存数据处理
            tradeExportService.offlineHandle(userRoleId);
            // 下线处理对rolebusiness缓存数据处理
            roleBusinessInfoExportService.offlineHandle(userRoleId);
            // 下线对在线时长数据处理
            roleOnlineExportService.offilineHandler(userRoleId);
            // 下线清除多人副本组队信息
            moreFubenExportService.offlineHandle(userRoleId);
            // 下线清除八卦副本组队信息
            baguaExportService.offlineHandle(userRoleId);
            // 下线清除埋骨之地副本组队信息
            maiguExportService.offlineHandle(userRoleId);
            // 下线清除镖车
            yabiaoExportService.offilineHandler(userRoleId);
            // 平台业务
            platformExportService.offlineHandle(userRoleId);
            // 热发布在线奖励
            onlineRewardsExportService.offlineHandle(userRoleId);
            // 送花
            flowerSendExportService.offlineHandle(userRoleId);
            // 红包业务
            hongbaoExportService.offlineHandle(userRoleId);
            // 婚姻下线业务
            marryExportService.offlineHandle(userRoleId);
            kuafuArena1v1SourceExportService.offlineHandle(userRoleId);
            kuafuArena4v4SourceExportService.offlineHandle(userRoleId);
            // 腾讯罗盘退出数据
            if (PlatformConstants.isQQ()) {
                tencentLuoPanExportService.tencentRegisterLuoPan(userRoleId);
                tencentLuoPanExportService.tencentLuoPanOssTuichu(userRoleId);
                qqExportService.offlineReVipInfo(userRoleId);
            }
            //下线处理下坐骑业务
            zuoQiExportService.offlineHandle(userRoleId);
            //心魔下线日志处理
            xinmoExportService.offLineRoleXinmoHandle(userRoleId);
            //心魔炼丹停止丹药生产定时器
            xinmoExportService.offlineLiandanHandle(userRoleId);
            //心魔-魔神停止噬体解体生产定时器
            xinmoExportService.offlineXinmoMoshenShitiHandle(userRoleId);
        } catch (Exception e) {
            ChuanQiLog.error("", e);
        } finally {
            // 下线业务最后触发业务
            stageControllExportService.exit(userRoleId);
        }
    }
	
	/**
	 * 服务器关闭时角色退出节点
	 * @param roleid
	 */
	public void roleOutOnServerClose(Long roleid) {
		roleOut(roleid);
		
		otherOnServerClose();
	}
	
	private void otherOnServerClose(){
		
	}
}