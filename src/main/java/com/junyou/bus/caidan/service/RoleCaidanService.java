package com.junyou.bus.caidan.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.caidan.dao.CaidanLogDao;
import com.junyou.bus.caidan.dao.RefbCaidanDao;
import com.junyou.bus.caidan.entity.CaidanCategoryConfig;
import com.junyou.bus.caidan.entity.CaidanConfig;
import com.junyou.bus.caidan.entity.CaidanLog;
import com.junyou.bus.caidan.entity.RefbCaidan;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.CaiDanDuihuanLogEvent;
import com.junyou.event.CaiDanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @author LiuYu
 * 2015-9-15 下午5:57:56
 */
@Service
public class RoleCaidanService {
	@Autowired
	private RefbCaidanDao refbCaidanDao;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private CaidanLogDao caidanLogDao;

	public List<RefbCaidan> initRefbCaidan(Long userRoleId) {
		return refbCaidanDao.initRefbCaidan(userRoleId);
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		CaidanCategoryConfig config = RoleCaidanConfigService.getInstance().loadByKeyId(subId);
		if(config == null){
			return null;
		}
		RefbCaidan caidan = getCaidan(userRoleId, subId, true);
		
		return new Object[]{
			config.getBg(),
			config.getInfo(),
			config.getShowItems(),
			getEggInfo(caidan),
			caidan.getLucky(),
			caidan.getTime(),
			getNextPrice(caidan),
			config.getRestetGold(),
			caidan.getJifen(),
			getLastAllPrice(caidan),
			new Object[]{caidan.getTime()-1,config.getMaxCount()}
		};
	}
	private RefbCaidan updateJianCe(int subId,RefbCaidan caidan){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = caidan.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			caidan.setLucky(0);
			caidan.setTime(1);
			caidan.setDanInfo("");
			caidan.setJifen(0);
			caidan.setUpdateTime(GameSystemTime.getSystemMillTime());
			refbCaidanDao.cacheUpdate(caidan, caidan.getUserRoleId());
		}
		return caidan;
	}
	public static void main(String[] args) {
		System.out.println(new Timestamp(1469442519229l));
		System.out.println(new Timestamp(1469376000000l));
		System.out.println(new Timestamp(1469442497602l));
		
		
	}
	
	/**砸下个蛋需要的元宝数*/
	private int getNextPrice(RefbCaidan caidan){
		CaidanCategoryConfig config = RoleCaidanConfigService.getInstance().loadByKeyId(caidan.getSubId());
		if(config == null){
			ChuanQiLog.error("彩蛋配置异常:{}",caidan.getSubId());
			return -1;
		}
		CaidanConfig caidanConfig = config.getCaidanConfig(caidan.getTime());
		if(caidanConfig == null){
			ChuanQiLog.error("彩蛋配置异常2:{}",caidan.getTime());
			return -1;
		}
		return caidanConfig.getGold();
	}
	/**砸下剩下所有蛋需要的元宝数*/
	private int getLastAllPrice(RefbCaidan caidan){
		CaidanCategoryConfig config = RoleCaidanConfigService.getInstance().loadByKeyId(caidan.getSubId());
		if(config == null){
			ChuanQiLog.error("彩蛋配置异常:{}",caidan.getSubId());
			return -1;
		}
		int gold = 0;
		for (int i = 0;i < GameConstants.MAX_GIFT_SIZE - caidan.getInfoJson().size();i++) {
			int time = caidan.getTime()+i;
			CaidanConfig caidanConfig = config.getCaidanConfig(time);
			if(caidanConfig == null){
				ChuanQiLog.error("彩蛋配置异常2:{}",time);
				return -1;
			}
			gold+=caidanConfig.getGold();
		}
		return gold;
	}
	
	private Object[] getEggInfo(RefbCaidan caidan){
		List<Object> info = new ArrayList<>();
		for (int i = 0;i < GameConstants.MAX_GIFT_SIZE;i++) {
			JSONArray eggInfo = caidan.getInfoJson().getJSONArray(i+"");
			if(eggInfo != null){
				info.add(new Object[]{eggInfo.get(0),eggInfo.get(1)});
			}else{
				info.add(null);
			}
		}
		return info.toArray();
	}
	
	private RefbCaidan getCaidan(Long userRoleId,final Integer subId,boolean create){
		List<RefbCaidan> list = refbCaidanDao.cacheLoadAll(userRoleId, new IQueryFilter<RefbCaidan>() {
			boolean stop = false;
			@Override
			public boolean check(RefbCaidan entity) {
				stop = entity.getSubId().equals(subId);
				return stop;
			}

			@Override
			public boolean stopped() {
				return stop;
			}
		});
		RefbCaidan caidan = null;
		if(list != null && list.size() > 0){
			caidan = list.get(0);
			caidan = updateJianCe(subId, caidan);
			if(!DatetimeUtil.dayIsToday(caidan.getUpdateTime())){
				caidan.setLucky(0);
				caidan.setTime(1);
				caidan.setDanInfo("");
				caidan.setUpdateTime(GameSystemTime.getSystemMillTime());
				refbCaidanDao.cacheUpdate(caidan, userRoleId);
			}
		}else if(create){
			caidan = new RefbCaidan();
			caidan.setId(IdFactory.getInstance().generateId(ServerIdType.REFABU));
			caidan.setUserRoleId(userRoleId);
			caidan.setSubId(subId);
			caidan.setJifen(0);
			caidan.setLucky(0);
			caidan.setTime(1);
			caidan.setUpdateTime(GameSystemTime.getSystemMillTime());
			caidan.setDanInfo("");
			refbCaidanDao.cacheInsert(caidan, userRoleId);
		}
		return caidan;
	}
	public Object[] getRefbState(Long userRoleId, Integer subId){
		RefbCaidan caidan = getCaidan(userRoleId, subId, true);
		return new Object[]{
				1,
				subId,
				caidan.getLucky(),
				caidan.getTime(),
				getNextPrice(caidan),
				caidan.getJifen(),
				getEggInfo(caidan)
		};
	}
	
	public Object[] getInfo(Long userRoleId,Integer version,int subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		RefbCaidan caidan = getCaidan(userRoleId, subId, true);
		return new Object[]{
				1,
				subId,
				caidan.getLucky(),
				caidan.getTime(),
				getNextPrice(caidan),
				caidan.getJifen(),
				getEggInfo(caidan)
		};
	}	
	
	/**
	 * 获取兑换配置
	 * @param subId
	 * @return
	 */
	public Object[] getCaidanDuihuanConfig(Integer subId) {
		CaidanCategoryConfig config = RoleCaidanConfigService.getInstance().loadByKeyId(subId);
		if(config == null){
			return null;
		}
		return config.getGetDuihuanInfo();
	}
	
	public Object[] getAllCaidanLog(Integer subId) {
		return caidanLogDao.getLogsOut(subId);
	}
	
	public Object[] resetCaidan(Long userRoleId,Integer subId) {
		CaidanCategoryConfig config = RoleCaidanConfigService.getInstance().loadByKeyId(subId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RefbCaidan caidan = getCaidan(userRoleId, subId, true);
		int gold = 0;
		if(caidan.getInfoJson().size() < GameConstants.MAX_GIFT_SIZE){
			gold = config.getRestetGold();
		}
		//验证消耗元宝
		if(gold > 0){
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId,LogPrintHandle.CONSUME_CAIDAN,true,LogPrintHandle.CBZ_RESET_CAIDAN);
			if(result != null){
				return result;
			}
		}
		caidan.setDanInfo("");
		refbCaidanDao.cacheUpdate(caidan, userRoleId);
		
		return new Object[]{1,subId,getLastAllPrice(caidan)};
	}
	
	public Object[] zaOneEgg(Long userRoleId,Integer subId,Integer version,Integer id) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		CaidanCategoryConfig categoryConfig = RoleCaidanConfigService.getInstance().loadByKeyId(subId);
		if(categoryConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(id < 0 || id >= GameConstants.MAX_GIFT_SIZE){
			return AppErrorCode.THIS_EGG_ERROR;
		}
		RefbCaidan caidan = getCaidan(userRoleId, subId, true);
		JSONObject info = caidan.getInfoJson();
		String index = id+"";
		if(info.containsKey(index)){
			return AppErrorCode.THIS_EGG_IS_OPEN;
		}
		if(categoryConfig.getMaxCount() > 0){
			if(caidan.getTime() > categoryConfig.getMaxCount()){
				return AppErrorCode.ACTITY_MAX_COUNT;
			}
		}
		CaidanConfig config = categoryConfig.getCaidanConfig(caidan.getTime());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int gold = config.getGold();
		//元宝验证
		if(gold > 0){
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId,LogPrintHandle.CONSUME_CAIDAN,true,LogPrintHandle.CBZ_RESET_CAIDAN);
			if(result != null){
				return result;
			}
		}
		GoodsConfigureVo gift = Lottery.getRandomKeyByInteger(config.getItemMap());
		String itemId = gift.getGoodsId();
		Integer num = gift.getGoodsCount();
		info.put(id+"", new Object[]{itemId,num});
		caidan.addLucky(config.getLucky());
		caidan.addJifen(config.getScore());
		caidan.addTime();
		refbCaidanDao.cacheUpdate(caidan, userRoleId);
		//发放奖励
		Map<String,Integer> goods = new HashMap<>();
		goods.put(gift.getGoodsId(), gift.getGoodsCount());
		roleBagExportService.putInBagOrEmailWithNumber(goods, userRoleId, GoodsSource.GOODS_CAIDAN, LogPrintHandle.GET_CAIDAN, LogPrintHandle.GBZ_ZADAN, true, GameConstants.SEND_EMAIL_CAIDAN_CONTENT);
		//打印日志
		caidanLog(userRoleId, gold, config.getScore(), itemId, num,subId);
		return new Object[]{1,subId,id,itemId,num,getNextPrice(caidan),caidan.getLucky(),caidan.getJifen()};
	}
	
	public Object[] zaAllEggs(Long userRoleId,Integer subId,Integer version) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		CaidanCategoryConfig categoryConfig = RoleCaidanConfigService.getInstance().loadByKeyId(subId);
		if(categoryConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RefbCaidan caidan = getCaidan(userRoleId, subId, true);
		JSONObject info = caidan.getInfoJson();
		if(info.size() == GameConstants.MAX_GIFT_SIZE){
			return AppErrorCode.ALL_EGGS_IS_OPEN;
		}
		int count = caidan.getTime();
		int needGold = 0;
		int allScore = 0;
		Map<String,Integer> itemMap = new HashMap<String, Integer>();
		List<Object[]> caidanList = new ArrayList<Object[]>();
		List<GoodsConfigureVo> giftList = new ArrayList<>();
		for (int i = 0; i < GameConstants.MAX_GIFT_SIZE; i++) {
			String index = i+"";
			if(info.containsKey(index)){
				JSONArray eggInfo = info.getJSONArray(index);
				caidanList.add(new Object[]{eggInfo.get(0),eggInfo.get(1)});
				continue;
			}
			if(categoryConfig.getMaxCount() > 0){
				if(caidan.getTime() > categoryConfig.getMaxCount()){
					return AppErrorCode.ACTITY_MAX_COUNT;
				}
			}
			CaidanConfig config = categoryConfig.getCaidanConfig(count);
			if(config == null){
				return AppErrorCode.CONFIG_ERROR;
			}
			needGold += config.getGold();
			//元宝验证
			if(needGold > 0){
				Object[] result = accountExportService.isEnought(GoodsCategory.GOLD, needGold, userRoleId);
				if(result != null){
					if(i > 0){
						needGold -= config.getGold();
						break;
					}else{
						return result;
					}
				}
			}
			count++;
			caidan.addTime();
			caidan.addJifen(config.getScore());
			allScore += config.getScore();
			caidan.addLucky(config.getLucky());
			GoodsConfigureVo gift = Lottery.getRandomKeyByInteger(config.getItemMap());
			String itemId = gift.getGoodsId();
			Integer num = gift.getGoodsCount();
			Object[] giftInfo = new Object[]{itemId,num};
			info.put(index, giftInfo);
			caidanList.add(giftInfo);
			giftList.add(gift);
			
			if(itemMap.containsKey(itemId)){
				itemMap.put(itemId, itemMap.get(itemId)+num);
			}else{
				itemMap.put(itemId, num);
			}
		}
		//消耗元宝
		if(needGold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_CAIDAN, true, LogPrintHandle.CBZ_CONSUME_ZADAN);
		}
		caidan.setInfoJson(info);
		refbCaidanDao.cacheUpdate(caidan, userRoleId);
		//发放奖励
		roleBagExportService.putInBagOrEmailWithNumber(itemMap, userRoleId, GoodsSource.GOODS_CAIDAN, LogPrintHandle.GET_CAIDAN, LogPrintHandle.GBZ_ZADAN, true, GameConstants.SEND_EMAIL_CAIDAN_CONTENT);

		//打印日志
		caidanLog(userRoleId, needGold, allScore,subId,giftList);
		
		return new Object[]{1,subId,caidan.getLucky(),caidan.getJifen(),getNextPrice(caidan),getLastAllPrice(caidan),caidanList.toArray(),caidan.getTime()-1};
	}
	
	private void caidanLog(Long userRoleId,int gold,int score,String itemId,int num ,Integer subId){
		try{
			GoodsConfig config = goodsConfigExportService.loadById(itemId);
			if(config == null){
				return;
			}
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			String name = "-";
			if(role != null){
				name = role.getName();
			}
			if(config.isNotify()){
				CaidanLog log = new CaidanLog(name, itemId, num);
				caidanLogDao.insert(log,subId);
			}
			GamePublishEvent.publishEvent(new CaiDanLogEvent(userRoleId,name, itemId, num, score, gold));
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	private void caidanLog(Long userRoleId,int gold,int score,Integer subId,List<GoodsConfigureVo> giftList){
		try{
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			String name = "-";
			if(role != null){
				name = role.getName();
			}
			for (GoodsConfigureVo gift : giftList) {
				GoodsConfig config = goodsConfigExportService.loadById(gift.getGoodsId());
				if(config == null){
					continue;
				}
				if(config.isNotify()){
					CaidanLog log = new CaidanLog(role.getName(), gift.getGoodsId(), gift.getGoodsCount());
					caidanLogDao.insert(log,subId);
				}
				GamePublishEvent.publishEvent(new CaiDanLogEvent(userRoleId,name, gift.getGoodsId(), gift.getGoodsCount(), score, gold));
				Object[] notice = new Object[]{GameConstants.ZADAN_NOTICE,new Object[]{name,new Object[]{3,config.getId(),gift.getGoodsCount()}}};
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, notice);
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	public Object[] duihuanItem(Long userRoleId,Integer subId,Integer version, String itemId) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object[] data = getCaidanDuihuanConfig(subId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CAIDAN_DUIHUAN_CONFIG,data );
			return null;
		}
		CaidanCategoryConfig categoryConfig = RoleCaidanConfigService.getInstance().loadByKeyId(subId);
		if(categoryConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RefbCaidan caidan = getCaidan(userRoleId, subId, false);
		if(caidan == null){
			return AppErrorCode.NO_ENOUGH_SCORE;
		}
		Integer score = categoryConfig.getDuihuanMap().get(itemId);
		if(score == null || score < 1){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(score > caidan.getJifen()){
			return AppErrorCode.NO_ENOUGH_SCORE;
		}
		Object[] result = roleBagExportService.checkPutInBag(itemId, 1, userRoleId);
		if(result != null){
			return result;
		}
		caidan.delJifen(score);
		refbCaidanDao.cacheUpdate(caidan, userRoleId);
		RoleItemInput goods = BagUtil.createItem(itemId, 1, 0);
		roleBagExportService.putInBag(goods, userRoleId, GoodsSource.GOODS_CAIDAN_DUIHUAN, true);
		
		//打印日志
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		String name = "-";
		if(role != null){
			name = role.getName();
		}
		GamePublishEvent.publishEvent(new CaiDanDuihuanLogEvent(userRoleId,name, itemId, score));
		
		return new Object[]{1,subId,caidan.getJifen()};
	}
}
