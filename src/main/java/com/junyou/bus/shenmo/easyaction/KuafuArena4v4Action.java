package com.junyou.bus.shenmo.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.shenmo.service.KuafuArena4v4KuafuService;
import com.junyou.bus.shenmo.service.KuafuArena4v4MatchService;
import com.junyou.bus.shenmo.service.KuafuArena4v4SourceService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.SHENMO)
public class KuafuArena4v4Action {
	@Autowired
	private KuafuArena4v4SourceService kuafuArena4v4SourceService;
	@Autowired
	private KuafuArena4v4MatchService kuafuArena4v4MatchService;
	@Autowired
	private KuafuArena4v4KuafuService kuafuArena4v4KuafuService;

	// -------------------------------------------------客户端发源服的begin-----------------------------------------
	/**
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_4V4_INFO)
	public void getKuafuArena4v4Info(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] info = kuafuArena4v4SourceService
				.getKuafuArena4v4Info(userRoleId);
		if (info != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_4V4_INFO, info);
		}
	}

	/**
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_4V4_YESTERDAY_INFO)
	public void getKuafuArena4v4YesterdayInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] info = kuafuArena4v4SourceService
				.getKuafuArena4v4YesterdayInfo(userRoleId);
		if (info != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_4V4_YESTERDAY_INFO, info);
		}
	}

	/**
	 * 开始匹配
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_4V4_MATCH)
	public void kuafuArenaMatch4v4(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuArena4v4SourceService
				.kuafuArenaMatch4v4(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_4V4_MATCH, ret);
		}
	}

	/**
	 * 开始匹配
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_4V4_TEAM_MATCH)
	public void kuafuArenaTeamMatch4v4(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuArena4v4SourceService.teamMatch(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_4V4_TEAM_MATCH, ret);
		}
	}

	/**
	 * 匹配超时容错
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_TEAM_MATCH_DJS)
	public void kuafuArena4v4TeamMatchDjs(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Boolean competition = inMsg.getData();
		kuafuArena4v4SourceService.sendTeamMatchRequest(userRoleId,competition);
	}

	/**
	 * 开始匹配
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_4V4_CONFIRM_TEAM_MATCH)
	public void kuafuArenaConfirmTeamMatch4v4(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer data = (Integer) inMsg.getData();
		if (data != null && data.intValue() == 0) {
			kuafuArena4v4SourceService.confirmTeamMatch(userRoleId);
		}
	}

	/**
	 * 取消匹配
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH)
	public void kuafuArenaCancelMatch(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.kuafuArenaCancelMatch4v4(userRoleId);
	}

	/**
	 * 匹配超时容错
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_MATCH_TIME_OUT)
	public void kuafuArena4v4MatchTimeOut(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.matchTimeOut(userRoleId);
	}

	/**
	 * 准备超时容错
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_PREPARE_TIME_OUT)
	public void kuafuArena4v4PrepareTimeOut(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.prepareTimeOut(userRoleId);
	}

	/**
	 * 退出跨服竞技场
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_4V4_EXIT)
	public void kuafuArenaExit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.exit(userRoleId);
	}

	/**
	 * 请求段位榜
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_4V4_RANK)
	public void getKuafuArena4v4Rank(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer beginIndex = (Integer) data[0];
		Integer endIndex = (Integer) data[1];
		Object[] ret = kuafuArena4v4SourceService.getKuafuArena4v4Rank(
				userRoleId, beginIndex, endIndex);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_4V4_RANK, ret);
		}
	}

	/**
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_4V4_PICK_REWARD)
	public void getKuafuArena4v4PickReward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuArena4v4SourceService
				.getKuafuArena4v4PickReward(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_4V4_PICK_REWARD, ret);
		}
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_2WEEK_JOB)
	public void kuafuArena4v4_2weekJob(Message inMsg) {
		kuafuArena4v4SourceService.cleanJob();
	}

//	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_CLEAN_REDIS_RANK)
//	public void kuafuArena4v4CleanRedisRank(Message inMsg) {
//		kuafuArena4v4SourceService.cleanRedisRank();
//	}
	
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_SEND_EMIAL_REWARD)
	 public void kuafuArena1v1SendEmialReward(Message inMsg) {
	    kuafuArena4v4SourceService.sendEmialReward();
    }

	// -------------------------------------------------客户端发源服的begin---------------------------------------

	// -------------------------------------------------源服发匹配服的begin-----------------------------------------
	/**
	 * 向匹配服发出匹配请求
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_MATCH)
	public void kuafuArena4v4MatchRequest(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer jifen = (Integer) data[1];
		String name = (String) data[2];
		kuafuArena4v4MatchService.kuafuArena4v4Match(userRoleId, name, jifen);
	}

	/**
	 * 向匹配服发出匹配请求
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_TEAM_MATCH)
	public void kuafuArena4v4TeamMatchRequest(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String serverId = (String) data[0];
		Object[] members = (Object[]) data[1];
		kuafuArena4v4MatchService.kuafuArena4v4TeamMatch(userRoleId, serverId,
				members);
	}

	/**
	 * 向匹配服发出取消匹配请求
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH)
	public void cancelMatchToMatchServer(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Boolean offline = inMsg.getData();
		kuafuArena4v4MatchService.cancelMatch(userRoleId, offline);
	}

	// -------------------------------------------------源服发匹配服的end-----------------------------------------

	// -------------------------------------------------匹配服发源服的begin---------------------------------------
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena4v4MatchFail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer reason = inMsg.getData();
		kuafuArena4v4SourceService.matchFail(userRoleId, reason);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_MATCH_SUCCESS, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena4v4MatchSuccess(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String serverId = (String) data[0];
		Long matchId = LongUtils.obj2long(data[1]);
		Integer camp = (Integer) data[2];
		kuafuArena4v4SourceService.matchSuccess(userRoleId, serverId, matchId,
				camp);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH_RESULT, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void cancelMatchResult(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Boolean success = inMsg.getData();
		if (success) {
			kuafuArena4v4SourceService
					.kuafuArenaCancelMatch4v4Success(userRoleId);
			BusMsgSender
					.send2One(userRoleId,
							ClientCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH,
							AppErrorCode.OK);
		} else {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH,
					AppErrorCode.KUAFU_ARENA_4V4_CANCEL_MATCH_FAIL);
		}
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_DISPOSE_TEAM, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void teamDispose(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.teamDispose(userRoleId);
		BusMsgSender.send2One(userRoleId,
				ClientCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
				null);
	}

	// -------------------------------------------------匹配服发源服的end---------------------------------------

	// -------------------------------------------------匹配服内部的begin---------------------------------------
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_REMOVE_REQUEST)
	public void kuafuArena4v4removeRequest(Message inMsg) {
		Object[] data = inMsg.getData();
		Long teamId = LongUtils.obj2long(data[0]);
		Long userRoleId = LongUtils.obj2long(data[1]);
		kuafuArena4v4MatchService.removeRequest(teamId, userRoleId);
	}

	// -------------------------------------------------匹配服发源服的end---------------------------------------

	// -------------------------------------------------源服发跨服的begin---------------------------------------
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_SEND_ROLE_DATA)
	public void kuafuArenaSendRoleData(Message inMsg) {
		Object[] roleData = inMsg.getData();
		kuafuArena4v4KuafuService.recieveRoleData(roleData);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_ENTER_STAGE)
	public void kuafuArenaEnterStage(Message inMsg) {
		Long matchId = LongUtils.obj2long(inMsg.getData());
		kuafuArena4v4KuafuService.enterStage(matchId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_START_TO_KUAFU)
	public void kuafuArenaStart(Message inMsg) {
		Long matchId = inMsg.getData();
		kuafuArena4v4KuafuService.start(matchId);
	}

	// @EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_DEAD_HANDLE)
	// public void kuafuArenaDeadHandle(Message inMsg) {
	// Long userRoleId = inMsg.getRoleId();
	// String stageId = inMsg.getData();
	// kuafuArena1v1KuafuService.kuafuArenaDeadHandle(userRoleId, stageId);
	// }

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_EXIT_FB_TO_KUAFU)
	public void kuafuArenaLeaveFbToKuafu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4KuafuService.exit(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_OFFLINE_TO_KUAFU)
	public void kuafuArenaOffline(Message inMsg) {
		Long userRoleId = LongUtils.obj2long(inMsg.getData());
		kuafuArena4v4KuafuService.handleOffline(userRoleId);
	}

	//
	// @EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_FORCE_END)
	// public void kuafuArenaForceEnd(Message inMsg) {
	// Long matchId = inMsg.getData();
	// kuafuArena1v1KuafuService.kuafuArenaEnd(matchId);
	// }

	// @EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_KICK_MEMBER)
	// public void kuafuArena1v1KickMember(Message inMsg) {
	// Long matchId = inMsg.getData();
	// kuafuArena1v1KuafuService.kickAllMember(matchId);
	// }

	// -------------------------------------------------源服发跨服的end---------------------------------------

	// -------------------------------------------------跨服发源服的begin---------------------------------------

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_ENTER_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena4v4EnterXiaoheiwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.enterXiaoheiwu(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_PK_START, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena4V4PkStart(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.start(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_CALC_RESULT, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArenaCalcResult(Message inMsg) {
		Object[] data = inMsg.getData();
		kuafuArena4v4SourceService.kuafuArenaCalcResult(data);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_LEAVE_FB, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArenaLeaveFb(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.leaveFb(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_4V4_ENTER_STAGE_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArenaEnterFail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena4v4SourceService.enterStageFail(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.SHENMO_ESCAPE, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArenaEscape(Message inMsg) {
		Long userRoleId = LongUtils.obj2long(inMsg.getData());
		kuafuArena4v4SourceService.escape(userRoleId);
	}
	// -------------------------------------------------跨服发源服的end---------------------------------------
}