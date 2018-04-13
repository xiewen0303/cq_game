package com.junyou.bus.kuafuxiaofei.service;

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
import com.junyou.bus.kuafuxiaofei.configure.export.KuaFuXiaoFeiPaiMingConfig;
import com.junyou.bus.kuafuxiaofei.configure.export.KuaFuXiaoFeiPaiMingConfigExportService;
import com.junyou.bus.kuafuxiaofei.configure.export.KuaFuXiaoFeiPaiMingGroupConfig;
import com.junyou.bus.kuafuxiaofei.vo.KuafuXiaoFeiRankVo;
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
public class KuafuXiaoFeiRankService{

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;


	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private List<KuafuXiaoFeiRankVo> rankList = null;
	private Map<Long, KuafuXiaoFeiRankVo> rankMap = null;
	private long lastFetchTime = 0L;
	private static long CACHE_TIME = 5 * 60 * 1000L;

	
	private final static int DATA_EXPIRE_TIME = 30 * 24 * 60 * 60;

	
	public Object[] getRankInfo(boolean checkVersion, Long userRoleId,int version, int subId, int pageIndex, int recordsPerPage) {
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (activity == null) {
			return new Object[] { subId, new Object[] { 0, 0, null } };
		} else {
			// 版本不一样
			if (checkVersion && activity.getClientVersion() != version) {
				// 处理数据变化:
				Object[] data = getInfo(userRoleId, subId);
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.GET_ZHIDINGZIACTIVITY, new Object[]{subId,activity.getClientVersion(),data});
				return null;
			}
			KuaFuXiaoFeiPaiMingGroupConfig config = KuaFuXiaoFeiPaiMingConfigExportService.getInstance().loadByMap(subId);
			if (config == null) {
				return null;
			}
			int myRank = 0;
			int myYb = 0;
			if (rankList == null || rankList.size() == 0) {
				 return null;
			}else{
				KuafuXiaoFeiRankVo myVo = rankMap.get(userRoleId);
				if(myVo != null){
					if(myVo.getYb() > config.getMinXiaoFei()){
						myRank = myVo.getRank();
					}
					myYb = myVo.getYb();
				}
			}
			return new Object[] {
					subId,new Object[]{(rankList != null && rankList.size() > 0 ) ?  rankList.get(0).getName() :null,myRank,myYb}};
		}
	}
	
	/**
	 * 请求拉取子活动  10021
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getInfo(Long userRoleId, Integer subId) {
		KuaFuXiaoFeiPaiMingGroupConfig config = KuaFuXiaoFeiPaiMingConfigExportService.getInstance().loadByMap(subId);
		if (config == null) {
			return null;
		}
		Object[] ret = new Object[4];
		ret[0] = config.getBg();
//		ret[1] = config.getDesc();
		ret[1] = config.getVo();
		int rank = 0;
		int myYb = 0;
		if (rankList == null || rankList.size() == 0) {
			ret[2] = null;
		}else{
			KuafuXiaoFeiRankVo myVo = rankMap.get(userRoleId);
			if(myVo != null){
				if(myVo.getYb() > config.getMinXiaoFei()){
					rank = myVo.getRank();
				}
				myYb = myVo.getYb();
			}
		}
		ret[2] = new Object[]{(rankList != null && rankList.size() > 0 ) ?  rankList.get(0).getName() :null,rank,myYb};
		ret[3] = new Object[]{config.getTop1SpecilLimit(),config.getClientSpecilItem()};
		return ret;
	}
	
	public void handleUserXiaoFei(Long userRoleId, long xiaoFeiTime, int yb) {
		ChuanQiLog.info("handle userRoleId={} xiaofei yb={} time={}", new Object[] {
				userRoleId, yb, xiaoFeiTime });
		if (GameServerContext.getRedis() == null) {
			ChuanQiLog.error("no redis when handleUserXiaoFei");
			return;
		}
		
		String platformId = ChuanQiConfigUtil.getPlatfromId();
		String xiaoFeiTimeRankKey = RedisKey.buildXiaoFeiTimeRankKey(platformId);
		Long xiaoFeiId = IdFactory.getInstance().generateId(ServerIdType.COMMON);
		GameServerContext.getRedis().zadd(xiaoFeiTimeRankKey, xiaoFeiTime,
				String.valueOf(xiaoFeiId));

		String xiaoFeiDetailKey = RedisKey.buildXiaoFeiDetailKey(xiaoFeiId+"");
		GameServerContext.getRedis().setex(
				xiaoFeiDetailKey,
				DATA_EXPIRE_TIME,
				String.valueOf(userRoleId) + ":" + String.valueOf(yb) + ":"
						+ String.valueOf(xiaoFeiTime));

		
		String xiaoFeiUserKey = RedisKey.buildXiaoFeiUserKey(userRoleId);
//		String xiaoFeiUserConfigIdKey = RedisKey.buildXiaoFeiUserConfigIdKey(userRoleId);
		String name = null;
		if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
			RoleWrapper role  = roleExportService.getLoginRole(userRoleId);
			name = role.getName();
		}else{
			RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
			name = role.getName();
		}
		GameServerContext.getRedis().setex(xiaoFeiUserKey, DATA_EXPIRE_TIME,name);
//		GameServerContext.getRedis().setex(xiaoFeiUserConfigIdKey, DATA_EXPIRE_TIME,configId+"");
		ChuanQiLog.info("userRoleId={} xiaofei yb={} time={} success", new Object[] {userRoleId, yb, xiaoFeiTime });
	}

	public Object[] getRankInfoList(boolean checkVersion, Long userRoleId,
			int version, int subId, int pageIndex, int recordsPerPage) {
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (activity == null) {
			return null;
		} else {
			// 版本不一样
			if (checkVersion && activity.getClientVersion() != version) {
				// 处理数据变化:
				Object[] data = getInfo(userRoleId, subId);
				BusMsgSender.send2One(userRoleId,
						ClientCmdType.GET_ZHIDINGZIACTIVITY, new Object[]{subId,activity.getClientVersion(),data});
				return null;
			}
			return new Object[] {AppErrorCode.SUCCESS,
					subId,
					pageIndex,
					recordsPerPage,
					getRankList(subId, pageIndex, recordsPerPage,
							activity.getStartTimeByMillSecond(),
							activity.getEndTimeByMillSecond()),rankList == null ? 0 : rankList.size()};
		}
	}
	
	
	/**
	 * 从0 开始
	 * 
	 * @param pageIndex
	 * @return
	 */

	private Object[] getRankList(int subId, int pageIndex, int recordsPerPage,long startTime, long endTime) {
		long currentTime = GameSystemTime.getSystemMillTime();
		if (currentTime > (lastFetchTime + CACHE_TIME)) {
			// 应该重新拉
			fetchData(startTime, endTime,subId);
		}
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			if (rankList == null || rankList.size() == 0) {
				return null;
			}
//			Object[] ret = new Object[3];
//			ret[0] = rankList.size();
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
			List<KuafuXiaoFeiRankVo> retList = rankList.subList(fromIndex,toIndex);
			Object[] ret = new Object[retList.size()];
			for (int i = 0; i < retList.size(); i++) {
				KuafuXiaoFeiRankVo vo = retList.get(i);
				ret[i] = new Object[] {vo.getName()};
			}
//			ret[2] = rankArray;
//			int totalSize = rankList.size();
//			int totalPage = (totalSize % recordsPerPage == 0) ? (totalSize / recordsPerPage)
//					: (totalSize / recordsPerPage + 1);
//			for (int i = 0; i < totalPage; i++) {
//				if ((i + 1) * recordsPerPage > fromIndex) {
//					pageIndex = i;
//					break;
//				}
//			}
//			ret[1] = pageIndex;
			return ret;
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		return null;
	}
	private Map<Long,String> nameCache = new HashMap<Long,String>();
	public void fetchData(long startTime, long endTime,Integer subId) {
		if (GameServerContext.getRedis() == null) {
			return;
		}
		String platformId = ChuanQiConfigUtil.getPlatfromId();
		String xiaoFeiTimeRankKey = RedisKey.buildXiaoFeiTimeRankKey(platformId);
		Set<String> xiaoFeiIds = GameServerContext.getRedis().zrangeByScore(
				xiaoFeiTimeRankKey, startTime, endTime);
		if (xiaoFeiIds == null || xiaoFeiIds.size() == 0) {
			lastFetchTime = GameSystemTime.getSystemMillTime();
			return;
		}
		String[] xiaoFeiIdKeyArray = new String[xiaoFeiIds.size()];
		int index = 0;
		for (String e : xiaoFeiIds) {
			xiaoFeiIdKeyArray[index++] = RedisKey.buildXiaoFeiDetailKey(e);
		}
		List<String> xiaoFeiDetails = GameServerContext.getRedis().mget(xiaoFeiIdKeyArray);
		if (xiaoFeiDetails == null || xiaoFeiDetails.size() == 0) {
			lastFetchTime = GameSystemTime.getSystemMillTime();
			return;
		}
		int validSzie = 0;
		for (String e : xiaoFeiDetails) {
			if (e != null && !e.equals("")) {
				validSzie++;
			}
		}
		if (validSzie == 0) {
			lastFetchTime = GameSystemTime.getSystemMillTime();
			return;
		}
		List<KuafuXiaoFeiRankVo> tmpRankList = new ArrayList<KuafuXiaoFeiRankVo>();
		Map<Long, KuafuXiaoFeiRankVo> tmpRankMap = new HashMap<Long, KuafuXiaoFeiRankVo>();
//		Long[] userRoleIdArray = new Long[validSzie];
		List<Long> userRoleIdList = new ArrayList<Long>();
//		String[] userRoleIdKeyArray = new String[validSzie];
		List<String> userRoleIdKeyList = new ArrayList<String>();
		
		KuaFuXiaoFeiPaiMingGroupConfig config = KuaFuXiaoFeiPaiMingConfigExportService.getInstance().loadByMap(subId);
//		int count = 0;
		for (int i = 0; i < xiaoFeiDetails.size(); i++) {
			String str = xiaoFeiDetails.get(i);
			if (str == null || str.equals("")) {
				continue;
			}
			String[] array = str.split(":");
			Long userRoleId = Long.parseLong(array[0]);
			Integer yb = Integer.parseInt(array[1]);
			long chargeTime = Long.parseLong(array[2]);
//			userRoleIdKeyArray[count] = RedisKey.buildXiaoFeiUserKey(userRoleId);
//			userRoleIdArray[count] = userRoleId;
//			count = count + 1;
			if(!userRoleIdList.contains(userRoleId)&& nameCache.get(userRoleId) == null){
				userRoleIdList.add(userRoleId);
				userRoleIdKeyList.add(RedisKey.buildXiaoFeiUserKey(userRoleId));
			}
			
			KuafuXiaoFeiRankVo vo = tmpRankMap.get(userRoleId);
			if (vo == null) {
				vo = new KuafuXiaoFeiRankVo();
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
			names = GameServerContext.getRedis().mget(
					userRoleIdKeyArray);
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
		for (KuafuXiaoFeiRankVo e : tmpRankList) {
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
				if(tmpRankList.get(i).getYb() <config.getMinXiaoFei()){
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
	
	/**
	 * 发送玩家排名奖励
	 * 
	 * @param userRoleId
	 */
	private void sendSpecialRewardEmailForSingle(Long userRoleId, int rank,Map<String, GoodsConfigureVo> item) {
		try {
			String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_XIAOFEI_SPECIAL_REWARD_TITLE);
			String content = EmailUtil
					.getCodeEmail(AppErrorCode.KUAFU_XIAOFEI_SPECIAL_REWARD,
							String.valueOf(rank));
			String[] attachment = EmailUtil.getAttachmentsForGoodsVo(item);
			for (String e : attachment) {
				emailExportService.sendEmailToOne(userRoleId,title, content,
						GameConstants.EMAIL_TYPE_SINGLE, e);
			}
		} catch (Exception e) {
			ChuanQiLog
					.error("kuafu charge rank sendSpecialRewardEmailForSingle error");
		}
	}
	
	public void handleUserModifyNameEvent(Long userRoleId, String afterName) {
		String xiaoFeiUserKey = RedisKey.buildXiaoFeiUserKey(userRoleId);
		if (GameServerContext.getRedis().exists(xiaoFeiUserKey)) {
			//更新redis
			GameServerContext.getRedis().setex(xiaoFeiUserKey, DATA_EXPIRE_TIME,
					afterName);
			//更新排行榜
			try {
				lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
				if (rankList == null || rankList.size() == 0) {
					return ;
				}
				for(KuafuXiaoFeiRankVo e:rankList){
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
	
	/**
	 * 结算邮件
	 */
	public void kuafuXiaoFeiDayJob() {
		Map<Integer, KuaFuXiaoFeiPaiMingGroupConfig> groups = KuaFuXiaoFeiPaiMingConfigExportService.getInstance().getAllConfig();
		if (groups.size() == 0) {
			return;
		}
		long curTime = GameSystemTime.getSystemMillTime();
		// 循环充值礼包配置数据
		for (Map.Entry<Integer, KuaFuXiaoFeiPaiMingGroupConfig> entry : groups.entrySet()) {
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
		KuaFuXiaoFeiPaiMingGroupConfig groupConfig = KuaFuXiaoFeiPaiMingConfigExportService.getInstance().loadByMap(subId);
		if (groupConfig == null) {
			return;
		}
		int maxRank = groupConfig.getMaxRank();
		fetchData(startTime, endTime,subId);
		if (rankList != null && rankList.size() > 0) {
			for (KuafuXiaoFeiRankVo e : rankList) {
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
							KuaFuXiaoFeiPaiMingConfig config = groupConfig.getConfigByRank(rank);
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
			KuaFuXiaoFeiPaiMingConfig config) {
		try {
			String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_XIAOFEI_SPECIAL_REWARD_TITLE);
			String content = EmailUtil
					.getCodeEmail(AppErrorCode.KUAFU_XIAOFEI_RANK_REWARD,
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
}
