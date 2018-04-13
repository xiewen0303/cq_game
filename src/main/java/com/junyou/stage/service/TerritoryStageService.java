package com.junyou.stage.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.territory.entity.Territory;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.TerritoryPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.guild.util.GuildUtil;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.configure.TerritoryConfig;
import com.junyou.stage.configure.export.impl.TerritoryConfigExportService;
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
import com.junyou.stage.model.element.goods.TerritoryQizi;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.model.stage.territory.TerritoryManager;
import com.junyou.stage.model.stage.territory.TerritoryStage;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;

/**
 * 领地战
 * 
 * @author LiuYu
 * @date 2015-4-20 下午1:58:28
 */
@Service
public class TerritoryStageService {

	private TerritoryManager territoryManager;

	@Autowired
	private TerritoryExportService territoryExport;
	@Autowired
	private TerritoryConfigExportService territoryConfigExportService;
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
	private ZiYuanConfigExportService ziYuanConfigExportService;

	private TerritoryPublicConfig getPublicConfig() {
		TerritoryPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_TERRITORY_WAR);
		return publicConfig;
	}

	/**
	 * 活动期间加经验 加真气
	 * 
	 * @param userRoleId
	 * @param stageId
	 */
	public void addExpZhenqi(Long userRoleId, String stageId) {
		if (!ActiveUtil.isTerritory()) {
			return;// 非领地战期间不加
		}

		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.TERRITORY_WAR) {
			return;// 当前地图不是领地战地图
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;// 角色不存在
		}

		TerritoryConfig config = territoryConfigExportService
				.getConfigByMapId(stage.getMapId());
		if (config == null) {
			return;
		}
		long exp = config.getExp() * role.getLevel() * role.getLevel();
		long zhenqi = config.getZhenqi() * role.getLevel();
		StageMsgSender.send2Bus(userRoleId,
				InnerCmdType.INNER_TERRITORY_ADD_EXP_ZHENQI, new Object[] {
						exp, zhenqi });
		role.startTerritoryAddExpSchedule(getPublicConfig().getDelay());
	}

	private Integer getTerritoryHaveDays(Long updateTime) {
		if (updateTime != null) {
			long current = GameSystemTime.getSystemMillTime();
			updateTime = updateTime.longValue();
			if (current > updateTime) {
				if (DatetimeUtil.dayIsToday(current, updateTime)) {
					return 1;
				} else {
					Date currentDate = new Date(current);
					long current00 = DatetimeUtil.getDate00Time(currentDate);
					Date updateDate = new Date(updateTime);
					long update00 = DatetimeUtil.getDate00Time(updateDate);
					return (int) ((current00 - update00) / (24 * 60 * 60 * 1000L)) + 1;
				}
			}
		}
		return null;
	}

	public Object[] getInfo(Long userRoleId) {
		List<TerritoryConfig> configs = territoryConfigExportService
				.loadAllConfigs();
		Object[] ret = new Object[2];
		Object[] haveArray = new Object[configs.size()];
		int index = 0;
		for (TerritoryConfig e : configs) {
			String guildName = null;
			Integer haveDays = null;
			Long guildId = null;
			Long guildLeaderRoleId = null;
			String guildLeaderName = null;
			Integer guildLevel = null;
			Territory t = territoryExport.loadTerritoryByMapId(e.getMap());
			if (t != null && t.getGuildId() != null
					&& t.getGuildId().longValue() != 0) {
				guildId = t.getGuildId();
				Object[] baseInfo = guildExportService
						.getGuildBaseInfo(guildId);
				if (baseInfo != null) {
					guildName = (String) baseInfo[0];
					guildLeaderRoleId = (Long) baseInfo[1];
					guildLeaderName = (String) baseInfo[2];
					guildLevel = (Integer) baseInfo[3];
				}
				haveDays = getTerritoryHaveDays(t.getUpdateTime());
			}
			haveArray[index] = new Object[] { e.getMap(), guildName, haveDays,
					guildId, guildLevel,
					new Object[] { guildLeaderRoleId, guildLeaderName } };
			index++;
		}
		ret[0] = haveArray;
		ret[1] = territoryExport.getTerritoryRewardInfo(userRoleId);
		return ret;
	}

	/**
	 * 领地战活动开始
	 */
	public void activeStart(int hdId) {
		DingShiActiveConfig activeConfig = dingShiHuoDongBiaoConfigExportService
				.getConfig(hdId);
		if (activeConfig == null) {
			return;
		}
		TerritoryPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			ChuanQiLog.error("TerritoryPublicConfig is null.");
			return;
		}
		ActiveUtil.setTerritory(true);
		ActiveUtil.setCanChangeGuild(false);
		ActiveUtil.setCanUseFX(false);
		try{
			territoryManager = new TerritoryManager();
			territoryManager.setPublicConfig(publicConfig);

			for (TerritoryConfig config : territoryConfigExportService
					.loadAllConfigs()) {
				int mapId = config.getMap();
				Territory territory = territoryExport.loadTerritoryByMapId(mapId);
				Long guildId = null;
				if (territory != null) {
					guildId = territory.getGuildId();
				}
				if(guildId!=null && guildId.longValue()!=0){
					Object[] info = guildExportService.getGuildBaseInfo(guildId);
					if(info == null){
						territoryExport.updateTerritory(mapId*1L, 0L);
						ChuanQiLog.error("不存在此公会:" + guildId);
						guildId= null;
					}else{
						Long leaderId = (Long) info[1];
						territoryManager.addTerritoryGuildLeaderId(leaderId);
					}
				}
				String stageId = StageUtil.getStageId(mapId, 1);
				IStage stage = StageManager.getStage(stageId);
				if (stage == null) {
					ChuanQiLog.error("领地战场景不存在，场景id:" + stageId);
					continue;
				}
				if (stage.getStageType() != StageType.NOMAL) {
					continue;
				}
				AoiStage aoiStage = (AoiStage) stage;
				TerritoryStage tStage = new TerritoryStage(aoiStage,
						territoryManager, guildId, config);
				territoryManager.addStageId(tStage.getId());
				StageManager.addStageCopy(tStage);
				tStage.init(publicConfig.getNeedTime());
				if (guildId != null && guildId.longValue() != 0) {
					BusMsgSender.send2Many(tStage.getAllRoleIds(),
							ClientCmdType.TERRITORY_FLAG_CHANGE, new Object[] {
									tStage.getMapId(), guildId });
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("领地战开始出错了",e);
		}
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.TERRITORY_ACTIVE_END, null);

		Long delay = activeConfig.getCalcEndSecondTime();
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.COMPONENT_TERRITORY_END, runable,
				delay.intValue(), TimeUnit.MILLISECONDS);
		ChuanQiLog.info("领地战开始了");
	}

	public Object[] fetchFlagBegin(Long userRoleId, Long flagGuid) {
		// 校验是否在活动中
		if (!ActiveUtil.isTerritory()) {
			return AppErrorCode.TERRITORY_NO_START;
		}
		String stageId = publicRoleStateService
				.getRolePublicStageId(userRoleId);
		IStage istage = StageManager.getStage(stageId);
		if (istage == null
				|| !StageType.TERRITORY_WAR.equals(istage.getStageType())) {
			return AppErrorCode.TERRITORY_ACTIVE_END;
		}
		TerritoryStage tStage = (TerritoryStage) istage;
		TerritoryConfig config = territoryConfigExportService
				.getConfigByMapId(tStage.getMapId());
		if (config == null) {
			return AppErrorCode.ERR;
		}
		IStageElement element = tStage
				.getElement(flagGuid, ElementType.COLLECT);
		if (element == null) {
			return AppErrorCode.TERRITORY_FLAG_NOT_EXIST;
		}
		Collect collect = (Collect) element;
		if (collect.getCollertType() != 4) {
			return AppErrorCode.ERR;
		}
		// 校验玩家有没有帮派
		Object[] result = guildExportService.flagCheck(userRoleId,
				getPublicConfig().getNeedGold());
		if (result != null) {
			return result;
		}
		tStage.setFlag(userRoleId, flagGuid);
		return new Object[] { AppErrorCode.SUCCESS, collect.getCollectTime() };
	}

	public Object[] fetchFlagEnd(Long userRoleId) {
		// 校验是否在活动中
		if (!ActiveUtil.isTerritory()) {
			return AppErrorCode.TERRITORY_NO_START;
		}
		String stageId = publicRoleStateService
				.getRolePublicStageId(userRoleId);
		IStage istage = StageManager.getStage(stageId);
		if (istage == null
				|| !StageType.TERRITORY_WAR.equals(istage.getStageType())) {
			return AppErrorCode.ERR;
		}
		TerritoryStage tStage = (TerritoryStage) istage;
		TerritoryConfig config = territoryConfigExportService
				.getConfigByMapId(tStage.getMapId());
		if (config == null) {
			return AppErrorCode.ERR;
		}
		Long flagGuild = tStage.getFlag(userRoleId);
		if (flagGuild == null) {
			return AppErrorCode.TERRITORY_FLAG_NOT_EXIST;
		}
		IStageElement element = tStage.getElement(flagGuild,
				ElementType.COLLECT);
		if (element == null) {
			return AppErrorCode.TERRITORY_FLAG_NOT_EXIST;
		}
		// 校验玩家有没有帮派
		Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
		if (guildInfo == null) {
			return AppErrorCode.TERRITORY_NO_GUILD;
		}
		// 校验帮派职位
		Integer position = (Integer) guildInfo[8];
		if (!GuildUtil.isLeaderOrViceLeader(position)) {
			return AppErrorCode.TERRITORY_POSITION_LIMIT;
		}
		// 校验帮派贡献银两
		Object[] result = guildExportService.flagCost(userRoleId,
				getPublicConfig().getNeedGold());
		if (result != null) {
			return result;
		}

		DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(istage
				.getMapId());
		if (dituConfig == null) {
			return AppErrorCode.ERR;
		}
		tStage.cancelTobeWinnerSchedule();
		tStage.startTobeWinnerSchedule(getPublicConfig().getNeedTime());
		tStage.clearFlagMap();
		// 移除旗帜
		tStage.leave(element);
		tStage.setFlag(false);
		ChuanQiLog.info("领地战 {} 旗子被 {} 抢了", tStage.getId(), userRoleId);
		// 显示图腾
		longtEnter(tStage);

		IStageElement roleElement = tStage.getElement(userRoleId,
				ElementType.ROLE);
		IRole role = (IRole) roleElement;
		tStage.setFlagOwnerRole(role);
		tStage.setOwnerGuildId(role.getBusinessData().getGuildId());
		// 全服广播
		BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[] {
				AppErrorCode.TERRITORY_FETCH_FLAG,
				new Object[] { (String) guildInfo[1], dituConfig.getName() } });
		IBuff buff = BuffFactory.create(role, role, getPublicConfig()
				.getKqbuff());
		role.getBuffManager().addBuff(buff);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		tStage.setFlagOwnerBuff(buff);
		tStage.startSynFlagPositionSchedule();
		BusMsgSender.send2Many(tStage.getAllRoleIds(),
				ClientCmdType.TERRITORY_FLAG_CHANGE,
				new Object[] { tStage.getMapId(),
						role.getBusinessData().getGuildId() });
		return null;
	}

	private void longtEnter(TerritoryStage tStage) {
		if (tStage.getLongt() != null) {
			return;
		}
		MonsterConfig monsterConfig = monsterExportService
				.load(getPublicConfig().getLongt());
		long id = IdFactory.getInstance().generateNonPersistentId();
		IMonster monster = MonsterFactory.create(id + "", id, monsterConfig);
		tStage.setLongt(monster);
		int[] zuobiao = territoryConfigExportService.getConfigByMapId(
				tStage.getMapId()).getZuobiao();
		tStage.enter(monster, zuobiao[0], zuobiao[1]);
		ChuanQiLog.info("领地战 {} 龙腾出现", tStage.getId());
	}

	private void qiziEnter(IStage stage) {
		if (stage.hasFlag()) {
			return;
		}
		Integer ziyuanId = getPublicConfig().getZiyuanid();
		ZiYuanConfig ziYuanConfig = ziYuanConfigExportService
				.loadById(ziyuanId);
		TerritoryQizi qizi = CollectFacory.createTerritoryQizi(IdFactory
				.getInstance().generateNonPersistentId(), ziYuanConfig, stage
				.getId());
		int[] zuobiao = territoryConfigExportService.getConfigByMapId(
				stage.getMapId()).getZuobiao();
		stage.enter(qizi, zuobiao[0], zuobiao[1]);
		stage.setFlag(true);
		ChuanQiLog.info("领地战 {} 旗子出现", stage.getId());
	}

	public void hasWinner(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isTerritory()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null
				|| !StageType.TERRITORY_WAR.equals(istage.getStageType())) {
			return;
		}
		territoryEnd(stageId, false);
	}

	public void flagOwnerDead(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isTerritory()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null
				|| !StageType.TERRITORY_WAR.equals(istage.getStageType())) {
			return;
		}
		TerritoryStage tStage = (TerritoryStage) istage;
		try {
			tStage.lock();
			IRole role = tStage.getFlagOwnerRole();
			if (role == null) {
				return;
			}
			IBuff buff = tStage.getFlagOwnerBuff();
			if (buff != null) {
				role.getBuffManager().removeBuff(buff.getId(),
						buff.getBuffCategory());
				role.getFightStatistic().flushChanges(
						DirectMsgWriter.getInstance());
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
			ChuanQiLog.info("领地战 {} 扛旗的人死了", stageId);
			// 旗子归位
			qiziEnter(tStage);
			tStage.cancelTobeWinnerSchedule();
			synFlag(stageId);
			BusMsgSender.send2Many(tStage.getAllRoleIds(),
					ClientCmdType.TERRITORY_FLAG_CHANGE,
					new Object[] { tStage.getMapId(), -1 });
		} finally {
			tStage.unlock();
		}
	}

	public void monsterDead(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isTerritory()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null
				|| !StageType.TERRITORY_WAR.equals(istage.getStageType())) {
			return;
		}
		TerritoryStage tStage = (TerritoryStage) istage;
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
				role.getBuffManager().removeBuff(buff.getId(),
						buff.getBuffCategory());
				role.getFightStatistic().flushChanges(
						DirectMsgWriter.getInstance());
				tStage.setFlagOwnerBuff(null);
			}
			ChuanQiLog.info("领地战 {} 龙腾死了", stageId);
			qiziEnter(tStage);
			tStage.setLongt(null);
			tStage.cancelTobeWinnerSchedule();
			synFlag(stageId);
			BusMsgSender.send2Many(tStage.getAllRoleIds(),
					ClientCmdType.TERRITORY_FLAG_CHANGE,
					new Object[] { tStage.getMapId(), -1 });
		} finally {
			tStage.unlock();
		}
	}

	public void activeEnd() {
		ActiveUtil.setTerritory(false);
		ActiveUtil.setCanChangeGuild(true);
		ActiveUtil.setCanUseFX(true);
		List<String> stageIdList = territoryManager.getStageIdList();
		for (String e : stageIdList) {
			try{
				territoryEnd(e, true);
			}catch (Exception ex) {
				ChuanQiLog.error("领地战结束出错了 stageId="+e,ex);
			}
		}
		territoryEndReward(territoryManager.getWinGuildIdList());
		territoryExport.removeGuildXunZhang(territoryManager.getTerritoryGuildLeaderIds());
		territoryExport.addGuildXunZhang();
		territoryManager.clear();
		ChuanQiLog.info("领地战结束了");
	}

	/**
	 * 领地战结束
	 * 
	 * @param stageId
	 */
	public void territoryEnd(String stageId, boolean normalEnd) {
		IStage istage = StageManager.getStage(stageId);
		if (istage == null
				|| !StageType.TERRITORY_WAR.equals(istage.getStageType())) {
			return;
		}
		TerritoryStage tStage = (TerritoryStage) istage;
		Long winGuildId = 0L;
		if (tStage.getOwnerGuildId() != null) {
			winGuildId = tStage.getOwnerGuildId();
			territoryManager.addWinGuildId(winGuildId);
		}
		Territory territory = territoryExport.loadTerritoryByMapId(istage
				.getMapId());
		if (winGuildId != 0L) {
			if (territory == null) {
				territoryExport.createTerritory(istage.getMapId().longValue(),
						winGuildId);
			} else {
				territoryExport.updateTerritory(istage.getMapId().longValue(),
						winGuildId);
			}
			// 全服广播
			Object[] guildInfo = guildExportService
					.getGuildBaseInfo(winGuildId);
			if (guildInfo != null) {
				String guildName = (String) guildInfo[0];
				DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(istage
						.getMapId());
				if (dituConfig != null) {
					BusMsgSender.send2All(
							ClientCmdType.NOTIFY_CLIENT_ALERT6,
							new Object[] {
									AppErrorCode.TERRITORY_HAVE_MAP,
									new Object[] { guildName,
											dituConfig.getName() } });
				}
			}
		} else {
			if (territory != null) {
				territoryExport.updateTerritory(istage.getMapId().longValue(),
						0);
			}
		}
		// 去除扛旗buff
		IRole role = tStage.getFlagOwnerRole();
		IBuff buff = tStage.getFlagOwnerBuff();
		if (role != null && buff != null) {
			role.getBuffManager().removeBuff(buff.getId(),
					buff.getBuffCategory());
			role.getFightStatistic()
					.flushChanges(DirectMsgWriter.getInstance());
		}
		// 龙腾消失,旗帜归位
		if (tStage.getLongt() != null) {
			tStage.leave(tStage.getLongt());
		}
		qiziEnter(tStage);

		Object[] roleIds = tStage.getAllRoleIds();
		if (roleIds != null) {
			for (Object e : roleIds) {
				Long elementId = (Long) e;
				IRole irole = tStage.getElement(elementId, ElementType.ROLE);
				irole.setStage(tStage.getStage());
			}
		}
		StageManager.addStageCopy(tStage.getStage());
		if (!normalEnd) {
			BusMsgSender.send2Many(tStage.getAllRoleIds(),
					ClientCmdType.TERRITORY_END, tStage.getMapId());
		}
		ChuanQiLog.info("领地战 {} 结束 正常{}", stageId, normalEnd);
	}

	public void territoryEndReward(List<Long> winGuildIdList) {
		List<Long> roleIds = territoryManager.getRoleIds();
		List<Long> _15minuteRewardRoleIds = new ArrayList<Long>();
		List<Long> winGuildRewardRoleIds = new ArrayList<Long>();
		List<Long> loseGuildRewardRoleIds = new ArrayList<Long>();
		Long currentTime = GameSystemTime.getSystemMillTime();
		TerritoryPublicConfig publicConfig = getPublicConfig();
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
			if (territoryManager.getRoleTotalOnlineTime(e) < _15minutesSeconds) {
				continue;
			}
			_15minuteRewardRoleIds.add(e);
			if (winGuildIdList.contains(guildMember.getGuildId())) {
				winGuildRewardRoleIds.add(e);
			} else {
				loseGuildRewardRoleIds.add(e);
			}
		}
		if (_15minuteRewardRoleIds.size() > 0) {
			int jingyan = publicConfig.getJingyan2();
			int zhenqi = publicConfig.getZhenqi2();
			Map<String, Integer> attachmentMap = new HashMap<String, Integer>();
			attachmentMap.put(ModulePropIdConstant.EXP_GOODS_ID, jingyan);
			attachmentMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, zhenqi);
			String[] attachmentArray = EmailUtil.getAttachments(attachmentMap);
			String title = EmailUtil.getCodeEmail(AppErrorCode.TERRITORY_WIN_EAMIL_CONTENT_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.TERRITORY_15_MINUTE_REWARD);
			sendRewardEmail(_15minuteRewardRoleIds,title, content, attachmentArray[0]);
		}
		if (winGuildRewardRoleIds.size() > 0) {
			int bg = publicConfig.getWinBg();
			String title = EmailUtil.getCodeEmail(AppErrorCode.TERRITORY_WIN_EAMIL_CONTENT_TITLE);
			for (Long e : winGuildRewardRoleIds) {
				try {
					RoleWrapper role =getRoleWrapper(e);
					guildExportService.addGongxian(e, bg);
					int jingyan = publicConfig.getWinExp() * role.getLevel()
							* role.getLevel();
					Map<String, Integer> attachmentMap = new HashMap<String, Integer>();
					attachmentMap.put(ModulePropIdConstant.EXP_GOODS_ID, jingyan);
					String[] attachmentArray = EmailUtil.getAttachments(attachmentMap);
					String content = EmailUtil.getCodeEmail(AppErrorCode.TERRITORY_WIN_EAMIL_CONTENT,String.valueOf(bg), String.valueOf(jingyan));
					emailExportService.sendEmailToOne(e,title, content,GameConstants.EMAIL_TYPE_SINGLE, attachmentArray[0]);
				} catch (Exception e2) {
					ChuanQiLog.error("领地战发胜利邮件错误，收件人：{}",e);
				}
				
			}
		}
		if (loseGuildRewardRoleIds.size() > 0) {
			int bg = publicConfig.getLoseBg();
			for (Long e : loseGuildRewardRoleIds) {
				try {
					RoleWrapper role =getRoleWrapper(e);
					guildExportService.addGongxian(e, bg);
					int jingyan = publicConfig.getLoseExp() * role.getLevel()
							* role.getLevel();
					Map<String, Integer> attachmentMap = new HashMap<String, Integer>();
					attachmentMap.put(ModulePropIdConstant.EXP_GOODS_ID, jingyan);
					String[] attachmentArray = EmailUtil
							.getAttachments(attachmentMap);
					String title = EmailUtil.getCodeEmail(AppErrorCode.TERRITORY_WIN_EAMIL_CONTENT_TITLE);
					String content = EmailUtil.getCodeEmail(
							AppErrorCode.TERRITORY_LOSE_EAMIL_CONTENT,
							String.valueOf(bg), String.valueOf(jingyan));
					emailExportService.sendEmailToOne(e,title, content,
							GameConstants.EMAIL_TYPE_SINGLE, attachmentArray[0]);
				} catch (Exception e2) {
					ChuanQiLog.error("领地战发战败邮件错误，收件人：{}",e);
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

	private void sendRewardEmail(List<Long> roleIds,String title, String content,String attachment) {
		emailExportService.sendEmailToMany(roleIds,title, content,GameConstants.EMAIL_TYPE_SINGLE, attachment);
	}

	public void initQizi(AoiStage stage) {
		if (territoryConfigExportService.getConfigByMapId(stage.getMapId()) != null
				&& stage.getLineNo().intValue() == 1) {
			qiziEnter(stage);
		}
	}

	public void addBanggong(Long userRoleId) {
		TerritoryPublicConfig config = getPublicConfig();
		if (config == null) {
			return;
		}
		if (territoryManager.getRoleBg(userRoleId) >= config.getJigongshu()) {
			return;
		}
		int bg = config.getJigong();
		guildExportService.addGongxian(userRoleId, bg);
		territoryManager.addRoleBg(userRoleId, bg);
	}

	public void synFlag(String stageId) {
		// 校验是否在活动中
		if (!ActiveUtil.isTerritory()) {
			return;
		}
		IStage istage = StageManager.getStage(stageId);
		if (istage == null
				|| !StageType.TERRITORY_WAR.equals(istage.getStageType())) {
			return;
		}
		int[] zuobiao = null;
		TerritoryStage tStage = (TerritoryStage) istage;
		if (tStage.getFlagOwnerRole() != null) {
			if (!tStage.getFlagOwnerRole().getStage().getId().equals(stageId)) {
				zuobiao = territoryConfigExportService.getConfigByMapId(
						tStage.getMapId()).getZuobiao();
			} else {
				zuobiao = new int[2];
				Point p = tStage.getFlagOwnerRole().getPosition();
				zuobiao[0] = p.getX();
				zuobiao[1] = p.getY();
				tStage.startSynFlagPositionSchedule();
			}
		} else {
			zuobiao = territoryConfigExportService.getConfigByMapId(
					tStage.getMapId()).getZuobiao();
		}
		BusMsgSender.send2Many(tStage.getAllRoleIds(),
				ClientCmdType.TERRITORY_SYN_FLAG, zuobiao);
	}
}
