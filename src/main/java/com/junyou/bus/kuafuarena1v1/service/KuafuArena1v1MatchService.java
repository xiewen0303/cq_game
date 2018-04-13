package com.junyou.bus.kuafuarena1v1.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuarena1v1.configure.export.GeRenPaiHangConfigExportService;
import com.junyou.bus.kuafuarena1v1.constants.KuafuArena1v1Constants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuArena1v1PublicConfig;
import com.junyou.kuafumatch.manager.KuafuMatchMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

@Service
public class KuafuArena1v1MatchService {
	@Autowired
	private GeRenPaiHangConfigExportService geRenPaiHangConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

	public static class MatchRequest implements Comparable<MatchRequest> {
		private Long userRoleId;
		private int jifen;
		private int duan;
		private boolean isSmallWin;
		private boolean isSmallLose;
		private boolean isBigWin;
		private boolean isBigLose;
		private long requestTime;

		public MatchRequest(Long userRoleId, int jifen, int duan,
				boolean isSmallWin, boolean isSmallLose, boolean isBigWin,
				boolean isBigLose, long requestTime) {
			super();
			this.userRoleId = userRoleId;
			this.jifen = jifen;
			this.duan = duan;
			this.isSmallWin = isSmallWin;
			this.isSmallLose = isSmallLose;
			this.isBigWin = isBigWin;
			this.isBigLose = isBigLose;
			this.requestTime = requestTime;
		}

		public Long getUserRoleId() {
			return userRoleId;
		}

		public int getJifen() {
			return jifen;
		}

		public int getDuan() {
			return duan;
		}

		public boolean isSmallWin() {
			return isSmallWin;
		}

		public boolean isSmallLose() {
			return isSmallLose;
		}

		public boolean isBigWin() {
			return isBigWin;
		}

		public boolean isBigLose() {
			return isBigLose;
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

		public List<Integer> getMatchDuans(int times, int maxDuan) {
			List<Integer> ret = new ArrayList<Integer>();
			if (isBigWin) {
				if (times == 1) {
					if (duan + 5 < maxDuan) {
						ret.add(duan + 5);
					}
				} else if (times == 2) {
					for (int i = 5; i <= 7; i++) {
						if (duan + i < maxDuan) {
							ret.add(duan + i);
						}
					}
				} else if (times == 3) {
					for (int i = 5; i <= 7; i++) {
						if (duan + i < maxDuan) {
							ret.add(duan + i);
						}
					}
					for (int i = 4; i >= -2; i--) {
						if (duan + i < maxDuan && duan + i > 0) {
							ret.add(duan + i);
						}
					}
				}
			} else if (isSmallWin) {
				if (times == 1) {
					if (duan + 3 < maxDuan) {
						ret.add(duan + 3);
					}
				} else if (times == 2) {
					for (int i = 3; i <= 5; i++) {
						if (duan + i < maxDuan) {
							ret.add(duan + i);
						}
					}
				} else if (times == 3) {
					for (int i = 3; i <= 5; i++) {
						if (duan + i < maxDuan) {
							ret.add(duan + i);
						}
					}
					for (int i = 2; i >= -2; i--) {
						if (duan + i < maxDuan && duan + i > 0) {
							ret.add(duan + i);
						}
					}
				}
			} else if (isBigLose) {
				if (times == 1) {
					if (duan - 5 > 0) {
						ret.add(duan - 5);
					}
				} else if (times == 2) {
					for (int i = -5; i >= -7; i--) {
						if (duan + i > 0) {
							ret.add(duan + i);
						}
					}
				} else if (times == 3) {
					for (int i = -5; i >= -7; i--) {
						if (duan + i > 0) {
							ret.add(duan + i);
						}
					}
					for (int i = -4; i <= 2; i++) {
						if (duan + i < maxDuan && duan + i > 0) {
							ret.add(duan + i);
						}
					}
				}

			} else if (isSmallLose) {
				if (times == 1) {
					if (duan - 3 > 0) {
						ret.add(duan - 3);
					}
				} else if (times == 2) {
					for (int i = -3; i >= -5; i--) {
						if (duan + i > 0) {
							ret.add(duan + i);
						}
					}
				} else if (times == 3) {
					for (int i = -3; i >= -5; i--) {
						if (duan + i > 0) {
							ret.add(duan + i);
						}
					}
					for (int i = -2; i <= 2; i++) {
						if (duan + i < maxDuan && duan + i > 0) {
							ret.add(duan + i);
						}
					}
				}
			} else {
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
			}
			return ret;
		}
	}

	public KuafuArena1v1PublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_KUAFU_ARENA_1v1);
	}

	private int coursor = 0;

	public void startMatchThread() {
		final int maxDuan = geRenPaiHangConfigExportService.getMaxDuan();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int size = 0;
					try {
						size = requestUserRoleIdList.size();
					} catch (Exception e) {
						ChuanQiLog.error("", e);
					}
					if (size > 1) {
						try {
							try {
								Thread.sleep(2000 / size);
							} catch (InterruptedException e) {
								ChuanQiLog.error("", e);
							}
							lock.tryLock(1, TimeUnit.SECONDS);
							if (!lock.isHeldByCurrentThread()) {
								ChuanQiLog
										.error("match thread can not get lock coursor is ={}",
												coursor);
								coursor++;
								if (coursor > 100) {
									lock = new ReentrantLock();
									coursor = 0;
									ChuanQiLog.info("lock reset");
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
								int times = getMatchTimes(request
										.getRequestTime());
								if (times == -1) {
									// 匹配超时了
									tobeRemoved.add(userRoleId);
									// KuafuMatchMsgSender
									// .send2SourceServer(
									// userRoleId,
									// InnerCmdType.KUAFU_ARENA_1V1_MATCH_FAIL,
									// KuafuArena1v1Constants.MATCH_FAIL_REASON_1);
									BusMsgSender.send2BusInner(userRoleId,
											InnerCmdType.INNER_KUAFU_LEAVE,
											null);
									ChuanQiLog.info(
											"userRoleid={} match time out",
											userRoleId);
									continue;
								}
								Long matchUserRoleId = null;
								List<Integer> duans = request.getMatchDuans(
										times, maxDuan);
								for (Integer duan : duans) {
									List<MatchRequest> duanRequestList = duanRequestMap
											.get(duan);
									if (duanRequestList != null
											&& duanRequestList.size() > 0) {
										int minP = Integer.MAX_VALUE;

										for (MatchRequest req : duanRequestList) {
											if (!req.getUserRoleId().equals(
													userRoleId)
													&& !tobeRemoved.contains(req
															.getUserRoleId())
													&& getMatchTimes(req
															.getRequestTime()) != -1) {
												// 同一段位，匹配积分最接近的
												int p = Math.abs(req.getJifen()
														- request.getJifen());
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
									matchSuccess(userRoleId, matchUserRoleId);
									tobeRemoved.add(userRoleId);
									tobeRemoved.add(matchUserRoleId);
									break;
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
											int times = getMatchTimes(request
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
												BusMsgSender
														.send2BusInner(
																requestUserRoleId,
																InnerCmdType.INNER_KUAFU_LEAVE,
																null);
												ChuanQiLog
														.info("userRoleId={} match time out",
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
		t.setName("kuafu-arena-match-thread");
		t.start();
		ChuanQiLog.info("kuafu arena match thread start");
	}

	private void matchSuccess(Long userRoleId1, Long userRoleId2) {
		try {
			Redis redis = GameServerContext.getRedis();
			if (redis == null) {
				ChuanQiLog.error(
						"userRoleId={},{} match success,redis is null",
						userRoleId1, userRoleId2);
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
			redis.setex(RedisKey.buildKMatchId(matchId), 1 * 60, userRoleId1
					+ ":" + userRoleId2);
			Object[] data = new Object[] { serverId, matchId };
			KuafuMatchMsgSender.send2SourceServer(userRoleId1,
					InnerCmdType.KUAFU_ARENA_1V1_MATCH_SUCCESS, data);
			BusMsgSender.send2BusInner(userRoleId1,
					InnerCmdType.INNER_KUAFU_LEAVE, null);
			KuafuMatchMsgSender.send2SourceServer(userRoleId2,
					InnerCmdType.KUAFU_ARENA_1V1_MATCH_SUCCESS, data);
			BusMsgSender.send2BusInner(userRoleId2,
					InnerCmdType.INNER_KUAFU_LEAVE, null);
			ChuanQiLog
					.info("match success serverId={} matchId={},{},{}",
							new Object[] { serverId, matchId, userRoleId1,
									userRoleId2 });
		} catch (Exception e) {
			ChuanQiLog.error("error exists when matchSuccess", e);
		}
	}

	private int getMatchTimes(long requestTime) {
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		int matchJie1Time = publicConfig.getMatchJie1();
		int matchJie2Time = publicConfig.getMatchJie2();
		int matchJie3Time = publicConfig.getMatchJie3();
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

	public void kuafuArena1v1Match(Long userRoleId, int jifen, int lianWin,
			int lianLose) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("userRoleId={} match request ,redis is null",
					userRoleId);
			return;
		}
		int duan = geRenPaiHangConfigExportService.getDuanByJifen(jifen);
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		int smallWin = publicConfig.getSmallWin();
		int bigWin = publicConfig.getBigWin();
		int smallLose = publicConfig.getSmallLose();
		int bigLose = publicConfig.getBigLose();
		MatchRequest request = new MatchRequest(userRoleId, jifen, duan,
				lianWin >= smallWin, lianLose >= smallLose, lianWin >= bigWin,
				lianLose >= bigLose, GameSystemTime.getSystemMillTime());
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
				ChuanQiLog
						.info("userRoleId={} match request duan={} lianwin={} lianlose={}",
								new Object[] { userRoleId, duan, lianWin,
										lianLose });
			} else {
				// 获得锁失败，匹配失败

				KuafuMatchMsgSender.send2SourceServer(userRoleId,
						InnerCmdType.KUAFU_ARENA_1V1_MATCH_FAIL,
						KuafuArena1v1Constants.MATCH_FAIL_REASON_2);
				BusMsgSender.send2BusInner(userRoleId,
						InnerCmdType.INNER_KUAFU_LEAVE, null);
				ChuanQiLog.error("userRoleId={} match can not get lock",
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

	public void cancelMatch(Long userRoleId,Boolean offline) {
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
				success = true;
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		if(offline==null){
			// 返回取消是否成功
			KuafuMatchMsgSender.send2SourceServer(userRoleId,
					InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH_RESULT, success);
		}
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
				null);
		ChuanQiLog.info("userRoleId={} cancel match request success={}",
				userRoleId, success);
	}
}
