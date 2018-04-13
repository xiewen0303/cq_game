package com.junyou.bus.kuafuarena1v1.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Tuple;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.kuafuarena1v1.configure.export.GeRenPaiHangConfig;
import com.junyou.bus.kuafuarena1v1.configure.export.GeRenPaiHangConfigExportService;
import com.junyou.bus.kuafuarena1v1.configure.export.GeRenPaiMingJiangLiConfig;
import com.junyou.bus.kuafuarena1v1.configure.export.GeRenPaiMingJiangLiConfigExportService;
import com.junyou.bus.kuafuarena1v1.configure.export.GongxunDuihuanConfig;
import com.junyou.bus.kuafuarena1v1.configure.export.GongxunDuihuanConfigExportService;
import com.junyou.bus.kuafuarena1v1.constants.KuafuArena1v1Constants;
import com.junyou.bus.kuafuarena1v1.dao.RoleGongxunDuihuanInfoDao;
import com.junyou.bus.kuafuarena1v1.dao.RoleKuafuArena1v1Dao;
import com.junyou.bus.kuafuarena1v1.entity.RoleGongxunDuihuanInfo;
import com.junyou.bus.kuafuarena1v1.entity.RoleKuafuArena1v1;
import com.junyou.bus.kuafuarena1v1.util.KuafuArena1v1Rank;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.shenmo.entity.RoleKuafuArena4v4;
import com.junyou.bus.shenmo.service.KuafuArena4v4SourceService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.GongxunDuihuanLogEvent;
import com.junyou.event.KuafuArena1v1LogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuArena1v1PublicConfig;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.kuafumatch.manager.KuafuMatchMsgSender;
import com.junyou.kuafumatch.util.KuafuCompetitionMatchServerUtil;
import com.junyou.kuafumatch.util.KuafuMatchServerUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class KuafuArena1v1SourceService {
	@Autowired
	private RoleKuafuArena1v1Dao roleKuafuArena1v1Dao;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GeRenPaiHangConfigExportService geRenPaiHangConfigExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private GongxunDuihuanConfigExportService gongxunDuihuanConfigExportService;
	@Autowired
	private RoleGongxunDuihuanInfoDao roleGongxunDuihuanInfoDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private RoleBusinessInfoExportService businessInfoExportService;

	@Autowired
	private KuafuArena4v4SourceService kuafuArena4v4SourceService;

	@Autowired
	private GeRenPaiMingJiangLiConfigExportService geRenPaiMingJiangLiConfigExportService;

	public KuafuArena1v1PublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_KUAFU_ARENA_1v1);
	}

	public List<RoleKuafuArena1v1> initRoleKuafuArena1v1(Long userRoleId) {
		statusMap.remove(userRoleId);
		return roleKuafuArena1v1Dao.initRoleKuafuArena1v1(userRoleId);
	}

	public void offlineHandle(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null) {
			if (status.intValue() == KuafuArena1v1Constants.STATUS_1) {
				scheduleExportService.cancelSchedule(userRoleId.toString(),
						GameConstants.COMPONENT_KUAFU_ARENA_MATCH_TIME_OUT);
			}
			if (status.intValue() == KuafuArena1v1Constants.STATUS_2) {
				scheduleExportService.cancelSchedule(userRoleId.toString(),
						GameConstants.COMPONENT_KUAFU_ARENA_PREPARE_TIME_OUT);
			}
		}
	}

	public RoleKuafuArena1v1 getRoleKuafuArena1v1(Long userRoleId,
			boolean online) {
		RoleKuafuArena1v1 ret = null;
		if (online) {
			ret = roleKuafuArena1v1Dao.cacheLoad(userRoleId, userRoleId);
		} else {
			ret = roleKuafuArena1v1Dao.load(userRoleId, userRoleId,
					AccessType.getDirectDbType());
		}
		if (ret != null) {
			if (!DatetimeUtil.dayIsToday(ret.getLastArenaTime(),
					GameSystemTime.getSystemMillTime())) {
				long today00Time = DatetimeUtil.getDate00Time();
				long yestoday00Time = today00Time - 24 * 60 * 60 * 1000L;
				if (ret.getLastArenaTime() >= yestoday00Time
						&& ret.getLastArenaTime() < today00Time) {
					int jifen = ret.getJifen();
					int duan = geRenPaiHangConfigExportService
							.getDuanByJifen(jifen);
					GeRenPaiHangConfig config = geRenPaiHangConfigExportService
							.loadById(duan);
					ret.setLastDuan(duan);
					ret.setLastArenaTimes(ret.getArenaTimes());
					if (config.getJiangcs() <= ret.getArenaTimes()) {
						ret.setGongxunStatus(KuafuArena1v1Constants.GONGXUN_STATUS_1);
					} else {
						ret.setGongxunStatus(KuafuArena1v1Constants.GONGXUN_STATUS_0);
					}
				} else {
					// 不是昨天，是昨天之前的数据
					ret.setLastArenaTimes(0);
					ret.setGongxunStatus(KuafuArena1v1Constants.GONGXUN_STATUS_0);
				}
				ret.setArenaTimes(0);
				ret.setLastArenaTime(GameSystemTime.getSystemMillTime());
				ret.setWinTimes(0);
				if (online) {
					roleKuafuArena1v1Dao.cacheUpdate(ret, userRoleId);
				} else {
					roleKuafuArena1v1Dao.update(ret, userRoleId,
							AccessType.getDirectDbType());
				}
			}
		}
		return ret;
	}

	public void createRoleKuafuArena1v1(Long userRoleId) {
		RoleKuafuArena1v1 info = new RoleKuafuArena1v1();
		info.setUserRoleId(userRoleId);
		info.setGongxun(0);
		info.setWinTimes(0);
		info.setLianWin(0);
		info.setLianLose(0);
		info.setArenaTimes(0);
		info.setLastArenaTime(GameSystemTime.getSystemMillTime());
		info.setLastArenaTimes(0);
		info.setLastDuan(1);
		info.setJifen(getPublicConfig().getInitJifen());
		info.setJifenUpdateTime(GameSystemTime.getSystemMillTime());
		info.setGongxunStatus(KuafuArena1v1Constants.GONGXUN_STATUS_0);
		roleKuafuArena1v1Dao.cacheInsert(info, userRoleId);
		updateJifenRank(true, info, 0);
	}

	/**
	 * 获得
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getKuafuArenaMyRankInfo(Long userRoleId) {
		RoleKuafuArena1v1 info1v1 = getRoleKuafuArena1v1(userRoleId, true);
		if (info1v1 == null) {
			createRoleKuafuArena1v1(userRoleId);
			info1v1 = getRoleKuafuArena1v1(userRoleId, true);
		}
		int gongxun = info1v1.getGongxun();
		int jifen1v1 = info1v1.getJifen();
		int rank1v1 = -1;
		Redis redis = GameServerContext.getRedis();
		if (redis != null) {
			Long rankL = redis.zrevrank(RedisKey.KUAFU_ARENA_1V1_RANK,
					userRoleId.toString());
			if (rankL != null) {
				rank1v1 = rankL.intValue() + 1;
			}
		}
		RoleKuafuArena4v4 info4v4 = kuafuArena4v4SourceService
				.getRoleKuafuArena4v4(userRoleId, true);
		if (info4v4 == null) {
			kuafuArena4v4SourceService.createRoleKuafuArena4v4(userRoleId);
			info4v4 = kuafuArena4v4SourceService.getRoleKuafuArena4v4(
					userRoleId, true);
		}
		int jifen4v4 = info4v4.getJifen();
		int rank4v4 = -1;
		if (redis != null) {
			Long rankL = redis.zrevrank(RedisKey.KUAFU_ARENA_4V4_RANK,
					userRoleId.toString());
			if (rankL != null) {
				rank4v4 = rankL.intValue() + 1;
			}
		}
		Object[] ret = new Object[] {
				new Object[] { new Object[] { 1, jifen1v1, rank1v1 },
						new Object[] { 2, jifen4v4, rank4v4 } }, gongxun };
		return ret;
	}

	public Object[] getKuafuArena1v1Info(Long userRoleId) {
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena1v1(userRoleId);
			info = getRoleKuafuArena1v1(userRoleId, true);
		}
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		long qingJifenTime = publicConfig.getQingjifenTime();
		long currentTime = GameSystemTime.getSystemMillTime();
		long jobTime = 0L;
		if (currentTime < qingJifenTime) {
			jobTime = qingJifenTime;
		} else {
			for (int i = 1; i < 10000; i++) {
				long time = qingJifenTime + i * 14 * 24 * 60 * 60 * 1000L;
				if (time > currentTime) {
					jobTime = time;
					break;
				}
			}
		}
		Object[] ret = new Object[] { AppErrorCode.SUCCESS,
				info.getArenaTimes(), info.getWinTimes(), jobTime,
				info.getJifen(), info.getGongxun() };
		return ret;
	}

	private static Map<Long, Integer> statusMap = new ConcurrentHashMap<Long, Integer>();

	public static boolean isInKuafuArena(Long userRoleId) {
		return statusMap.get(userRoleId) != null;
	}

	public static boolean isInMatch(Long userRoleId) {
		return statusMap.get(userRoleId) != null
				&& statusMap.get(userRoleId).intValue() == KuafuArena1v1Constants.STATUS_1;
	}

	public boolean isRoleInCompetition(Long userRoleId) {
		try {
			if(!PlatformConstants.isYueNan()){
				return false;
			}
			Redis redis = GameServerContext.getRedis();
			if (redis == null) {
				return false;
			}
			RoleWrapper roleWrapper = roleExportService
					.getLoginRole(userRoleId);
			if (roleWrapper == null) {
				return false;
			}
			String serverId = roleWrapper.getServerId();
			String userId = roleWrapper.getUserId();

			String flag = redis.hget(RedisKey.KUAFU_COMPETITION_ROLES_1V1, serverId
					+ "_" + userId);
			if (flag != null && flag.equals("1")) {
				return true;
			}
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		return false;
	}

	public boolean isInCompetitionTime() {
		try {
			if(!PlatformConstants.isYueNan()){
				return false;
			}
			Redis redis = GameServerContext.getRedis();
			if (redis == null) {
				return false;
			}
			List<String> list = redis.lrange(RedisKey.KUAFU_COMPETITION_TIME_1V1,
					0, -1);
			if (list == null || list.size() == 0) {
				return false;
			}
			long currentTime = GameSystemTime.getSystemMillTime();
			for (String e : list) {
				String[] timeZone = e.split("_");
				Long s1 = Long.parseLong(timeZone[0]);
				Long s2 = Long.parseLong(timeZone[1]);
				if (currentTime >= s1 && currentTime <= s2) {
					return true;
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		return false;
	}

	public Object[] kuafuArenaMatch1v1(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null) {
			ChuanQiLog.error("userRoleId={} match status={}", userRoleId,
					status);
			if (status.intValue() == KuafuArena1v1Constants.STATUS_1) {
				return AppErrorCode.KUAFU_ARENA_1V1_MATCH_ING;
			} else if (status.intValue() == KuafuArena1v1Constants.STATUS_2) {
				return AppErrorCode.KUAFU_ARENA_1V1_PREPARE_TING;
			} else if (status.intValue() == KuafuArena1v1Constants.STATUS_3) {
				return AppErrorCode.KUAFU_ARENA_1V1_PK_ING;
			} else if (status.intValue() == KuafuArena1v1Constants.STATUS_4) {
				return AppErrorCode.KUAFU_ARENA_EXIT_CURRENT;
			} else {
				return AppErrorCode.ERR;
			}
		}
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		// 检测开放等级
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper.getLevel() < publicConfig.getOpenLevel()) {
			return AppErrorCode.KUAFU_ARENA_1V1_LEVEL_LIMIT;
		}
		if (!isInOpenTime(publicConfig.getOpenBeginTime(),
				publicConfig.getOpenEndTime())) {
			return AppErrorCode.KUAFU_ARENA_1V1_NOT_OPEN_TIME;
		}
		// 是否已经在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		boolean competition = false;
		if(isRoleInCompetition(userRoleId) && isInCompetitionTime()){
			competition =true;
			if (!KuafuCompetitionMatchServerUtil.isMatchServerAvailable()) {
				return AppErrorCode.MATCH_SERVER_NOT_CONNECT;// 跨服连接失败
			}
		}else{
			if (!KuafuMatchServerUtil.isMatchServerAvailable()) {
				return AppErrorCode.MATCH_SERVER_NOT_CONNECT;// 跨服连接失败
			}
		}
		if (!KuafuConfigUtil.isKuafuAvailable()) {
			return AppErrorCode.KUAFU_NO_CONNECTION;// 跨服连接失败
		}
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena1v1(userRoleId);
			info = getRoleKuafuArena1v1(userRoleId, true);
		}
		String serverId = ChuanQiConfigUtil.getServerId();
		Object[] data = new Object[] { serverId, info.getJifen(),info.getLianWin(), info.getLianLose(), };
		if(competition){
			KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(userRoleId,InnerCmdType.KUAFU_ARENA_1V1_MATCH, data);
		}else{
			KuafuMatchMsgSender.send2KuafuMatchServer(userRoleId,InnerCmdType.KUAFU_ARENA_1V1_MATCH, data);
		}
		statusMap.put(userRoleId, KuafuArena1v1Constants.STATUS_1);

		BusTokenRunable runable = new BusTokenRunable(userRoleId,InnerCmdType.KUAFU_ARENA_1V1_MATCH_TIME_OUT, null);
		scheduleExportService.schedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_MATCH_TIME_OUT, runable,
				publicConfig.getMatchJie1() + publicConfig.getMatchJie2()
						+ publicConfig.getMatchJie3(), TimeUnit.SECONDS);
		ChuanQiLog.info("userRoleId={} request match", userRoleId);
		stageControllExportService.changeFuben(userRoleId, true);
		return AppErrorCode.OK;
	}

	private static boolean isInOpenTime(String openBeginTimeStr,
			String openEndTimeStr) {
		// 检测开放时间
		long currentTime = GameSystemTime.getSystemMillTime();
		String[] openBeginTimeArray = openBeginTimeStr.split(":");
		Long openBeginTime = DatetimeUtil.getCalcTimeCurTime(currentTime,
				Integer.parseInt(openBeginTimeArray[0]),
				Integer.parseInt(openBeginTimeArray[1]),
				Integer.parseInt(openBeginTimeArray[2]));
		String[] openEndTimeArray = openEndTimeStr.split(":");
		Long openEndTime = DatetimeUtil.getCalcTimeCurTime(currentTime,
				Integer.parseInt(openEndTimeArray[0]),
				Integer.parseInt(openEndTimeArray[1]),
				Integer.parseInt(openEndTimeArray[2]));
		return currentTime > openBeginTime && currentTime < openEndTime;
	}

	public void matchFail(Long userRoleId, Integer reason) {
		Integer status = statusMap.remove(userRoleId);
		if (status != null
				&& status.intValue() == KuafuArena1v1Constants.STATUS_1) {
			if (reason.intValue() == KuafuArena1v1Constants.MATCH_FAIL_REASON_2) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_1V1_MATCH_FAIL, null);
			}
			scheduleExportService.cancelSchedule(userRoleId.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_MATCH_TIME_OUT);
			stageControllExportService.changeFuben(userRoleId, false);
			ChuanQiLog.info("userRoleId={} match fail,reason={}", userRoleId,
					reason);
		}
	}

	public void matchTimeOut(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null
				&& status.intValue() == KuafuArena1v1Constants.STATUS_1) {
			statusMap.remove(userRoleId);
			stageControllExportService.changeFuben(userRoleId, false);
			ChuanQiLog.info("userRoleId={} match time out", userRoleId);
		}
	}

	public void matchSuccess(Long userRoleId, String serverId, long matchId) {
		scheduleExportService.cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_MATCH_TIME_OUT);
		Integer status = statusMap.get(userRoleId);
		if (status == null
				|| status.intValue() != KuafuArena1v1Constants.STATUS_1) {
			ChuanQiLog.error("userRoleId={} match success,status={} error",
					userRoleId, status);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("userRoleId={} match success, redis is null",
					userRoleId);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			return;
		}
		String key = RedisKey.buildKMatchId(matchId);
		String membersStr = redis.get(key);
		if (membersStr == null) {
			ChuanQiLog.error("userRoleId={} match success membersStr is null",
					userRoleId);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			return;
		}
		String[] membersArray = membersStr.split(":");
		boolean flag = false;
		for (String e : membersArray) {
			if (e.equals(userRoleId.toString())) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			ChuanQiLog.error("userRoleId={} match success is not in members",
					userRoleId);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			return;
		}
		// 通知前端匹配成功，开始倒计时
		BusMsgSender.send2One(userRoleId,
				ClientCmdType.KUAFU_ARENA_1V1_MATCH_SUCCESS, null);
		// 为玩家绑定跨服
		KuafuServerInfo serverInfo = KuafuServerInfoManager.getInstance()
				.getKuafuServerInfo(serverId);
		if (serverInfo == null) {
			serverInfo = KuafuServerInfoManager.getInstance()
					.getKuafuServerInfo(serverId, redis);
			if (serverInfo == null) {
				return;
			}
		}
		// 校验连接是否可用
		KuafuNetTunnel tunnel = KuafuConfigUtil.getConnection(serverInfo);
		if (tunnel == null || !tunnel.isConnected()) {
			KuafuConfigUtil.returnBrokenConnection(tunnel);
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_1V1_MATCH,
					AppErrorCode.KUAFU_NO_CONNECTION);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			String errorKey = RedisKey.buildKuafuServerErrorKey(serverId);
			redis.hset(errorKey, ChuanQiConfigUtil.getServerId(),
					String.valueOf(1));
			Map<String, String> timesMap = redis.hgetAll(errorKey);
			if (timesMap != null
					&& timesMap.size() >= KuafuConfigUtil.CAN_NOT_CONNECT_CLEAN_TIMES) {
				redis.zrem(RedisKey.KUAFU_SERVER_LIST_KEY, serverId);
				redis.hset(RedisKey.KUAFU_DELETE_SERVER_LIST, serverId,
						DatetimeUtil.formatTime(
								GameSystemTime.getSystemMillTime(),
								DatetimeUtil.FORMART3));
				ChuanQiLog.error("跨服服务器与源服连接不通超过{}个，将被从跨服候选列表中删除，id:{}",
						KuafuConfigUtil.CAN_NOT_CONNECT_CLEAN_TIMES, serverId);
			}
			return;
		} else {
			redis.del(RedisKey.buildKuafuServerErrorKey(serverId));
		}
		KuafuConfigUtil.returnConnection(tunnel);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId, serverInfo);

		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if (ObjectUtil.strIsEmpty(stageId)) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null || stage.isCopy()) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		// 向跨服发送绑定RoleId serverId
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] {
						ChuanQiConfigUtil.getServerId(), userRoleId });

		Object roleData = RoleFactory.createKuaFuRoleData(role);
		int jifen = getRoleKuafuArena1v1(userRoleId, true).getJifen();
		KuafuMsgSender.send2KuafuServer(
				GameConstants.DEFAULT_ROLE_ID,
				userRoleId,
				InnerCmdType.KUAFU_ARENA_1V1_SEND_ROLE_DATA,
				new Object[] {
						new Object[] { roleData, userRoleId, role.getName() },
						matchId, jifen });
		statusMap.put(userRoleId, KuafuArena1v1Constants.STATUS_2);

		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();

		BusTokenRunable runable = new BusTokenRunable(userRoleId,
				InnerCmdType.KUAFU_ARENA_1V1_PREPARE_TIME_OUT, null);
		scheduleExportService.schedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_PREPARE_TIME_OUT, runable,
				publicConfig.getDaojishi1() + publicConfig.getDaojishi2() + 5,
				TimeUnit.SECONDS);

		ChuanQiLog.info(
				"userRoleId={} match sucess matchId={} kuafu serverId={}",
				new Object[] { userRoleId, matchId, serverId });
	}

	public void kuafuArenaCancelMatch1v1(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null&& status.intValue() == KuafuArena1v1Constants.STATUS_1) {
			if(isRoleInCompetition(userRoleId) && isInCompetitionTime()){
				KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(userRoleId,InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH, null);
			}else{
				KuafuMatchMsgSender.send2KuafuMatchServer(userRoleId,InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH, null);
			}
		} else {
				BusMsgSender.send2One(userRoleId,ClientCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH,AppErrorCode.KUAFU_ARENA_1V1_CANCEL_MATCH_FAIL);
		}
	}

	public void kuafuArenaCancelMatch1v1Success(Long userRoleId) {
		statusMap.remove(userRoleId);
		stageControllExportService.changeFuben(userRoleId, false);
		ChuanQiLog.info("userRoleId = {} cancel match success", userRoleId);
	}

	public void prepareTimeOut(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null
				&& status.intValue() == KuafuArena1v1Constants.STATUS_2) {
			statusMap.remove(userRoleId);
			BusMsgSender.send2BusInner(userRoleId,
					InnerCmdType.S_APPLY_LEAVE_STAGE, null);
			stageControllExportService.changeFuben(userRoleId, false);
			KuafuManager.removeKuafu(userRoleId);
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_1V1_MATCH,
					AppErrorCode.KUAFU_ARENA_1V1_ENTER_FAIL);
			ChuanQiLog.error("userRoleId={} prepare time out, reset status",
					userRoleId);
			KuafuRoleServerManager.getInstance().removeBind(userRoleId);
		}
	}

	public void enterXiaoheiwu(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status == null
				|| status.intValue() != KuafuArena1v1Constants.STATUS_2) {
			ChuanQiLog.error("enterXiaoheiwu status ={} error", status);
			return;
		}
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		KuafuManager.startKuafu(userRoleId);
		// 标记当前为副本状态
		stageControllExportService.changeFuben(userRoleId, true);
		// 发送到场景控制中心进入小黑屋地图，以便从跨服服务器出来时走完整流程
		DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
		int[] birthXy = config.getRandomBirth();
		Object[] applyEnterData = new Object[] { config.getId(), birthXy[0],
				birthXy[1], MapType.KUAFU_SAFE_MAP };
		// 传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setChanging(true);
		StageMsgSender.send2StageControl(userRoleId,
				InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		ChuanQiLog.info("userRoleId={} enter xiaoheiwu", userRoleId);
	}

	public void start(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null
				&& status.intValue() == KuafuArena1v1Constants.STATUS_2) {
			statusMap.put(userRoleId, KuafuArena1v1Constants.STATUS_3);
			scheduleExportService.cancelSchedule(userRoleId.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_PREPARE_TIME_OUT);
			ChuanQiLog.info("userRoleId={} pk start", userRoleId);
		}
	}

	public void updateJifenRank(boolean online, RoleKuafuArena1v1 info,
			int beforeJifen) {
		Long userRoleId = info.getUserRoleId();
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("userRoleId ={} updateJifenRank redis is null",
					userRoleId);
			return;
		}
		int minRankJifen = getPublicConfig().getPaijifen();
		if (info.getJifen() >= minRankJifen) {
			redis.zadd(RedisKey.KUAFU_ARENA_1V1_RANK, info.getJifen(),
					userRoleId.toString());
			// 更新其他信息
			String key = RedisKey.buildkuafuArena1v1RankRoleKey(userRoleId);
			if (!online) {
				// 玩家有可能不在线，此时我们就不更新了
				return;
			}
			RoleWrapper roleWrapper = roleExportService
					.getLoginRole(userRoleId);
			redis.hset(key, KuafuArena1v1Rank.FIELD_USER_ROLE_ID,
					userRoleId.toString());

			redis.hset(key, KuafuArena1v1Rank.FIELD_CONFIG_ID, roleWrapper
					.getConfigId().toString());

			redis.hset(key, KuafuArena1v1Rank.FIELD_NAME, roleWrapper.getName());

			redis.hset(key, KuafuArena1v1Rank.FIELD_LEVEL, roleWrapper
					.getLevel().toString());

			String guildName = guildExportService.getGuildName(userRoleId);
			guildName = guildName == null ? "" : guildName;
			redis.hset(key, KuafuArena1v1Rank.FIELD_GUILD_NAME, guildName);

			Integer chibangLevel = 0;
			ChiBangInfo chibangInfo = chiBangExportService
					.getChiBangInfo(userRoleId);
			if (chibangInfo != null) {
				chibangLevel = chibangInfo.getChibangLevel();
			}
			redis.hset(key, KuafuArena1v1Rank.FIELD_CHIBANG_ID,
					chibangLevel.toString());

			Integer zuoqiLevel = 0;
			ZuoQiInfo zuoQiInfo = zuoQiExportService.getZuoQiInfo(userRoleId);
			if (zuoQiInfo != null) {
				zuoqiLevel = zuoQiInfo.getZuoqiLevel();
			}
			redis.hset(key, KuafuArena1v1Rank.FIELD_ZUOQI_ID,
					zuoqiLevel.toString());
			Long zplus = 0L;
			if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
				RoleBusinessInfoWrapper wrapper = businessInfoExportService
						.getRoleBusinessInfoWrapper(userRoleId);
				if (wrapper != null) {
					zplus = wrapper.getCurFighter();
				}
			} else {
				RoleBusinessInfoWrapper wrapper = businessInfoExportService
						.getRoleBusinessInfoForDB(userRoleId);
				if (wrapper != null) {
					zplus = wrapper.getCurFighter();
				}
			}
			redis.hset(key, KuafuArena1v1Rank.FIELD_ZPLUS, zplus.toString());
			ChuanQiLog.info("userRoleId={} in kuafu arena rank jifen={}",
					userRoleId, info.getJifen());
		} else {
			// 判断本来是否上榜
			if (beforeJifen >= minRankJifen) {
				// 之前是上榜的状态，删掉个人信息
				String key = RedisKey.buildkuafuArena1v1RankRoleKey(userRoleId);
				redis.del(key);
			}
			ChuanQiLog.info("userRoleId={} out kuafu arena rank", userRoleId);
		}
	}

	public void kuafuArenaCalcResult(Object[] data) {
		Long userRoleId = LongUtils.obj2long(data[0]);
		boolean online = publicRoleStateExportService
				.isPublicOnline(userRoleId);
		Integer status = statusMap.get(userRoleId);
		if (status != null
				&& status.intValue() == KuafuArena1v1Constants.STATUS_3) {
			if (online) {
				statusMap.put(userRoleId, KuafuArena1v1Constants.STATUS_4);
			} else {
				statusMap.remove(userRoleId);
			}
		} else if (status != null
				&& status.intValue() == KuafuArena1v1Constants.STATUS_2) {
			if (online) {
				statusMap.put(userRoleId, KuafuArena1v1Constants.STATUS_4);
			} else {
				statusMap.remove(userRoleId);
			}
			stageControllExportService.changeFuben(userRoleId, false);
			scheduleExportService.cancelSchedule(userRoleId.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_PREPARE_TIME_OUT);
		} else {
			ChuanQiLog.error("userRoleId={} status error is {}", userRoleId,
					status);
		}
		Boolean result = (Boolean) data[1];
		String otherName = (String) data[2];
		Integer otherJifen = (Integer) data[3];
		Integer useTime = (Integer) data[4];
		Long matchId = LongUtils.obj2long(data[5]);
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, online);
		int myJifen = info.getJifen();
		int myAfterJifen = myJifen;
		int gongxunChange = 0;
		if (result
				&& info.getWinTimes() < getPublicConfig().getDayGongxunTimes()) {
			int duan = geRenPaiHangConfigExportService.getDuanByJifen(myJifen);
			gongxunChange = geRenPaiHangConfigExportService.loadById(duan)
					.getWingx();
			info.setGongxun(info.getGongxun() + gongxunChange);
		}
		if (result) {
			myAfterJifen = calcJifen(myJifen, otherJifen, true,
					getPublicConfig().getJifenP());
			int otherAfterJifen = calcJifen(otherJifen, myJifen, false,
					getPublicConfig().getJifenP());
			if (online) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_1V1_RESULT, new Object[] { 1,
								myAfterJifen, myAfterJifen - myJifen,
								gongxunChange, otherName, otherAfterJifen,
								otherAfterJifen - otherJifen, useTime });
			}
			info.setWinTimes(info.getWinTimes() + 1);
			info.setLianWin(info.getLianWin() + 1);
			info.setLianLose(0);
		} else {
			myAfterJifen = calcJifen(myJifen, otherJifen, false,
					getPublicConfig().getJifenP());
			int otherAfterJifen = calcJifen(otherJifen, myJifen, true,
					getPublicConfig().getJifenP());
			if (online) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_1V1_RESULT, new Object[] { 0,
								myAfterJifen, myAfterJifen - myJifen,
								gongxunChange, otherName, otherAfterJifen,
								otherAfterJifen - otherJifen, useTime });
			}
			info.setLianLose(info.getLianLose() + 1);
			info.setLianWin(0);
		}
		info.setArenaTimes(info.getArenaTimes() + 1);
		info.setJifen(myAfterJifen);
		info.setJifenUpdateTime(GameSystemTime.getSystemMillTime());
		String myName = null;
		if (online) {
			roleKuafuArena1v1Dao.cacheUpdate(info, userRoleId);
			try {
				myName = roleExportService.getLoginRole(userRoleId).getName();
			} catch (Exception ex) {
				ChuanQiLog.error("error exit when get role name userRoleId={}",
						userRoleId);
			}
		} else {
			roleKuafuArena1v1Dao.update(info, userRoleId,
					AccessType.getDirectDbType());
			try {
				myName = roleExportService.getUserRoleFromDb(userRoleId)
						.getName();
			} catch (Exception ex) {
				ChuanQiLog.error("error exit when get role name userRoleId={}",
						userRoleId);
			}
		}
		updateJifenRank(online, info, myJifen);
		ChuanQiLog.info("userRoleId={} result={} in matchId={}", new Object[] {
				userRoleId, result, matchId });
		GamePublishEvent.publishEvent(new KuafuArena1v1LogEvent(userRoleId,
				myName, myJifen, myAfterJifen, otherName));
	}

	private int calcJifen(int myJifen, int otherJifen, boolean win, int jifenP) {
		int myDuan = geRenPaiHangConfigExportService.getDuanByJifen(myJifen);
		GeRenPaiHangConfig config = geRenPaiHangConfigExportService
				.loadById(myDuan);
		int otherDuan = geRenPaiHangConfigExportService
				.getDuanByJifen(otherJifen);
		if (myDuan == otherDuan) {
			if (win) {
				return Math.max(myJifen + config.getWinjf(), 0);
			} else {
				return Math.max(myJifen - config.getLosejf(), 0);
			}
		} else if (myDuan > otherDuan) {
			if (win) {
				return Math.max(myJifen + config.getWinjf()
						- (myDuan - otherDuan) * jifenP, 0);
			} else {
				return Math.max(myJifen
						- (config.getLosejf() + (myDuan - otherDuan) * jifenP),
						0);
			}
		} else if (myDuan < otherDuan) {
			if (win) {
				return Math.max(myJifen + config.getWinjf()
						+ (otherDuan - myDuan) * jifenP, 0);
			} else {
				return Math.max(myJifen
						- (config.getLosejf() - (otherDuan - myDuan) * jifenP),
						0);
			}
		}
		RuntimeException e = new RuntimeException("duanwei error");
		ChuanQiLog.error("duanwei error ", e);
		throw e;
	}

	public void exit(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null
				&& (status.intValue() == KuafuArena1v1Constants.STATUS_3 || status
						.intValue() == KuafuArena1v1Constants.STATUS_4)) {
			KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId,
					InnerCmdType.KUAFU_ARENA_1V1_EXIT_FB_TO_KUAFU, null);
		} else {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_1V1_EXIT,
					AppErrorCode.KUAFU_ARENA_1V1_CAN_NOT_EXIT);
		}
	}

	public void leaveFb(Long userRoleId) {
		statusMap.remove(userRoleId);
		KuafuManager.removeKuafu(userRoleId);
		BusMsgSender.send2BusInner(userRoleId,
				InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		stageControllExportService.changeFuben(userRoleId, false);
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
	}

	public void enterStageFail(Long userRoleId) {
		statusMap.remove(userRoleId);
		scheduleExportService.cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_PREPARE_TIME_OUT);
		stageControllExportService.changeFuben(userRoleId, false);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_ARENA_1V1_MATCH,
				AppErrorCode.KUAFU_ARENA_1V1_ENTER_FAIL);
		ChuanQiLog.error("userRoleId={} enter stage fail, reset status",
				userRoleId);
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
	}

	/**
	 * 获取跨服1v1排行信息
	 * 
	 * @param userRoleId
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public Object[] getKuafuArena1v1Rank(Long userRoleId, int beginIndex,
			int endIndex) {
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena1v1(userRoleId);
			info = getRoleKuafuArena1v1(userRoleId, true);
		}
		Object[] ret = new Object[2];
		Object[] rankInfo = getRank(beginIndex, endIndex);
		ret[0] = rankInfo;
		ret[1] = (rankList == null ? 0 : rankList.size());
		return ret;
	}

	/**
	 * 获取跨服1v1排行信息
	 * 
	 * @param userRoleId
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public Object[] getKuafuArena1v1YesterdayInfo(Long userRoleId) {
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena1v1(userRoleId);
			info = getRoleKuafuArena1v1(userRoleId, true);
		}
		Object[] ret = new Object[4];
		ret[0] = info.getLastDuan();
		int duanGongxunReward = 0;
		//if (info.getGongxunStatus().intValue() == KuafuArena1v1Constants.GONGXUN_STATUS_1) {
			GeRenPaiHangConfig config = geRenPaiHangConfigExportService.loadById(info.getLastDuan());
			duanGongxunReward = config.getGongxun();
		//}
		ret[1] = duanGongxunReward;
		ret[2] = info.getLastArenaTimes();
		int gongxunStatus = info.getGongxunStatus();
		//if (gongxunStatus == KuafuArena1v1Constants.GONGXUN_STATUS_2) {
			//gongxunStatus = KuafuArena1v1Constants.GONGXUN_STATUS_0;
		//}
		ret[3] = gongxunStatus;
		return ret;
	}

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private List<KuafuArena1v1Rank> rankList = null;
	private long lastFetchTime = 0L;
	private long rankCacheTime = 60 * 60 * 100L;// 排行榜缓存时间（一小時）

	private Object[] getRank(int beginIndex, int endIndex) {
		long currentTime = GameSystemTime.getSystemMillTime();
		if (currentTime - lastFetchTime > rankCacheTime) {
			fetchRank();
		}
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);

			if (rankList != null) {
				int lastIndex = rankList.size() - 1;
				if (beginIndex <= lastIndex) {
					if (endIndex > lastIndex) {
						endIndex = lastIndex;
					}
					int size = endIndex - beginIndex;
					if (size >= 0) {
						Object[] ret = new Object[size + 1];
						List<KuafuArena1v1Rank> retList = rankList.subList(
								beginIndex, endIndex + 1);
						for (int i = 0; i < retList.size(); i++) {
							KuafuArena1v1Rank tmp = retList.get(i);
							ret[i] = new Object[] { tmp.getConfigId(),
									tmp.getUserRoleId(), tmp.getName(),
									tmp.getRank(), tmp.getGuildName(),
									tmp.getLevel(), tmp.getZplus(),
									tmp.getDuan(), tmp.getZuoqiId(),
									tmp.getChibangId() };
						}
						return ret;
					}
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		return new Object[] {};
	}

	private void fetchRank() {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("fetch kuafu arena rank error,redis is null");
			return;
		}
		try {
			List<KuafuArena1v1Rank> list = new ArrayList<KuafuArena1v1Rank>();
			KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
			Set<Tuple> tuples = redis.zrevrangeWithScore(
					RedisKey.KUAFU_ARENA_1V1_RANK, 0,
					publicConfig.getPaiming() - 1);
			int rank = 1;
			for (Tuple e : tuples) {
				int jifen = (int) e.getScore();
				String member = e.getElement();
				String key = RedisKey.KUAFU_ARENA_1V1_RANK_ROLE + member;
				Map<String, String> infoMap = redis.hgetAll(key);
				if (infoMap == null || infoMap.size() == 0) {
					redis.zrem(RedisKey.KUAFU_ARENA_1V1_RANK, member);
					continue;
				}
				String userRoleId = infoMap
						.get(KuafuArena1v1Rank.FIELD_USER_ROLE_ID);
				String configId = infoMap
						.get(KuafuArena1v1Rank.FIELD_CONFIG_ID);
				String name = infoMap.get(KuafuArena1v1Rank.FIELD_NAME);
				String guildName = infoMap
						.get(KuafuArena1v1Rank.FIELD_GUILD_NAME);
				String level = infoMap.get(KuafuArena1v1Rank.FIELD_LEVEL);
				String zplus = infoMap.get(KuafuArena1v1Rank.FIELD_ZPLUS);
				String zuoqiId = infoMap.get(KuafuArena1v1Rank.FIELD_ZUOQI_ID);
				String chibangId = infoMap
						.get(KuafuArena1v1Rank.FIELD_CHIBANG_ID);
				KuafuArena1v1Rank tmpRank = new KuafuArena1v1Rank();
				tmpRank.setUserRoleId(Long.valueOf(userRoleId));
				tmpRank.setConfigId(Integer.valueOf(configId));
				tmpRank.setName(name);
				tmpRank.setGuildName(guildName);
				tmpRank.setLevel(Integer.valueOf(level));
				tmpRank.setZplus(Long.valueOf(zplus));
				tmpRank.setZuoqiId(Integer.parseInt(zuoqiId));
				tmpRank.setChibangId(Integer.parseInt(chibangId));
				int duan = geRenPaiHangConfigExportService
						.getDuanByJifen(jifen);
				tmpRank.setDuan(duan);
				tmpRank.setRank(rank++);
				list.add(tmpRank);
			}
			lastFetchTime = GameSystemTime.getSystemMillTime();
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			rankList = list;
			ChuanQiLog.info("fetch kuafu arena rank finish");
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	/**
	 * 领取昨日满5场的奖励功勋
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getKuafuArena1v1PickReward(Long userRoleId) {
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena1v1(userRoleId);
			info = getRoleKuafuArena1v1(userRoleId, true);
		}
		if (info.getGongxunStatus().intValue() == KuafuArena1v1Constants.GONGXUN_STATUS_0) {
			return AppErrorCode.KUAFU_ARENA_CAN_NOT_PICK;
		} else if (info.getGongxunStatus().intValue() == KuafuArena1v1Constants.GONGXUN_STATUS_2) {
			return AppErrorCode.KUAFU_ARENA_CAN_PICKED;
		}
		int duan = info.getLastDuan();
		GeRenPaiHangConfig config = geRenPaiHangConfigExportService
				.loadById(duan);
		info.setGongxun(info.getGongxun() + config.getGongxun());
		info.setGongxunStatus(KuafuArena1v1Constants.GONGXUN_STATUS_2);
		roleKuafuArena1v1Dao.cacheUpdate(info, userRoleId);
		ChuanQiLog.info("userRoleId={} pick day gongxun reward gongxun={}",
				userRoleId, config.getGongxun());
		return AppErrorCode.OK;
	}

	public List<RoleGongxunDuihuanInfo> initRoleGongxunDuihuanInfo(
			Long userRoleId) {
		List<RoleGongxunDuihuanInfo> list = roleGongxunDuihuanInfoDao
				.initRoleGongxunDuihuanInfo(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list;
	}

	public void createRoleGongxunDuihuanInfo(Long userRoleId, Integer orderId,
			int num) {
		RoleGongxunDuihuanInfo info = new RoleGongxunDuihuanInfo();
		info.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		info.setOrderId(orderId);
		info.setNum(num);
		info.setUpdateTime(GameSystemTime.getSystemMillTime());
		info.setUserRoleId(userRoleId);
		roleGongxunDuihuanInfoDao.cacheInsert(info, userRoleId);
	}

	public RoleGongxunDuihuanInfo getRoleGongxunDuihuanInfo(Long userRoleId,
			final Integer orderId) {
		List<RoleGongxunDuihuanInfo> list = roleGongxunDuihuanInfoDao
				.cacheLoadAll(userRoleId,
						new IQueryFilter<RoleGongxunDuihuanInfo>() {
							private boolean stop = false;

							@Override
							public boolean check(RoleGongxunDuihuanInfo info) {
								if (info.getOrderId().equals(orderId)) {
									stop = true;
								}
								return stop;
							}

							@Override
							public boolean stopped() {
								return stop;
							}
						});
		if (list != null && list.size() > 0) {
			RoleGongxunDuihuanInfo ret = list.get(0);
			if (!DatetimeUtil.dayIsToday(ret.getUpdateTime(),
					GameSystemTime.getSystemMillTime())) {
				ret.setNum(0);
				ret.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleGongxunDuihuanInfoDao.cacheUpdate(ret, userRoleId);
			}
			return ret;
		}
		return null;
	}

	private List<RoleGongxunDuihuanInfo> getRoleGongxunDuihuanInfo(
			Long userRoleId) {
		List<RoleGongxunDuihuanInfo> list = roleGongxunDuihuanInfoDao
				.cacheLoadAll(userRoleId);
		if (list != null && list.size() > 0) {
			for (RoleGongxunDuihuanInfo e : list) {
				if (!DatetimeUtil.dayIsToday(e.getUpdateTime(),
						GameSystemTime.getSystemMillTime())) {
					e.setNum(0);
					e.setUpdateTime(GameSystemTime.getSystemMillTime());
					roleGongxunDuihuanInfoDao.cacheUpdate(e, userRoleId);
				}
			}
		}
		return list;
	}

	/**
	 * 请求兑换信息
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getKuafuArena1v1DuihuanInfo(Long userRoleId) {
		List<RoleGongxunDuihuanInfo> list = getRoleGongxunDuihuanInfo(userRoleId);
		if (list != null && list.size() > 0) {
			Object[] ret = new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				RoleGongxunDuihuanInfo info = list.get(i);
				ret[i] = new Object[] { info.getOrderId(), info.getNum() };
			}
			return ret;
		}
		return new Object[] {};
	}

	/**
	 * 功勋兑换道具
	 * 
	 * @param userRoleId
	 * @param order
	 * @return
	 */
	public Object[] kuafuArena1v1Duihuan(Long userRoleId, int order) {
		GongxunDuihuanConfig config = gongxunDuihuanConfigExportService
				.loadById(order);
		if (config == null) {
			return null;
		}
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena1v1(userRoleId);
			info = getRoleKuafuArena1v1(userRoleId, true);
		}
		// 校验段位
		int jifen = info.getJifen();
		int duan = geRenPaiHangConfigExportService.getDuanByJifen(jifen);
		if (duan < config.getNeeddw()) {
			return AppErrorCode.KUAFU_ARENA_DUAN_LIMIT;
		}
		// 校验功勋
		if (info.getGongxun() < config.getNeedgx()) {
			return AppErrorCode.KUAFU_ARENA_GONGXUN_LIMIT;
		}
		// 校验每日兑换次数
		RoleGongxunDuihuanInfo duihuanInfo = getRoleGongxunDuihuanInfo(
				userRoleId, order);
		if (duihuanInfo != null && duihuanInfo.getNum() >= config.getMaxcount()) {
			return AppErrorCode.KUAFU_ARENA_DAY_NUM_LIMIT;
		}
		String goodsId = config.getItem();
		// 校验背包
		Object[] checkResult = roleBagExportService.checkPutInBag(goodsId, 1,
				userRoleId);
		if (checkResult != null) {
			return checkResult;
		}
		info.setGongxun(info.getGongxun() - config.getNeedgx());
		roleKuafuArena1v1Dao.cacheUpdate(info, userRoleId);
		Map<String, GoodsConfigureVo> itemMap = new HashMap<String, GoodsConfigureVo>();
		itemMap.put(goodsId, new GoodsConfigureVo(goodsId, 1));
		// 获得新道具
		roleBagExportService.putInBagVo(itemMap, userRoleId,
				GoodsSource.KUAFU_ARENA_1V1_GONGXUN, true);
		if (duihuanInfo == null) {
			createRoleGongxunDuihuanInfo(userRoleId, order, 1);
		} else {
			duihuanInfo.setNum(duihuanInfo.getNum() + 1);
			roleGongxunDuihuanInfoDao.cacheUpdate(duihuanInfo, userRoleId);
		}
		ChuanQiLog.info("userRoleId={} gongxun duihuan orderId={}", userRoleId,
				order);
		String name = null;
		try {
			name = roleExportService.getLoginRole(userRoleId).getName();
		} catch (Exception e) {
		}
		// 兑换日志
		GamePublishEvent.publishEvent(new GongxunDuihuanLogEvent(userRoleId,
				name, goodsId, 1, config.getNeedgx()));
		return new Object[] { AppErrorCode.SUCCESS, info.getGongxun(), order };
	}

	/**
	 * 服务器启动的时候，初始化2周清的job
	 */
	public void startKuafuArenaCleanJob() {
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		long qingJifenTime = publicConfig.getQingjifenTime();
		long currentTime = GameSystemTime.getSystemMillTime();
		long jobTime = 0L;
		if (currentTime < qingJifenTime) {
			jobTime = qingJifenTime;
		} else {
			for (int i = 1; i < 10000; i++) {
				long time = qingJifenTime + i * 14 * 24 * 60 * 60 * 1000L;
				if (time > currentTime) {
					jobTime = time;
					break;
				}
			}
		}
		if (jobTime == 0L) {
			return;
		}
        BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_ARENA_1V1_2WEEK_JOB, null);
        scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.COMPONENT_KUAFU_ARENA_2WEEK_JOB, runable, (int) (jobTime - currentTime), TimeUnit.MILLISECONDS);
        ChuanQiLog.info("kuafu arena clean job start time={}", new Date(jobTime));
	}

	/**
     * 2周执行一次1v1奖励结算任务
     */
    public void cleanJob() {
        createRankRewardData();
        clearKuafuAreaData();
        // 启动根据排名奖励数据发奖的定时器
        BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_ARENA_1V1_SEND_EMIAL_REWARD, null);
        scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_ARENA_1V1_SEND_EMAIL_REWARD, runable, GameConstants.SEND_REWARD_TIME, TimeUnit.SECONDS);
        // 确保跨天，初始化下一个2周结算job
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            ChuanQiLog.error("", e);
        }
        try {
            startKuafuArenaCleanJob();
        } catch (Exception e) {
            ChuanQiLog.error("", e);
        }
    }
    
    /**
     * 2周执行一次,根据排名数据生成唯一的1v1排名奖励数据
     */
    private void createRankRewardData() {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("2week create 1v1 rank reward ,redis is null");
            return;
        }
        try {
            int minRank = 1;
            int maxRank = geRenPaiMingJiangLiConfigExportService.getMaxRank();
            Set<String> sets = redis.zrevrange(RedisKey.KUAFU_ARENA_1V1_RANK, minRank - 1, maxRank - 1);
            if (!ObjectUtil.isEmpty(sets)) {
                // 创建排名奖励数据
                int rank = 1;
                Map<String, String> _1v1RankReward = new HashMap<String, String>();
                for (String userRoleId : sets) {
                    ChuanQiLog.info("kuafu arena 1v1 rank reward userRolId={} rank={} ", userRoleId, rank);
                    _1v1RankReward.put(userRoleId, String.valueOf(rank));
                    rank++;
                }
                if(redis.exists(RedisKey.KUAFU_AREA_1V1_RANK_REWARD)){
                    ChuanQiLog.info("kuafu arena 1v1 rank reward exists!!!!");
                    redis.del(RedisKey.KUAFU_AREA_1V1_RANK_REWARD);
                }
                redis.hmset(RedisKey.KUAFU_AREA_1V1_RANK_REWARD, _1v1RankReward);
                // 清空排行数据
                cleanRedisRank();
            }
        } catch (Exception e) {
            ChuanQiLog.error("kuafu arena 1v1 create rank reward redis data by rank error , info={}" + e.getMessage());
        }
    }

    /**
     * 2周结算时,清理本轮活动业务数据
     */
    private void clearKuafuAreaData(){
        long beginTime = GameSystemTime.getSystemMillTime();
        // 先清在线玩家的内存信息
        List<Long> roleIds = publicRoleStateExportService.getAllOnlineRoleids();
        for (Long e : roleIds) {
            try {
                RoleKuafuArena1v1 info = getRoleKuafuArena1v1(e, true);
                if (info == null) {
                    continue;
                }
                info.setJifen(getPublicConfig().getInitJifen());
                info.setLianLose(0);
                info.setLianWin(0);
                roleKuafuArena1v1Dao.cacheUpdate(info, e);
            } catch (Exception ex) {
                ChuanQiLog.error("error exit when clean userRoleId=" + e, ex);
            }
        }
        try {
            // 再清库
            roleKuafuArena1v1Dao.cleanAllJifen(getPublicConfig().getInitJifen());
        } catch (Exception e) {
            ChuanQiLog.error("cleanAllJifen error", e);
        }
        ChuanQiLog.info("kuafu arena clean job execute finish cost time={}ms", GameSystemTime.getSystemMillTime() - beginTime);

        // 清除内存中的排行缓存
        try {
            lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
            rankList = null;
            lastFetchTime = GameSystemTime.getSystemMillTime();
        } catch (Exception e) {
            ChuanQiLog.error("", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
    
    /**
     * 2周执行一次,根据排名奖励数据发奖
     */
    public void sendEmialReward() {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("2week rank reward ,redis is null");
            return;
        }
        Map<String, String> _1v1RankReward = redis.hgetAll(RedisKey.KUAFU_AREA_1V1_RANK_REWARD);
        if (!ObjectUtil.isEmpty(_1v1RankReward)) {
            for (String roleId : _1v1RankReward.keySet()) {
                try {
                    Long userRoleId = Long.parseLong(roleId);
                    Integer rank = Integer.parseInt(_1v1RankReward.get(roleId));
                    RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
                    ChuanQiLog.info("kuafu arena 1v1 rank reward userRolId={} rank={} isBenfu={}", new Object[] { userRoleId, rank, roleWrapper != null });
                    if (roleWrapper != null) {
                        GeRenPaiMingJiangLiConfig config = geRenPaiMingJiangLiConfigExportService.loadByRank(rank);
                        // 以邮件形式发放奖励
                        if (sendSystemEamilForSingle2(userRoleId, rank, config)) {
                            redis.hdel(RedisKey.KUAFU_AREA_1V1_RANK_REWARD, roleId);
                        }
                    }
                } catch (Exception e) {
                    ChuanQiLog.error("kuafu arena 1v1 send rank email reward error , info={}" + e);
                }
            }
        }
    }
    
    
	/**
    public void clean() {
		long beginTime = GameSystemTime.getSystemMillTime();

		// 先清在线玩家的内存信息
		List<Long> roleIds = publicRoleStateExportService.getAllOnlineRoleids();
		for (Long e : roleIds) {
			try {
				RoleKuafuArena1v1 info = getRoleKuafuArena1v1(e, true);
				if (info == null) {
					continue;
				}
				info.setJifen(getPublicConfig().getInitJifen());
				info.setLianLose(0);
				info.setLianWin(0);
				roleKuafuArena1v1Dao.cacheUpdate(info, e);
			} catch (Exception ex) {
				ChuanQiLog.error("error exit when clean userRoleId=" + e, ex);
			}
		}
		try {
			// 再清库
			roleKuafuArena1v1Dao
					.cleanAllJifen(getPublicConfig().getInitJifen());
		} catch (Exception e) {
			ChuanQiLog.error("cleanAllJifen error", e);
		}
		ChuanQiLog.info("kuafu arena clean job execute finish cost time={}ms",
				GameSystemTime.getSystemMillTime() - beginTime);

		// 给本服上榜的人 发放奖励
		try {
			Redis redis = GameServerContext.getRedis();
			if (redis == null) {
				ChuanQiLog.error("2week rank reward ,redis is null");
			} else {
				int minRank = 1;
				int maxRank = geRenPaiMingJiangLiConfigExportService
						.getMaxRank();
				Set<String> sets = redis
						.zrevrange(RedisKey.KUAFU_ARENA_1V1_RANK, minRank - 1,
								maxRank - 1);
				
				
				if (sets != null && sets.size() > 0) {
					int rank = 1;
					for (String e : sets) {
						try {
							Long userRoleId = Long.valueOf(e);
							RoleWrapper roleWrapper = roleExportService
									.getUserRoleFromDb(userRoleId);
							ChuanQiLog
									.info("kuafu arena 1v1 rank reward userRolId={} rank={} isBenfu={}",
											new Object[] { userRoleId, rank,
													roleWrapper != null });
							if (roleWrapper != null) {
								GeRenPaiMingJiangLiConfig config = geRenPaiMingJiangLiConfigExportService
										.loadByRank(rank);
								// 以邮件形式发放奖励
								sendSystemEamilForSingle(userRoleId, rank,
										config);
							}
							rank++;
						} catch (Exception ex) {
							ChuanQiLog.error("rank reward error userRoleId="
									+ e, ex);
						}
					}
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		// 清除内存中的排行缓存
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			rankList = null;
			lastFetchTime = GameSystemTime.getSystemMillTime();
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		// 确保跨天，初始化下一个job
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		}
		try {
			startKuafuArenaCleanJob();
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		// 90秒之后清空redis排名
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.KUAFU_ARENA_1V1_CLEAN_REDIS_RANK, null);
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.KUAFU_ARENA_CLEAN_REDIS_RANK, runable,
				90 + new Random().nextInt(30), TimeUnit.SECONDS);
	}
    **/
    
	/**
	 * 两周清排行榜
	 */
    public void cleanRedisRank() {
        try {
            Redis redis = GameServerContext.getRedis();
            if (redis == null) {
                ChuanQiLog.error("clean redis rank ,redis is null");
            } else {
                String flag = redis.get(RedisKey.KUAFU_ARENA_1V1_CLEAN_FLAG);
                if (flag == null) {
                    redis.del(RedisKey.KUAFU_ARENA_1V1_RANK);
                    redis.setex(RedisKey.KUAFU_ARENA_1V1_CLEAN_FLAG, 60, "1");
                    ChuanQiLog.info("clean redis rank finish");
                } else {
                    ChuanQiLog.info("clean redis rank finish already");
                }
                Set<String> keys = redis.keys(RedisKey.KUAFU_ARENA_1V1_RANK_ROLE + "*");
                if (keys != null && keys.size() > 0) {
                    for (String e : keys) {
                        redis.del(e);
                    }
                    ChuanQiLog.info("clean redis rank user info size ={}", keys.size());
                }
            }
        } catch (Exception ex) {
            ChuanQiLog.error("clean redis rank error", ex);
        }
    }

	/**
	 * 两周排行榜的奖励邮件发放
	 * 
	 * @param userRoleId
	 * @param rank
	 * @param config
	 */
    /**
    private void sendSystemEamilForSingle(Long userRoleId, int rank, GeRenPaiMingJiangLiConfig config) {
        try {
            String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_ARENA_REWARD_MAIL_CODE, String.valueOf(rank));
            String[] attachment = EmailUtil.getAttachments(config.getJiangitem());
            for (String e : attachment) {
                emailExportService.sendEmailToOne(userRoleId, content, GameConstants.EMAIL_TYPE_SINGLE, e);
            }
        } catch (Exception e) {
            ChuanQiLog.error("kuafu arena 1v1 rank sendSystemEamilForSingle error");
        }
    }
    **/

    
    private boolean sendSystemEamilForSingle2(Long userRoleId, int rank, GeRenPaiMingJiangLiConfig config) {
        boolean flag = false;
        try {
        	String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_ARENA_REWARD_MAIL_CODE_TITLE);
            String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_ARENA_REWARD_MAIL_CODE, String.valueOf(rank));
            String[] attachment = EmailUtil.getAttachments(config.getJiangitem());
            for (String e : attachment) {
                emailExportService.sendEmailToOne(userRoleId, title,content, GameConstants.EMAIL_TYPE_SINGLE, e);
            }
            flag = true;
        } catch (Exception e) {
            ChuanQiLog.error("kuafu arena 1v1 rank sendSystemEamilForSingle error, errorMsg={}", e);
        }
        return flag;
    }

	/**
	 * 给http调用，防止异常清空
	 * 
	 * @param userRoleId
	 */
	public void cleanUserJifen(Long userRoleId) {
		boolean online = publicRoleStateExportService
				.isPublicOnline(userRoleId);
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, online);
		info.setJifen(0);
		info.setLianLose(0);
		info.setLianWin(0);
		if (online) {
			roleKuafuArena1v1Dao.cacheUpdate(info, userRoleId);
		} else {
			roleKuafuArena1v1Dao.update(info, userRoleId,
					AccessType.getDirectDbType());
		}
		ChuanQiLog.info("kuafu arena 1v1 clean userRoleId={}", userRoleId);
	}

	public void addGongxun(Long userRoleId, int gongxun) {
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena1v1(userRoleId);
			info = getRoleKuafuArena1v1(userRoleId, true);
		}
		if (gongxun > 0) {
			info.setGongxun(info.getGongxun() + gongxun);
			roleKuafuArena1v1Dao.cacheUpdate(info, userRoleId);
			ChuanQiLog
					.info("userRoleId={} add gongxun={}", userRoleId, gongxun);
		}
	}

	public int getGongxun(Long userRoleId) {
		RoleKuafuArena1v1 info = getRoleKuafuArena1v1(userRoleId, true);
		if (info == null) {
			return 0;
		} else {
			return info.getGongxun();
		}
	}
}
