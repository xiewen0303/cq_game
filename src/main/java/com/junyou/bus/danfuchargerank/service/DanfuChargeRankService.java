package com.junyou.bus.danfuchargerank.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.danfuchargerank.configure.export.DanFuChongZhiPaiMingConfig;
import com.junyou.bus.danfuchargerank.configure.export.DanFuChongZhiPaiMingConfigExportService;
import com.junyou.bus.danfuchargerank.configure.export.DanFuChongZhiPaiMingGroupConfig;
import com.junyou.bus.danfuchargerank.vo.DanfuChargeRankVo;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.recharge.export.RechargeExportService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class DanfuChargeRankService {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RechargeExportService rechargeExportService;

	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;

	public Object[] getInfo(Long userRoleId, Integer subId) {
		DanFuChongZhiPaiMingGroupConfig config = DanFuChongZhiPaiMingConfigExportService.getInstance().loadByMap(subId);
		if (config == null) {
			return null;
		}
		Object[] ret = new Object[11];
		ret[0] = config.getBg();
		ret[1] = config.getDesc();
		ret[2] = config.getVo();
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (activity == null) {
			ret[3] = new Object[] { 0, null };
		} else {
			long goldEndTime = activity.getEndTimeByMillSecond() - GOLD_RANK_END;
			ret[3] = getRankList(subId, activity.getStartTimeByMillSecond(),goldEndTime);
		}
//		List<DanfuChargeRankVo> rankList = rankListMap.get(subId);
//		if (rankList == null || rankList.size() == 0) {
//			ret[4] = null;
//		} else {
//			ret[4] = rankList.get(0).getName();
//		}
		if (rankMapMap.get(userRoleId) == null|| rankMapMap.get(userRoleId).get(subId) == null) {
			ret[4] = 0;
			if (chargeMap.get(userRoleId) == null || chargeMap.get(userRoleId).get(subId) == null) {
				long goldEndTime = activity.getEndTimeByMillSecond() - GOLD_RANK_END;
				int yb = rechargeExportService.getTotalRechargesByTime(userRoleId, activity.getStartTimeByMillSecond(),goldEndTime);
				ret[5] = yb;
				if (chargeMap.get(userRoleId) == null) {
					Map<Integer, Integer> map = new HashMap<Integer, Integer>();
					map.put(subId, yb);
					chargeMap.put(userRoleId, map);
				} else {
					chargeMap.get(userRoleId).put(subId, yb);
				}
			} else {
				ret[5] = chargeMap.get(userRoleId).get(subId);
			}
		} else {
			DanfuChargeRankVo myVo = rankMapMap.get(userRoleId).get(subId);
			ret[4] = myVo.getRank();
			ret[5] = myVo.getYb();
		}
		ret[6] = config.getMinCharge();
		ret[7] = config.getTxVo();
		ret[8] = config.getGoldItem();
		ret[9] = config.getExtraRank();
		ret[10] = config.getItemGold();
		return ret;
	}

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private Map<Integer, List<DanfuChargeRankVo>> rankListMap = new HashMap<Integer, List<DanfuChargeRankVo>>();
	private Map<Long, Map<Integer, DanfuChargeRankVo>> rankMapMap = new HashMap<Long, Map<Integer, DanfuChargeRankVo>>();
	private long lastFetchTime = 0L;
	private static long CACHE_TIME = 10 * 60 * 1000L;//排名刷新间隔
	private static long GOLD_RANK_END = 30 * 60 * 1000L;//活动结束前XX分钟截止元宝统计
	private static long SEND_RANK_REWARD = 10 * 60 * 1000L;//活动结束前XX分钟发送奖励

	public void fetchData(int subId, long startTime, long endTime) {
		Map<Integer, DanFuChongZhiPaiMingGroupConfig> groups = DanFuChongZhiPaiMingConfigExportService
				.getInstance().getAllConfig();
		if (groups.size() == 0) {
			return;
		}
		for (Map.Entry<Integer, DanFuChongZhiPaiMingGroupConfig> entry : groups.entrySet()) {
			if (entry.getKey().intValue() != subId) {
				continue;
			}
			// 是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if (configSong == null || !configSong.isRunActivity()) {
				continue;
			}
			DanFuChongZhiPaiMingGroupConfig config = DanFuChongZhiPaiMingConfigExportService.getInstance().loadByMap(entry.getKey());
			if (config == null) {
				continue;
			}
			try {
				lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
				List<DanfuChargeRankVo> rankList = rechargeExportService.getTotalRechargesByTime(startTime, endTime,config.getMaxRank(), config.getMinCharge());
				if (rankList != null && rankList.size() > 0) {
					rankListMap.put(subId, rankList);
					for (Map<Integer, DanfuChargeRankVo> e : rankMapMap.values()) {
						e.remove(subId);
					}
					for (DanfuChargeRankVo e : rankList) {
						if (rankMapMap.get(e.getUserRoleId()) == null) {
							Map<Integer, DanfuChargeRankVo> map = new HashMap<Integer, DanfuChargeRankVo>();
							map.put(subId, e);
							rankMapMap.put(e.getUserRoleId(), map);
						} else {
							rankMapMap.get(e.getUserRoleId()).put(subId, e);
						}
					}
				}
			} catch (InterruptedException e) {
				ChuanQiLog.error("", e);
			} finally {
				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
				}
			}
			break;
		}
	}

	public void handleUserCharge(Long userRoleId, long chargeTime, int yb) {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			boolean flag = false;
			for (List<DanfuChargeRankVo> list : rankListMap.values()) {
				for (DanfuChargeRankVo e : list) {
					if (e.getUserRoleId().equals(userRoleId)) {
						e.setYb(e.getYb() + yb);
						e.setUpdateTime(chargeTime);
						flag = true;
					}
				}
			}
			if (!flag) {
				Map<Integer, DanFuChongZhiPaiMingGroupConfig> groups = DanFuChongZhiPaiMingConfigExportService.getInstance().getAllConfig();
				if (groups.size() == 0) {
					return;
				}
				for (Map.Entry<Integer, DanFuChongZhiPaiMingGroupConfig> entry : groups.entrySet()) {
					// 是否在有这个活动或者是否在时间内
					ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
					if (configSong == null || !configSong.isRunActivity()) {
						continue;
					}
					DanFuChongZhiPaiMingGroupConfig config = DanFuChongZhiPaiMingConfigExportService.getInstance().loadByMap(entry.getKey());
					if (config == null) {
						continue;
					}
					long goldEndTime = configSong.getEndTimeByMillSecond() - GOLD_RANK_END;
					int totalYb = rechargeExportService.getTotalRechargesByTime(userRoleId,configSong.getStartTimeByMillSecond(),goldEndTime);
					if (config.getMinCharge() <= totalYb) {
						List<DanfuChargeRankVo> rankList = rankListMap.get(entry.getKey());
						if (rankList == null) {
							rankList = new ArrayList<DanfuChargeRankVo>();
							rankListMap.put(entry.getKey(), rankList);
						}
						DanfuChargeRankVo vo = new DanfuChargeRankVo();
						vo.setUserRoleId(userRoleId);
						vo.setYb(totalYb);
						vo.setUpdateTime(GameSystemTime.getSystemMillTime());
						String name = null;
						if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
							name = roleExportService.getLoginRole(userRoleId).getName();
						} else {
							name = roleExportService.getUserRoleFromDb(userRoleId).getName();
						}
						vo.setName(name);
						vo.setRank(rankList.size() + 1);
						rankList.add(vo);
						Collections.sort(rankList);
						int count = 1;
						for (DanfuChargeRankVo e : rankList) {
							Map<Integer, DanfuChargeRankVo> map = new HashMap<Integer, DanfuChargeRankVo>();
							map.put(entry.getKey(), e);
							rankMapMap.put(e.getUserRoleId(), map);
							e.setRank(count);
							count++;
						}
						Map<Integer, Integer> map = chargeMap.get(userRoleId);
						if (map != null) {
							map.put(entry.getKey(), totalYb);
						} else {
							map = new HashMap<Integer, Integer>();
							map.put(entry.getKey(), totalYb);
							chargeMap.put(userRoleId, map);
						}
					}
				}
			}

		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public Object[] getRankInfo(boolean checkVersion, Long userRoleId,int version, int subId) {
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (activity == null) {
			return new Object[] { subId, new Object[] { 0, 0, null } };
		} else {
			// 版本不一样
			if (checkVersion && activity.getClientVersion() != version) {
				// 处理数据变化:
				Object[] data = getInfo(userRoleId, subId);
				BusMsgSender.send2One(userRoleId,ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
				return null;
			}
			long goldEndTime = activity.getEndTimeByMillSecond() - GOLD_RANK_END;
			return new Object[] {
					subId,
					getRankList(subId,activity.getStartTimeByMillSecond(),goldEndTime) };
		}
	}

	private Map<Long, Map<Integer, Integer>> chargeMap = new HashMap<Long, Map<Integer, Integer>>();

	public Object[] getMyInfo(Long userRoleId, int subId, int version) {
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (activity == null) {
			return new Object[] { subId, new Object[] { null, 0, 0 } };
		} else {
			// 版本不一样
			if (activity.getClientVersion() != version) {
				// 处理数据变化:
				Object[] data = getInfo(userRoleId, subId);
				BusMsgSender.send2One(userRoleId,ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
				return null;
			}
			Object[] ret = new Object[2];
			try {
				lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
//				List<DanfuChargeRankVo> rankList = rankListMap.get(subId);
//				if (rankList == null || rankList.size() == 0) {
//					ret[0] = null;
//				} else {
//					ret[0] = rankList.get(0).getName();
//				}
				if (rankMapMap.get(userRoleId) == null || rankMapMap.get(userRoleId).get(subId) == null) {
					ret[0] = 0;
					if (chargeMap.get(userRoleId) == null || chargeMap.get(userRoleId).get(subId) == null) {
						long goldEndTime = activity.getEndTimeByMillSecond() - GOLD_RANK_END;
						int yb = rechargeExportService.getTotalRechargesByTime(
								userRoleId,
								activity.getStartTimeByMillSecond(),
								goldEndTime);
						ret[1] = yb;
						if (chargeMap.get(userRoleId) == null) {
							Map<Integer, Integer> map = new HashMap<Integer, Integer>();
							map.put(subId, yb);
							chargeMap.put(userRoleId, map);
						} else {
							chargeMap.get(userRoleId).put(subId, yb);
						}
					} else {
						ret[1] = chargeMap.get(userRoleId).get(subId);
					}
				} else {
					DanfuChargeRankVo myVo = rankMapMap.get(userRoleId).get(subId);
					ret[0] = myVo.getRank();
					ret[1] = myVo.getYb();
				}
			} catch (InterruptedException e) {
				ChuanQiLog.error("", e);
			} finally {
				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
				}
			}
			return new Object[] { subId, ret };
		}
	}

	/**
	 * 从0 开始
	 * 
	 * @param pageIndex
	 * @return
	 */

	private Object[] getRankList(int subId, long startTime, long endTime) {
		long currentTime = GameSystemTime.getSystemMillTime();
		if (currentTime > (lastFetchTime + CACHE_TIME)) {
			// 应该重新拉
			fetchData(subId, startTime, endTime);
		}
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			List<DanfuChargeRankVo> rankList = rankListMap.get(subId);
			if (rankList == null || rankList.size() == 0) {
				return new Object[] { 0, null };
			}
			Object[] ret = new Object[2];
			ret[0] = rankList.size();
//			int fromIndex = pageIndex * recordsPerPage;
//			int toIndex = fromIndex + recordsPerPage;
//			if (fromIndex > rankList.size() - 1) {
//				toIndex = rankList.size();
//				fromIndex = toIndex - recordsPerPage - 1;
//				if (fromIndex < 0) {
//					fromIndex = 0;
//				}
//			}
//			if (toIndex > rankList.size() - 1) {
//				toIndex = rankList.size();
//			}
//			List<DanfuChargeRankVo> retList = rankList.subList(fromIndex,toIndex);
			Object[] rankArray = new Object[rankList.size()];
			for (int i = 0; i < rankList.size(); i++) {
				DanfuChargeRankVo vo = rankList.get(i);
				rankArray[i] = new Object[] { vo.getRank(), vo.getName(),vo.getYb() };
			}
			ret[1] = rankArray;
//			int totalSize = rankList.size();
//			int totalPage = (totalSize % recordsPerPage == 0) ? (totalSize / recordsPerPage) : (totalSize / recordsPerPage + 1);
//			for (int i = 0; i < totalPage; i++) {
//				if ((i + 1) * recordsPerPage > fromIndex) {
//					pageIndex = i;
//					break;
//				}
//			}
			//ret[1] = pageIndex;
			return ret;
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		return new Object[] { 0,  null };
	}

	/**
	 * 结算邮件
	 */
	public void danfuChargeRankDayJob() {
		Map<Integer, DanFuChongZhiPaiMingGroupConfig> groups = DanFuChongZhiPaiMingConfigExportService.getInstance().getAllConfig();
		if (groups.size() == 0) {
			return;
		}
		long curTime = GameSystemTime.getSystemMillTime();
		// 循环充值礼包配置数据
		for (Map.Entry<Integer, DanFuChongZhiPaiMingGroupConfig> entry : groups.entrySet()) {
			// 判断活动是否结束
			ActivityConfigSon configSong = ActivityAnalysisManager
					.getInstance().loadByZiId(entry.getKey());
			if (configSong != null) {
				long endTime = configSong.getEndTimeByMillSecond();// 活动结束时间
				long emailTime = endTime - SEND_RANK_REWARD;
				long goldRankEnd = endTime - GOLD_RANK_END;
				long bigTime = 1 * 60 * 1000;// 结算间隔时间
				// 否则 当前时间减去结束时间   相差在1分钟之内
				if (emailTime - bigTime <= curTime && emailTime + bigTime >= curTime) {
					rankReward(entry.getKey(),configSong.getSubName(),configSong.getStartTimeByMillSecond(), goldRankEnd);
				}

			}

		}

	}

	public void rankReward(int subId,String acName, long startTime, long endTime) {
		DanFuChongZhiPaiMingGroupConfig groupConfig = DanFuChongZhiPaiMingConfigExportService.getInstance().loadByMap(subId);
		if (groupConfig == null) {
			return;
		}
		int maxRank = groupConfig.getMaxRank();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(groupConfig.getGoldItem());
		String goodsName = "";
		if(goodsConfig != null){
			goodsName = goodsConfig.getName();//额外道具的名字
		}
		String gold = String.valueOf(groupConfig.getItemGold());//额外道具需要的元宝
		fetchData(subId, startTime, endTime);
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			List<DanfuChargeRankVo> rankList = rankListMap.get(subId);
			if (rankList != null && rankList.size() > 0) {
				for (DanfuChargeRankVo e : rankList) {
					int rank = e.getRank();
					if (rank <= maxRank) {
						long userRoleId = e.getUserRoleId();
						try {
							RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
							ChuanQiLog.info("danfu charge rank reward userRolId={} rank={}",new Object[] { userRoleId, rank,roleWrapper != null });
							if (roleWrapper != null) {
								// 是本服玩家
								DanFuChongZhiPaiMingConfig config = groupConfig.getConfigByRank(rank);
								Map<String, GoodsConfigureVo>  itemMap = config.getItemMap();
								//邮件内容
								String content = EmailUtil.getCodeEmail(AppErrorCode.DANFU_CHARGE_RANK_REWARD,acName,String.valueOf(rank));
								//是否可领取额外物品
								if(rank <= groupConfig.getExtraRank()){
									if(e.getYb() >= groupConfig.getItemGold()){
										mapAddItem(itemMap, groupConfig.getGoldItem());
										content = EmailUtil.getCodeEmail(AppErrorCode.DANFU_CHARGE_RANK_EWAI,acName,String.valueOf(rank),gold,goodsName);
									}else{
										content = EmailUtil.getCodeEmail(AppErrorCode.DANFU_CHARGE_RANK_NO_EWAI,acName,String.valueOf(rank),gold);
									}
								}
								String title = EmailUtil.getCodeEmail(AppErrorCode.DANFU_CHARGE_RANK_REWARD_TITLE);
								sendSystemEamilForSingle(userRoleId,title, content,itemMap);
							}
						} catch (Exception ex) {
							ChuanQiLog.info("danfu charge rank reward error userRolId={} rank={}",userRoleId, rank);
						}
					}
				}
			}
			rankListMap.remove(subId);
			for (Map<Integer, DanfuChargeRankVo> e : rankMapMap.values()) {
				e.remove(subId);
			}
			ChuanQiLog.info("本次单服充值排名结束 subId={}", subId);
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
	
	
	/**
	 * 道具map加道具
	 */
	public void mapAddItem(Map<String,GoodsConfigureVo> main,String goodsId){
		if(goodsId == null || "".equals(goodsId)){
			return;
		}
		GoodsConfigureVo vo = main.get(goodsId);
		if(vo == null){
			vo = new GoodsConfigureVo(goodsId, 1);
		}else{
			vo.setGoodsCount(vo.getGoodsCount()+1);
		}
		main.put(goodsId, vo);
	}

	/**
	 * 发送玩家排名奖励
	 * 
	 * @param userRoleId
	 */
	private void sendSystemEamilForSingle(Long userRoleId,String title, String content,Map<String, GoodsConfigureVo> itemMap) {
		try {
			String[] attachment = EmailUtil.getAttachmentsForGoodsVo(itemMap);
			for (String e : attachment) {
				emailExportService.sendEmailToOne(userRoleId,title,content ,GameConstants.EMAIL_TYPE_SINGLE, e);
			}
		} catch (Exception e) {
			ChuanQiLog.error("danfu charge rank sendSystemEamilForSingle error");
		}
	}

	public void handleUserModifyNameEvent(Long userRoleId, String afterName) {
		// 更新排行榜
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			if (rankListMap == null || rankListMap.size() == 0) {
				return;
			}
			for (List<DanfuChargeRankVo> list : rankListMap.values()) {
				for (DanfuChargeRankVo e : list) {
					if (e.getUserRoleId().equals(userRoleId)) {
						e.setName(afterName);
					}
				}
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
