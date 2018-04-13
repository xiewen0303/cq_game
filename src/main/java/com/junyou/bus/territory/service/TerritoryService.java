package com.junyou.bus.territory.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.territory.dao.TerritoryDao;
import com.junyou.bus.territory.dao.TerritoryDayRewardDao;
import com.junyou.bus.territory.entity.Territory;
import com.junyou.bus.territory.entity.TerritoryDayReward;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.configure.TerritoryConfig;
import com.junyou.stage.configure.export.impl.TerritoryConfigExportService;
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
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class TerritoryService {
	@Autowired
	private TerritoryDao territoryDao;
	@Autowired
	private TerritoryDayRewardDao territoryDayRewardDao;
	@Autowired
	private TerritoryConfigExportService territoryConfigExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;

	@Autowired
	private PublicRoleStateService publicRoleStateService;

	public List<Territory> initTerritory() {
		return territoryDao.initTerritory();
	}

	public List<TerritoryDayReward> initDayReward(Long userRoleId) {
		return territoryDayRewardDao.initTerritoryDayReward(userRoleId);
	}

	public Map<String, Long> getGuildXunZhangAttr(Long userRoleId) {
		GuildMember member = guildExportService.getGuildMember(userRoleId);
		if (member != null) {
			if (member.getPostion() == GameConstants.GUILD_LEADER_POSTION) {
				Long guildId = member.getGuildId();
				List<Integer> mapIds = getTerritories(guildId);
				if(mapIds ==null){
					return null;
				}
				Map<String, Long> attrMap = new HashMap<String, Long>();
				for (Integer mapId : mapIds) {
					TerritoryConfig config = territoryConfigExportService
							.getConfigByMapId(mapId);
					if (config != null) {
						ObjectUtil.longMapAdd(attrMap, config.getAttrs());
					}
				}
				return attrMap;
			}
		}
		return null;
	}

	public Object[] getTerritoryRewardInfo(Long userRoleId) {
		// 校验是否有公会
		Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
		if (guildInfo == null) {
			return null;
		}
		List<TerritoryDayReward> list = territoryDayRewardDao.cacheLoadAll(userRoleId);
		Set<Integer> rewardedMapIds = new HashSet<>();
		if (list != null) {
			long currentTime = GameSystemTime.getSystemMillTime();
			for (TerritoryDayReward e : list) {
				long updateTime = e.getUpdateTime();
				if (DatetimeUtil.dayIsToday(currentTime, updateTime)) {
					rewardedMapIds.add(e.getMapId());
				}
			}

			//填充当当日占领的不可领奖的信息
			List<TerritoryConfig> territoryConfigs =  territoryConfigExportService.loadAllConfigs();
			for (TerritoryConfig territoryConfig: territoryConfigs) {
				Territory territory = loadTerritoryByMapId(territoryConfig.getMap());
				if(territory != null){
					if (DatetimeUtil.dayIsToday(territory.getUpdateTime(), currentTime) && (Long)guildInfo[0] == territory.getGuildId().longValue()) {
						rewardedMapIds.add(territoryConfig.getMap());
					}
				}
			}
		}
		return rewardedMapIds.toArray();
	}

	public Integer getGuildLeaderMapId(Long userRoleId) {
		// 校验是否在活动期间
		if (!ActiveUtil.isTerritory()) {
			return null;
		} else {
			Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
			if (guildInfo == null) {
				return null;
			}
			Long guildId = (Long) guildInfo[0];
			Object[] baseGuildInfo = guildExportService
					.getGuildBaseInfo(guildId);
			Long leaderUserRoleId = (Long) baseGuildInfo[1];
			if (!publicRoleStateService.isPublicOnline(leaderUserRoleId)) {
				return null;
			}
			String leaderStageId = publicRoleStateService
					.getRolePublicStageId(leaderUserRoleId);
			IStage leaderStage = StageManager.getStage(leaderStageId);
			Integer mapId = leaderStage.getMapId();
			if (territoryConfigExportService.getConfigByMapId(mapId) == null) {
				return null;
			}
			return mapId;
		}
	}

	private TerritoryDayReward getTerritoryDayReward(Long userRoleId,
			final Integer mapId) {
		List<TerritoryDayReward> list = territoryDayRewardDao.cacheLoadAll(
				userRoleId, new IQueryFilter<TerritoryDayReward>() {
					private boolean stop = false;

					@Override
					public boolean check(TerritoryDayReward territoryDayReward) {
						if (territoryDayReward.getMapId().intValue() == mapId
								.intValue()) {
							stop = true;
						}
						return stop;
					}

					@Override
					public boolean stopped() {
						return stop;
					}
				});
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public void createTerritory(Long mapId, Long guildId) {
		Territory territory = new Territory();
		territory.setMapId(mapId);
		territory.setGuildId(guildId);
		territory.setUpdateTime(GameSystemTime.getSystemMillTime());
		territoryDao.cacheInsert(territory, GameConstants.DEFAULT_ROLE_ID);
	}

	public void updateTerritory(Long mapId, long guildId) {
		Territory territory = territoryDao.cacheLoad(mapId,
				GameConstants.DEFAULT_ROLE_ID);
		boolean updateTime = true;
		if (guildId == 0) {
			updateTime = true;
		} else {
			if (territory.getGuildId() == null
					|| territory.getGuildId().longValue() == 0) {
				updateTime = true;
			} else {
				if (territory.getGuildId().longValue() == guildId) {
					updateTime = false;
				} else {
					updateTime = true;
				}
			}
		}
		territory.setGuildId(guildId);
		if (updateTime) {
			territory.setUpdateTime(GameSystemTime.getSystemMillTime());
		}
		territoryDao.cacheUpdate(territory, GameConstants.DEFAULT_ROLE_ID);
	}

	private TerritoryDayReward createTerritoryDayReward(Long userRoleId,
			Integer mapId, long updateTime) {
		TerritoryDayReward dayReward = new TerritoryDayReward();
		dayReward
				.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		dayReward.setUserRoleId(userRoleId);
		dayReward.setMapId(mapId);
		dayReward.setUpdateTime(updateTime);
		territoryDayRewardDao.cacheInsert(dayReward, userRoleId);
		return dayReward;
	}

	/**
	 * 领取每日奖励
	 * 
	 * @param userRoleId
	 * @param mapIds
	 * @return
	 */
	public Object[] getReward(Long userRoleId, Integer[] mapIds) {
		// 校验是否有公会
		Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
		if (guildInfo == null) {
			return AppErrorCode.TERRITORY_NO_GUILD;
		}
		// 校验是否在活动期间
		if (ActiveUtil.isTerritory()) {
			return AppErrorCode.TERRITORY_IN;
		}
		// 校验mapId是否是占领,是否领取
		Long guildId = (Long) guildInfo[0];
		List<TerritoryDayReward> territoryDayRewardList = territoryDayRewardDao
				.cacheLoadAll(userRoleId);
		long currentTime = GameSystemTime.getSystemMillTime();
		for (Integer mapId : mapIds) {
			Territory territory = loadTerritoryByMapId(mapId);
			if (territory == null
					|| territory.getGuildId() == null
					|| territory.getGuildId().longValue() == 0
					|| territory.getGuildId().longValue() != guildId
							.longValue()) {
				return AppErrorCode.TERRITORY_CAN_NOT_REWARD;
			}
			if (DatetimeUtil.dayIsToday(territory.getUpdateTime(), currentTime)) {
				return AppErrorCode.TERRITORY_CAN_NOT_REWARD_FIRST_DAY;
			}
			if (territoryDayRewardList != null) {
				for (TerritoryDayReward e : territoryDayRewardList) {
					if (e.getMapId().intValue() == territory.getMapId()
							.intValue()) {
						long updateTime = e.getUpdateTime();
						if (DatetimeUtil.dayIsToday(currentTime, updateTime)) {
							return AppErrorCode.TERRITORY_ALREADY_REWARD;
						}
					}
				}
			}
		}
		Map<String, Integer> jiangItem = new HashMap<String, Integer>();
		for (Integer mapId : mapIds) {
			TerritoryConfig config = territoryConfigExportService
					.getConfigByMapId(mapId);
			ObjectUtil.mapAdd(jiangItem, config.getJiangitem());
			TerritoryDayReward territoryDayReward = getTerritoryDayReward(
					userRoleId, mapId);
			if (territoryDayReward != null) {
				territoryDayReward.setUpdateTime(currentTime);
				territoryDayRewardDao.cacheUpdate(territoryDayReward,
						userRoleId);
			} else {
				createTerritoryDayReward(userRoleId, mapId, currentTime);
			}

		}
		roleBagExportService.putGoodsAndNumberAttr(jiangItem, userRoleId,
				GoodsSource.TERRITORY_DAY_REWARD, LogPrintHandle.GET_NORMAL,
				LogPrintHandle.GBZ_NORMAL, true);
		return new Object[] { AppErrorCode.SUCCESS, jiangItem, mapIds };
	}

	/**
	 * 根据地图id获取领地信息(可能为null)
	 * 
	 * @param mapId
	 * @return
	 */
	public Territory loadTerritoryByMapId(Integer mapId) {
		return territoryDao.cacheLoad(mapId.longValue(),
				GameConstants.DEFAULT_ROLE_ID);
	}

	public boolean territoryHasReward(Long userRoleId) {
		if (ActiveUtil.isTerritory()) {
			return false;
		}
		// 校验是否有公会
		Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
		if (guildInfo == null) {
			return false;
		}
		Long guildId = (Long) guildInfo[0];

		List<TerritoryDayReward> territoryDayRewardList = territoryDayRewardDao
				.cacheLoadAll(userRoleId);
		long currentTime = GameSystemTime.getSystemMillTime();
		// 检测是否占领城池
		List<TerritoryConfig> list = territoryConfigExportService
				.loadAllConfigs();
		for (TerritoryConfig e : list) {
			int mapId = e.getMap();
			Territory territory = loadTerritoryByMapId(mapId);
			if (territory != null
					&& territory.getGuildId() != null
					&& territory.getGuildId().longValue() != 0
					&& territory.getGuildId().longValue() == guildId
							.longValue()) {
				boolean todayRewarded = false;
				if (territoryDayRewardList != null) {
					for (TerritoryDayReward reward : territoryDayRewardList) {
						if (reward.getMapId().intValue() == mapId) {
							long updateTime = reward.getUpdateTime();
							if (DatetimeUtil
									.dayIsToday(currentTime, updateTime)) {
								todayRewarded = true;
								break;
							}
						}
					}
				}
				if (!todayRewarded
						&& !DatetimeUtil.dayIsToday(currentTime,
								territory.getUpdateTime())) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Integer> getTerritories(final Long guildId) {
		List<Territory> list = territoryDao.cacheLoadAll(
				GameConstants.DEFAULT_ROLE_ID, new IQueryFilter<Territory>() {

					@Override
					public boolean check(Territory territory) {
						if (territory.getGuildId() != null
								&& territory.getGuildId().longValue() == guildId
										.longValue()) {
							return true;
						}
						return false;
					}

					@Override
					public boolean stopped() {
						return false;
					}
				});
		if (list != null && list.size() > 0) {
			List<Integer> ret = new ArrayList<Integer>();
			for (Territory t : list) {
				ret.add(t.getMapId().intValue());
			}
			return ret;
		}
		return null;
	}

	public void removeGuildXunZhang(List<Long> leaderIds) {
		for (Long userRoleId : leaderIds) {
			if (publicRoleStateService.isPublicOnline(userRoleId)) {
				BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_TERRITORY_XUNZHANG,
						null);
				String stageId = publicRoleStateService
						.getRolePublicStageId(userRoleId);
				IStage stage = StageManager.getStage(stageId);
				IRole role = stage.getElement(userRoleId, ElementType.ROLE);
				role.getFightAttribute().clearBaseAttribute(
						BaseAttributeType.GUILD_XUANZHANG_TERRITORY, true);
				role.getFightStatistic().flushChanges(
						DirectMsgWriter.getInstance());
			}
		}
	}

	public void addGuildXunZhang() {
		Map<Long, List<Integer>> map = new HashMap<Long, List<Integer>>();
		List<TerritoryConfig> mapList = territoryConfigExportService
				.loadAllConfigs();
		for (TerritoryConfig config : mapList) {
			Territory t = territoryDao.cacheLoad(config.getMap() * 1L,
					GameConstants.DEFAULT_ROLE_ID);
			if (t != null && t.getGuildId() != null
					&& t.getGuildId().longValue() != 0) {
				Object[] guildInfo = guildExportService.getGuildBaseInfo(t
						.getGuildId());
				Long guildLeaderId = (Long) guildInfo[1];
				List<Integer> list = map.get(guildLeaderId);
				if (list == null) {
					list = new ArrayList<Integer>();
					map.put(guildLeaderId, list);
				}
				list.add(config.getMap());
			}
		}
		for (Long userRoleId : map.keySet()) {
			List<Integer> list = map.get(userRoleId);
			
			Integer[] arr = new Integer[list.size()];
			for(int i=0;i<list.size();i++){
				arr[i] = list.get(i);
			}
			BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_TERRITORY_XUNZHANG,
					arr);
			
			Map<String, Long> attrMap = new HashMap<String, Long>();
			for (Integer mapId : list) {
				TerritoryConfig config = territoryConfigExportService
						.getConfigByMapId(mapId);
				if (config != null) {
					ObjectUtil.longMapAdd(attrMap, config.getAttrs());
				}
			}
			if (publicRoleStateService.isPublicOnline(userRoleId)) {
				String stageId = publicRoleStateService
						.getRolePublicStageId(userRoleId);
				IStage stage = StageManager.getStage(stageId);
				IRole role = stage.getElement(userRoleId, ElementType.ROLE);
				RoleFactoryHelper.setRoleBaseAttrs(attrMap, role,
						BaseAttributeType.GUILD_XUANZHANG_TERRITORY);
				role.getFightStatistic().flushChanges(
						DirectMsgWriter.getInstance());
			}
		}
	}
	
	/**
	 * 掌门让位
	 */
	public void territoryLeaderOff(Long userRoleId){
		if (publicRoleStateService.isPublicOnline(userRoleId)) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_TERRITORY_XUNZHANG,
					null);
			String stageId = publicRoleStateService
					.getRolePublicStageId(userRoleId);
			IStage stage = StageManager.getStage(stageId);
			IRole role = stage.getElement(userRoleId, ElementType.ROLE);
			role.getFightAttribute().clearBaseAttribute(BaseAttributeType.GUILD_XUANZHANG_TERRITORY, true);
			role.getFightStatistic().flushChanges(
					DirectMsgWriter.getInstance());
		}
	}
	/**
	 * 新掌门上位
	 */
	public void territoryLeaderON(Long userRoleId,Long guildId){
		if (publicRoleStateService.isPublicOnline(userRoleId)) {
			List<Integer> mapIdList = getTerritories(guildId);
			if(mapIdList==null|| mapIdList.size() == 0 ){
				return;
			}
			Integer[] arr = new Integer[mapIdList.size()];
			for(int i=0;i<mapIdList.size();i++){
				arr[i] = mapIdList.get(i);
			}
			BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_TERRITORY_XUNZHANG,
					arr);
			
			Map<String, Long> attrMap = new HashMap<String, Long>();
			for (Integer mapId : mapIdList) {
				TerritoryConfig config = territoryConfigExportService
						.getConfigByMapId(mapId);
				if (config != null) {
					ObjectUtil.longMapAdd(attrMap, config.getAttrs());
				}
			}
			String stageId = publicRoleStateService
					.getRolePublicStageId(userRoleId);
			IStage stage = StageManager.getStage(stageId);
			IRole role = stage.getElement(userRoleId, ElementType.ROLE);
			RoleFactoryHelper.setRoleBaseAttrs(attrMap, role,
					BaseAttributeType.GUILD_XUANZHANG_TERRITORY);
			role.getFightStatistic().flushChanges(
					DirectMsgWriter.getInstance());
		}
	}
	/**
	 * 掌门上线领地战勋章buff
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		GuildMember member = guildExportService.getGuildMember(userRoleId);
		if (member != null) {
			if (member.getPostion() == GameConstants.GUILD_LEADER_POSTION) {
				Long guildId = member.getGuildId();
				List<Integer> mapIds = getTerritories(guildId);
				if(mapIds ==null || mapIds.size() == 0){
					return;
				}
				Integer[] arr = new Integer[mapIds.size()];
				for(int i=0;i<mapIds.size();i++){
					arr[i] = mapIds.get(i);
				}
				BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_TERRITORY_XUNZHANG,
						arr);
			}
		}
	}
}
