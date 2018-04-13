/**
 * 
 */
package com.junyou.bus.zhuanpan.server;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tongyong.dao.ActityCountLogDao;
import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zhuanpan.configure.export.ZhuanPanConfig;
import com.junyou.bus.zhuanpan.configure.export.ZhuanPanConfigExportService;
import com.junyou.bus.zhuanpan.configure.export.ZhuanPanConfigGroup;
import com.junyou.bus.zhuanpan.dao.RefabuZhuanpanDao;
import com.junyou.bus.zhuanpan.dao.ZhuanPanLogDao;
import com.junyou.bus.zhuanpan.entity.RefabuZhuanpan;
import com.junyou.bus.zhuanpan.entity.ZhuanPanLog;
import com.junyou.bus.zhuanpan.filter.ZhuanPanFilter;
import com.junyou.bus.zhuanpan.utils.ZhuanPanUtils;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ZhuanPanDuiHuanLogEvent;
import com.junyou.event.ZhuanPanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;



/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class ZhuanPanService { 
	
	@Autowired
	private RefabuZhuanpanDao zhuanpanDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ZhuanPanLogDao zhuanPanLogDao;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private ActityCountLogDao actityCountLogDao;
	
	public List<RefabuZhuanpan> initRefabuZhuanpan(Long userRoleId){
		return zhuanpanDao.initRefabuZhuanpan(userRoleId);
	}
	
	private RefabuZhuanpan getRefabuZhuanpan(Long userRoleId,int subId){
		List<RefabuZhuanpan> list = zhuanpanDao.cacheLoadAll(userRoleId, new ZhuanPanFilter(subId));
		if(list == null || list.size() <= 0){
			RefabuZhuanpan zhuanpan = new RefabuZhuanpan();
			zhuanpan.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			zhuanpan.setUserRoleId(userRoleId);
			zhuanpan.setSubId(subId);
			zhuanpan.setJifen(0);
			zhuanpan.setCreateTime(new Timestamp(System.currentTimeMillis()));
			zhuanpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			zhuanpan.setCount(0);
			zhuanpan.setCountUpdateTime(new Timestamp(System.currentTimeMillis()));
			zhuanpanDao.cacheInsert(zhuanpan, userRoleId);
			
			return zhuanpan;
		}
		RefabuZhuanpan zhuanpan = list.get(0);
		//不是同一天，清理数据
		if(!DateUtils.isSameDay(zhuanpan.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
			zhuanpan.setJifen(0);
			zhuanpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			zhuanpan.setCount(0);
			zhuanpan.setCountUpdateTime(new Timestamp(System.currentTimeMillis()));
			zhuanpanDao.cacheUpdate(zhuanpan, userRoleId);
			
		}
		return zhuanpan;
	}
	
	
	public Object[] zhuan(Long userRoleId,Integer version,int subId,BusMsgQueue busMsgQueue,boolean is){
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
		ZhuanPanConfigGroup config = ZhuanPanConfigExportService.getInstance().loadByMap(subId);
		
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Object[]> nMap = ZhuanPanUtils.getXYZPMap(userRoleId,subId);
		if(nMap == null){
			return AppErrorCode.ZP_DATA_ERROR;
		}
		/*String needGoodsId = config.getNeeditem();//转盘所需道具
		int needGoodsIdCount = roleBagExportService.getBagItemCountByGoodsId(needGoodsId, userRoleId);	//玩家背包物品数量
		int yb = 0;//转盘需要元宝
		
		//没有对应物品，判断元宝是否够
		if(needGoodsIdCount <= 0){
			yb = config.getGold();
		}*/
		RefabuZhuanpan zhuanpan = getRefabuZhuanpan(userRoleId, subId);
		int count = 1;
		int yb = getGoldByCount(zhuanpan.getCount(), config);
		if(is){
			yb =  getGoldByCount10(zhuanpan.getCount(), config);
			count = 10;
		}
		boolean insert = true;
		//如果配置了次数
		if(config.getMaxCount() > 0){
			//判断玩家次数
			ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
			if(log != null){
				insert = false;
				if(log.getCount() != null && log.getCount()+count > config.getMaxCount()){
					return AppErrorCode.ACTITY_MAX_COUNT;
				}
			}
		}
		if(yb > 0){
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,yb, userRoleId);
			if(null != goldError){ 
				return goldError;
			}
		}
		
		Map<Integer,Object[]> nnGoodsMap = new HashMap<>(); 
		List<Object[]> zhuanList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			//进行抽奖
			Integer jiang = Lottery.getRandomKeyByInteger(config.getZpMap());
			Map<Integer, ZhuanPanConfig> configMap = config.getConfigMap();
			ZhuanPanConfig zpConfig = configMap.get(jiang);
			
			//转盘物品
			Map<Integer, Object[]> map = ZhuanPanUtils.getXYZPMap(userRoleId,subId);
			if(map == null || map.get(jiang) == null){
				return AppErrorCode.ZP_NOT_GOODS; 
			}
			Map<String, Integer> goodMap = new HashMap<String, Integer>();
			Object[] obj = map.get(jiang);
			goodMap.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
			//获取本次需要消费的元宝
			int xyb = getGoldByCount(zhuanpan.getCount(), config);
			if(xyb > 0){
				//消耗元宝
				roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, xyb, userRoleId, LogPrintHandle.CONSUME_ZHUANPAN, true,LogPrintHandle.CBZ_ZHUANPAN);
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,xyb,LogPrintHandle.CONSUME_ZHUANPAN,QQXiaoFeiType.CONSUME_ZHUANPAN,1});
				}
			}
			//检查物品是否可以进背包
			Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
			if(bagCheck != null){
				String title = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL_TITLE);
				String content = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL);
				emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, obj[0]+GameConstants.CONFIG_SUB_SPLIT_CHAR+obj[1]);
			}else{
				zhuanList.add(obj);
				//物品进背包
				roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_GET_ZHUANPAN, LogPrintHandle.GET_RFB_ZHUANPAN, LogPrintHandle.GBZ_RFB_ZHUANPAN, true);
			}
			
			//新roll一个物品
			Object[] newGoods = getNewGood(zpConfig.getItemMap());
			if(newGoods == null){
				newGoods = obj;//没有找到新的物品，重新赋值老物品
			}
			
			//设置新物品
			map.put(jiang, newGoods);
			ZhuanPanUtils.setXYZPMap(userRoleId, map,subId);
			
			/*if(needGoodsIdCount > 0){
			//检查消耗物品
			Object[] error = roleBagExportService.checkRemoveBagItemByGoodsId(needGoodsId, 1, userRoleId);
			if(error != null){
				return error;
			}
			roleBagExportService.removeBagItemByGoodsId(needGoodsId, 1, userRoleId, GoodsSource.GOODS_GET_ZHUANPAN_XH, true, true);
			}*/
			//物品进背包
			//roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_GET_ZHUANPAN, LogPrintHandle.GET_RFB_ZHUANPAN, LogPrintHandle.GBZ_RFB_ZHUANPAN, true);
			
			//增加积分
			zhuanpan.setJifen(zhuanpan.getJifen() + 1);
			zhuanpan.setCount(zhuanpan.getCount() + 1);
			//面板上变化的新物品
			nnGoodsMap.put(jiang, newGoods);
			
			//公告
			GoodsConfig goodsConfig = goodsConfigExportService.loadById(obj[0].toString()); 
			saveLogAndNotify(subId,userRoleId, goodsConfig, Integer.parseInt(obj[1].toString()),configSong.getSkey(),busMsgQueue);
			
			//日志打印 
			try {
				GamePublishEvent.publishEvent(new ZhuanPanLogEvent(userRoleId, yb,getRoleName(userRoleId), obj[0].toString(), Integer.parseInt(obj[1].toString())));
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		zhuanpan.setCountUpdateTime(new Timestamp(System.currentTimeMillis()));
		zhuanpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		zhuanpanDao.cacheUpdate(zhuanpan, userRoleId);
		List<Object[]> list = new ArrayList<>();
		for (Integer key:nnGoodsMap.keySet()) {
			list.add(new Object[]{key,nnGoodsMap.get(key)});
		}
		if(config.getMaxCount() > 0){
			if(insert){
				ActityCountLog log = new ActityCountLog();
				log.setUserRoleId(userRoleId);
				log.setCount(count);
				log.setUpdateTime(GameSystemTime.getSystemMillTime());
				actityCountLogDao.insertDb(log, subId);
			}else{
				actityCountLogDao.addActivityCount(subId, userRoleId, count);
			}
		}
		return new Object[]{1,subId,list.toArray(),zhuanList.toArray(),zhuanpan.getJifen(),getGoldByCount(zhuanpan.getCount(), config),getGoldByCount10(zhuanpan.getCount(), config),count};
	}
	
	
	private String getRoleName(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		return role.getName();
	}
	/**
	 * 兑换
	 * @param userRoleId
	 * @param version
	 * @param subId
	 * @param configId
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] duihuan(Long userRoleId,Integer version,int subId,int configId){
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
		ZhuanPanConfigGroup config = ZhuanPanConfigExportService.getInstance().loadByMap(subId);
		
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Map<Integer, ZhuanPanConfig> configMap = config.getConfigMap();
		ZhuanPanConfig zpConfig = configMap.get(configId);
		if(zpConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//兑换所需积分
		int jifen  = zpConfig.getJifen();
		RefabuZhuanpan zhuanpan = getRefabuZhuanpan(userRoleId, subId);
		if(zhuanpan.getJifen() < jifen){
			return AppErrorCode.ZP_DUIHUAN_JIFEN;
		}
		
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(zpConfig.getDuiHuanMap(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		//消耗积分
		zhuanpan.setJifen(zhuanpan.getJifen() - jifen);
		zhuanpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		zhuanpanDao.cacheUpdate(zhuanpan, userRoleId);
		
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(zpConfig.getDuiHuanMap(), userRoleId, GoodsSource.GOODS_GET_ZHUANPAN, LogPrintHandle.GET_RFB_ZHUANPAN, LogPrintHandle.GBZ_RFB_ZHUANPAN, true);
		
		//日志打印 
		try {
			GamePublishEvent.publishEvent(new ZhuanPanDuiHuanLogEvent(userRoleId, jifen,getRoleName(userRoleId), zpConfig.getGoodId(), zpConfig.getGoodCount()));
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		return new Object[]{1,subId,configId,zhuanpan.getJifen()};
	}
	
	
	/**
	 * 保存广播日志并全服广播
	 * @param userRoleId
	 * @param goodsConfig
	 * @param count
	 * @param busMsgQueue
	 */
	private void saveLogAndNotify(int subId,long userRoleId,GoodsConfig goodsConfig,int count,String key,BusMsgQueue busMsgQueue){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		String roleName = role.getName();
		//全局通知
		if(goodsConfig.isNotify()){
			
			
			if(role.isGm()){
				return;//GM不广播
			}
			
			busMsgQueue.addBroadcastMsg(ClientCmdType.ZP_SYSTEM_NOTIFY, new Object[]{subId,new Object[]{goodsConfig.getId(),count,roleName},key});
		}
		//记录通知
		ZhuanPanLog log = new ZhuanPanLog();
		
		log.setRoleName(roleName);
		log.setGoodsId(goodsConfig.getId());
		log.setGoodsCount(count);
		log.setCreateTime(GameSystemTime.getSystemMillTime());
		log.setUserRoleId(userRoleId);
		try {
			zhuanPanLogDao.insertDb(log);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		//busMsgQueue.addBroadcastMsg(ClientCmdType.ZP_ONE_SYSTEM_NOTIFY, new Object[]{subId,new Object[]{goodsConfig.getId(),count,roleName},key});
	}
	

	/**
	 * 获取转盘物品
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	private Object[] getUtilGood(Long userRoleId,Integer subId){
		ZhuanPanConfigGroup config = ZhuanPanConfigExportService.getInstance().loadByMap(subId);
		Map<Integer, ZhuanPanConfig> configMap = config.getConfigMap();
		Map<Integer, Object[]> map = ZhuanPanUtils.getXYZPMap(userRoleId,subId);
		
		Object[] returnObj = new Object[config.getMaxGe()];
		if(map != null && map.size() >0){
			for (int i = 0; i < config.getMaxGe(); i++) {
				returnObj[i] = map.get(i+1);
			}
			return returnObj;
		}
		map = new HashMap<Integer, Object[]>();
		for (int i = 0; i < returnObj.length; i++) {
			ZhuanPanConfig zpConfig = configMap.get(i+1);
			if(zpConfig == null){
				return null;
			}
			Object[] newGoods = getNewGood(zpConfig.getItemMap());
			returnObj[i] = newGoods;
			map.put(i+1, newGoods);
		}
		ZhuanPanUtils.setXYZPMap(userRoleId, map,subId);
		return returnObj;
		
	}
	
	/**
	 * 获取新物品
	 * @param item
	 * @return
	 */
	private Object[] getNewGood(Map<String, Integer> item){
		if(item == null || item.size() <= 0){
			return null;
		}
		String good = Lottery.getRandomKeyByInteger(item);
		if(good == null){
			return null;
		}
		String[] g = good.split(":");
		if(g.length <= 1){
			return new Object[]{g[0],1};	
		}
		return new Object[]{g[0],g[1]};
	}
	
	
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		ZhuanPanConfigGroup config = ZhuanPanConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		RefabuZhuanpan zhuanpan = getRefabuZhuanpan(userRoleId, subId);
		ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
		int count = 0;
		if(log != null){
			long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
			long upTime = log.getUpdateTime();
			long dTime = GameSystemTime.getSystemMillTime();
			if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
				actityCountLogDao.cleanActivityCount(subId, userRoleId);
			}else{
				count = log.getCount();
			}
		}
		return new Object[]{
				config.getPic(),
				getUtilGood(userRoleId, subId),
				config.getNeeditem(),
				zhuanpan.getJifen(),
				config.getDuiHuanData().toArray(),
				getLog(userRoleId),
				//config.getGold(),
				getGoldByCount(zhuanpan.getCount(), config),
				getGoldByCount10(zhuanpan.getCount(), config),
				new Object[]{count,config.getMaxCount()}
		};
	}
	
	/**
	 * 获取10次消耗的元宝数
	 * @param count
	 * @param goldList
	 * @return
	 */
	private int getGoldByCount10(int count,ZhuanPanConfigGroup config){
		count = count +1;
		if(config == null){
			return 30;
		}
		if(config.getGoldList() == null || config.getGoldList().size() <= 0){
			return config.getGold()*10;
		}
		List<Object[]> goldList = config.getGoldList();
		int gold = 0;
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < goldList.size(); i++) {
				Object[] obj = goldList.get(i);
				int min = Integer.parseInt(obj[0].toString());
				int max = Integer.parseInt(obj[1].toString());
				if(count >= min && count <= max){
					gold += Integer.parseInt(obj[2].toString());
					break;
				}
			}
			count++;
		}
		return gold;
	}
	
	/**
	 * 根据转的次数获取要消耗元宝
	 * @return
	 */
	private int getGoldByCount(int count,ZhuanPanConfigGroup config){
		count = count +1;
		if(config == null){
			return 30;
		}
		if(config.getGoldList() == null || config.getGoldList().size() <= 0){
			return config.getGold();
		}
		List<Object[]> goldList = config.getGoldList();
		
		for (int i = 0; i < goldList.size(); i++) {
			Object[] obj = goldList.get(i);
			int min = Integer.parseInt(obj[0].toString());
			int max = Integer.parseInt(obj[1].toString());
			if(count >= min && count <= max){
				return  Integer.parseInt(obj[2].toString());
			}
		}
		return 30;
	}
	
	/**
	 * 获取个人购买日志
	 * @return
	 */
	private Object[] getLog(Long userRoleId){
		List<ZhuanPanLog> xunbaoLogs = null;
		try {
			xunbaoLogs = zhuanPanLogDao.getXunbaonfoByIdDb(userRoleId);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		if(xunbaoLogs == null || xunbaoLogs.size() <= 0){
			return null;
		}
		List<Object[]> list = new ArrayList<>();
		for (int i = 0; i < xunbaoLogs.size(); i++) {
			ZhuanPanLog log = xunbaoLogs.get(i);
			list.add(new Object[]{log.getGoodsId(),log.getGoodsCount()});
		}
		return list.toArray();
		
	}
	
}