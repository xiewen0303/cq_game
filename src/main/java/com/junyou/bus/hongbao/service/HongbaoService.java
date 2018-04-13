package com.junyou.bus.hongbao.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.hongbao.configure.HongbaoConfig;
import com.junyou.bus.hongbao.configure.HongbaoConfigExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.HongbaoGetLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;

@Service
public class HongbaoService {
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	@Autowired
	private HongbaoConfigExportService hongbaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;

	private ConcurrentMap<Long, Long> onLineHongbaoPublicCacheMap = new ConcurrentHashMap<>();// 所有的首冲红包
	private ConcurrentMap<Long, Map<Long, Integer>> roleHistoryHongbaoGetCacheMap = new ConcurrentHashMap<>(); // 玩家在线红包领取状态
	private long EXPIRE_TIME =  GameConstants.DAY_TIME;//红包过期时间  GameConstants.DAY_TIME

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private Random random = new Random();
	/**
	 * 玩家首冲给在线所有玩家发红包咯
	 */
	public void sendHongbao(Long sendUserRoleId) {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			RoleWrapper roleWrapper = getRoleWrapper(sendUserRoleId);
			onLineHongbaoPublicCacheMap.put(sendUserRoleId, GameSystemTime.getSystemMillTime());
			BusMsgSender.send2All(ClientCmdType.HONGBAO_NEW, new Object[] { new Object[] { roleWrapper.getId(), roleWrapper.getName() } });
			ChuanQiLog.debug("首冲红包,id={}",sendUserRoleId);
		} catch (InterruptedException e) {
			ChuanQiLog.error("领取首冲奖励后发红包逻辑出错：{}", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	/**
	 * 玩家下线清空个人红包领取历史记录
	 */
	public void offlineHandle(Long userRoleId) {
		this.roleHistoryHongbaoGetCacheMap.remove(userRoleId);
	}

	/**
	 * 凌晨清理过期红包
	 */
	public void clearExpireHongbao() {
		if (!onLineHongbaoPublicCacheMap.isEmpty()) {
			Iterator<Map.Entry<Long, Long>> iterator = onLineHongbaoPublicCacheMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Long, Long> entry = iterator.next();
				long time = entry.getValue();
					if (GameSystemTime.getSystemMillTime() - time >= EXPIRE_TIME) {
					// 红包过期
					iterator.remove();
				}
			}
		}
		ChuanQiLog.info("凌晨执行清理过期红包");
	}

	/**
	 * 玩家拆红包
	 * @param userRoleId
	 * @param type
	 * @param hongbaoGuild
	 * @return
	 */
	public Object[] chaiHongbao(Long userRoleId, int type, long hongbaoGuild) {
		
		HongbaoConfig hongbaoConfig = hongbaoConfigExportService.loadPublicConfig(type);
		if (hongbaoConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		HongbaoConfig hongbaoConfig2 = hongbaoConfigExportService.loadPublicConfig(-1);
		if (hongbaoConfig2 == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (!onLineHongbaoPublicCacheMap.containsKey(hongbaoGuild)) {
			return  new Object[]{2,AppErrorCode.HONGBAO_NO_EXIST,hongbaoGuild};
		}
		Map<Long, Integer> roleHistoryMap  = null;
		int state = 0;
		// 检查领取情况
		roleHistoryMap = this.roleHistoryHongbaoGetCacheMap.get(userRoleId);
		if (roleHistoryMap != null) {
			state = roleHistoryMap.get(hongbaoGuild)==null?0:roleHistoryMap.get(hongbaoGuild);
		}
		if ((state == 1 && type == 1) || state == 2) {
			return AppErrorCode.HONGBAO_GET_ALREADY;
		}
		//只有第一次抽取时候红包有概率抽到道具，即：type==1
		if(type==1){
			int gailv = hongbaoConfig2.getGailv();
			int rank = random.nextInt(100) + 1; // 1-100
			if (rank > gailv) {
				roleHistoryMap = this.roleHistoryHongbaoGetCacheMap.get(userRoleId);
				if(roleHistoryMap==null){
					roleHistoryMap = new HashMap<>();
					this.roleHistoryHongbaoGetCacheMap.put(userRoleId, roleHistoryMap);
				}
				state  = type;
				this.roleHistoryHongbaoGetCacheMap.get(userRoleId).put(hongbaoGuild, state); //标示红包领取状态
				return new Object[] { 1, hongbaoGuild,null };// 很遗憾什么都没有，可元宝再来
			}
		}
		RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
		
		if(GameSystemTime.getSystemMillTime()-onLineHongbaoPublicCacheMap.get(hongbaoGuild)>= EXPIRE_TIME){
			 // 领取的红包过期了
			return  new Object[]{2,AppErrorCode.HONGBAO_EXPIRE,hongbaoGuild} ;  
		}
		// 红包创建时间跟玩家登陆时间做比对,玩家登陆前的红包不可领取
		long createTime = onLineHongbaoPublicCacheMap.get(hongbaoGuild);
		if (createTime < roleWrapper.getOnlineTime()) {
			return AppErrorCode.HONGBAO_NOT_GET;
		}
		
		// 随机到一个红包
		String itemStr = Lottery.getRandomKeyByInteger(hongbaoConfig.getDataMap(), hongbaoConfig.getCount());
		Object[] currentItem = itemStr.split(":");
		Map<String, Integer> itemMap = new HashMap<>();
		itemMap.put((String) currentItem[0], Integer.parseInt((String) currentItem[1]));

		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		if (type == 2) {
			int needGold = hongbaoConfig2.getGold();
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_HONGBAO, true,
					LogPrintHandle.CBZ_CONSUME_HONGBAO);
			if (result != null) {
				return AppErrorCode.YUANBAO_NOT_ENOUGH;
			}
		}
		
		if(roleHistoryMap==null){
			roleHistoryMap = new HashMap<>();
			this.roleHistoryHongbaoGetCacheMap.put(userRoleId, roleHistoryMap);
		}
		state  = type;
		// 记录红包领取状态
		roleHistoryMap.put(hongbaoGuild, state);
		// 物品进背包+打印领取红包日志
		roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.GOODS_HONGBAO, LogPrintHandle.GET_HONGBAO, LogPrintHandle.GBZ_GET_HONGBAO, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(itemMap, null);
		GamePublishEvent.publishEvent(new HongbaoGetLogEvent(userRoleId, jsonArray, hongbaoGuild)); // 角色roleId作为红包id

		return new Object[] { 1, hongbaoGuild, new Object[] { currentItem } };

	}

	private RoleWrapper getRoleWrapper(long userRoleId) {
		RoleWrapper role = null;
		if (publicRoleStateService.isPublicOnline(userRoleId)) {
			role = roleExportService.getLoginRole(userRoleId);
		} else {
			role = roleExportService.getUserRoleFromDb(userRoleId);
		}
		return role;
	}

}
