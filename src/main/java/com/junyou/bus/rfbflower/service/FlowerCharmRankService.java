package com.junyou.bus.rfbflower.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Tuple;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbflower.configue.export.FlowerCharmRankConfig;
import com.junyou.bus.rfbflower.configue.export.FlowerCharmRankConfigExportService;
import com.junyou.bus.rfbflower.configue.export.FlowerCharmRankConfigGroup;
import com.junyou.bus.rfbflower.dao.RoleRfbFlowerDao;
import com.junyou.bus.rfbflower.entity.RoleRfbFlower;
import com.junyou.bus.rfbflower.util.FlowerCharmRank;
import com.junyou.bus.rfbflower.util.FlowerRankCompareTo;
import com.junyou.bus.rfbflower.util.RfbFlowerConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.PaiMingLogEvent;
import com.junyou.event.RfbFlowerRankBuyLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 热发布鲜花榜
 * 
 * @author lxn
 * 
 */
@Service
public class FlowerCharmRankService {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	@Autowired
	private RoleRfbFlowerDao roleRfbFlowerDao;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private List<FlowerCharmRank> rankList = null;
	private long lastFetchTime = 0L;
//	 private long rankCacheTime = 1 * 1000;// 排行榜缓存时间（1分钟）
	private long rankCacheTime = 10 * 60 * 1000;// 排行榜缓存时间（10分钟）

	/**
	 * 登陆后加载到缓存
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleRfbFlower> initRoleRfbFlower(Long userRoleId) {
		return roleRfbFlowerDao.initRoleRfbFlower(userRoleId);
	}

	/**
	 * 初始化某个子活动的热发布某个活动信息
	 * 
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId) {
		FlowerCharmRankConfigGroup group = FlowerCharmRankConfigExportService.getInstance().loadConfigBySubId(subId);
		if (group == null) {
			return null;// 配置不存在
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (configSong == null) {
			return null;
		}
		Object[] data = new Object[4];
		data[0] = group.getPic();
		data[1] = group.getDes();
		data[2] = group.getItemPrice().toString().replace(" ", "");
		data[3] = group.getClientData();
		return data;
	}

	/**
	 * 获取排行榜面板数据
	 */
	public Object[] getPanelInfo(int subId, Integer version, Long userRoleId, int beginIndex, int endIndex) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (configSong == null) {
			return AppErrorCode.ACTIVITY_END;
		}
		if (beginIndex > endIndex) {
			return null;
		}
		// 版本不一样
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		FlowerCharmRankConfigGroup group = FlowerCharmRankConfigExportService.getInstance().loadConfigBySubId(subId);
		if (group == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int maxRank = group.getMaxRank();
		if (endIndex > maxRank) {
			endIndex = maxRank;
		}

		RoleRfbFlower roleRfbFlower = getRoleRfbFlower(userRoleId, subId);
		Object[] data = new Object[8];
		data[0] = 1;
		data[1] = subId;
		data[2] = beginIndex;
		data[3] = endIndex;
		data[4] = getRank(userRoleId, subId, beginIndex, endIndex);
		data[5] = rankList != null ? rankList.size() : 0;
		data[6] = getMyOrder(userRoleId);
		data[7] = roleRfbFlower.getCharmValue();
		return data;
	}

	/**
	 * 购买鲜花
	 * 
	 * @param subId
	 * @param version
	 * @param userRoleId
	 * @param goodId
	 * @param num
	 * @return
	 */
	public Object[] buy(Integer subId, Integer version, Long userRoleId, String goodsId, Integer num) {

		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (configSong == null) {
			return AppErrorCode.ACTIVITY_END;
		}
		// 版本不一样
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		Integer consumeGold = FlowerCharmRankConfigExportService.getInstance().getFlowerPriceByGoodId(subId, goodsId);
		if (consumeGold == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		consumeGold = consumeGold * num;
		RoleAccountWrapper roleAccountWrapper = accountExportService.getAccountWrapper(userRoleId);
		if (roleAccountWrapper.getYb() < consumeGold) {
			return AppErrorCode.YUANBAO_NOT_ENOUGH;
		}
		Map<String, Integer> items = new HashMap<>();
		items.put(goodsId, num);
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, consumeGold, userRoleId, LogPrintHandle.CONSUME_FLOWER_RANK_BUG, true, LogPrintHandle.CBZ_FLOWER_RANK_BUG);
		roleBagExportService.putInBag(items, userRoleId, GoodsSource.FLOWER_RANK_BUY, true);
		// 日志
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new RfbFlowerRankBuyLogEvent(userRoleId, jsonArray, consumeGold));
		return AppErrorCode.OK;
	}

	/**
	 * 更新个人布鲜花榜魅力值数据
	 */
	public void updateRedisFlowerCharmRank(Long userRoleId, int charmValue) {

		Map<Integer, FlowerCharmRankConfigGroup> allActivity = FlowerCharmRankConfigExportService.getInstance().getAllConfig();
		if (allActivity == null || allActivity.size() == 0) {
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("更新鲜花榜个人数据失败！updateFlowerCharmRank,redis is null");
			return;
		}

		for (Entry<Integer, FlowerCharmRankConfigGroup> entry : allActivity.entrySet()) { // 可能配了多个活动
			// 是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if (configSong == null || !configSong.isRunActivity()) {
				continue;
			}
			int subId = entry.getKey();
			// 更新redis
			try {
				// 获得对应活动的key
				String flowerDataSubIdKey = RedisKey.getRedisKuafuRfbFlowerCharmKey(subId);
				// redis个人信息key
				String flowerRoleMapKey = RedisKey.getRedisKuafuRfbFlowerCharmRoleInfoKey(userRoleId);
				// 上榜总人数
				long count = redis.zcount(flowerDataSubIdKey);

				FlowerCharmRankConfigGroup group = FlowerCharmRankConfigExportService.getInstance().loadConfigBySubId(subId);
				int maxRank = 0;
				if (group != null) {
					maxRank = group.getMaxRank();
				} else {
					// 容错
					maxRank = RfbFlowerConstants.FLOWER_MAX_RANK;
				}
				String member = userRoleId.toString();
				Long myRank = redis.zrevrank(flowerDataSubIdKey, member);

				RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
				if (count < maxRank || myRank != null) {
					// 未满额或已在名单中
					redis.zincrby(flowerDataSubIdKey, charmValue, member);
					// 对应的个人数据map更新，这里更名了也刚好同步下名字
					redis.hset(flowerRoleMapKey, RfbFlowerConstants.FIELD_USER_NAME, roleWrapper.getName());
					redis.hset(flowerRoleMapKey, RfbFlowerConstants.FIELD_USER_JOB, roleWrapper.getConfigId().toString());
					redis.hset(flowerRoleMapKey, RfbFlowerConstants.FIELD_SHANGBANG_TIME, String.valueOf(GameSystemTime.getSystemMillTime())); // 最后收到花的时间
					redis.hset(flowerRoleMapKey, RfbFlowerConstants.FIELD_SERVER_ID, ChuanQiConfigUtil.getServerId());
				} else {

					// 获取最后一名来对比魅力值
					Set<Tuple> lastMember = redis.zrevrangeWithScore(flowerDataSubIdKey, maxRank - 1, maxRank - 1);
					RoleRfbFlower roleRfbFlower = getRoleRfbFlower(userRoleId, subId);
					int totalCharm = charmValue + roleRfbFlower.getCharmValue();// 加上之前的魅力值

					for (Tuple tuple : lastMember) {
						if (tuple.getScore() < totalCharm) {
							redis.zrem(flowerDataSubIdKey, tuple.getElement());
							// 更新个人榜单数据
							redis.zadd(flowerDataSubIdKey, totalCharm, member);
							// 对应的个人数据map更新
							redis.hset(flowerRoleMapKey, RfbFlowerConstants.FIELD_USER_NAME, roleWrapper.getName());
							redis.hset(flowerRoleMapKey, RfbFlowerConstants.FIELD_USER_JOB, roleWrapper.getConfigId().toString());
							redis.hset(flowerRoleMapKey, RfbFlowerConstants.FIELD_SHANGBANG_TIME, String.valueOf(GameSystemTime.getSystemMillTime()));

						}
					}
				}
			} catch (Exception e) {
				ChuanQiLog.error("更新redis个人鲜花榜数据出错：{},userRoleId={}", e, userRoleId);
			}
			// 更新缓存数据
			RoleRfbFlower roleRfbFlower = getRoleRfbFlower(userRoleId, subId);
			roleRfbFlower.setCharmValue(roleRfbFlower.getCharmValue() + charmValue);// 更新活动期间魅力值
			roleRfbFlowerDao.cacheUpdate(roleRfbFlower, userRoleId);

		}

	}

	private Object[] getRank(Long userRoleId, int subId, int beginIndex, int endIndex) {
		long currentTime = GameSystemTime.getSystemMillTime();
		if (currentTime - lastFetchTime > rankCacheTime) {
			fetchRank(subId);
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
						List<FlowerCharmRank> retList = rankList.subList(beginIndex, endIndex + 1);
						for (int i = 0; i < retList.size(); i++) {
							ret[i] = retList.get(i).toArray();
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

		return null;
	}

	/**
	 * 刷新数据排行榜
	 * 
	 * @param userRoleId
	 * @param subId
	 */
	private void fetchRank(int subId) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("fetch kuafu flower rank error,redis is null");
			return;
		}
		try {
			String flowerDataSubIdKey = RedisKey.getRedisKuafuRfbFlowerCharmKey(subId);
			List<FlowerCharmRank> list = new ArrayList<FlowerCharmRank>();
			Set<Tuple> set = redis.zrevrangeWithScore(flowerDataSubIdKey, 0, -1);
			String userName = null;
			int userJob = 0;
			String infoKey = null;
			FlowerCharmRank flowerCharmRank = null;
			for (Tuple tuple : set) {
				String member = tuple.getElement();
				Double charm = tuple.getScore();
				infoKey = RedisKey.getRedisKuafuRfbFlowerCharmRoleInfoKey(Long.parseLong(member));
				Map<String, String> infoMap = redis.hgetAll(infoKey);
				userName = infoMap.get(RfbFlowerConstants.FIELD_USER_NAME);
				String job = infoMap.get(RfbFlowerConstants.FIELD_USER_JOB);
				String time = infoMap.get(RfbFlowerConstants.FIELD_SHANGBANG_TIME);

				userJob = Integer.parseInt(job == null ? "0" : job);
				flowerCharmRank = new FlowerCharmRank();
				flowerCharmRank.setUserRoleId(Long.parseLong(member));
				flowerCharmRank.setUserName(userName);
				flowerCharmRank.setJob(userJob);
				flowerCharmRank.setCharmValue(charm.intValue());
				flowerCharmRank.setShangBangTime(Long.parseLong(time));
				list.add(flowerCharmRank);
			}

			lastFetchTime = GameSystemTime.getSystemMillTime();
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			rankList = list;
			Collections.sort(rankList, new FlowerRankCompareTo());
			for (int i = 0; i < rankList.size(); i++) {
				flowerCharmRank = rankList.get(i);
				flowerCharmRank.setRank(i + 1);// 排名
			}
			ChuanQiLog.info("fetch kuafu flowerCharm arena rank finish");
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}

	}

	/**
	 * 获取我的排行榜
	 * 
	 * @param userRoleId
	 * @return
	 */
	private int getMyOrder(long userRoleId) {
		if (rankList != null) {
			for (FlowerCharmRank vo : rankList) {
				if (vo.getUserRoleId() == userRoleId) {
					return vo.getRank();
				}
			}
		}
		return 0;
	}

	/**
	 * 获取状态数据,处理数据
	 * 
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getFlowerRankStates(Long userRoleId, int subId) {
		return null;
	}

	private RoleWrapper getRoleWrapper(long e) {
		RoleWrapper role = null;
		if (publicRoleStateService.isPublicOnline(e)) {
			role = roleExportService.getLoginRole(e);
		} else {
			role = roleExportService.getUserRoleFromDb(e);
		}
		return role;
	}

	private RoleRfbFlower getRoleRfbFlower(Long userRoleId, final int subId) {
		List<RoleRfbFlower> list = roleRfbFlowerDao.cacheLoadAll(userRoleId, new IQueryFilter<RoleRfbFlower>() {
			private boolean stop = false;

			@Override
			public boolean check(RoleRfbFlower info) {
				if (subId == info.getSubId().intValue()) {
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
			return list.get(0);
		} else {
			RoleRfbFlower roleRfbFlower = createEntity(userRoleId, subId);
			roleRfbFlowerDao.cacheInsert(roleRfbFlower, userRoleId);
			return roleRfbFlower;
		}
	}

	private RoleRfbFlower createEntity(Long userRoleId, int subId) {
		RoleRfbFlower entity = new RoleRfbFlower();
		entity.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		entity.setUserRoleId(userRoleId);
		entity.setSubId(subId);
		entity.setCharmValue(0);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		entity.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		return entity;
	}

	/**
	 * 当前存在活动的话会提前20秒结算 活动结算邮件
	 */
	public void flowerCharmRankJieSuan() {
		// 判断配置
		Map<Integer, FlowerCharmRankConfigGroup> allActivity = FlowerCharmRankConfigExportService.getInstance().getAllConfig();
		if (allActivity == null || allActivity.size() == 0) {
			return;
		}
		long curTime = GameSystemTime.getSystemMillTime();
		for (Entry<Integer, FlowerCharmRankConfigGroup> entry : allActivity.entrySet()) {
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if (configSong == null || !configSong.isRunActivity()) {
				continue;
			}
			long endTime = configSong.getEndTimeByMillSecond();// 活动结束时间
			long bigTime = 1 * 60 * 1000;// 结算间隔时间
			// 否则 当前时间减去结束时间 相差在1分钟之内
			if (endTime - bigTime <= curTime && endTime + bigTime >= curTime) {
				// 进行邮件结算奖励
				int subId = entry.getKey();
				// 刷新下排行榜
				fetchRank(subId);
				if (rankList != null && rankList.size() > 0) {
					//先打印排行日志
					try {
						JSONArray consumeItemArray = new JSONArray(); 
						parseJSONArray(rankList,consumeItemArray);
						GamePublishEvent.publishEvent(new PaiMingLogEvent(LogPrintHandle.PAIMING_XIANHUA_BANG,consumeItemArray));
					} catch (Exception e) {
						ChuanQiLog.error(""+e);
					}
					for (FlowerCharmRank e : rankList) {
						int rank = e.getRank();// 排名
						long charm = e.getCharmValue();// 魅力值
						long userRoleId = e.getUserRoleId();
						try {
							RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
							ChuanQiLog.info("kuafu flowerCharm rank reward userRolId={} rank={} isBenfu={}", new Object[] { userRoleId, rank, roleWrapper != null });
							if (roleWrapper != null) {
								// 是本服玩家
								FlowerCharmRankConfigGroup groupConfig = FlowerCharmRankConfigExportService.getInstance().loadConfigBySubId(subId);
								if (groupConfig != null) {
									FlowerCharmRankConfig rankConfig = groupConfig.getConfigById(rank);
									if (rankConfig != null) {
										// 发邮件
										sendSystemEamilForSingle(userRoleId, rank, rankConfig);
									} else {
										ChuanQiLog.error("kuafu flowerCharm rankConfig is null,userRoleId={},rank={},charm={}", userRoleId, rank, charm);
									}
								} else {
									ChuanQiLog.error("kuafu flowerCharm rankConfig  is null,userRoleId={},rank={},charm={}", userRoleId, rank, charm);
								}

							}
						} catch (Exception ex) {
							ChuanQiLog.info("kuafu flowerCharm rank send reward error userRolId={} rank={}", userRoleId, rank);
						}
					}
					//清除内存中的排行缓存
					try {
						lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
						rankList = null;
						lastFetchTime = GameSystemTime.getSystemMillTime();
						ChuanQiLog.info("本次鲜花榜活动结束 subId={}", subId);
					} catch (Exception e) {
						ChuanQiLog.error("", e);
					} finally {
						if (lock.isHeldByCurrentThread()) {
							lock.unlock();
						}
					}
					// 此次活动数据设置过期时间
					Redis redis = GameServerContext.getRedis();
					if (redis == null) {
						ChuanQiLog.error("flower data expire set error,redis is null");
						return;
					}
					String flowerDataSubIdKey = RedisKey.getRedisKuafuRfbFlowerCharmKey(subId);
					redis.expire(flowerDataSubIdKey, RfbFlowerConstants.FLOWER_OLD_DATA_EXPIRE_TIME);
				}
				
				
			}

		}

	}

	/**
	 * 转换成日志对应的格式
	 * @param goodsMap
	 * @param receiveItems
	 */
	public void parseJSONArray(List<FlowerCharmRank>  userList,JSONArray receiveItems) {
		if(userList == null || userList.size() == 0){
			return;
		}
		for (int i = 0; i < userList.size();i++) {
			FlowerCharmRank vo =  userList.get(i);
			Map<String,Object> entity = new HashMap<>();
			entity.put("userRoleId", vo.getUserRoleId());
			entity.put("name",vo.getUserName());
			entity.put("rank",vo.getRank());
			entity.put("numer",vo.getCharmValue());
			receiveItems.add(entity);
		}
	}
	
	/**
	 * 发送玩家排名奖励
	 * 
	 * @param userRoleId
	 */
	private void sendSystemEamilForSingle(Long userRoleId, int rank, FlowerCharmRankConfig config) {
		try {
			String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_FLOWER_RANK_REWARD_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_FLOWER_RANK_REWARD, String.valueOf(rank));
			String[] attachment = EmailUtil.getAttachments(config.getRewards());
			for (String e : attachment) {
				emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, e);
			}
		} catch (Exception e) {
			ChuanQiLog.error("kuafu flowerCharm rank sendSystemEamilForSingle error,userRoleId={}，rank={}", userRoleId, rank);
		}
	}

}
