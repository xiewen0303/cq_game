package com.junyou.bus.hczbs.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.hczbs.dao.ZhengbasaiDao;
import com.junyou.bus.hczbs.dao.ZhengbasaiDayRewardDao;
import com.junyou.bus.hczbs.entity.Zhengbasai;
import com.junyou.bus.hczbs.entity.ZhengbasaiDayReward;
import com.junyou.bus.marry.export.MarryExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.role.configure.export.ZhuJueDzConfig;
import com.junyou.gameconfig.role.configure.export.ZhuJueDzConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.guild.entity.Guild;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.guild.manager.GuildManager;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.configure.HcZhengBaSaiConfig;
import com.junyou.stage.configure.export.impl.HcZhengBaSaiConfigExportService;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactoryHelper;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

@Service
public class HcZhengBaSaiService {
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;

	@Autowired
	private PublicRoleStateService publicRoleStateService;
	@Autowired
	private ZhengbasaiDao zhengbasaiDao;

	@Autowired
	private HcZhengBaSaiConfigExportService hcZhengBaSaiConfigExportService;
	@Autowired
	private ZhengbasaiDayRewardDao zhengbasaiDayRewardDao;
	
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ZhuJueDzConfigExportService zhuJueDzConfigExportService;
	@Autowired
	private MarryExportService marryExportService;

	@Autowired
	private DataContainer dataContainer;

	public List<Zhengbasai> initZhengbasai() {
		return zhengbasaiDao.initZhengbasai();
	}
	/**
	 * 初始化奖励名单数据
	 * @return
	 */
	public List<ZhengbasaiDayReward> initZhengbasaiDayReward() {
		return zhengbasaiDayRewardDao.loadAll();
	}

	/**
	 * 获取Zhengbasai对象 可能null
	 */
	public Zhengbasai loadZhengbasai() {
		Zhengbasai zhengbasai = zhengbasaiDao.cacheLoad(GameConstants.DEFAULT_ROLE_ID, GameConstants.DEFAULT_ROLE_ID);
		return zhengbasai;
	}
	/**
	 *公共缓存取数据
	 */
	public ZhengbasaiDayReward loadZhengbasaiDayReward(long userRoleId) {
		ZhengbasaiDayReward  vo = zhengbasaiDayRewardDao.cacheLoad(userRoleId, GameConstants.DEFAULT_ROLE_ID);
		return vo;
	}

	public Object[] enterZBS(Long userRoleId) {
		// 判断是否有活动
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_HCZBS_ACTIVE_ID, GameConstants.COMPONENT_HCZBS_ACTIVE_ID);
		if (hdId == null) {
			return AppErrorCode.ACTIVE_IS_NOT_START;
		}

		// 判断是否有配置
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(hdId);
		if (config == null || config.getMapId() == 0) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 判断等级是否满足
		if (config.getMinLevel() > roleExportService.getUserRole(userRoleId).getLevel()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}
		// 没有帮派的不能进
		Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
		if (guildInfo == null) {
			return AppErrorCode.HCZBS_NO_GUILD;
		}
		// 判断是否在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}

		HcZhengBaSaiConfig hcZhengBaSaiConfig = hcZhengBaSaiConfigExportService.loadPublicConfig();
		if (hcZhengBaSaiConfig == null || hcZhengBaSaiConfig.getMap() == 0) {
			return AppErrorCode.CONFIG_ERROR;
		}
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
		if (dituCoinfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 发送到场景进入地图
		Object[] point = new Object[] { hcZhengBaSaiConfig.getGongfuhuo()[0], hcZhengBaSaiConfig.getGongfuhuo()[1], hcZhengBaSaiConfig.getShoufuhuo()[0],
				hcZhengBaSaiConfig.getShoufuhuo()[1] };
		int index = new Random().nextInt(4);
		int[] birthPoint = (int[]) point[index];
		int x = (int) birthPoint[0];
		int y = (int) birthPoint[1];
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, new Object[] { dituCoinfig.getId(), x, y, MapType.HC_ZBS_MAP_TYPE });
		return null;
	}

	/**
	 * 胜利帮派门主属性加成
	 */
	public Map<String, Long> getGuildXunZhangAttr(Long userRoleId) {
		Zhengbasai zhengbasai = loadZhengbasai();
		if (zhengbasai != null && userRoleId == zhengbasai.getGuildLeaderId().longValue()) {
			HcZhengBaSaiConfig hcZhengBaSaiConfig = hcZhengBaSaiConfigExportService.loadPublicConfig();
			if (hcZhengBaSaiConfig != null) {
//				BusMsgSender.send2Stage(userRoleId, InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES, new Object[]{true,1});
				return hcZhengBaSaiConfig.getAttrs();
			}
		}
		return null;
	}
	/**
	 * 胜利帮派门主,仙侣套装外显
	 */
	public boolean showCloth(Long userRoleId) {
		ZhengbasaiDayReward  zhengbasaiDayReward  = loadZhengbasaiDayReward(userRoleId);
		if(zhengbasaiDayReward!=null){
			if(zhengbasaiDayReward.getPosition().longValue()==1||zhengbasaiDayReward.getPosition().longValue()==-1){
				int roleType = -1;
				if(zhengbasaiDayReward.getPosition().longValue()==GameConstants.GUILD_LEADER_POSTION){
					roleType =1;
				} 
				//门主或者仙侣 套装显示
				BusMsgSender.send2Stage(userRoleId, InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES, new Object[]{true,roleType});
				return true;
			}
			
		}
		return false;
	}
	/**
	 * 领取每日官职奖励 即使是帮派解散了也可以领 这里领奖时按照人来领user_role_id
	 * 
	 * @param userRoleId
	 * @param mapIds
	 * @return
	 */
	public Object[] getReward(Long userRoleId) {
		// 校验是否在活动期间
		if (ActiveUtil.isHcZBS()) {
			return AppErrorCode.HCZBS_IN;
		}
		HcZhengBaSaiConfig hcZhengBaSaiConfig = hcZhengBaSaiConfigExportService.loadPublicConfig();
		if (hcZhengBaSaiConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		ZhengbasaiDayReward zhengbasaiDayReward = this.loadZhengbasaiDayReward(userRoleId);

		Object[] ret = this.isReward(zhengbasaiDayReward);
		if ((Integer) ret[0] != 1) {
			return ret;
		}
		zhengbasaiDayReward.setState(1);
		zhengbasaiDayReward.setUpdateTime(GameSystemTime.getSystemMillTime());
		zhengbasaiDayRewardDao.cacheUpdate(zhengbasaiDayReward, GameConstants.DEFAULT_ROLE_ID);

		Map<String, Integer> item = hcZhengBaSaiConfig.getJiangitem();
		roleBagExportService.putGoodsAndNumberAttr(item, userRoleId, GoodsSource.HCZBS_DAY_REWARD, LogPrintHandle.GET_NORMAL, LogPrintHandle.GBZ_NORMAL, true);
		return new Object[] { AppErrorCode.SUCCESS };
	}

	/**
	 * 2131请求皇城占领信息
	 * 
	 * @param userRoleId
	 */
	public Object[] getWinInfo(long userRoleId) {
		Zhengbasai zhengbasai = loadZhengbasai();
		if (zhengbasai == null) {
			return null; // 还没有占领
		}
		Object[] arr1 = new Object[3];
		arr1[0] = zhengbasai.getGuildName(); // 门派名称
		double time = GameSystemTime.getSystemMillTime() - zhengbasai.getUpdateTime();
		double day = Math.ceil(time / GameConstants.DAY_TIME);
		arr1[1] = (int) day;
		arr1[2] = (Integer) isReward(userRoleId)[0] == 1 ? false : true; // true表示已领
		List<ZhengbasaiDayReward> all = zhengbasaiDayRewardDao.cacheLoadAll(GameConstants.DEFAULT_ROLE_ID);

		List<Object[]> clientInfo = Arrays.asList(null,null,null,null,null);
		clientInfo.set(0,arr1);
		Object[] leader = new Object[2];
		Object[] leaderPeiOu = new Object[2];// 门主配偶
		int fuLeaderIndex = 2; //副掌门索引起始位
		for (int i = 0; i < all.size(); i++) {
			ZhengbasaiDayReward reward = all.get(i);
			if (reward.getPosition()>2) {//排除长老
				continue;
			}
			Object[] arr2 = new Object[2];
			RoleWrapper userRole = getRoleWrapper(reward.getUserRoleId());
			ZhuJueDzConfig zhuJueDzConfig = zhuJueDzConfigExportService.loadById(userRole.getConfigId());
			arr2[0] = userRole.getName();
			arr2[1] = zhuJueDzConfig.getSex();
			//门主
			if (reward.getPosition()==1 || reward.getUserRoleId().longValue()==zhengbasai.getGuildLeaderId().longValue()) {
				leader[0] = userRole.getName();
				leader[1] = zhuJueDzConfig.getSex();
			}
			if (reward.getPosition() == -1) {// -1表示配偶
				leaderPeiOu[0] = userRole.getName();
				leaderPeiOu[1] = zhuJueDzConfig.getSex();
			}
			if (reward.getPosition() == 2) {
				clientInfo.set(fuLeaderIndex,arr2);
				fuLeaderIndex++;
			}
		}
		if(leader[0]==null){
			leader =null;
		}
		clientInfo.set(1, leader);// 调整门主顺序
		if(leaderPeiOu[0]==null){
			leaderPeiOu =null;//无配偶
		} 
		clientInfo.set(4,leaderPeiOu);// 配偶
		return clientInfo.toArray();
	}
	private RoleWrapper getRoleWrapper(long userRoleId){
		RoleWrapper role =null;
		if(publicRoleStateService.isPublicOnline(userRoleId)){
			 role = roleExportService.getLoginRole(userRoleId);
		}else{
			 role = roleExportService.getUserRoleFromDb(userRoleId);
		}
		return role;
	}

	/**
	 * 是否可以领取奖励
	 * 
	 * @param args
	 */
	private Object[] isReward(long userRoleId) {
		ZhengbasaiDayReward zhengbasaiDayReward = loadZhengbasaiDayReward(userRoleId);
		return isReward(zhengbasaiDayReward);
	}

	private Object[] isReward(ZhengbasaiDayReward zhengbasaiDayReward) {
		if (zhengbasaiDayReward == null) {
			return AppErrorCode.PARAMETER_ERROR;
		}
		// 官职奖励每天可领取
		if (DatetimeUtil.dayIsToday(zhengbasaiDayReward.getUpdateTime(), GameSystemTime.getSystemMillTime())) {
			if (zhengbasaiDayReward.getState() == 1) {
				return AppErrorCode.GET_ALREADY_ERROR;
			}
		}

		return AppErrorCode.OK; //
	}

	/**
	 * 移除勋章
	 */
	public void removeGuildXunZhang(List<Long> leaderIds) {
		for (Long userRoleId : leaderIds) {
			if (publicRoleStateService.isPublicOnline(userRoleId)) {
				String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
				IStage stage = StageManager.getStage(stageId);
				IRole role = stage.getElement(userRoleId, ElementType.ROLE);
				role.getFightAttribute().clearBaseAttribute(BaseAttributeType.GUILD_XUANZHANG_HCZBS, true);
				role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			}
		}
	} 

	/**
	 * 获得勋章
	 */
	public void addGuildXunZhang() {
		Zhengbasai zhengbasai = loadZhengbasai();
		if(zhengbasai==null){
			return;
		}
		Object[] info = guildExportService.getGuildBaseInfo(zhengbasai.getGuildId());
		long userRoleId = (Long) info[1];
		if (publicRoleStateService.isPublicOnline(userRoleId)) {
			HcZhengBaSaiConfig config = hcZhengBaSaiConfigExportService.loadPublicConfig();
			String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
			IStage stage = StageManager.getStage(stageId);
			IRole role = stage.getElement(userRoleId, ElementType.ROLE);
			RoleFactoryHelper.setRoleBaseAttrs(config.getAttrs(), role, BaseAttributeType.GUILD_XUANZHANG_HCZBS);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
	}

	/**
	 * 掌门让位
	 */
	@Deprecated
	public void hcZbsLeaderOff(Long userRoleId, Long guildId) {
		if (publicRoleStateService.isPublicOnline(userRoleId)) {
			Zhengbasai zhengbasai = loadZhengbasai();
			if (zhengbasai != null && zhengbasai.getGuildId().longValue() == guildId) {
				BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_HCZBS_XUNZHANG, false);
				String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
				IStage stage = StageManager.getStage(stageId);
				IRole role = stage.getElement(userRoleId, ElementType.ROLE);
				role.getBusinessData().setHcZbsWinnerGuildLeader(false);
				role.getFightAttribute().clearBaseAttribute(BaseAttributeType.GUILD_XUANZHANG_HCZBS, false);
				role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			}
		}
	}

	/**
	 * 新掌门上位
	 */
	@Deprecated
	public void hcZbsLeaderON(Long userRoleId, Long guildId) {
		if (publicRoleStateService.isPublicOnline(userRoleId)) {
			Map<String, Long> attrMap = new HashMap<String, Long>();
			Zhengbasai zhengbasai = loadZhengbasai();
			if (guildId == zhengbasai.getGuildId().longValue()) {
				// 是获胜帮派的门主
				HcZhengBaSaiConfig config = hcZhengBaSaiConfigExportService.loadPublicConfig();
				if (config != null) {
					ObjectUtil.longMapAdd(attrMap, config.getAttrs());
				}
				String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
				IStage stage = StageManager.getStage(stageId);
				IRole role = stage.getElement(userRoleId, ElementType.ROLE);
				RoleFactoryHelper.setRoleBaseAttrs(attrMap, role, BaseAttributeType.GUILD_XUANZHANG_HCZBS);
				role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
				role.getBusinessData().setHcZbsWinnerGuildLeader(true);
				BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_HCZBS_XUNZHANG, true);
			}

		}
	}

	/**
	 * 帮派解散 废弃
	 */
	@Deprecated
	public void guildDissolve(long guilidId, Long userRoleId) {
		Zhengbasai zhengbasai = loadZhengbasai();
		if (zhengbasai != null && zhengbasai.getGuildId().longValue() == guilidId) {
			zhengbasai.setGuildId(0L);
			zhengbasai.setUpdateTime(0L);
			zhengbasaiDao.cacheUpdate(zhengbasai, GameConstants.DEFAULT_ROLE_ID);
			BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_HCZBS_XUNZHANG, false);
		}
	}

	/**
	 * 玩家上限皇城战城主buff
	 * 
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId) {
		Zhengbasai zhengbasai = loadZhengbasai();
		if (zhengbasai != null) {
			if (zhengbasai != null && zhengbasai.getGuildLeaderId().longValue() == userRoleId) {
				BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_HCZBS_XUNZHANG, true);
			}
		}
	}
	/**
	 * 检查玩家是不是皇城争霸赛的胜利门主
	 * @param guildId
	 */
	public Long getHcZbsWinerGuildId() {
		Zhengbasai zhengbasai = loadZhengbasai();
		if (zhengbasai != null) {
				return zhengbasai.getGuildId();
		}
		return null;
	}
	/*
	 * 创建获胜帮派对象 Zhengbasai
	 */
	public void createZhengbasai(long guildId) {
		Object[] info = guildExportService.getGuildBaseInfo(guildId);
		if (info != null) {
			Zhengbasai zhengbasai = new Zhengbasai();
			zhengbasai.setId(GameConstants.DEFAULT_ROLE_ID);// 主键
			zhengbasai.setGuildId(guildId);
			zhengbasai.setGuildName((String) info[0]);
			zhengbasai.setGuildLeaderId((long) info[1]);
			zhengbasai.setUpdateTime(GameSystemTime.getSystemMillTime());
			zhengbasaiDao.cacheInsert(zhengbasai, GameConstants.DEFAULT_ROLE_ID);
			// 同时创建领奖人数据
			createZhengbasaiReward(guildId);
			BusMsgSender.send2Stage((long) info[1], InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES, new Object[]{true,1});
			Guild guild = GuildManager.getManager().getGuild(guildId);
			if(guild != null){
				guild.setHcZbsWinner(true);
				GuildManager.getManager().needSort();
			}
		}

	}

	/**
	 * 更新获胜帮派对象
	 * 
	 * @param guildId
	 */
	public void updateZhengbasai(long guildId) {
		Zhengbasai zhengbasai = loadZhengbasai();

		if (zhengbasai == null) {
			return;
		}
		long oldLeaderId = zhengbasai.getGuildLeaderId();
		long oldGuildId = zhengbasai.getGuildId();
		//帮派皇城战胜利者变更
		if(oldGuildId != guildId){
			Guild oldGuild = GuildManager.getManager().getGuild(oldGuildId);
			if(oldGuild != null){
				oldGuild.setHcZbsWinner(false);
			}
			Guild newGuild = GuildManager.getManager().getGuild(guildId);
			if(newGuild != null){
				newGuild.setHcZbsWinner(true);
			}
			GuildManager.getManager().needSort();
		}

		Object[] newInfo = guildExportService.getGuildBaseInfo(guildId);

		if (newInfo == null) {
			return;
		}
		long newLeaderId = (long) newInfo[1];
		zhengbasai.setGuildLeaderId(newLeaderId);
		zhengbasai.setGuildId(guildId);
		zhengbasai.setGuildName((String) newInfo[0]);
		zhengbasai.setUpdateTime(GameSystemTime.getSystemMillTime());
		zhengbasaiDao.cacheUpdate(zhengbasai, GameConstants.DEFAULT_ROLE_ID);
		// 创建奖励
		createZhengbasaiReward(guildId);
		// 通知切换外显
		if (oldLeaderId != newLeaderId) {
			// 还是原来帮派占领这次活动就不通知
			BusMsgSender.send2Stage(oldLeaderId, InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES,new Object[]{false,1} );
			BusMsgSender.send2Stage(newLeaderId, InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES,new Object[]{true,1} );
		}
	}

	/**
	 * 创建奖励对象
	 * 
	 * @param guildId
	 */
	private void createZhengbasaiReward(long guildId) {
		List<ZhengbasaiDayReward> zhengbasaiDayRewards=  zhengbasaiDayRewardDao.cacheLoadAll(GameConstants.DEFAULT_ROLE_ID);
		Long prePeiOuId  = null;//上一届配偶
		//删除历史数据
		 if(zhengbasaiDayRewards!=null && zhengbasaiDayRewards.size()>0){
			 for (int i = 0; i < zhengbasaiDayRewards.size(); i++) {
				 ZhengbasaiDayReward zhengbasaiDayReward = zhengbasaiDayRewards.get(i);
				 if(zhengbasaiDayReward.getPosition().intValue()==-1){
					 	//找上一届仙侣
						prePeiOuId = zhengbasaiDayReward.getUserRoleId();
					}
				 zhengbasaiDayRewardDao.cacheDelete(zhengbasaiDayReward.getUserRoleId(),GameConstants.DEFAULT_ROLE_ID); 
			}
		 }
		//把帮派的官职人员存到表里面
		List<GuildMember> allMembers = guildExportService.getAllLeader(guildId);
		long guildLeaderId = 0;
		boolean isRewardsExistPeiOui = false; // 配偶是否在奖励人中
		List<ZhengbasaiDayReward> rewards = new ArrayList<>();
		if (allMembers != null) {
			for (GuildMember guildMember : allMembers) {
				ZhengbasaiDayReward zhengbasaiDayReward = new ZhengbasaiDayReward();
				zhengbasaiDayReward.setGuildId(guildId);
				zhengbasaiDayReward.setPosition(guildMember.getPostion());
				zhengbasaiDayReward.setState(0);
				zhengbasaiDayReward.setUserRoleId(guildMember.getUserRoleId());
				zhengbasaiDayReward.setUpdateTime(GameSystemTime.getSystemMillTime());
				rewards.add(zhengbasaiDayReward);
				if (guildMember.getPostion().intValue() == GameConstants.GUILD_LEADER_POSTION) {
					guildLeaderId = guildMember.getUserRoleId();
				}
			}
		}
		if(guildLeaderId==0){
			ChuanQiLog.error("***在云宫的奖励名单中没有发现门主本人****");
		}
		Long peiOuId = null;
		
		 try {
			  // 查看门主是否有配偶
		      peiOuId = marryExportService.getPeiouUserRoleId(guildLeaderId);	
			} catch (Exception e) {
				ChuanQiLog.error("获取门主配偶数据报错，marryExportService.getPeiouUserRoleId(uid)!!! e={}",e);
			}
		 
		if (peiOuId != null) {
			for (ZhengbasaiDayReward zhengbasaiDayReward : rewards) {
				if (zhengbasaiDayReward.getUserRoleId().longValue() == peiOuId.longValue()) {
					zhengbasaiDayReward.setPosition(-1);// 标记为配偶
					isRewardsExistPeiOui = true;
					break;
				}
			}
			if (!isRewardsExistPeiOui) {
				//配偶不在奖励人中
				ZhengbasaiDayReward zhengbasaiDayReward = new ZhengbasaiDayReward();
				zhengbasaiDayReward.setGuildId(guildId);
				zhengbasaiDayReward.setPosition(-1);// 表示配偶
				zhengbasaiDayReward.setState(0);
				zhengbasaiDayReward.setUserRoleId(peiOuId);
				zhengbasaiDayReward.setUpdateTime(GameSystemTime.getSystemMillTime());
				rewards.add(zhengbasaiDayReward);
			}

		}
		// 统一插入数据到公共缓存
		for (ZhengbasaiDayReward zhengbasaiDayReward : rewards) {
			zhengbasaiDayRewardDao.cacheInsert(zhengbasaiDayReward, GameConstants.DEFAULT_ROLE_ID);
		}
		//配偶外显处理
		if(prePeiOuId!=null){
			BusMsgSender.send2Stage(prePeiOuId, InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES,new Object[]{false,-1} );
		}
		if(peiOuId!=null){
			BusMsgSender.send2Stage(peiOuId, InnerCmdType.HCZBS_WINNER_LEADER_CHANGE_CLOTHES,new Object[]{true,-1} );
		}
	}
	// 检查是否有奖励
	public boolean hcZbsHasReward(Long userRoleId) {
		Object[] state = this.isReward(userRoleId);
		if ((Integer) state[0] == 1) {
			return true;
		} else {
			return false;
		}
	}

}
