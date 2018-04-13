package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.hczbs.entity.Zhengbasai;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.HcZBSPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.guild.util.GuildUtil;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.configure.HcZhengBaSaiConfig;
import com.junyou.stage.configure.export.impl.HcZhengBaSaiConfigExportService;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.configure.export.impl.ZiYuanConfigExportService;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.goods.Collect;
import com.junyou.stage.model.element.goods.CollectFacory;
import com.junyou.stage.model.element.goods.HcZBSQizi;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.zhengbasai.HcZhengBaSaiManager;
import com.junyou.stage.model.stage.zhengbasai.HcZhengBaSaiStage;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.gen.id.IdFactory;
import com.kernel.spring.container.DataContainer;

/**
 * 争霸赛--云宫之战
 */
@Service
public class HcZhengBaSaiStageService {

	private HcZhengBaSaiManager hcZhengBaSaiManager;

	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private HcZhengBaSaiExportService hcZhengBaSaiExportService;
	@Autowired
	private HcZhengBaSaiConfigExportService hcZhengBaSaiConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private DingShiActiveConfigExportService dingShiHuoDongBiaoConfigExportService;
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private RoleExportService roleExportService;
	
	

	@Autowired
	private StageControllExportService stageControllExportService;

	@Autowired
	private ZiYuanConfigExportService ziYuanConfigExportService;

	private HcZBSPublicConfig getPublicConfig() {
		HcZBSPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_HCZBS_WAR);
		return publicConfig;
	}

	/**
	 * 活动期间加经验 加真气
	 * 
	 * @param userRoleId
	 * @param stageId
	 */
	public void addExpZhenqi(Long userRoleId, String stageId) {
		if (!ActiveUtil.isHcZBS()) {
			return;// 非领地战期间不加
		}

		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.HCZBS_WAR) {
			return;// 当前地图不是领地战地图
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;// 角色不存在
		}

		HcZhengBaSaiConfig config = hcZhengBaSaiConfigExportService.loadPublicConfig();
		if (config == null) {
			return;
		}
		long exp = config.getExp() * role.getLevel() * role.getLevel();
		long zhenqi = config.getZhenqi() * role.getLevel();
		StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_HCZBS_ADD_EXP_ZHENQI, new Object[] { exp, zhenqi });
		role.startHcZhengBaSaiAddExpSchedule(getPublicConfig().getDelay());
	}

	/**
	 * 争霸赛活动开始
	 */
	public void activeStart(int hdId) {
		DingShiActiveConfig activeConfig = dingShiHuoDongBiaoConfigExportService.getConfig(hdId);
		if (activeConfig == null) {
			return;
		}
		HcZBSPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			ChuanQiLog.error("HcZhengBaSaiConfig is null.");
			return;
		}
		ActiveUtil.setHcZBS(true);
		ActiveUtil.setCanChangeGuild(false);
		try{
			hcZhengBaSaiManager = HcZhengBaSaiManager.getManager(); // 活动一开始就初始化
			hcZhengBaSaiManager.setPublicConfig(publicConfig);
			HcZhengBaSaiConfig hcZhengBaSaiConfig = hcZhengBaSaiConfigExportService.loadPublicConfig();
			if (hcZhengBaSaiConfig == null) {
				return;
			}
			int mapId = hcZhengBaSaiConfig.getMap();

			Zhengbasai zhengbasai = hcZhengBaSaiExportService.loadZhengbasai();
			Long guildId = null;
			if (zhengbasai != null) {
				guildId = zhengbasai.getGuildId();
			}
			if (guildId != null && guildId.longValue() != 0) {
				Object[] info = guildExportService.getGuildBaseInfo(guildId);
				Long leaderId =0L;
				if(info==null){//可能帮派解散
					leaderId = zhengbasai.getGuildLeaderId();
				}else{
					 leaderId = (Long) info[1];
				}
				hcZhengBaSaiManager.addGuildLeaderId(leaderId);
			}
			String stageId = StageUtil.getStageId(mapId, 1);
			IStage stage = StageManager.getStage(stageId);
			dataContainer.putData(GameConstants.COMPONENT_HCZBS_ACTIVE_ID, GameConstants.COMPONENT_HCZBS_ACTIVE_ID, hdId);
			if (stage == null) {
				// 创建活动
				createStage(hcZhengBaSaiConfig.getMap());
			} else {
				ChuanQiLog.debug("hcZhengBaSaiStage is exist,ERROR");
			}
		}catch (Exception e) {
			ChuanQiLog.error("争霸赛开始出错了",e);
		}
		BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.HCZBS_ACTIVE_END, null);
		Long delay = activeConfig.getCalcEndSecondTime();
		scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.COMPONENT_HCZBS_END, runable, delay.intValue(), TimeUnit.MILLISECONDS);
		ChuanQiLog.info("争霸赛开始了");
	}

	public void createStage(int mapId) {
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_HCZBS_ACTIVE_ID, GameConstants.COMPONENT_HCZBS_ACTIVE_ID);
		if (hdId == null) {
			return;
		}
		HcZhengBaSaiConfig hcZhengBaSaiConfig = hcZhengBaSaiConfigExportService.loadPublicConfig();
		if (hcZhengBaSaiConfig == null) {
			return;
		}
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(hdId);
		MapConfig mapConfig = mapConfigExportService.load(mapId);
		PublicFubenStage stage = publicFubenStageFactory.create(StageUtil.getStageId(mapId, 1), 1, mapConfig);
		if (stage != null) {
			HcZhengBaSaiStage hczbs = (HcZhengBaSaiStage) stage;
			hczbs.setDingShiActiveConfig(config);
			hczbs.setHcZBSPublicConfig(getPublicConfig());
			hczbs.setHcZhengBaSaiConfig(hcZhengBaSaiConfig);
			hczbs.setHcZhengBaSaiManager(hcZhengBaSaiManager);
			HcZhengBaSaiManager.getManager().addStage(hczbs);
			hcZhengBaSaiManager.addStageId(hczbs.getId());
			hczbs.init(hcZhengBaSaiManager.getPublicConfig().getNeedTime());
			hczbs.start();
			initQizi(stage); // 初始化旗帜
		}
	}

	/**
	 * 开始夺旗
	 * 
	 * @param userRoleId
	 * @param flagGuid
	 * @return
	 */
	public Object[] fetchFlagBegin(Long userRoleId, Long flagGuid) {
		// 校验是否在活动中
		if (!ActiveUtil.isHcZBS()) {
			return AppErrorCode.HCZBS_NOT_START;
		}
		String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
		IStage istage = StageManager.getStage(stageId);
		if (istage == null || !StageType.HCZBS_WAR.equals(istage.getStageType())) {
			return AppErrorCode.HCZBS_ACTIVE_END;
		}
		HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) istage;
		// 旗帜被永久占领 不能拔旗
		if (tStage.isFlagOccupyEnd()) {
			return AppErrorCode.HCZBS_FLAG_HOLD_END;
		}

		HcZhengBaSaiConfig config = hcZhengBaSaiConfigExportService.loadPublicConfig();
		if (config == null) {
			return AppErrorCode.ERR;
		}
		IStageElement element = tStage.getElement(flagGuid, ElementType.COLLECT);
		if (element == null) {
			return AppErrorCode.HCZBS_FLAG_NOT_EXIST;
		}
		Collect collect = (Collect) element;
		if (collect.getCollertType() != 5) {
			return AppErrorCode.ERR;
		}
		// 校验玩家有没有帮派以及是否有权限拔旗
		Object[] result = guildExportService.flagCheck(userRoleId, getPublicConfig().getNeedGold());
		if (result != null) {
			return result;
		}
		tStage.setFlag(userRoleId, flagGuid);
		return new Object[] { AppErrorCode.SUCCESS, collect.getCollectTime() };
	}

	/**
	 * 皇城争霸赛夺旗结束
	 */
	public Object[] fetchFlagEnd(Long userRoleId) {
		// 校验是否在活动中
		if (!ActiveUtil.isHcZBS()) {
			return AppErrorCode.HCZBS_NOT_START;
		}
		String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
		IStage istage = StageManager.getStage(stageId);
		if (istage == null || !StageType.HCZBS_WAR.equals(istage.getStageType())) {
			return AppErrorCode.ERR;
		}
		HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) istage;
		HcZhengBaSaiConfig config = hcZhengBaSaiConfigExportService.loadPublicConfig();
		if (config == null) {
			return AppErrorCode.ERR;
		}
		Long flagGuild = tStage.getFlag(userRoleId);
		if (flagGuild == null) {
			return AppErrorCode.HCZBS_FLAG_NOT_EXIST;
		}
		IStageElement element = tStage.getElement(flagGuild, ElementType.COLLECT);
		if (element == null) {
			return AppErrorCode.HCZBS_FLAG_NOT_EXIST;
		}
		// 校验玩家有没有帮派
		Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
		if (guildInfo == null) {
			return AppErrorCode.HCZBS_NO_GUILD;
		}
		// 校验帮派职位
		Integer position = (Integer) guildInfo[8];
		if (!GuildUtil.isLeaderOrViceLeader(position)) {
			return AppErrorCode.HCZBS_POSITION_LIMIT;
		}
		// 校验帮派贡献银两
		Object[] result = guildExportService.flagCost(userRoleId, getPublicConfig().getNeedGold());
		if (result != null) {
			return result;
		}

		DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(istage.getMapId());
		if (dituConfig == null) {
			return AppErrorCode.ERR;
		}
		tStage.cancelTobeWinnerSchedule();
		tStage.startTobeWinnerSchedule(getPublicConfig().getNeedTime());
		tStage.clearFlagMap();
		// 移除旗帜
		tStage.leave(element);
		tStage.setFlag(false);
		HcZhengBaSaiManager.getManager().setZhanglingTime(GameSystemTime.getSystemMillTime());// 更新占领旗帜时间
		ChuanQiLog.info("皇城争霸赛 {} 旗子被 {} 抢了", tStage.getId(), userRoleId);
		// 显示图腾
		longtEnter(tStage);

		IStageElement roleElement = tStage.getElement(userRoleId, ElementType.ROLE);
		IRole role = (IRole) roleElement;
		tStage.setFlagOwnerRole(role);
		tStage.setOwnerGuildId(role.getBusinessData().getGuildId());
		// 全服广播
		BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[] { AppErrorCode.TERRITORY_FETCH_FLAG, new Object[] { (String) guildInfo[1], dituConfig.getName() } });
		IBuff buff = BuffFactory.create(role, role, getPublicConfig().getKqbuff());
		role.getBuffManager().addBuff(buff);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		tStage.setFlagOwnerBuff(buff);
		tStage.startSynFlagPositionSchedule();
		BusMsgSender.send2Many(tStage.getAllRoleIds(), ClientCmdType.HC_ZBS_FLAG_CHANGE, new Object[] { role.getBusinessData().getGuildId(), role.getBusinessData().getName() });
		return null;
	}

	private void longtEnter(HcZhengBaSaiStage tStage) {
		if (tStage.getLongt() != null) {
			return;
		}
		MonsterConfig monsterConfig = monsterExportService.load(getPublicConfig().getLongt());
		long id = IdFactory.getInstance().generateNonPersistentId();
		IMonster monster = MonsterFactory.create(id + "", id, monsterConfig);
		tStage.setLongt(monster);
		int[] zuobiao = hcZhengBaSaiConfigExportService.loadPublicConfig().getZuobiao();
		tStage.enter(monster, zuobiao[0], zuobiao[1]);
		ChuanQiLog.info("争霸赛 {} 龙腾出现", tStage.getId());
	}

	private void qiziEnter(IStage stage) {
		if (stage.hasFlag()) {
			return;
		}
		Integer ziyuanId = getPublicConfig().getZiyuanid();
		ZiYuanConfig ziYuanConfig = ziYuanConfigExportService.loadById(ziyuanId);
		HcZBSQizi qizi = CollectFacory.createHcZBSQizi(IdFactory.getInstance().generateNonPersistentId(), ziYuanConfig, stage.getId());
		int[] zuobiao = hcZhengBaSaiConfigExportService.loadPublicConfig().getZuobiao();
		stage.enter(qizi, zuobiao[0], zuobiao[1]);
		stage.setFlag(true);
		hcZhengBaSaiManager.setZhanglingTime(0);// 每次旗子重新出现都归0
		ChuanQiLog.info("争霸赛 {} 旗子出现", stage.getId());
	}

	public void hasWinner(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isHcZBS()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null || !StageType.HCZBS_WAR.equals(istage.getStageType())) {
			return;
		}
		hasWinnerEnd(stageId);
	}

	public void flagOwnerDead(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isHcZBS()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null || !StageType.HCZBS_WAR.equals(istage.getStageType())) {
			return;
		}
		HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) istage;
		try {
			if (tStage.isFlagOccupyEnd()) {
				return;// 夺旗结束
			}
			tStage.lock();
			IRole role = tStage.getFlagOwnerRole();
			if (role == null) {
				return;
			}
			IBuff buff = tStage.getFlagOwnerBuff();
			if (buff != null) {
				role.getBuffManager().removeBuff(buff.getId(), buff.getBuffCategory());
				role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
				tStage.setFlagOwnerBuff(null);
			}
			tStage.setFlagOwnerRole(null);
			tStage.setOwnerGuildId(null);
			if (tStage.getLongt() == null) {
				return;
			}
			// 图腾消失
			tStage.leave(tStage.getLongt());
			tStage.setLongt(null);
			ChuanQiLog.info("争霸赛 {} 扛旗的人死了", stageId);
			// 旗子归位
			qiziEnter(tStage);
			tStage.cancelTobeWinnerSchedule();
			synFlag(stageId);
			BusMsgSender.send2Many(tStage.getAllRoleIds(), ClientCmdType.HC_ZBS_FLAG_CHANGE, new Object[] { -1, null });
		} finally {
			tStage.unlock();
		}
	}

	public void monsterDead(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isHcZBS()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null || !StageType.HCZBS_WAR.equals(istage.getStageType())) {
			return;
		}
		HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) istage;
		try {
			tStage.lock();
			IRole role = tStage.getFlagOwnerRole();

			tStage.setFlagOwnerRole(null);
			tStage.setOwnerGuildId(null);
			if (role == null) {
				return;
			}
			IBuff buff = tStage.getFlagOwnerBuff();
			if (buff != null) {
				role.getBuffManager().removeBuff(buff.getId(), buff.getBuffCategory());
				role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
				tStage.setFlagOwnerBuff(null);
			}
			ChuanQiLog.info("争霸赛 {} 龙腾死了", stageId);
			qiziEnter(tStage);
			tStage.setLongt(null);
			tStage.cancelTobeWinnerSchedule();
			synFlag(stageId);
			BusMsgSender.send2Many(tStage.getAllRoleIds(), ClientCmdType.HC_ZBS_FLAG_CHANGE, new Object[] { -1, null });
		} finally {
			tStage.unlock();
		}
	}

	public void activeEnd() {
	    addKuafuYunGongServerData();
		ActiveUtil.setHcZBS(false);
		ActiveUtil.setCanChangeGuild(true);
		//增加或者移除功勋
		try {
			hcZhengBaSaiExportService.removeGuildXunZhang(hcZhengBaSaiManager.getGuildLeaderIds());
			hcZhengBaSaiExportService.addGuildXunZhang();
		} catch (Exception e) {
			ChuanQiLog.error("***云宫战打完：增加功勋或者移除功勋出错，error={}***",e);
		}
		List<String> stageIdList = hcZhengBaSaiManager.getStageIdList();
		hcZhengBaSaiManager.stop();
		for (String e : stageIdList) {
			activeTimeEnd(e);
		}
		//奖励邮件
		zbsEndReward(hcZhengBaSaiManager.getWinGuildIdList());
		hcZhengBaSaiManager.clear();
		ChuanQiLog.info("争霸赛结束了");
	}

	/**
	 * 拔旗出结果
	 * 
	 * @param stageId
	 */
	private void hasWinnerEnd(String stageId) {
		IStage istage = StageManager.getStage(stageId);
		HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) istage;
		Long winGuildId = 0L;
		if (tStage.getOwnerGuildId() != null) {
			winGuildId = tStage.getOwnerGuildId();
			hcZhengBaSaiManager.addWinGuildId(winGuildId);
		}
		Object[] guildInfo = guildExportService.getGuildBaseInfo(winGuildId);
		// 全服广播
		if (guildInfo != null) {
			String guildName = (String) guildInfo[0];
			DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(istage.getMapId());
			if (dituConfig != null) {
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[] { AppErrorCode.TERRITORY_HAVE_MAP, new Object[] { guildName, dituConfig.getName() } });
			}
		}
		// 去除扛旗buff
		IRole role = tStage.getFlagOwnerRole();
		IBuff buff = tStage.getFlagOwnerBuff();
		if (role != null && buff != null) {
			role.getBuffManager().removeBuff(buff.getId(), buff.getBuffCategory());
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
		// 龙腾消失
		if (tStage.getLongt() != null) {
			tStage.leave(tStage.getLongt());
		}
		// 旗帜归位
		qiziEnter(tStage);
		tStage.setFlagOccupyEnd(true); // 旗帜永久占领
		BusMsgSender.send2Many(tStage.getAllRoleIds(), ClientCmdType.HC_ZBS_END, tStage.getMapId());
		ChuanQiLog.info("争霸赛 {}结束,旗帜被成功占领30分钟 ", stageId);
	}

	/**
	 * 活动时间到活动结束
	 */
	private void activeTimeEnd(String stageId) {
		IStage istage = StageManager.getStage(stageId);
		HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) istage;

		// 踢人
		Object[] roleIds = tStage.getAllRoleIds();
		if (roleIds != null) {
			for (Object o : roleIds) {
				Long userRoleId = (Long) o;
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
			}
		}
		// 活动时间内有帮派成功占领了旗帜
		Long winGuildId = tStage.getOwnerGuildId();
		if (winGuildId != null) {
			hcZhengBaSaiManager.addWinGuildId(winGuildId);
			// 活动时间到结束更新数据库
			Zhengbasai zhengbasai = hcZhengBaSaiExportService.loadZhengbasai();
			if (zhengbasai == null) {
				hcZhengBaSaiExportService.createZhengbasai(winGuildId);
			} else {
				hcZhengBaSaiExportService.updateZhengbasai(winGuildId);
			}
		}
		//回收场景
		if(tStage.isCanRemove()){ 
			StageManager.removeCopy(tStage);
		}
		ChuanQiLog.info("争霸赛 {} 活动时间到活动正常结束", stageId);
	}
	
	/**
	 * 添加本服到redis作为跨服云宫之巅的参战服务器 
	 */
	private void addKuafuYunGongServerData(){
	    Redis redis = GameServerContext.getRedis();
	    if(null != redis){
	        List<String> list = redis.lrange(RedisKey.KUAFU_YUNGONG_SERVER_KEY, 0, -1);
	        String serverId = ChuanQiConfigUtil.getServerId();
	        if(ObjectUtil.isEmpty(list) || !list.contains(serverId)){
	            redis.lpush(RedisKey.KUAFU_YUNGONG_SERVER_KEY, serverId);
	        }
	        ChuanQiLog.info("源服serverId={}云宫之战结束,添加到redis作为跨服云宫之巅的参赛服务器", serverId);
	    }
	}
	
	public void zbsEndReward(List<Long> winGuildIdList) {
		List<Long> roleIds = hcZhengBaSaiManager.getRoleIds();
		List<Long> _15minuteRewardRoleIds = new ArrayList<Long>();
		List<Long> winGuildRewardRoleIds = new ArrayList<Long>();
		List<Long> loseGuildRewardRoleIds = new ArrayList<Long>();
		Long currentTime = GameSystemTime.getSystemMillTime();
		HcZBSPublicConfig publicConfig = getPublicConfig();
		long _15minutesSeconds = publicConfig.getTime2();
		for (Long e : roleIds) {
			GuildMember guildMember = guildExportService.getGuildMember(e);
			if (guildMember == null) {
				continue;
			}
			Long enterGuildTime = guildMember.getEnterTime();
			if (enterGuildTime == null) {
				continue;
			}
			if (DatetimeUtil.dayIsToday(enterGuildTime, currentTime)) {
				continue;
			}
			if (hcZhengBaSaiManager.getRoleTotalOnlineTime(e) < _15minutesSeconds) {
				continue;
			}
			_15minuteRewardRoleIds.add(e);
			if (winGuildIdList.contains(guildMember.getGuildId())) {
				winGuildRewardRoleIds.add(e);
			} else {
				loseGuildRewardRoleIds.add(e);
			}
		}
		String title = EmailUtil.getCodeEmail(AppErrorCode.HCZBS_EAMIL_TITLE);
		if (_15minuteRewardRoleIds.size() > 0) {
			int jingyan = publicConfig.getJingyan2();
			int zhenqi = publicConfig.getZhenqi2();
			Map<String, Integer> attachmentMap = new HashMap<String, Integer>();
			attachmentMap.put(ModulePropIdConstant.EXP_GOODS_ID, jingyan);
			attachmentMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, zhenqi);
			String[] attachmentArray = EmailUtil.getAttachments(attachmentMap);
			String content = EmailUtil.getCodeEmail(AppErrorCode.HCZBS_15_MINUTE_REWARD);
			sendRewardEmail(_15minuteRewardRoleIds,title, content, attachmentArray[0]);
		}
		if (winGuildRewardRoleIds.size() > 0) {
			int bg = publicConfig.getWinBg();
			for (Long e : winGuildRewardRoleIds) {
				try {
					RoleWrapper role =getRoleWrapper(e);
					int jingyan = publicConfig.getWinExp() * role.getLevel() * role.getLevel();
					Map<String, Integer> attachmentMap = new HashMap<String, Integer>();
					attachmentMap.put(ModulePropIdConstant.EXP_GOODS_ID, jingyan);
					attachmentMap.put(ModulePropIdConstant.GONGXIAN_GOODS_ID,bg);
					String[] attachmentArray = EmailUtil.getAttachments(attachmentMap);
					String content = EmailUtil.getCodeEmail(AppErrorCode.HCZBS_WIN_EAMIL_CONTENT, String.valueOf(bg), String.valueOf(jingyan));
					emailExportService.sendEmailToOne(e, title,content, GameConstants.EMAIL_TYPE_SINGLE, attachmentArray[0]);
				} catch (Exception e2) {
					ChuanQiLog.error("皇城站发胜利邮件错误，收件人：{}",e);
				}
				
			}
		}
		if (loseGuildRewardRoleIds.size() > 0) {
			int bg = publicConfig.getLoseBg();
			for (Long e : loseGuildRewardRoleIds) {
				try {
					RoleWrapper role =getRoleWrapper(e);
					int jingyan = publicConfig.getLoseExp() * role.getLevel() * role.getLevel();
					Map<String, Integer> attachmentMap = new HashMap<String, Integer>();
					attachmentMap.put(ModulePropIdConstant.EXP_GOODS_ID, jingyan);
					attachmentMap.put(ModulePropIdConstant.GONGXIAN_GOODS_ID,bg);
					String[] attachmentArray = EmailUtil.getAttachments(attachmentMap);
					String content = EmailUtil.getCodeEmail(AppErrorCode.HCZBS_LOSE_EAMIL_CONTENT, String.valueOf(bg), String.valueOf(jingyan));
					emailExportService.sendEmailToOne(e,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachmentArray[0]);
				} catch (Exception e2) {
					ChuanQiLog.error("皇城站发战败邮件错误，收件人：{}",e);
				}
			}
		}
	}

	private RoleWrapper getRoleWrapper(long e){
		RoleWrapper role =null;
		if(publicRoleStateService.isPublicOnline(e)){
			 role = roleExportService.getLoginRole(e);
		}else{
			 role = roleExportService.getUserRoleFromDb(e);
		}
		return role;
	}
	
	private void sendRewardEmail(List<Long> roleIds,String title, String content, String attachment) {
		emailExportService.sendEmailToMany(roleIds,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
	}
	// 初始化旗帜
	public void initQizi(AoiStage stage) {
		if (hcZhengBaSaiConfigExportService.loadPublicConfig() != null && stage.getLineNo().intValue() == 1) {
			qiziEnter(stage);
		}
	}

	public void synFlag(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isHcZBS()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null || !StageType.HCZBS_WAR.equals(istage.getStageType())) {
			return;
		}
		int[] zuobiao = null;
		HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) istage;
		if (tStage.getFlagOwnerRole() != null) {
			if (tStage.isFlagOccupyEnd()) {
				//持旗30分
				zuobiao = hcZhengBaSaiConfigExportService.loadPublicConfig().getZuobiao();
			} else {
				zuobiao = new int[2];
				Point p = tStage.getFlagOwnerRole().getPosition();
				zuobiao[0] = p.getX();
				zuobiao[1] = p.getY();
				tStage.startSynFlagPositionSchedule();
			}
		} else {
			zuobiao = hcZhengBaSaiConfigExportService.loadPublicConfig().getZuobiao();
		}
		if (!tStage.isFlagOccupyEnd()) {
			BusMsgSender.send2Many(tStage.getAllRoleIds(), ClientCmdType.HC_ZBS_SYN_FLAG, zuobiao);
		}
	}

	/**
	 * 退出副本
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] exitZhengBaSai(Long userRoleId) {
		// 判断是否有活动
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_HCZBS_ACTIVE_ID, GameConstants.COMPONENT_HCZBS_ACTIVE_ID);
		if (hdId == null) {
			return AppErrorCode.HCZBS_NOT_START;
		}

		// 判断是否有配置
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(hdId);
		if (config == null || config.getMapId() == 0) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 判断是否在副本中
		if (!stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_NOT_IN_FUBEN;
		}
		// 通知场景离开战场
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
		return AppErrorCode.OK;
	}

	/**
	 * 切换门主外显+buff
	 * roleType=1门主，=-1仙侣
	 */
	public void updateClothes(long userRoleId, String stageId, boolean isShow,int roleType) {
		HcZhengBaSaiConfig hcZhengBaSaiConfig = hcZhengBaSaiConfigExportService.loadPublicConfig();
		if (hcZhengBaSaiConfig == null) {
			return;
		}
		IStage tStage = StageManager.getStage(stageId);
		IStageElement roleElement = tStage.getElement(userRoleId, ElementType.ROLE);
		IRole role = (IRole) roleElement;
		role.getBusinessData().setHcZbsWinnerGuildLeader(isShow);//更新外显
		BusMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_CLOTHES_SHOW,isShow);
		if(roleType==1){
			//只有门主才会推送buff变更
			BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_HCZBS_XUNZHANG, isShow);
		} 
		
	}
}
