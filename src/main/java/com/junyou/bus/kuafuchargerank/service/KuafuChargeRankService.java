package com.junyou.bus.kuafuchargerank.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.kuafuchargerank.configure.export.KuaFuChongZhiPaiMingConfig;
import com.junyou.bus.kuafuchargerank.configure.export.KuaFuChongZhiPaiMingConfigExportService;
import com.junyou.bus.kuafuchargerank.configure.export.KuaFuChongZhiPaiMingGroupConfig;
import com.junyou.bus.kuafuchargerank.vo.KuafuChargeRankVo;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.RedisKey;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class KuafuChargeRankService {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private EmailExportService emailExportService;

	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;

	public Object[] getInfo(Long userRoleId, Integer subId) {
		KuaFuChongZhiPaiMingGroupConfig config = KuaFuChongZhiPaiMingConfigExportService
				.getInstance().loadByMap(subId);
		if (config == null) {
			return null;
		}
		Object[] ret = new Object[8];
		ret[0] = config.getBg();
		ret[1] = config.getDesc();
		ret[2] = config.getVo();
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (activity == null) {
			ret[3] = new Object[] { 0, 0, null };
		} else {
			ret[3] = getRankList(subId, 0, 10,
					activity.getStartTimeByMillSecond(),
					activity.getEndTimeByMillSecond());
		}
		if (rankList == null || rankList.size() == 0) {
			ret[4] = null;
		} else {
			ret[4] = rankList.get(0).getName();
		}
		if (rankMap == null || rankMap.get(userRoleId) == null) {
			ret[5] = 0;
			ret[6] = 0;
		} else {
			KuafuChargeRankVo myVo = rankMap.get(userRoleId);
			if(myVo.getYb()< config.getMinCharge()){
				//没有满足上榜条件
				ret[5] =0;
			}else{
				ret[5] = myVo.getRank();
			}
			ret[6] = myVo.getYb();
		}
		
		Object[] specialItem = config.getClientSpecilItem() ;
		//第一名特殊奖励信息
		if(specialItem !=null && specialItem.length >0){
			ret[7] = new Object[]{
					config.getSpecilDesc(),
					specialItem
			};
		}
		
		return ret;
	}

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private List<KuafuChargeRankVo> rankList = null;
	private Map<Long, KuafuChargeRankVo> rankMap = null;
	private long lastFetchTime = 0L;
	private static long CACHE_TIME = 10 * 60 * 1000L;

	
	private Map<Long,String> nameCache = new HashMap<Long,String>();
	public void fetchData(long startTime, long endTime,Integer subId) {
		if (GameServerContext.getRedis() == null) {
			return;
		}
		String platformId = ChuanQiConfigUtil.getPlatfromId();
		String chargeTimeRankKey = RedisKey.buildChargeTimeRankKey(platformId);
		Set<String> chargeIds = GameServerContext.getRedis().zrangeByScore(chargeTimeRankKey, startTime, endTime);
		if (chargeIds == null || chargeIds.size() == 0) {
			lastFetchTime = GameSystemTime.getSystemMillTime();
			return;
		}
		String[] chargeIdKeyArray = new String[chargeIds.size()];
		int index = 0;
		for (String e : chargeIds) {
			chargeIdKeyArray[index++] = RedisKey.buildChargeDetailKey(e);
		}
		List<String> chargeDetails = GameServerContext.getRedis().mget(chargeIdKeyArray);
		if (chargeDetails == null || chargeDetails.size() == 0) {
			lastFetchTime = GameSystemTime.getSystemMillTime();
			return;
		}
		int validSzie = 0;
		for (String e : chargeDetails) {
			if (e != null && !e.equals("")) {
				validSzie++;
			}
		}
		if (validSzie == 0) {
			lastFetchTime = GameSystemTime.getSystemMillTime();
			return;
		}
		List<KuafuChargeRankVo> tmpRankList = new ArrayList<KuafuChargeRankVo>();
		Map<Long, KuafuChargeRankVo> tmpRankMap = new HashMap<Long, KuafuChargeRankVo>();
//		Long[] userRoleIdArray = new Long[validSzie];
		List<Long> userRoleIdList = new ArrayList<Long>();
//		String[] userRoleIdKeyArray = new String[validSzie];
		List<String> userRoleIdKeyList = new ArrayList<String>();
		
		KuaFuChongZhiPaiMingGroupConfig config = KuaFuChongZhiPaiMingConfigExportService.getInstance().loadByMap(subId);
//		int count = 0;
		for (int i = 0; i < chargeDetails.size(); i++) {
			String str = chargeDetails.get(i);
			if (str == null || str.equals("")) {
				continue;
			}
			String[] array = str.split(":");
			Long userRoleId = Long.parseLong(array[0]);
			Integer yb = Integer.parseInt(array[1]);
			long chargeTime = Long.parseLong(array[2]);
//			userRoleIdKeyArray[count] = RedisKey.buildChargeUserKey(userRoleId);
//			userRoleIdArray[count] = userRoleId;
//			count = count + 1;
			if(!userRoleIdList.contains(userRoleId)&& nameCache.get(userRoleId) == null){
				userRoleIdList.add(userRoleId);
				userRoleIdKeyList.add(RedisKey.buildChargeUserKey(userRoleId));
			}
			KuafuChargeRankVo vo = tmpRankMap.get(userRoleId);
			if (vo == null) {
				vo = new KuafuChargeRankVo();
				vo.setUserRoleId(userRoleId);
				tmpRankMap.put(userRoleId, vo);
				tmpRankList.add(vo);
			}
			vo.addYb(yb);
			vo.setUpdateTime(chargeTime);
		}
		Long[] userRoleIdArray = new Long[userRoleIdList.size()];
		userRoleIdList.toArray(userRoleIdArray);
		String[] userRoleIdKeyArray = new String[userRoleIdKeyList.size()];
		userRoleIdKeyList.toArray(userRoleIdKeyArray);
		
		List<String> names = null;
		if(userRoleIdKeyArray !=null && userRoleIdKeyArray.length >0){
			names = GameServerContext.getRedis().mget(userRoleIdKeyArray);
		}
		Map<Long, String> nameMap = new HashMap<Long, String>();
		for (int i = 0; i < userRoleIdArray.length; i++) {
			Long id = userRoleIdArray[i];
			if(names!=null){
				if (!nameMap.containsKey(id)) {
					nameMap.put(id, names.get(i));
				}
			}
		}
		for (KuafuChargeRankVo e : tmpRankList) {
			String name = nameMap.get(e.getUserRoleId());
			if(name == null){
				name = nameCache.get(e.getUserRoleId());
			}else{
				nameCache.put(e.getUserRoleId(), name);
			}
			e.setName(name);
		}
		Collections.sort(tmpRankList);
		for (int i = 0; i < tmpRankList.size(); i++) {
			tmpRankList.get(i).setRank(i + 1);
		}
		
		if(tmpRankList.size() >0){
			int subIndex=-1;
			for(int i=0 ; i<tmpRankList.size() ; i++){
				if(tmpRankList.get(i).getYb() <config.getMinCharge()){
					subIndex=i;
					break;
				}
			}
			
			if(subIndex ==0){
				tmpRankList =Collections.emptyList();
			}else if(subIndex > 0) {
				tmpRankList = tmpRankList.subList(0, subIndex);
			}
		}
		
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			rankList = tmpRankList;
			rankMap = tmpRankMap;
			lastFetchTime = GameSystemTime.getSystemMillTime();
		} catch (InterruptedException e) {
			ChuanQiLog.error("set kuafu charge rank data error", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}

	public Object[] getRankInfo(boolean checkVersion, Long userRoleId, int version, int subId, int pageIndex, int recordsPerPage) {
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
			return new Object[] {
					subId,
					getRankList(subId, pageIndex, recordsPerPage,activity.getStartTimeByMillSecond(),activity.getEndTimeByMillSecond()) };
		}
	}

	public Object[] getMyInfo(Long userRoleId, int subId, int version) {
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		
		KuaFuChongZhiPaiMingGroupConfig config = KuaFuChongZhiPaiMingConfigExportService
				.getInstance().loadByMap(subId);
		if (activity == null) {
			return new Object[] { subId, new Object[] { null, 0, 0 } };
		} else {
			// 版本不一样
			if (activity.getClientVersion() != version) {
				// 处理数据变化:
				Object[] data = getInfo(userRoleId, subId);
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
				return null;
			}
			Object[] ret = new Object[3];
			try {
				lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
				if (rankList == null || rankList.size() == 0) {
					ret[0] = null;
				} else {
					ret[0] = rankList.get(0).getName();
				}
				if (rankMap == null || rankMap.get(userRoleId) == null) {
					ret[1] = 0;
					ret[2] = 0;
				} else {
					KuafuChargeRankVo myVo = rankMap.get(userRoleId);
					if(myVo.getYb() < config.getMinCharge()){
						ret[1] = 0;
					}else{
						ret[1] = myVo.getRank();
					}
					
					ret[2] = myVo.getYb();
				}
			} catch (InterruptedException e) {
				ChuanQiLog.error("", e);
			} finally {
				if(lock.isHeldByCurrentThread()){
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

	private Object[] getRankList(int subId, int pageIndex, int recordsPerPage,
			long startTime, long endTime) {
		long currentTime = GameSystemTime.getSystemMillTime();
		if (currentTime > (lastFetchTime + CACHE_TIME)) {
			// 应该重新拉
			fetchData(startTime, endTime,subId);
		}
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			if (rankList == null || rankList.size() == 0) {
				return new Object[] { 0, 0, null };
			}
			Object[] ret = new Object[3];
			ret[0] = rankList.size();
			int fromIndex = pageIndex * recordsPerPage;
			int toIndex = fromIndex + recordsPerPage;
			if (fromIndex > rankList.size() - 1) {
				toIndex = rankList.size();
				fromIndex = toIndex - recordsPerPage - 1;
				if (fromIndex < 0) {
					fromIndex = 0;
				}
			}
			if (toIndex > rankList.size() - 1) {
				toIndex = rankList.size();
			}
			List<KuafuChargeRankVo> retList = rankList.subList(fromIndex,toIndex);
			Object[] rankArray = new Object[retList.size()];
			for (int i = 0; i < retList.size(); i++) {
				KuafuChargeRankVo vo = retList.get(i);
				rankArray[i] = new Object[] { vo.getRank(), vo.getName() };
			}
			ret[2] = rankArray;
			int totalSize = rankList.size();
			int totalPage = (totalSize % recordsPerPage == 0) ? (totalSize / recordsPerPage)
					: (totalSize / recordsPerPage + 1);
			for (int i = 0; i < totalPage; i++) {
				if ((i + 1) * recordsPerPage > fromIndex) {
					pageIndex = i;
					break;
				}
			}
			ret[1] = pageIndex;
			return ret;
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		return new Object[] { 0, 0, null };
	}

	private final static int DATA_EXPIRE_TIME = 30 * 24 * 60 * 60;

	public void handleUserCharge(Long userRoleId, long chargeTime, int yb) {
		ChuanQiLog.info("handle userRoleId={} charge yb={} time={}", new Object[] {userRoleId, yb, chargeTime });
		if (GameServerContext.getRedis() == null) {
			ChuanQiLog.error("no redis when handleUserCharge");
			return;
		}
		String platformId = ChuanQiConfigUtil.getPlatfromId();
		String chargeTimeRankKey = RedisKey.buildChargeTimeRankKey(platformId);
		Long chargeId = IdFactory.getInstance().generateId(ServerIdType.COMMON);
		GameServerContext.getRedis().zadd(chargeTimeRankKey, chargeTime,String.valueOf(chargeId));

		String chargeDetailKey = RedisKey.buildChargeDetailKey(chargeId);
		GameServerContext.getRedis().setex(
				chargeDetailKey,
				DATA_EXPIRE_TIME,
				String.valueOf(userRoleId) + ":" + String.valueOf(yb) + ":" + String.valueOf(chargeTime));

		String chargeUserKey = RedisKey.buildChargeUserKey(userRoleId);
		String name = null;
		if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
			name = roleExportService.getLoginRole(userRoleId).getName();
		} else {
			name = roleExportService.getUserRoleFromDb(userRoleId)
					.getName();
		}
		GameServerContext.getRedis().setex(chargeUserKey, DATA_EXPIRE_TIME,
				name);
		ChuanQiLog.info("userRoleId={} charge yb={} time={} success", new Object[] {
				userRoleId, yb, chargeTime });
	}

	/**
	 * 结算邮件
	 */
	public void kuafuChargeRankDayJob() {
		Map<Integer, KuaFuChongZhiPaiMingGroupConfig> groups = KuaFuChongZhiPaiMingConfigExportService
				.getInstance().getAllConfig();
		if (groups.size() == 0) {
			return;
		}
		long curTime = GameSystemTime.getSystemMillTime();
		// 循环充值礼包配置数据
		for (Map.Entry<Integer, KuaFuChongZhiPaiMingGroupConfig> entry : groups
				.entrySet()) {
			// 判断活动是否结束
			ActivityConfigSon configSong = ActivityAnalysisManager
					.getInstance().loadByZiId(entry.getKey());
			if (configSong != null) {
				long endTime = configSong.getEndTimeByMillSecond();// 活动结束时间
				long bigTime = 1 * 60 * 1000;// 结算间隔时间
				if (endTime - bigTime <= curTime
						&& endTime + bigTime >= curTime) {// 否则 当前时间减去结束时间
															// 相差在1分钟之内
					rankReward(entry.getKey(),
							configSong.getStartTimeByMillSecond(), endTime);
				}

			}

		}

	}

	public void rankReward(int subId, long startTime, long endTime) {
		KuaFuChongZhiPaiMingGroupConfig groupConfig = KuaFuChongZhiPaiMingConfigExportService
				.getInstance().loadByMap(subId);
		if (groupConfig == null) {
			return;
		}
		int maxRank = groupConfig.getMaxRank();
		fetchData(startTime, endTime,subId);
		if (rankList != null && rankList.size() > 0) {
			for (KuafuChargeRankVo e : rankList) {
				int rank = e.getRank();
				if (rank <= maxRank) {
					long userRoleId = e.getUserRoleId();

					try {
						RoleWrapper roleWrapper = roleExportService
								.getUserRoleFromDb(userRoleId);
						ChuanQiLog
								.info("kuafu charge rank reward userRolId={} name={} rank={} isBenfu={}",
										new Object[] { userRoleId, e.getName() ,rank,
												roleWrapper != null });
						
						boolean canSendSpecialItem = false;
						if(rank ==1){
							// 判断第一名是否满足特殊奖励发放要求
							int specialLimit = groupConfig.getTop1SpecilLimit();
							if(e.getYb() >=specialLimit){
								canSendSpecialItem =true;
							}
							
							ChuanQiLog
							.info("跨服充值排名第一名特殊奖励验证结果:{}, userRolId={} name={} rank={} isBenfu={}",
									new Object[] { canSendSpecialItem,userRoleId, e.getName() ,rank,
											roleWrapper != null });
						}
						
						if (roleWrapper != null) {
							// 是本服玩家
							KuaFuChongZhiPaiMingConfig config = groupConfig
									.getConfigByRank(rank);
							sendSystemEmailForSingle(userRoleId, rank, config);
							
							if(canSendSpecialItem && groupConfig.getSpecilItemMap() !=null && groupConfig.getSpecilItemMap().size() >0){
								sendSpecialRewardEmailForSingle(userRoleId, rank, groupConfig.getSpecilItemMap());
							}
						}
					} catch (Exception ex) {
						ChuanQiLog
								.info("kuafu charge rank reward error userRolId={} rank={}",
										userRoleId, rank);
					}
				}
			}
		}
		rankList = null;
		rankMap = null;
		ChuanQiLog.info("本次跨服充值排名结束 subId={}", subId);
	}

	/**
	 * 发送玩家排名奖励
	 * 
	 * @param userRoleId
	 */
	private void sendSystemEmailForSingle(Long userRoleId, int rank,
			KuaFuChongZhiPaiMingConfig config) {
		try {
			String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_CHARGE_SPECIAL_REWARD_TITLE);
			String content = EmailUtil
					.getCodeEmail(AppErrorCode.KUAFU_CHARGE_RANK_REWARD,
							String.valueOf(rank));
			String[] attachment = EmailUtil.getAttachmentsForGoodsVo(config
					.getItemMap());
			for (String e : attachment) {
				emailExportService.sendEmailToOne(userRoleId,title, content,
						GameConstants.EMAIL_TYPE_SINGLE, e);
			}
		} catch (Exception e) {
			ChuanQiLog
					.error("kuafu charge rank sendSystemEamilForSingle error");
		}
	}
	
	/**
	 * 发送玩家排名奖励
	 * 
	 * @param userRoleId
	 */
	private void sendSpecialRewardEmailForSingle(Long userRoleId, int rank,
			Map<String, GoodsConfigureVo> item) {
		try {
			String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_CHARGE_SPECIAL_REWARD_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_CHARGE_SPECIAL_REWARD,String.valueOf(rank));
			String[] attachment = EmailUtil.getAttachmentsForGoodsVo(item);
			for (String e : attachment) {
				emailExportService.sendEmailToOne(userRoleId, title,content,
						GameConstants.EMAIL_TYPE_SINGLE, e);
			}
		} catch (Exception e) {
			ChuanQiLog
					.error("kuafu charge rank sendSpecialRewardEmailForSingle error");
		}
	}

	public void handleUserModifyNameEvent(Long userRoleId, String afterName) {
		String chargeUserKey = RedisKey.buildChargeUserKey(userRoleId);
		if (GameServerContext.getRedis().exists(chargeUserKey)) {
			//更新redis
			GameServerContext.getRedis().setex(chargeUserKey, DATA_EXPIRE_TIME,
					afterName);
			//更新排行榜
			try {
				lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
				if (rankList == null || rankList.size() == 0) {
					return ;
				}
				for(KuafuChargeRankVo e:rankList){
					if(e.getUserRoleId().equals(userRoleId)){
						e.setName(afterName);
					}
				}
			} catch (InterruptedException e) {
				ChuanQiLog.error("", e);
			} finally {
				if(lock.isHeldByCurrentThread()){
					lock.unlock();
				}
			}
		}
	}
}
