package com.junyou.bus.shenmo.service;

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

import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.kuafuarena1v1.service.export.KuafuArena1v1SourceExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.shenmo.configure.ShenMoPaiHangConfig;
import com.junyou.bus.shenmo.configure.ShenMoPaiMingJiangLiConfig;
import com.junyou.bus.shenmo.configure.export.ShenMoPaiHangConfigExportService;
import com.junyou.bus.shenmo.configure.export.ShenMoPaiMingJiangLiConfigExportService;
import com.junyou.bus.shenmo.constants.ShenmoConstants;
import com.junyou.bus.shenmo.dao.RoleKuafuArena4v4Dao;
import com.junyou.bus.shenmo.entity.RoleKuafuArena4v4;
import com.junyou.bus.shenmo.util.KuafuArena4v4Rank;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.KuafuArena4v4LogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ShenMoPublicConfig;
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
import com.junyou.stage.entity.Team;
import com.junyou.stage.entity.TeamMember;
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

@Service
public class KuafuArena4v4SourceService {

	@Autowired
	private RoleKuafuArena4v4Dao roleKuafuArena4v4Dao;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
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
	private ShenMoPaiHangConfigExportService shenMoPaiHangConfigExportService;
	@Autowired
	private KuafuArena1v1SourceExportService kuafuArena1v1SourceExportService;

	@Autowired
	private ShenMoPaiMingJiangLiConfigExportService shenMoPaiMingJiangLiConfigExportService;

	public ShenMoPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_SHENMO);
	}

	public List<RoleKuafuArena4v4> initRoleKuafuArena4v4(Long userRoleId) {
		return roleKuafuArena4v4Dao.initRoleKuafuArena4v4(userRoleId);
	}

	public void offlineHandle(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null) {
			if (status.intValue() == ShenmoConstants.STATUS_1) {
				scheduleExportService.cancelSchedule(userRoleId.toString(),
						GameConstants.COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT);
			}
			if (status.intValue() == ShenmoConstants.STATUS_2) {
				scheduleExportService
						.cancelSchedule(
								userRoleId.toString(),
								GameConstants.COMPONENT_KUAFU_ARENA_4V4_PREPARE_TIME_OUT);
			}
			statusMap.remove(userRoleId);
		}
	}

	public RoleKuafuArena4v4 getRoleKuafuArena4v4(Long userRoleId,
			boolean online) {
		RoleKuafuArena4v4 ret = null;
		if (online) {
			ret = roleKuafuArena4v4Dao.cacheLoad(userRoleId, userRoleId);
		} else {
			ret = roleKuafuArena4v4Dao.load(userRoleId, userRoleId,
					AccessType.getDirectDbType());
		}
		if (ret != null) {
			if (!DatetimeUtil.dayIsToday(ret.getLastEscapeTime(),
					GameSystemTime.getSystemMillTime())) {
				ret.setLastEscapeTime(GameSystemTime.getSystemMillTime());
				ret.setEscapeTimes(0);
				if (online) {
					roleKuafuArena4v4Dao.cacheUpdate(ret, userRoleId);
				} else {
					roleKuafuArena4v4Dao.update(ret, userRoleId,
							AccessType.getDirectDbType());
				}
			}
			if (!DatetimeUtil.dayIsToday(ret.getLastArenaTime(),
					GameSystemTime.getSystemMillTime())) {
				long today00Time = DatetimeUtil.getDate00Time();
				long yestoday00Time = today00Time - 24 * 60 * 60 * 1000L;
				if (ret.getLastArenaTime() >= yestoday00Time
						&& ret.getLastArenaTime() < today00Time) {
					int jifen = ret.getJifen();
					int duan = shenMoPaiHangConfigExportService
							.getDuanByJifen(jifen);
					ShenMoPaiHangConfig duanConfig = shenMoPaiHangConfigExportService
							.loadById(duan);
					ret.setLastDuan(duan);
					ret.setLastArenaTimes(ret.getArenaTimes());
					if (duanConfig.getJiangcs() <= ret.getArenaTimes()) {
						ret.setGongxunStatus(ShenmoConstants.GONGXUN_STATUS_1);
					} else {
						ret.setGongxunStatus(ShenmoConstants.GONGXUN_STATUS_0);
					}
				} else {
					// 不是昨天，是昨天之前的数据
					ret.setLastArenaTimes(0);
					ret.setGongxunStatus(ShenmoConstants.GONGXUN_STATUS_0);
				}
				ret.setArenaTimes(0);
				ret.setLastArenaTime(GameSystemTime.getSystemMillTime());
				ret.setWinTimes(0);
				if (online) {
					roleKuafuArena4v4Dao.cacheUpdate(ret, userRoleId);
				} else {
					roleKuafuArena4v4Dao.update(ret, userRoleId,
							AccessType.getDirectDbType());
				}
			}
		}
		return ret;
	}

	public void createRoleKuafuArena4v4(Long userRoleId) {
		RoleKuafuArena4v4 info = new RoleKuafuArena4v4();
		info.setUserRoleId(userRoleId);
		info.setWinTimes(0);
		info.setArenaTimes(0);
		info.setLastArenaTime(GameSystemTime.getSystemMillTime());
		info.setLastArenaTimes(0);
		info.setLastEscapeTime(GameSystemTime.getSystemMillTime());
		info.setEscapeTimes(0);
		info.setLastDuan(1);
		info.setJifen(0);
		info.setJifenUpdateTime(GameSystemTime.getSystemMillTime());
		info.setGongxunStatus(ShenmoConstants.GONGXUN_STATUS_0);
		roleKuafuArena4v4Dao.cacheInsert(info, userRoleId);
		updateJifenRank(true, info, 0);
	}

	public Object[] getKuafuArena4v4Info(Long userRoleId) {
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena4v4(userRoleId);
			info = getRoleKuafuArena4v4(userRoleId, true);
		}
		ShenMoPublicConfig publicConfig = getPublicConfig();
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
				info.getJifen(),
				kuafuArena1v1SourceExportService.getGongxun(userRoleId) };
		return ret;
	}

	public Object[] getKuafuArena4v4YesterdayInfo(Long userRoleId) {
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena4v4(userRoleId);
			info = getRoleKuafuArena4v4(userRoleId, true);
		}
		Object[] ret = new Object[4];
		ret[0] = info.getLastDuan();
		int duanGongxunReward = 0;
		//if (info.getGongxunStatus().intValue() == ShenmoConstants.GONGXUN_STATUS_1) {
			ShenMoPaiHangConfig config = shenMoPaiHangConfigExportService.loadById(info.getLastDuan());
			duanGongxunReward = config.getGongxun();
		//}
		ret[1] = duanGongxunReward;
		ret[2] = info.getLastArenaTimes();
		int gongxunStatus = info.getGongxunStatus();
		/*if (gongxunStatus == ShenmoConstants.GONGXUN_STATUS_2) {
			gongxunStatus = ShenmoConstants.GONGXUN_STATUS_0;
		}*/
		ret[3] = gongxunStatus;
		return ret;

	}

	private static Map<Long, Integer> statusMap = new ConcurrentHashMap<Long, Integer>();

	public static boolean isInKuafuArena(Long userRoleId) {
		return statusMap.get(userRoleId) != null;
	}

	public static boolean isInMatch(Long userRoleId) {
		return statusMap.get(userRoleId) != null
				&& statusMap.get(userRoleId).intValue() == ShenmoConstants.STATUS_1;
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

			String flag = redis.hget(RedisKey.KUAFU_COMPETITION_ROLES_4V4, serverId
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
			List<String> list = redis.lrange(RedisKey.KUAFU_COMPETITION_TIME_4V4,
					0, -1);
			if (list == null || list.size() == 0) {
				return false;
			}
			long currentTime = GameSystemTime.getSystemMillTime();
			for (String e : list) {
				String[] timeZone = e.split("_");
				Long s1 = Long.parseLong(timeZone[0]);
				Long s2 = Long.parseLong(timeZone[0]);
				if (currentTime >= s1 && currentTime <= s2) {
					return true;
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		return false;
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

	public Object[] kuafuArenaMatch4v4(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null) {
			ChuanQiLog.error("userRoleId={} 4v4 match status={}", userRoleId,
					status);
			if (status.intValue() == ShenmoConstants.STATUS_1) {
				return AppErrorCode.KUAFU_ARENA_4V4_MATCH_ING;
			} else if (status.intValue() == ShenmoConstants.STATUS_2) {
				return AppErrorCode.KUAFU_ARENA_4V4_PREPARE_TING;
			} else if (status.intValue() == ShenmoConstants.STATUS_3) {
				return AppErrorCode.KUAFU_ARENA_4V4_PK_ING;
			} else if (status.intValue() == ShenmoConstants.STATUS_4) {
				return AppErrorCode.KUAFU_ARENA_4V4_EXIT_CURRENT;
			} else {
				return AppErrorCode.ERR;
			}
		}
		ShenMoPublicConfig publicConfig = getPublicConfig();
		if (!isInOpenTime(publicConfig.getOpenBeginTime(),
				publicConfig.getOpenEndTime())) {
			return AppErrorCode.KUAFU_ARENA_4V4_NOT_OPEN_TIME;
		}
		// 检测开放等级
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper.getLevel() < publicConfig.getLevel()) {
			return AppErrorCode.KUAFU_ARENA_4V4_LEVEL_LIMIT;
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
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena4v4(userRoleId);
			info = getRoleKuafuArena4v4(userRoleId, true);
		}
		if (info.getEscapeTimes() >= getPublicConfig().getOutCf()) {
			return AppErrorCode.KUAFU_ARENA_4V4_ESCAPE_TIMES_LIMIT;// 跨服连接失败
		}
		String serverId = ChuanQiConfigUtil.getServerId();
		Object[] data = new Object[] { serverId, info.getJifen(),
				roleExportService.getUserRole(userRoleId).getName() };
		if(competition){
			KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(userRoleId,
					InnerCmdType.KUAFU_ARENA_4V4_MATCH, data);
		}else{
			KuafuMatchMsgSender.send2KuafuMatchServer(userRoleId,
					InnerCmdType.KUAFU_ARENA_4V4_MATCH, data);
		}
		statusMap.put(userRoleId, ShenmoConstants.STATUS_1);

		BusTokenRunable runable = new BusTokenRunable(userRoleId,
				InnerCmdType.KUAFU_ARENA_4V4_MATCH_TIME_OUT, null);
		scheduleExportService.schedule(
				userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT,
				runable,
				publicConfig.getPiptime1() + publicConfig.getPiptime2()
						+ publicConfig.getPiptime3()
						+ publicConfig.getPiptime4()
						+ publicConfig.getPiptime5(), TimeUnit.SECONDS);
		ChuanQiLog.info("userRoleId={} request shenmo match", userRoleId);
		stageControllExportService.changeFuben(userRoleId, true);
		return AppErrorCode.OK;
	}

	private Map<Integer, List<Long>> teamRejectListMap = new ConcurrentHashMap<Integer, List<Long>>();

	public Object[] teamMatch(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null) {
			ChuanQiLog.error("userRoleId={} 4v4 match status={}", userRoleId,
					status);
			if (status.intValue() == ShenmoConstants.STATUS_1) {
				return AppErrorCode.KUAFU_ARENA_4V4_MATCH_ING;
			} else if (status.intValue() == ShenmoConstants.STATUS_2) {
				return AppErrorCode.KUAFU_ARENA_4V4_PREPARE_TING;
			} else if (status.intValue() == ShenmoConstants.STATUS_3) {
				return AppErrorCode.KUAFU_ARENA_4V4_PK_ING;
			} else if (status.intValue() == ShenmoConstants.STATUS_4) {
				return AppErrorCode.KUAFU_ARENA_4V4_EXIT_CURRENT;
			} else {
				return AppErrorCode.ERR;
			}
		}
		ShenMoPublicConfig publicConfig = getPublicConfig();
		if (!isInOpenTime(publicConfig.getOpenBeginTime(),
				publicConfig.getOpenEndTime())) {
			return AppErrorCode.KUAFU_ARENA_4V4_NOT_OPEN_TIME;
		}
		// 检测开放等级
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper.getLevel() < publicConfig.getLevel()) {
			return AppErrorCode.KUAFU_ARENA_4V4_LEVEL_LIMIT;
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
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena4v4(userRoleId);
			info = getRoleKuafuArena4v4(userRoleId, true);
		}
		if (info.getEscapeTimes() >= getPublicConfig().getOutCf()) {
			return AppErrorCode.KUAFU_ARENA_4V4_ESCAPE_TIMES_LIMIT;// 跨服连接失败
		}
		String stageId = publicRoleStateExportService
				.getRolePublicStageId(userRoleId);
		if (stageId == null) {
			return AppErrorCode.ERR;
		}
		IStage stage = StageManager.getStage(stageId);
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		TeamMember teamMember = role.getBusinessData().getTeamMember();
		if (teamMember == null) {
			return AppErrorCode.KUAFU_ARENA_4V4_NO_TEAM;
		}
		Team team = teamMember.getTeam();
		if (team == null) {
			return AppErrorCode.KUAFU_ARENA_4V4_NO_TEAM;
		}
		if (team.getLeader().getRole().getId().longValue() != userRoleId
				.longValue()) {
			return AppErrorCode.KUAFU_ARENA_4V4_NOT_CAPTAIN;
		}
		Long[] roleIdArray = team.getRoleIdList();
		for (Long e : roleIdArray) {
			if (stageControllExportService.inFuben(e)) {
				return AppErrorCode.KUAFU_ARENA_4V4_MEMBER_IN_FUBEN;
			}
			if (roleExportService.getUserRole(e).getLevel() < publicConfig
					.getLevel()) {
				return AppErrorCode.KUAFU_ARENA_4V4_MEMBER_LEVEL_LIMIT;
			}
			RoleKuafuArena4v4 member4v4Info = getRoleKuafuArena4v4(e, true);
			if (member4v4Info == null) {
				createRoleKuafuArena4v4(e);
				member4v4Info = getRoleKuafuArena4v4(e, true);
			}
			if (member4v4Info.getEscapeTimes() >= getPublicConfig().getOutCf()) {
				return AppErrorCode.KUAFU_ARENA_4V4_MEMBER_ESCAPE_LIMIT;
			}
			if(competition){
				if(isRoleInCompetition(e)){
					return AppErrorCode.KUAFU_ARENA_4V4_MEMBER_CAN_NOT_COMPETITION;
				}
			}
		}
		for (Long e : roleIdArray) {
			BusMsgSender.send2One(e,
					ClientCmdType.KUAFU_ARENA_4V4_CONFIRM_TEAM_MATCH, null);
		}
		BusTokenRunable runable = new BusTokenRunable(userRoleId,
				InnerCmdType.KUAFU_ARENA_4V4_TEAM_MATCH_DJS, competition);
		scheduleExportService.schedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_TEAM_MATCH_DJS,
				runable, 11500, TimeUnit.MILLISECONDS);
		return null;
	}

	public void sendTeamMatchRequest(Long userRoleId,boolean competition) {
		ShenMoPublicConfig publicConfig = getPublicConfig();
		String stageId = publicRoleStateExportService
				.getRolePublicStageId(userRoleId);
		if (stageId == null) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		TeamMember teamMember = role.getBusinessData().getTeamMember();
		if (teamMember == null) {
			return;
		}
		Team team = teamMember.getTeam();
		if (team == null) {
			return;
		}
		if (team.getLeader().getRole().getId().longValue() != userRoleId
				.longValue()) {
			return;
		}
		Long[] roleIdArray = team.getRoleIdList();
		List<Long> validList = new ArrayList<Long>();
		for (Long e : roleIdArray) {
			if (stageControllExportService.inFuben(e)) {
				continue;
			}
			if (roleExportService.getUserRole(e).getLevel() < publicConfig
					.getLevel()) {
				continue;
			}
			RoleKuafuArena4v4 member4v4Info = getRoleKuafuArena4v4(e, true);
			if (member4v4Info.getEscapeTimes() >= getPublicConfig().getOutCf()) {
				continue;
			}
			List<Long> rejectIdList = teamRejectListMap.get(team.getTeamId());
			if (rejectIdList != null && rejectIdList.contains(e)) {
				continue;
			}
			validList.add(e);
		}
		teamRejectListMap.remove(team.getTeamId());
		Object[] memberInfo = new Object[validList.size()];
		int count = 0;
		for (Long e : validList) {
			statusMap.put(e, ShenmoConstants.STATUS_1);
			stageControllExportService.changeFuben(e, true);
			RoleKuafuArena4v4 member4v4Info = getRoleKuafuArena4v4(e, true);
			memberInfo[count] = new Object[] { e, member4v4Info.getJifen(),
					roleExportService.getUserRole(e).getName() };
			count++;
			BusTokenRunable runable = new BusTokenRunable(e,
					InnerCmdType.KUAFU_ARENA_4V4_MATCH_TIME_OUT, null);
			scheduleExportService.schedule(
					e.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT,
					runable,
					publicConfig.getPiptime1() + publicConfig.getPiptime2()
							+ publicConfig.getPiptime3()
							+ publicConfig.getPiptime4()
							+ publicConfig.getPiptime5(), TimeUnit.SECONDS);
			ChuanQiLog.info("userRoleId={} request shenmo team match", e);
			BusMsgSender.send2One(e, ClientCmdType.KUAFU_ARENA_4V4_MATCH,
					AppErrorCode.OK);
		}

		String serverId = ChuanQiConfigUtil.getServerId();
		Object[] data = new Object[] { serverId, memberInfo };
		if(competition){
			KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(userRoleId,
					InnerCmdType.KUAFU_ARENA_4V4_TEAM_MATCH, data);
		}else{
			KuafuMatchMsgSender.send2KuafuMatchServer(userRoleId,
					InnerCmdType.KUAFU_ARENA_4V4_TEAM_MATCH, data);
		}
		
	}

	public void confirmTeamMatch(Long userRoleId) {
		String stageId = publicRoleStateExportService
				.getRolePublicStageId(userRoleId);
		if (stageId == null) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		TeamMember teamMember = role.getBusinessData().getTeamMember();
		if (teamMember == null) {
			return;
		}
		Team team = teamMember.getTeam();
		if (team == null) {
			return;
		}
		synchronized (teamRejectListMap) {
			List<Long> idList = teamRejectListMap.get(team.getTeamId());
			if (idList == null) {
				idList = new ArrayList<Long>();
				teamRejectListMap.put(team.getTeamId(), idList);
			}
			idList.add(userRoleId);
		}
		ChuanQiLog.info("userRoleId={} reject team match", userRoleId);
	}

	public void matchFail(Long userRoleId, Integer reason) {
		Integer status = statusMap.remove(userRoleId);
		if (status != null && status.intValue() == ShenmoConstants.STATUS_1) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_4V4_MATCH_FAIL, null);
			scheduleExportService.cancelSchedule(userRoleId.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT);
			stageControllExportService.changeFuben(userRoleId, false);
			ChuanQiLog.info("userRoleId={} shemo match fail,reason={}",
					userRoleId, reason);
		}
	}

	public void matchTimeOut(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null && status.intValue() == ShenmoConstants.STATUS_1) {
			statusMap.remove(userRoleId);
			stageControllExportService.changeFuben(userRoleId, false);
			ChuanQiLog.info("userRoleId={} shenmo match time out", userRoleId);
		}
	}

	public void matchSuccess(Long userRoleId, String serverId, long matchId,
			int camp) {
		scheduleExportService.cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT);
		Integer status = statusMap.get(userRoleId);
		if (status == null || status.intValue() != ShenmoConstants.STATUS_1) {
			ChuanQiLog.error(
					"userRoleId={} shenmo match success,status={} error",
					userRoleId, status);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error(
					"userRoleId={} shenmo match success, redis is null",
					userRoleId);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			return;
		}
		String key = RedisKey.buildKMatchId(matchId);
		String membersStr = redis.get(key);
		if (membersStr == null) {
			ChuanQiLog.error(
					"userRoleId={} shenmo match success membersStr is null",
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
			ChuanQiLog.error(
					"userRoleId={} shenmo match success is not in members",
					userRoleId);
			stageControllExportService.changeFuben(userRoleId, false);
			statusMap.remove(userRoleId);
			return;
		}
		// 通知前端匹配成功，开始倒计时
		BusMsgSender.send2One(userRoleId,
				ClientCmdType.KUAFU_ARENA_4V4_MATCH_SUCCESS, null);
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
					ClientCmdType.KUAFU_ARENA_4V4_MATCH,
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
		int jifen = getRoleKuafuArena4v4(userRoleId, true).getJifen();
		KuafuMsgSender
				.send2KuafuServer(
						GameConstants.DEFAULT_ROLE_ID,
						userRoleId,
						InnerCmdType.KUAFU_ARENA_4V4_SEND_ROLE_DATA,
						new Object[] {
								new Object[] { roleData, userRoleId,
										role.getName() }, matchId, jifen, camp,
								ChuanQiConfigUtil.getServerId() });
		statusMap.put(userRoleId, ShenmoConstants.STATUS_2);

		ShenMoPublicConfig publicConfig = getPublicConfig();

		BusTokenRunable runable = new BusTokenRunable(userRoleId,
				InnerCmdType.KUAFU_ARENA_4V4_PREPARE_TIME_OUT, null);
		scheduleExportService.schedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_PREPARE_TIME_OUT,
				runable,
				publicConfig.getDaojishi1() + publicConfig.getDaojishi2() + 5,
				TimeUnit.SECONDS);

		ChuanQiLog
				.info("userRoleId={} shenmo match sucess matchId={} kuafu serverId={}",
						new Object[] { userRoleId, matchId, serverId });
	}

	public void kuafuArenaCancelMatch4v4(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null && status.intValue() == ShenmoConstants.STATUS_1) {
			if(isRoleInCompetition(userRoleId) && isInCompetitionTime()){
				KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(userRoleId,
						InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH, null);
			}else{
				KuafuMatchMsgSender.send2KuafuMatchServer(userRoleId,
						InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH, null);
			}
		} else {
			if (status == null) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH,
						AppErrorCode.KUAFU_ARENA_4V4_CANCEL_MATCH_FAIL_1);
			} else {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH,
						AppErrorCode.KUAFU_ARENA_4V4_CANCEL_MATCH_FAIL);
			}

		}
	}

	public void kuafuArenaCancelMatch4v4Success(Long userRoleId) {
		statusMap.remove(userRoleId);
		stageControllExportService.changeFuben(userRoleId, false);
		ChuanQiLog.info("userRoleId = {} cancel match success", userRoleId);
		scheduleExportService.cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT);
	}

	public void teamDispose(Long userRoleId) {
		statusMap.remove(userRoleId);
		stageControllExportService.changeFuben(userRoleId, false);
		scheduleExportService.cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT);
	}

	public void prepareTimeOut(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null && status.intValue() == ShenmoConstants.STATUS_2) {
			statusMap.remove(userRoleId);
			BusMsgSender.send2BusInner(userRoleId,
					InnerCmdType.S_APPLY_LEAVE_STAGE, null);
			stageControllExportService.changeFuben(userRoleId, false);
			KuafuManager.removeKuafu(userRoleId);
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_4V4_MATCH,
					AppErrorCode.KUAFU_ARENA_4V4_ENTER_FAIL);
			ChuanQiLog.error(
					"userRoleId={} shenmo prepare time out, reset status",
					userRoleId);
			KuafuRoleServerManager.getInstance().removeBind(userRoleId);
		}
	}

	public void enterXiaoheiwu(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status == null || status.intValue() != ShenmoConstants.STATUS_2) {
			ChuanQiLog.error("shenmo enterXiaoheiwu status ={} error", status);
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
		ChuanQiLog.info("userRoleId={} shenmo enter xiaoheiwu", userRoleId);
	}

	public void start(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null && status.intValue() == ShenmoConstants.STATUS_2) {
			statusMap.put(userRoleId, ShenmoConstants.STATUS_3);
			scheduleExportService.cancelSchedule(userRoleId.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_4V4_PREPARE_TIME_OUT);
			ChuanQiLog.info("userRoleId={} shenmo pk start", userRoleId);
		}
	}

	public void updateJifenRank(boolean online, RoleKuafuArena4v4 info,
			int beforeJifen) {
		Long userRoleId = info.getUserRoleId();
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error(
					"userRoleId ={} shenmo updateJifenRank redis is null",
					userRoleId);
			return;
		}
		int minRankJifen = getPublicConfig().getPaijifen();
		if (info.getJifen() >= minRankJifen) {
			redis.zadd(RedisKey.KUAFU_ARENA_4V4_RANK, info.getJifen(),
					userRoleId.toString());
			// 更新其他信息
			String key = RedisKey.buildkuafuArena4v4RankRoleKey(userRoleId);
			if (!online) {
				// 玩家有可能不在线，此时我们就不更新了
				return;
			}
			RoleWrapper roleWrapper = roleExportService
					.getLoginRole(userRoleId);
			redis.hset(key, KuafuArena4v4Rank.FIELD_USER_ROLE_ID,
					userRoleId.toString());

			redis.hset(key, KuafuArena4v4Rank.FIELD_CONFIG_ID, roleWrapper
					.getConfigId().toString());

			redis.hset(key, KuafuArena4v4Rank.FIELD_NAME, roleWrapper.getName());

			redis.hset(key, KuafuArena4v4Rank.FIELD_LEVEL, roleWrapper
					.getLevel().toString());

			String guildName = guildExportService.getGuildName(userRoleId);
			guildName = guildName == null ? "" : guildName;
			redis.hset(key, KuafuArena4v4Rank.FIELD_GUILD_NAME, guildName);

			Integer chibangLevel = 0;
			ChiBangInfo chibangInfo = chiBangExportService
					.getChiBangInfo(userRoleId);
			if (chibangInfo != null) {
				chibangLevel = chibangInfo.getChibangLevel();
			}
			redis.hset(key, KuafuArena4v4Rank.FIELD_CHIBANG_ID,
					chibangLevel.toString());

			Integer zuoqiLevel = 0;
			ZuoQiInfo zuoQiInfo = zuoQiExportService.getZuoQiInfo(userRoleId);
			if (zuoQiInfo != null) {
				zuoqiLevel = zuoQiInfo.getZuoqiLevel();
			}
			redis.hset(key, KuafuArena4v4Rank.FIELD_ZUOQI_ID,
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
			redis.hset(key, KuafuArena4v4Rank.FIELD_ZPLUS, zplus.toString());
			ChuanQiLog.info(
					"userRoleId={} in kuafu arena rank shenmo jifen={}",
					userRoleId, info.getJifen());
		} else {
			// 判断本来是否上榜
			if (beforeJifen >= minRankJifen) {
				// 之前是上榜的状态，删掉个人信息
				String key = RedisKey.buildkuafuArena4v4RankRoleKey(userRoleId);
				redis.del(key);
				ChuanQiLog.info("userRoleId={} out kuafu arena shenmo rank",
						userRoleId);
			}
		}
	}

	public void exit(Long userRoleId) {
		Integer status = statusMap.get(userRoleId);
		if (status != null
				&& (status.intValue() == ShenmoConstants.STATUS_3 || status
						.intValue() == ShenmoConstants.STATUS_4)) {
			KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId,
					InnerCmdType.KUAFU_ARENA_4V4_EXIT_FB_TO_KUAFU, null);
		} else {
			if (status != null) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_4V4_EXIT,
						AppErrorCode.KUAFU_ARENA_4V4_CAN_NOT_EXIT);
			}
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
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_PREPARE_TIME_OUT);
		stageControllExportService.changeFuben(userRoleId, false);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_ARENA_4V4_MATCH,
				AppErrorCode.KUAFU_ARENA_4V4_ENTER_FAIL);
		ChuanQiLog.error("userRoleId={} shenmo enter stage fail, reset status",
				userRoleId);
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
	}

	public void escape(Long userRoleId) {
		boolean online = publicRoleStateExportService
				.isPublicOnline(userRoleId);
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, online);
		info.setLastEscapeTime(GameSystemTime.getSystemMillTime());
		info.setEscapeTimes(info.getEscapeTimes() + 1);
		if (online) {
			roleKuafuArena4v4Dao.cacheUpdate(info, userRoleId);
		} else {
			roleKuafuArena4v4Dao.update(info, userRoleId,
					AccessType.getDirectDbType());
		}
	}

	public void kuafuArenaCalcResult(Object[] data) {
		Long userRoleId = LongUtils.obj2long(data[0]);
		boolean online = publicRoleStateExportService
				.isPublicOnline(userRoleId);
		Integer status = statusMap.get(userRoleId);
		if (status != null && status.intValue() == ShenmoConstants.STATUS_3) {
			if (online) {
				statusMap.put(userRoleId, ShenmoConstants.STATUS_4);
			} else {
				statusMap.remove(userRoleId);
			}
		} else if (status != null
				&& status.intValue() == ShenmoConstants.STATUS_2) {
			if (online) {
				statusMap.put(userRoleId, ShenmoConstants.STATUS_4);
			} else {
				statusMap.remove(userRoleId);
			}
			stageControllExportService.changeFuben(userRoleId, false);
			scheduleExportService.cancelSchedule(userRoleId.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_4V4_PREPARE_TIME_OUT);
		} else {
			ChuanQiLog.error("userRoleId={} shenmo status error is {}",
					userRoleId, status);
		}
		Integer result = (Integer) data[1];
		Integer shenbinScore = (Integer) data[2];
		Integer xuemoScore = (Integer) data[3];
		Object[] scoreRank = (Object[]) data[4];
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, online);
		int myJifen = info.getJifen();
		int myAfterJifen = myJifen;
		int myDuan = shenMoPaiHangConfigExportService.getDuanByJifen(myJifen);
		ShenMoPaiHangConfig duanConfig = shenMoPaiHangConfigExportService
				.loadById(myDuan);
		int jiangGongxun = 0;
		int jiangJingyan = 0;
		if (result == 1) {
			if (info.getArenaTimes() < getPublicConfig().getDayGongxunTimes()) {
				roleExportService.incrExp(userRoleId,
						duanConfig.getWinExp() * 1L);
				kuafuArena1v1SourceExportService.addGongxun(userRoleId,
						duanConfig.getWingx());
				jiangGongxun = duanConfig.getWingx();
				jiangJingyan = duanConfig.getWinExp();
			}
			myAfterJifen = myJifen + duanConfig.getWinjf();
			if (online) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_4V4_RESULT, new Object[] {
								result, shenbinScore, xuemoScore,
								myAfterJifen - myJifen, jiangGongxun,
								jiangJingyan, scoreRank });
			}
			info.setWinTimes(info.getWinTimes() + 1);
		} else if (result == 2) {
			if (info.getArenaTimes() < getPublicConfig().getDayGongxunTimes()) {
				roleExportService.incrExp(userRoleId,
						duanConfig.getLoseExp() * 1L);
				jiangJingyan = duanConfig.getLoseExp();
			}
			myAfterJifen = Math.max(0, myJifen - duanConfig.getLosejf());
			if (online) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_4V4_RESULT, new Object[] {
								result, shenbinScore, xuemoScore,
								myAfterJifen - myJifen, 0, jiangJingyan,
								scoreRank });
			}
		} else if (result == 0) {
			if (online) {
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.KUAFU_ARENA_4V4_RESULT, new Object[] {
								result, shenbinScore, xuemoScore, 0, 0, 0,
								scoreRank });
			}
		}
		info.setArenaTimes(info.getArenaTimes() + 1);
		info.setJifen(myAfterJifen);
		info.setJifenUpdateTime(GameSystemTime.getSystemMillTime());
		String myName=null;
		if (online) {
			roleKuafuArena4v4Dao.cacheUpdate(info, userRoleId);
			try{
				myName = roleExportService.getLoginRole(userRoleId).getName();
			}catch (Exception ex) {
				ChuanQiLog.error("error exit when get role name userRoleId={}",userRoleId);
			}
		} else {
			roleKuafuArena4v4Dao.update(info, userRoleId,
					AccessType.getDirectDbType());
			try{
				myName = roleExportService.getUserRoleFromDb(userRoleId).getName();
			}catch (Exception ex) {
				ChuanQiLog.error("error exit when get role name userRoleId={}",userRoleId);
			}
		}
		updateJifenRank(online, info, myJifen);
		ChuanQiLog.info("userRoleId={} result={} ", new Object[] { userRoleId,
				result });
		String shenbinNames = null;
		String xuemoNames = null;
		if(data.length>5 && data[5]!=null){
			Object[] memberNames = (Object[])data[5];
			shenbinNames =(String) memberNames[0];
			xuemoNames =(String) memberNames[1];
		}
		GamePublishEvent.publishEvent(new KuafuArena4v4LogEvent(userRoleId, myName, myJifen, myAfterJifen, shenbinNames, xuemoNames));
	}

	/**
	 * 获取跨服1v1排行信息
	 * 
	 * @param userRoleId
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public Object[] getKuafuArena4v4Rank(Long userRoleId, int beginIndex,
			int endIndex) {
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena4v4(userRoleId);
			info = getRoleKuafuArena4v4(userRoleId, true);
		}
		Object[] ret = new Object[2];
		Object[] rankInfo = getRank(beginIndex, endIndex);
		ret[0] = rankInfo;
		ret[1] = (rankList == null ? 0 : rankList.size());
		return ret;
	}

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private List<KuafuArena4v4Rank> rankList = null;
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
						List<KuafuArena4v4Rank> retList = rankList.subList(
								beginIndex, endIndex + 1);
						for (int i = 0; i < retList.size(); i++) {
							KuafuArena4v4Rank tmp = retList.get(i);
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
			ChuanQiLog.error("fetch kuafu arena 4v4 rank error,redis is null");
			return;
		}
		try {
			List<KuafuArena4v4Rank> list = new ArrayList<KuafuArena4v4Rank>();
			ShenMoPublicConfig publicConfig = getPublicConfig();
			Set<Tuple> tuples = redis.zrevrangeWithScore(
					RedisKey.KUAFU_ARENA_4V4_RANK, 0,
					publicConfig.getPaiming() - 1);
			int rank = 1;
			for (Tuple e : tuples) {
				int jifen = (int) e.getScore();
				String member = e.getElement();
				String key = RedisKey.KUAFU_ARENA_4V4_RANK_ROLE + member;
				Map<String, String> infoMap = redis.hgetAll(key);
				if (infoMap == null || infoMap.size() == 0) {
					redis.zrem(RedisKey.KUAFU_ARENA_4V4_RANK, member);
					continue;
				}
				String userRoleId = infoMap
						.get(KuafuArena4v4Rank.FIELD_USER_ROLE_ID);
				String configId = infoMap
						.get(KuafuArena4v4Rank.FIELD_CONFIG_ID);
				String name = infoMap.get(KuafuArena4v4Rank.FIELD_NAME);
				String guildName = infoMap
						.get(KuafuArena4v4Rank.FIELD_GUILD_NAME);
				String level = infoMap.get(KuafuArena4v4Rank.FIELD_LEVEL);
				String zplus = infoMap.get(KuafuArena4v4Rank.FIELD_ZPLUS);
				String zuoqiId = infoMap.get(KuafuArena4v4Rank.FIELD_ZUOQI_ID);
				String chibangId = infoMap
						.get(KuafuArena4v4Rank.FIELD_CHIBANG_ID);
				KuafuArena4v4Rank tmpRank = new KuafuArena4v4Rank();
				tmpRank.setUserRoleId(Long.valueOf(userRoleId));
				tmpRank.setConfigId(Integer.valueOf(configId));
				tmpRank.setName(name);
				tmpRank.setGuildName(guildName);
				tmpRank.setLevel(Integer.valueOf(level));
				tmpRank.setZplus(Long.valueOf(zplus));
				tmpRank.setZuoqiId(Integer.parseInt(zuoqiId));
				tmpRank.setChibangId(Integer.parseInt(chibangId));
				int duan = shenMoPaiHangConfigExportService
						.getDuanByJifen(jifen);
				tmpRank.setDuan(duan);
				tmpRank.setRank(rank++);
				list.add(tmpRank);
			}
			lastFetchTime = GameSystemTime.getSystemMillTime();
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			rankList = list;
			ChuanQiLog.info("fetch shenmo kuafu arena rank finish");
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
	public Object[] getKuafuArena4v4PickReward(Long userRoleId) {
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, true);
		if (info == null) {
			createRoleKuafuArena4v4(userRoleId);
			info = getRoleKuafuArena4v4(userRoleId, true);
		}
		if (info.getGongxunStatus().intValue() == ShenmoConstants.GONGXUN_STATUS_0) {
			return AppErrorCode.KUAFU_ARENA_4V4_CAN_NOT_PICK;
		} else if (info.getGongxunStatus().intValue() == ShenmoConstants.GONGXUN_STATUS_2) {
			return AppErrorCode.KUAFU_ARENA_4V4_PICKED;
		}
		int duan = info.getLastDuan();
		ShenMoPaiHangConfig config = shenMoPaiHangConfigExportService
				.loadById(duan);
		kuafuArena1v1SourceExportService.addGongxun(userRoleId,
				config.getGongxun());
		info.setGongxunStatus(ShenmoConstants.GONGXUN_STATUS_2);
		roleKuafuArena4v4Dao.cacheUpdate(info, userRoleId);
		ChuanQiLog.info(
				"userRoleId={} pick day gongxun shenmo reward gongxun={}",
				userRoleId, config.getGongxun());
		return AppErrorCode.OK;
	}

	/**
	 * 服务器启动的时候，初始化2周清的job
	 */
	public void startKuafuArenaCleanJob() {
		ShenMoPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			ChuanQiLog.error("ShenMoPublicConfig is null-[神魔公共配置为空，请策划上传配置]!");
			return;
		}

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
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.KUAFU_ARENA_4V4_2WEEK_JOB, null);
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_2WEEK_JOB, runable,
				(int) (jobTime - currentTime), TimeUnit.MILLISECONDS);
		ChuanQiLog.info("shenmo kuafu arena clean job start time={}", new Date(
				jobTime));
	}
	
	
	
	
    /**
     * 2周执行一次4v4奖励结算任务
     */
    public void cleanJob() {
        createRankRewardData();
        clearKuafuAreaData();
        // 启动根据排名奖励数据发奖的定时器
        BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_ARENA_4V4_SEND_EMIAL_REWARD, null);
        scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_ARENA_4V4_SEND_EMAIL_REWARD, runable, GameConstants.SEND_REWARD_TIME, TimeUnit.SECONDS);
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
     * 2周执行一次,根据排名数据生成唯一的4v4排名奖励数据
     */
    private void createRankRewardData() {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("2week create rank reward ,redis is null");
            return;
        }
        try {
            int minRank = 1;
            int maxRank = shenMoPaiMingJiangLiConfigExportService.getMaxRank();
            Set<String> sets = redis.zrevrange(RedisKey.KUAFU_ARENA_4V4_RANK, minRank - 1, maxRank - 1);
            if (!ObjectUtil.isEmpty(sets)) {
                // 创建排名奖励数据
                int rank = 1;
                Map<String, String> _4v4RankReward = new HashMap<String, String>();
                for (String userRoleId : sets) {
                    ChuanQiLog.info("kuafu arena 4v4 rank reward userRolId={} rank={} ", userRoleId, rank);
                    _4v4RankReward.put(userRoleId, String.valueOf(rank));
                    rank++;
                }
                if(redis.exists(RedisKey.KUAFU_AREA_4V4_RANK_REWARD)){
                    ChuanQiLog.info("kuafu arena 4v4 rank reward exists!!!!");
                    redis.del(RedisKey.KUAFU_AREA_4V4_RANK_REWARD);
                }
                redis.hmset(RedisKey.KUAFU_AREA_4V4_RANK_REWARD, _4v4RankReward);
                // 清空排行数据
                cleanRedisRank();
            }
        } catch (Exception e) {
            ChuanQiLog.error("kuafu arena 4v4 create rank reward redis data by rank error , info={}" + e.getMessage());
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
                RoleKuafuArena4v4 info = getRoleKuafuArena4v4(e, true);
                if (info == null) {
                    continue;
                }
                info.setJifen(0);
                roleKuafuArena4v4Dao.cacheUpdate(info, e);
            } catch (Exception ex) {
                ChuanQiLog.error("error exit when clean shenmo userRoleId=" + e, ex);
            }
        }
        try {
            // 再清库
            roleKuafuArena4v4Dao.cleanAllJifen(0);
        } catch (Exception e) {
            ChuanQiLog.error("shenmo cleanAllJifen error", e);
        }
        ChuanQiLog.info("kuafu arena 4v4 shenmo clean job execute finish cost time={}ms", GameSystemTime.getSystemMillTime() - beginTime);
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
	public void clean() {
		long beginTime = GameSystemTime.getSystemMillTime();

		// 先清在线玩家的内存信息
		List<Long> roleIds = publicRoleStateExportService.getAllOnlineRoleids();
		for (Long e : roleIds) {
			try {
				RoleKuafuArena4v4 info = getRoleKuafuArena4v4(e, true);
				if (info == null) {
					continue;
				}
				info.setJifen(0);
				roleKuafuArena4v4Dao.cacheUpdate(info, e);
			} catch (Exception ex) {
				ChuanQiLog.error(
						"error exit when clean shenmo userRoleId=" + e, ex);
			}
		}
		try {
			// 再清库
			roleKuafuArena4v4Dao.cleanAllJifen(0);
		} catch (Exception e) {
			ChuanQiLog.error("shenmo cleanAllJifen error", e);
		}
		ChuanQiLog.info(
				"kuafu arena shenmo clean job execute finish cost time={}ms",
				GameSystemTime.getSystemMillTime() - beginTime);

		// 给本服上榜的人 发放奖励
		try {
			Redis redis = GameServerContext.getRedis();
			if (redis == null) {
				ChuanQiLog.error("shenmo 2week rank reward ,redis is null");
			} else {
				int minRank = 1;
				int maxRank = shenMoPaiMingJiangLiConfigExportService
						.getMaxRank();
				Set<String> sets = redis
						.zrevrange(RedisKey.KUAFU_ARENA_4V4_RANK, minRank - 1,
								maxRank - 1);
				if (sets != null && sets.size() > 0) {
					int rank = 1;
					for (String e : sets) {
						try {
							Long userRoleId = Long.valueOf(e);
							RoleWrapper roleWrapper = roleExportService
									.getUserRoleFromDb(userRoleId);
							ChuanQiLog
									.info("kuafu arena 4v4 rank reward userRolId={} rank={} isBenfu={}",
											new Object[] { userRoleId, rank,
													roleWrapper != null });
							if (roleWrapper != null) {
								ShenMoPaiMingJiangLiConfig config = shenMoPaiMingJiangLiConfigExportService
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
				InnerCmdType.KUAFU_ARENA_4V4_CLEAN_REDIS_RANK, null);
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.KUAFU_ARENA_4V4_CLEAN_REDIS_RANK, runable,
				60 + new Random().nextInt(30), TimeUnit.SECONDS);
	}
     **/
    
	/**
	 * 两周清排行榜
	 */
	public void cleanRedisRank() {
		try {
			Redis redis = GameServerContext.getRedis();
			if (redis == null) {
				ChuanQiLog.error("clean redis shenmo rank ,redis is null");
			} else {
				String flag = redis.get(RedisKey.KUAFU_ARENA_4V4_CLEAN_FLAG);
				if (flag == null) {
					redis.del(RedisKey.KUAFU_ARENA_4V4_RANK);
					redis.setex(RedisKey.KUAFU_ARENA_4V4_CLEAN_FLAG, 60, "1");
					ChuanQiLog.info("clean redis shenmo rank finish");
				} else {
					ChuanQiLog.info("clean redis shenmo rank finish already");
				}
				Set<String> keys = redis
						.keys(RedisKey.KUAFU_ARENA_4V4_RANK_ROLE + "*");
				if (keys != null && keys.size() > 0) {
					for (String e : keys) {
						redis.del(e);
					}
					ChuanQiLog.info(
							"clean redis shenmo rank user info size ={}",
							keys.size());
				}
			}
		} catch (Exception ex) {
			ChuanQiLog.error("clean redis shenmo rank error", ex);
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
        Map<String, String> _4v4RankReward = redis.hgetAll(RedisKey.KUAFU_AREA_4V4_RANK_REWARD);
        if (!ObjectUtil.isEmpty(_4v4RankReward)) {
            for (String roleId : _4v4RankReward.keySet()) {
                try {
                    Long userRoleId = Long.parseLong(roleId);
                    Integer rank = Integer.parseInt(_4v4RankReward.get(roleId));
                    RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
                    ChuanQiLog.info("kuafu arena 4v4 rank reward userRolId={} rank={} isBenfu={}", new Object[] { userRoleId, rank, roleWrapper != null });
                    if (roleWrapper != null) {
                        ShenMoPaiMingJiangLiConfig config = shenMoPaiMingJiangLiConfigExportService.loadByRank(rank);
                        // 以邮件形式发放奖励
                        if (sendSystemEamilForSingle2(userRoleId, rank, config)) {
                            redis.hdel(RedisKey.KUAFU_AREA_4V4_RANK_REWARD, roleId);
                        }
                    }
                } catch (NumberFormatException e) {
                    ChuanQiLog.error("kuafu arena 4v4 send rank email reward error , info={}" + e);
                }
            }
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
	private void sendSystemEamilForSingle(Long userRoleId, int rank,
			ShenMoPaiMingJiangLiConfig config) {
		try {
			String content = EmailUtil.getCodeEmail(
					AppErrorCode.KUAFU_ARENA_4V4_REWARD_MAIL_CODE,
					String.valueOf(rank));
			String[] attachment = EmailUtil.getAttachments(config
					.getJiangitemMap());
			for (String e : attachment) {
				emailExportService.sendEmailToOne(userRoleId, content,
						GameConstants.EMAIL_TYPE_SINGLE, e);
			}
		} catch (Exception e) {
			ChuanQiLog
					.error("kuafu arena 4v4 rank sendSystemEamilForSingle error");
		}
	}
	**/
    
	/**
	 * 两周排行榜的奖励邮件发放
	 * 
	 * @param userRoleId
	 * @param rank
	 * @param config
	 * @return
	 */
    private boolean sendSystemEamilForSingle2(Long userRoleId, int rank, ShenMoPaiMingJiangLiConfig config) {
        boolean flag = false;
        try {
        	String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_ARENA_4V4_REWARD_MAIL_CODE_TITLE);
            String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_ARENA_4V4_REWARD_MAIL_CODE, String.valueOf(rank));
            String[] attachment = EmailUtil.getAttachments(config.getJiangitemMap());
            for (String e : attachment) {
                emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, e);
            }
            flag = true;
        } catch (Exception e) {
            ChuanQiLog.error("kuafu arena 4v4 rank sendSystemEamilForSingle error");
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
		RoleKuafuArena4v4 info = getRoleKuafuArena4v4(userRoleId, online);
		info.setJifen(0);
		if (online) {
			roleKuafuArena4v4Dao.cacheUpdate(info, userRoleId);
		} else {
			roleKuafuArena4v4Dao.update(info, userRoleId,
					AccessType.getDirectDbType());
		}
		ChuanQiLog.info("kuafu arena 4v4 clean userRoleId={}", userRoleId);
	}

}
