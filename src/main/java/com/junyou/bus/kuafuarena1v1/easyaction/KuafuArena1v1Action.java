package com.junyou.bus.kuafuarena1v1.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1KuafuService;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1MatchService;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1SourceService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_ARENA_1V1)
public class KuafuArena1v1Action {
	@Autowired
	private KuafuArena1v1SourceService kuafuArena1v1SourceService;
	@Autowired
	private KuafuArena1v1MatchService kuafuArena1v1MatchService;
	@Autowired
	private KuafuArena1v1KuafuService kuafuArena1v1KuafuService;

	// -------------------------------------------------客户端发源服的begin-----------------------------------------
	/**
	 * 获取自己跨服竞技排名信息
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_1V1_GET_MY_RANK)
	public void getKuafuArenaMyRankInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] info = kuafuArena1v1SourceService
				.getKuafuArenaMyRankInfo(userRoleId);
		if (info != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_1V1_GET_MY_RANK, info);
		}
	}

	/**
	 * 获取跨服竞技1v1个人信息
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_1V1_INFO)
	public void getKuafuArena1v1Info(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] info = kuafuArena1v1SourceService
				.getKuafuArena1v1Info(userRoleId);
		if (info != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_1V1_INFO, info);
		}
	}

	/**
	 * 开始匹配
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_1V1_MATCH)
	public void kuafuArenaMatch1v1(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuArena1v1SourceService.kuafuArenaMatch1v1(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.KUAFU_ARENA_1V1_MATCH, ret);
		}
	}

	/**
	 * 取消匹配
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH)
	public void kuafuArenaCancelMatch(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.kuafuArenaCancelMatch1v1(userRoleId);
	}

	/**
	 * 匹配超时容错
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_MATCH_TIME_OUT)
	public void kuafuArena1v1MatchTimeOut(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.matchTimeOut(userRoleId);
	}

	/**
	 * 准备超时容错
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_PREPARE_TIME_OUT)
	public void kuafuArena1v1PrepareTimeOut(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.prepareTimeOut(userRoleId);
	}

	/**
	 * 退出跨服竞技场
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_1V1_EXIT)
	public void kuafuArenaExit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.exit(userRoleId);
	}
	/**
	 * 请求段位榜
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_1V1_RANK)
	public void getKuafuArena1v1Rank(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer beginIndex = (Integer) data[0];
		Integer endIndex = (Integer) data[1];
		Object[] ret = kuafuArena1v1SourceService.getKuafuArena1v1Rank(
				userRoleId, beginIndex, endIndex);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_1V1_RANK, ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_1V1_YESTERDAY_INFO)
	public void getKuafuArena1v1YesterdayInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuArena1v1SourceService.getKuafuArena1v1YesterdayInfo(
				userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_1V1_YESTERDAY_INFO, ret);
		}
	}
	/**
	 * 1v1领取奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_1V1_PICK_REWARD)
	public void getKuafuArena1v1PickReward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuArena1v1SourceService
				.getKuafuArena1v1PickReward(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_1V1_PICK_REWARD, ret);
		}
	}
	/**
	 * 1v1请求今日已兑换信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_ARENA_1V1_DUIHUAN_INFO)
	public void getKuafuArena1v1DuihuanInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuArena1v1SourceService
				.getKuafuArena1v1DuihuanInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_KUAFU_ARENA_1V1_DUIHUAN_INFO, ret);
		}
	}
	/**
	 * 1v1兑换道具
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_ARENA_1V1_DUIHUAN)
	public void kuafuArena1v1Duihuan(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer order = inMsg.getData();
		Object[] ret = kuafuArena1v1SourceService.kuafuArena1v1Duihuan(
				userRoleId, order);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.KUAFU_ARENA_1V1_DUIHUAN, ret);
		}
	}
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_2WEEK_JOB)
	public void kuafuArena1v1_2weekJob(Message inMsg) {
		kuafuArena1v1SourceService.cleanJob();
	}
	
//	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_CLEAN_REDIS_RANK)
//	public void kuafuArena1v1CleanRedisRank(Message inMsg) {
//		kuafuArena1v1SourceService.cleanRedisRank();
//	}
	
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_SEND_EMIAL_REWARD)
    public void kuafuArena1v1SendEmialReward(Message inMsg) {
        kuafuArena1v1SourceService.sendEmialReward();
    }
	
	// -------------------------------------------------客户端发源服的begin---------------------------------------

	// -------------------------------------------------源服发匹配服的begin-----------------------------------------
	/**
	 * 向匹配服发出匹配请求
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_MATCH)
	public void kuafuArena1v1MatchRequest(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer jifen = (Integer) data[1];
		Integer lianWin = (Integer) data[2];
		Integer lianLose = (Integer) data[3];
		kuafuArena1v1MatchService.kuafuArena1v1Match(userRoleId, jifen,
				lianWin, lianLose);
	}

	/**
	 * 向匹配服发出取消匹配请求
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH)
	public void cancelMatchToMatchServer(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Boolean offline = inMsg.getData();
		kuafuArena1v1MatchService.cancelMatch(userRoleId,offline);
	}

	// -------------------------------------------------源服发匹配服的end-----------------------------------------

	// -------------------------------------------------匹配服发源服的begin---------------------------------------
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_MATCH_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena1v1MatchFail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer reason = inMsg.getData();
		kuafuArena1v1SourceService.matchFail(userRoleId, reason);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_MATCH_SUCCESS, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena1v1MatchSuccess(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String serverId = (String) data[0];
		Long matchId = LongUtils.obj2long(data[1]);
		kuafuArena1v1SourceService.matchSuccess(userRoleId, serverId, matchId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH_RESULT, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void cancelMatchResult(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Boolean success = inMsg.getData();
		if (success) {
			kuafuArena1v1SourceService.kuafuArenaCancelMatch1v1Success(userRoleId);
			BusMsgSender.send2One(userRoleId,ClientCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH,AppErrorCode.OK);
		} else {
			BusMsgSender.send2One(userRoleId,ClientCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH,AppErrorCode.KUAFU_ARENA_1V1_CANCEL_MATCH_FAIL);
		}
	}

	// -------------------------------------------------匹配服发源服的end---------------------------------------

	// -------------------------------------------------源服发跨服的begin---------------------------------------
	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_SEND_ROLE_DATA)
	public void kuafuArenaSendRoleData(Message inMsg) {
		Object[] roleData = inMsg.getData();
		kuafuArena1v1KuafuService.recieveRoleData(roleData);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_ENTER_STAGE)
	public void kuafuArenaEnterStage(Message inMsg) {
		Long matchId = LongUtils.obj2long(inMsg.getData());
		kuafuArena1v1KuafuService.enterStage(matchId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_START_TO_KUAFU)
	public void kuafuArenaStart(Message inMsg) {
		Long matchId = inMsg.getData();
		kuafuArena1v1KuafuService.start(matchId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_DEAD_HANDLE)
	public void kuafuArenaDeadHandle(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getData();
		kuafuArena1v1KuafuService.kuafuArenaDeadHandle(userRoleId, stageId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_EXIT_FB_TO_KUAFU)
	public void kuafuArenaLeaveFbToKuafu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1KuafuService.exit(userRoleId);
	}

	// @EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_OFFLINE_TO_KUAFU)
	// public void kuafuArenaOffline(Message inMsg) {
	// Long userRoleId = LongUtils.obj2long(inMsg.getData());
	// kuafuArena1v1KuafuService.handleOffline(userRoleId);
	// }

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_FORCE_END)
	public void kuafuArenaForceEnd(Message inMsg) {
		Long matchId = inMsg.getData();
		kuafuArena1v1KuafuService.kuafuArenaEnd(matchId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_KICK_MEMBER)
	public void kuafuArena1v1KickMember(Message inMsg) {
		Long matchId = inMsg.getData();
		kuafuArena1v1KuafuService.kickAllMember(matchId);
	}

	// -------------------------------------------------源服发跨服的end---------------------------------------

	// -------------------------------------------------跨服发源服的begin---------------------------------------

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_ENTER_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena1v1EnterXiaoheiwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.enterXiaoheiwu(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_PK_START, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArena1V1PkStart(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.start(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_CALC_RESULT, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArenaCalcResult(Message inMsg) {
		Object[] data = inMsg.getData();
		kuafuArena1v1SourceService.kuafuArenaCalcResult(data);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_LEAVE_FB, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArenaLeaveFb(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.leaveFb(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_ARENA_1V1_ENTER_STAGE_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void kuafuArenaEnterFail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuArena1v1SourceService.enterStageFail(userRoleId);
	}
	// -------------------------------------------------跨服发源服的end---------------------------------------
}