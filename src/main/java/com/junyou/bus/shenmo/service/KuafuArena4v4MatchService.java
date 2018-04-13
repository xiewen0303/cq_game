package com.junyou.bus.shenmo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.shenmo.configure.export.ShenMoPaiHangConfigExportService;
import com.junyou.bus.shenmo.constants.ShenmoConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ShenMoPublicConfig;
import com.junyou.kuafumatch.manager.KuafuMatchMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

@Service
public class KuafuArena4v4MatchService {
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private ShenMoPaiHangConfigExportService shenMoPaiHangConfigExportService;
	@Autowired
	private BusScheduleExportService busScheduleExportService;

	public class MatchRequest implements Comparable<MatchRequest> {
		private Long userRoleId;
		private String name;
		private int jifen;
		private int duan;
		private long requestTime;

		public MatchRequest(Long userRoleId, String name, int jifen, int duan,
				long requestTime) {
			super();
			this.userRoleId = userRoleId;
			this.name = name;
			this.jifen = jifen;
			this.duan = duan;
			this.requestTime = requestTime;
		}

		public Long getUserRoleId() {
			return userRoleId;
		}

		public String getName() {
			return name;
		}

		public int getJifen() {
			return jifen;
		}

		public int getDuan() {
			return duan;
		}

		public long getRequestTime() {
			return requestTime;
		}

		@Override
		public int compareTo(MatchRequest o) {
			if (this.jifen > o.jifen) {
				return -1;
			} else {
				return 1;
			}
		}

		public List<Integer> getMatchDuans(int times) {
			int maxDuan = shenMoPaiHangConfigExportService.getMaxDuan();
			List<Integer> ret = new ArrayList<Integer>();
			if (times == 1) {
				ret.add(duan);
			} else if (times == 2) {
				ret.add(duan);
				if (duan + 1 < maxDuan) {
					ret.add(duan + 1);
				}
				if (duan - 1 > 0) {
					ret.add(duan - 1);
				}
			} else if (times == 3) {
				ret.add(duan);
				if (duan + 1 < maxDuan) {
					ret.add(duan + 1);
				}
				if (duan - 1 > 0) {
					ret.add(duan - 1);
				}
				if (duan + 2 < maxDuan) {
					ret.add(duan + 2);
				}
				if (duan - 2 > 0) {
					ret.add(duan - 2);
				}
			}

			return ret;
		}
	}

	/**
	 * 只有满员的队伍 才会进入teamIdList
	 */
	private List<Long> teamIdList = new ArrayList<Long>();

	private Map<Long, MatchTeam> matchTeams = new ConcurrentHashMap<Long, MatchTeam>();
	/**
	 * key:duan value:按积分倒序排列的
	 */
	private Map<Integer, List<MatchTeam>> duanTeamMap = new ConcurrentHashMap<Integer, List<MatchTeam>>();

	private static AtomicLong idGenerator = new AtomicLong();

	public class MatchTeam {
		private boolean teamEnter = false;

		public boolean isTeamEnter() {
			return teamEnter;
		}

		public void setTeamEnter(boolean teamEnter) {
			this.teamEnter = teamEnter;
		}

		private long id;

		public long getId() {
			return id;
		}

		private int duan;

		public int getDuan() {
			return duan;
		}

		private boolean full;

		public boolean isFull() {
			return full;
		}

		public void setFull(boolean full) {
			this.full = full;
		}

		private long fullTime;

		public long getFullTime() {
			return fullTime;
		}

		private Map<Long, MatchRequest> requests = new HashMap<Long, MatchRequest>();

		public MatchTeam(int duan) {
			id = idGenerator.getAndIncrement();
			this.duan = duan;
		}

		public MatchRequest removeRequest(Long userRoleId) {
			return requests.remove(userRoleId);
		}

		public void addRequest(MatchRequest request) {
			if (getMemberSize() >= 4) {
				return;
			}
			Long userRoleId = request.getUserRoleId();
			ShenMoPublicConfig publicConfig = getPublicConfig();
			Long delay = request.getRequestTime()
					+ (publicConfig.getPiptime1() + publicConfig.getPiptime2() + publicConfig
							.getPiptime3()) * 1000L
					- GameSystemTime.getSystemMillTime();
			if (delay <= 0) {
				return;
			}
			requests.put(userRoleId, request);

			BusTokenRunable runable = new BusTokenRunable(
					GameConstants.DEFAULT_ROLE_ID,
					InnerCmdType.KUAFU_ARENA_4V4_REMOVE_REQUEST, new Object[] {
							getId(), userRoleId });
			busScheduleExportService.schedule(
					GameConstants.DEFAULT_ROLE_ID + "",
					GameConstants.KUAFU_ARENA_4V4_REMOVE_REQUEST
							+ String.valueOf(getId())
							+ String.valueOf(userRoleId), runable,
					delay.intValue(), TimeUnit.MILLISECONDS);
			if (getMemberSize() == 4) {
				full = true;
				fullTime = GameSystemTime.getSystemMillTime();
				// 开始匹配对手
				if (!teamIdList.contains(getId())) {
					teamIdList.add(getId());
				}
				// 消除所有成员的定时
				Set<Long> userRoleIds = getMemberUserRoleIds();
				for (Long e : userRoleIds) {
					busScheduleExportService.cancelSchedule(
							GameConstants.DEFAULT_ROLE_ID + "",
							GameConstants.KUAFU_ARENA_4V4_REMOVE_REQUEST
									+ String.valueOf(getId())
									+ String.valueOf(e));
				}
			}
		}

		public int getMemberSize() {
			return requests.size();
		}

		public boolean hasMember(Long userRoleId) {
			return requests.containsKey(userRoleId);
		}

		public int getMaxDuan() {
			int max = 0;
			for (MatchRequest e : requests.values()) {
				if (e.getDuan() > max) {
					max = e.getDuan();
				}
			}
			return max;
		}

		public List<Integer> getMatchDuans(int times) {
			int maxDuan = shenMoPaiHangConfigExportService.getMaxDuan();
			List<Integer> ret = new ArrayList<Integer>();
			int teamMaxDuan = getMaxDuan();
			if (times == 1) {
				ret.add(teamMaxDuan);
			} else if (times == 2) {
				ret.add(teamMaxDuan);
				if (teamMaxDuan + 1 < maxDuan) {
					ret.add(teamMaxDuan + 1);
				}
				if (teamMaxDuan - 1 > 0) {
					ret.add(teamMaxDuan - 1);
				}
			}
			return ret;
		}

		public Set<Long> getMemberUserRoleIds() {
			return requests.keySet();
		}
	}

	/**
	 * 玩家进入队伍，在匹配对手队伍的过程中，部分玩家出现超时的情况，将此玩家从队伍中移除
	 * 
	 * @param teamId
	 * @param userRoleId
	 */
	public void removeRequest(long teamId, long userRoleId) {
		try {
			lock.tryLock(1, TimeUnit.SECONDS);
			if (lock.isHeldByCurrentThread()) {
				MatchTeam team = matchTeams.get(teamId);
				if (team != null) {
					MatchRequest request = team.removeRequest(userRoleId);
					if (request != null) {
						KuafuMatchMsgSender.send2SourceServer(userRoleId,
								InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
								ShenmoConstants.MATCH_FAIL_REASON_1);
						BusMsgSender.send2BusInner(userRoleId,
								InnerCmdType.INNER_KUAFU_LEAVE, null);
					}
					if (team.getMemberSize() == 0) {
						for (List<MatchTeam> list : duanTeamMap.values()) {
							if (list.contains(team)) {
								list.remove(team);
								break;
							}
						}
						matchTeams.remove(teamId);
					}
				}
			} else {
				ChuanQiLog
						.error("can not get lock when remove request userRoleId={} teamId={} ",
								userRoleId, teamId);
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public ShenMoPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_SHENMO);
	}

	private int coursor = 0;

	public void startMatchPartnerThread() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int teamSize = 0;
					int size = 0;
					try {
						teamSize = matchTeams.size();
						size = requestUserRoleIdList.size();
					} catch (Exception e) {
						ChuanQiLog.error("", e);
					}
					if ((size > 1) || (teamSize > 0 && size > 0)) {
						try {
							try {
								Thread.sleep(1000 / size);
							} catch (InterruptedException e) {
								ChuanQiLog.error("", e);
							}
							lock.tryLock(1, TimeUnit.SECONDS);
							if (!lock.isHeldByCurrentThread()) {
								ChuanQiLog
										.error("shenmo match thread can not get lock coursor is ={}",
												coursor);
								coursor++;
								if (coursor > 100) {
									lock = new ReentrantLock();
									coursor = 0;
									ChuanQiLog.info("shenmo lock reset");
								}
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									ChuanQiLog.error("", e);
								}
								continue;
							}
							List<Long> tobeRemoved = new ArrayList<Long>();
							for (Long userRoleId : requestUserRoleIdList) {
								MatchRequest request = userRequestMap
										.get(userRoleId);
								if (request == null) {
									tobeRemoved.add(userRoleId);
									continue;
								}
								int times = getPartnerMatchTimes(request
										.getRequestTime());
								if (times == -1) {
									// 匹配超时了
									tobeRemoved.add(userRoleId);
									KuafuMatchMsgSender
											.send2SourceServer(
													userRoleId,
													InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
													ShenmoConstants.MATCH_FAIL_REASON_1);
									BusMsgSender.send2BusInner(userRoleId,
											InnerCmdType.INNER_KUAFU_LEAVE,
											null);
									ChuanQiLog
											.info("userRoleid={} shenmo match time out",
													userRoleId);
									continue;
								}
								Long matchTeamId = null;
								List<Integer> duans = request
										.getMatchDuans(times);
								// 优先找队伍
								for (Integer duan : duans) {
									List<MatchTeam> duanTeamList = duanTeamMap
											.get(duan);
									if (duanTeamList != null
											&& duanTeamList.size() > 0) {
										for (MatchTeam team : duanTeamList) {
											if (team.getMemberSize() > 0
													&& team.getMemberSize() < 4
													&& !team.hasMember(userRoleId)) {
												matchTeamId = team.getId();
												break;
											}
										}
									}
									if (matchTeamId != null) {
										break;
									}
								}
								if (matchTeamId != null) {
									// 找到了队伍
									MatchTeam team = matchTeams
											.get(matchTeamId);
									if (team != null) {
										team.addRequest(request);
									}
									tobeRemoved.add(userRoleId);
									ChuanQiLog.info("{} shenmo match team={}",
											userRoleId, team.getId());
								} else {
									// 未找到队伍
									Long matchUserRoleId = null;
									for (Integer duan : duans) {
										List<MatchRequest> duanRequestList = duanRequestMap
												.get(duan);
										if (duanRequestList != null
												&& duanRequestList.size() > 0) {
											int minP = Integer.MAX_VALUE;

											for (MatchRequest req : duanRequestList) {
												if (!req.getUserRoleId()
														.equals(userRoleId)
														&& !tobeRemoved.contains(req
																.getUserRoleId())
														&& getPartnerMatchTimes(req
																.getRequestTime()) != -1) {
													// 同一段位，匹配积分最接近的
													int p = Math.abs(req
															.getJifen()
															- request
																	.getJifen());
													if (p < minP) {
														minP = p;
														matchUserRoleId = req
																.getUserRoleId();
													}
												}
											}
										}
										if (matchUserRoleId != null) {
											break;
										}
									}
									if (matchUserRoleId != null) {
										matchUserSuccess(userRoleId,
												matchUserRoleId);
										tobeRemoved.add(userRoleId);
										tobeRemoved.add(matchUserRoleId);
										break;
									}
								}
							}
							for (Long e : tobeRemoved) {
								requestUserRoleIdList.remove(e);
								MatchRequest req = userRequestMap.remove(e);
								if (req != null) {
									for (List<MatchRequest> list : duanRequestMap
											.values()) {
										if (list.contains(req)) {
											list.remove(req);
											break;
										}
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
					} else {
						if (size > 0) {
							try {
								lock.tryLock(1, TimeUnit.SECONDS);
								if (lock.isHeldByCurrentThread()) {
									size = requestUserRoleIdList.size();
									if (size == 1) {
										Long requestUserRoleId = requestUserRoleIdList
												.get(0);
										MatchRequest request = userRequestMap
												.get(requestUserRoleId);
										if (request != null) {
											int times = getPartnerMatchTimes(request
													.getRequestTime());
											if (times == -1) {
												requestUserRoleIdList
														.remove(requestUserRoleId);
												userRequestMap
														.remove(requestUserRoleId);
												for (List<MatchRequest> list : duanRequestMap
														.values()) {
													if (list.contains(request)) {
														list.remove(request);
														break;
													}
												}
												KuafuMatchMsgSender
														.send2SourceServer(
																requestUserRoleId,
																InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
																ShenmoConstants.MATCH_FAIL_REASON_1);
												BusMsgSender
														.send2BusInner(
																requestUserRoleId,
																InnerCmdType.INNER_KUAFU_LEAVE,
																null);
												ChuanQiLog
														.info("userRoleId={} shenmo match time out",
																requestUserRoleId);
											}
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
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							ChuanQiLog.error("", e);
						}
					}
				}

			}
		});
		t.setDaemon(true);
		t.setName("shenmo-team-match-thread");
		t.start();
		ChuanQiLog.info("shenmo team match thread start");
	}

	private void matchUserSuccess(Long userRoleId1, Long userRoleId2) {
		MatchRequest request1 = userRequestMap.get(userRoleId1);
		if (request1 == null) {
			return;
		}
		MatchRequest request2 = userRequestMap.get(userRoleId2);
		if (request2 == null) {
			return;
		}
		MatchTeam team = new MatchTeam(request1.getDuan());
		team.addRequest(request1);
		team.addRequest(request2);
		matchTeams.put(team.getId(), team);
		List<MatchTeam> duanTeamList = duanTeamMap.get(team.getDuan());
		if (duanTeamList == null) {
			duanTeamList = new ArrayList<MatchTeam>();
			duanTeamMap.put(team.getDuan(), duanTeamList);
		}
		duanTeamList.add(team);
		ChuanQiLog.info("{} {} shenmo match user", userRoleId1, userRoleId2);
	}

	public void startMatchOpponentThread() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int teamSize = 0;
					try {
						teamSize = teamIdList.size();
					} catch (Exception e) {
						ChuanQiLog.error("", e);
					}
					if (teamSize > 1) {
						try {
							try {
								Thread.sleep(2000 / teamSize);
							} catch (InterruptedException e) {
								ChuanQiLog.error("", e);
							}
							lock.tryLock(1, TimeUnit.SECONDS);
							if (!lock.isHeldByCurrentThread()) {
								ChuanQiLog
										.error("shenmo match opponent thread can not get lock coursor is ={}",
												coursor);
								coursor++;
								if (coursor > 100) {
									lock = new ReentrantLock();
									coursor = 0;
									ChuanQiLog
											.info("shenmo opponent lock reset");
								}
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									ChuanQiLog.error("", e);
								}
								continue;
							}
							List<Long> toBeRemoved = new ArrayList<Long>();
							for (Long teamId : teamIdList) {
								MatchTeam team = matchTeams.get(teamId);
								if (team == null || team.getMemberSize() < 4) {
									toBeRemoved.add(teamId);
									continue;
								}
								int times = getOpponentMatchTimes(team
										.getFullTime());
								if (times == -1) {
									toBeRemoved.add(teamId);
									Set<Long> roleIds = team
											.getMemberUserRoleIds();
									for (Long e : roleIds) {
										KuafuMatchMsgSender
												.send2SourceServer(
														e,
														InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
														ShenmoConstants.MATCH_FAIL_REASON_1);
										BusMsgSender.send2BusInner(e,
												InnerCmdType.INNER_KUAFU_LEAVE,
												null);
									}
									continue;
								} else {
									Long matchTeamId = null;
									List<Integer> duans = team
											.getMatchDuans(times);
									// 优先找队伍
									for (Integer duan : duans) {
										List<MatchTeam> duanTeamList = duanTeamMap
												.get(duan);
										if (duanTeamList != null
												&& duanTeamList.size() > 0) {
											for (MatchTeam tmp : duanTeamList) {
												if (tmp.getId() != team.getId()) {
													matchTeamId = tmp.getId();
													break;
												}
											}
										}
										if (matchTeamId != null) {
											break;
										}
									}
									if (matchTeamId != null) {
										matchOpponentSuccess(teamId,
												matchTeamId);
										toBeRemoved.add(teamId);
										toBeRemoved.add(matchTeamId);
										break;
									}
								}
							}
							for (Long e : toBeRemoved) {
								MatchTeam team = matchTeams.get(e);
								teamIdList.remove(e);
								matchTeams.remove(e);
								for (List<MatchTeam> list : duanTeamMap
										.values()) {
									if (list.contains(team)) {
										list.remove(team);
										break;
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
					} else {
						if (teamSize > 0) {
							try {
								lock.tryLock(1, TimeUnit.SECONDS);
								if (lock.isHeldByCurrentThread()) {
									teamSize = teamIdList.size();
									if (teamSize == 1) {
										Long teamId = teamIdList.get(0);
										MatchTeam team = matchTeams.get(teamId);
										if (team != null) {
											int times = getOpponentMatchTimes(team
													.getFullTime());
											if (times == -1) {
												teamIdList.remove(teamId);
												matchTeams.remove(teamId);
												for (List<MatchTeam> list : duanTeamMap
														.values()) {
													if (list.contains(team)) {
														list.remove(team);
														break;
													}
												}
												Set<Long> roleIds = team
														.getMemberUserRoleIds();
												for (Long e : roleIds) {
													KuafuMatchMsgSender
															.send2SourceServer(
																	e,
																	InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
																	ShenmoConstants.MATCH_FAIL_REASON_1);
													BusMsgSender
															.send2BusInner(
																	e,
																	InnerCmdType.INNER_KUAFU_LEAVE,
																	null);
												}
											}
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
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							ChuanQiLog.error("", e);
						}

					}
				}

			}
		});
		t.setDaemon(true);
		t.setName("shenmo-opponent-match-thread");
		t.start();
		ChuanQiLog.info("shenmo opponent match thread start");

	}

	private void matchOpponentSuccess(Long teamId1, Long teamId2) {
		try {
			MatchTeam team1 = matchTeams.get(teamId1);
			MatchTeam team2 = matchTeams.get(teamId2);
			if (team1 == null || team2 == null) {
				return;
			}
			Redis redis = GameServerContext.getRedis();
			if (redis == null) {
				ChuanQiLog.error("teamId={},{} match success,redis is null",
						teamId1, teamId2);
				return;
			}
			Set<String> serverIdSets = redis.zrange(RedisKey.KUAFU_SERVER_LIST_KEY, 0, 0);
			if (serverIdSets == null || serverIdSets.size() == 0) {
				ChuanQiLog.error("no any kuafu server is available");
				return;
			}
			String serverId = null;
			for (String e : serverIdSets) {
				serverId = e;
			}
			if (serverId.equals(ChuanQiConfigUtil.getServerId())) {
				ChuanQiLog.error("选取跨服错误，不能选取匹配服务器作为跨服。");
				return;
			}
			long matchId = redis.generateId(RedisKey.KUAFU_MATCH_ID,
					RedisKey.KUAFU_MATCH_ID);
			// matchId有效期为1分钟
			StringBuffer sb = new StringBuffer();
			Set<Long> memberIdList1 = team1.getMemberUserRoleIds();
			for (Long e : memberIdList1) {
				sb.append(e.longValue()).append(":");
			}
			Set<Long> memberIdList2 = team2.getMemberUserRoleIds();
			for (Long e : memberIdList2) {
				sb.append(e.longValue()).append(":");
			}
			redis.setex(RedisKey.buildKMatchId(matchId), 1 * 60, sb.toString());
			for (Long e : memberIdList1) {
				KuafuMatchMsgSender.send2SourceServer(e,
						InnerCmdType.KUAFU_ARENA_4V4_MATCH_SUCCESS,
						new Object[] { serverId, matchId,
								GameConstants.SHENMO_TEAM_SHENBING });
				BusMsgSender.send2BusInner(e, InnerCmdType.INNER_KUAFU_LEAVE,
						null);
			}
			for (Long e : memberIdList2) {
				KuafuMatchMsgSender.send2SourceServer(e,
						InnerCmdType.KUAFU_ARENA_4V4_MATCH_SUCCESS,
						new Object[] { serverId, matchId,
								GameConstants.SHENMO_TEAM_XUEMO });
				BusMsgSender.send2BusInner(e, InnerCmdType.INNER_KUAFU_LEAVE,
						null);
			}
			ChuanQiLog.info("match success serverId={} matchId={},{},{}",
					new Object[] { serverId, matchId, teamId1, teamId2 });
		} catch (Exception e) {
			ChuanQiLog.error("error exists when matchSuccess", e);
		}
	}

	private int getPartnerMatchTimes(long requestTime) {
		ShenMoPublicConfig publicConfig = getPublicConfig();
		int matchJie1Time = publicConfig.getPiptime1();
		int matchJie2Time = publicConfig.getPiptime2();
		int matchJie3Time = publicConfig.getPiptime3();
		long currentTime = GameSystemTime.getSystemMillTime();
		if (matchJie1Time * 1000L + requestTime >= currentTime) {
			return 1;
		}
		if ((matchJie1Time + matchJie2Time) * 1000L + requestTime >= currentTime) {
			return 2;
		}
		if ((matchJie1Time + matchJie2Time + matchJie3Time) * 1000L
				+ requestTime >= currentTime) {
			return 3;
		}
		return -1;
	}

	private int getOpponentMatchTimes(long requestTime) {
		ShenMoPublicConfig publicConfig = getPublicConfig();
		int matchJie4Time = publicConfig.getPiptime4();
		int matchJie5Time = publicConfig.getPiptime5();
		long currentTime = GameSystemTime.getSystemMillTime();
		if (matchJie4Time * 1000L + requestTime >= currentTime) {
			return 1;
		}
		if ((matchJie4Time + matchJie5Time) * 1000L + requestTime >= currentTime) {
			return 2;
		}
		return -1;
	}

	private ReentrantLock lock = new ReentrantLock();
	/**
	 * 所有正在匹配的userRoleId
	 */
	private List<Long> requestUserRoleIdList = new ArrayList<Long>();
	/**
	 * key：userRoleId value：MatchRequest
	 */
	private Map<Long, MatchRequest> userRequestMap = new ConcurrentHashMap<Long, MatchRequest>();
	/**
	 * key:玩家duan value:按积分倒序排列的
	 */
	private Map<Integer, List<MatchRequest>> duanRequestMap = new ConcurrentHashMap<Integer, List<MatchRequest>>();

	public void kuafuArena4v4Match(Long userRoleId, String name, int jifen) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error(
					"userRoleId={}  shenmo match request ,redis is null",
					userRoleId);
			return;
		}
		int duan = shenMoPaiHangConfigExportService.getDuanByJifen(jifen);
		MatchRequest request = new MatchRequest(userRoleId, name, jifen, duan,
				GameSystemTime.getSystemMillTime());
		try {
			lock.tryLock(1, TimeUnit.SECONDS);
			if (lock.isHeldByCurrentThread()) {
				if (!requestUserRoleIdList.contains(userRoleId)) {
					requestUserRoleIdList.add(userRoleId);
				}
				userRequestMap.put(userRoleId, request);
				List<MatchRequest> list = duanRequestMap.get(duan);
				if (list == null) {
					list = new ArrayList<MatchRequest>();
					duanRequestMap.put(duan, list);
				}
				list.add(request);
				Collections.sort(list);
				ChuanQiLog.info("userRoleId={}  shenmo match request duan={}",
						new Object[] { userRoleId, duan });
			} else {
				// 获得锁失败，匹配失败

				KuafuMatchMsgSender.send2SourceServer(userRoleId,
						InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
						ShenmoConstants.MATCH_FAIL_REASON_2);
				BusMsgSender.send2BusInner(userRoleId,
						InnerCmdType.INNER_KUAFU_LEAVE, null);
				ChuanQiLog.error(
						"userRoleId={}  shenmo match can not get lock",
						userRoleId);
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public void kuafuArena4v4TeamMatch(Long userRoleId, String serverId,
			Object[] members) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			return;
		}
		try {
			lock.tryLock(1, TimeUnit.SECONDS);
			if (lock.isHeldByCurrentThread()) {
				int leaderDuan = 1;
				for (Object e : members) {
					Object[] info = (Object[]) e;
					Long roleId = (Long) info[0];
					if (roleId.longValue() == userRoleId.longValue()) {
						int jifen = (Integer) info[1];
						leaderDuan = shenMoPaiHangConfigExportService
								.getDuanByJifen(jifen);
						break;
					}
				}
				MatchTeam team = new MatchTeam(leaderDuan);
				team.setTeamEnter(true);
				matchTeams.put(team.getId(), team);
				List<MatchTeam> duanTeamList = duanTeamMap.get(team.getDuan());
				if (duanTeamList == null) {
					duanTeamList = new ArrayList<MatchTeam>();
					duanTeamMap.put(team.getDuan(), duanTeamList);
				}
				duanTeamList.add(team);
				for (Object e : members) {
					Object[] info = (Object[]) e;
					Long roleId = (Long) info[0];
					int jifen = (Integer) info[1];
					String name = (String) info[2];
					int duan = shenMoPaiHangConfigExportService
							.getDuanByJifen(jifen);
					MatchRequest request = new MatchRequest(roleId, name,
							jifen, duan, GameSystemTime.getSystemMillTime());
					userRequestMap.put(roleId, request);
					team.addRequest(request);
					ChuanQiLog.info(
							"userRoleId={}  shenmo team match request duan={}",
							new Object[] { roleId, duan });
				}
			} else {
				// 获得锁失败，匹配失败
				KuafuMatchMsgSender.send2SourceServer(userRoleId,
						InnerCmdType.KUAFU_ARENA_4V4_MATCH_FAIL,
						ShenmoConstants.MATCH_FAIL_REASON_2);
				BusMsgSender.send2BusInner(userRoleId,
						InnerCmdType.INNER_KUAFU_LEAVE, null);
				ChuanQiLog.error(
						"userRoleId={}  shenmo match can not get lock",
						userRoleId);
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public void cancelMatch(Long userRoleId, Boolean offline) {
		boolean success = false;
		try {
			lock.tryLock(1, TimeUnit.SECONDS);
			if (lock.isHeldByCurrentThread()) {
				requestUserRoleIdList.remove(userRoleId);
				MatchRequest req = userRequestMap.remove(userRoleId);
				if (req != null) {
					for (List<MatchRequest> list : duanRequestMap.values()) {
						if (list.contains(req)) {
							list.remove(req);
							break;
						}
					}
				}
				for (List<MatchTeam> list : duanTeamMap.values()) {
					MatchTeam tmp = null;
					for (MatchTeam team : list) {
						if (team.hasMember(userRoleId)) {
							tmp = team;
							break;
						}
					}
					if (tmp != null) {
						tmp.removeRequest(userRoleId);
						if (tmp.isFull() || tmp.isTeamEnter()) {
							teamIdList.remove(tmp.getId());
							matchTeams.remove(tmp.getId());
							list.remove(tmp);
							Set<Long> ids = tmp.getMemberUserRoleIds();
							for (Long e : ids) {
								if (e.longValue() != userRoleId.longValue()) {
									KuafuMatchMsgSender
											.send2SourceServer(
													e,
													InnerCmdType.KUAFU_ARENA_4V4_DISPOSE_TEAM,
													null);
									BusMsgSender.send2BusInner(e,
											InnerCmdType.INNER_KUAFU_LEAVE,
											null);
								}
							}
							tmp.setFull(false);
						} else {
							if (tmp.getMemberSize() == 0) {
								list.remove(tmp);
								matchTeams.remove(tmp.getId());
							}
						}
						break;
					}
				}
				success = true;
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		if (offline == null) {
			// 返回取消是否成功
			KuafuMatchMsgSender.send2SourceServer(userRoleId,
					InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH_RESULT, success);
		}
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
				null);
		ChuanQiLog.info("userRoleId={} cancel shenmo match request success={}",
				userRoleId, success);
	}
}
