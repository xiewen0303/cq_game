/**
 * 
 */
package com.junyou.bus.smsd.server;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.smsd.configue.export.ShenMiShangDianConfig;
import com.junyou.bus.smsd.configue.export.ShenMiShangDianConfigExportService;
import com.junyou.bus.smsd.configue.export.ShenMiShangDianConfigGroup;
import com.junyou.bus.smsd.dao.ShenMiShangDianLogDao;
import com.junyou.bus.smsd.dao.ShenmiShangdianDao;
import com.junyou.bus.smsd.entity.ShenMiShangDianLog;
import com.junyou.bus.smsd.entity.ShenmiShangdian;
import com.junyou.bus.smsd.filter.ShenMiShangDianFilter;
import com.junyou.bus.smsd.utils.ShenMiShangDianUtils;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.SMSDBuyLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
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
public class ShenMiShangDianService { 
	
	@Autowired
	private ShenmiShangdianDao shenmiShangdianDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ShenMiShangDianLogDao shenMiShangDianLogDao;
	
	public List<ShenmiShangdian> initShenmiShangdian(Long userRoleId){
		return shenmiShangdianDao.initShenmiShangdian(userRoleId);
	}
	
	private ShenmiShangdian getShenmiShangdian(Long userRoleId,int subId){
		List<ShenmiShangdian> list = shenmiShangdianDao.cacheLoadAll(userRoleId, new ShenMiShangDianFilter(subId));
		if(list == null || list.size() <= 0){
			ShenmiShangdian shangdian = new ShenmiShangdian();
			shangdian.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			shangdian.setUserRoleId(userRoleId);
			shangdian.setSubId(subId);
			shangdian.setBuyId("");
			shangdian.setShuaxiTime(setSxTime(subId));
			shangdian.setCreateTime(new Timestamp(System.currentTimeMillis()));
			shangdian.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			shenmiShangdianDao.cacheInsert(shangdian, userRoleId);
			
			return shangdian;
		}
		return list.get(0);
	}
	
	
	public Object[] buy(Long userRoleId,Integer version,int subId,int geWeiId,BusMsgQueue busMsgQueue){
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

		Map<Integer, ShenMiShangDianConfig> nMap = ShenMiShangDianUtils.getSMSDMap(userRoleId,subId);
		if(nMap == null){
			return AppErrorCode.SMSD_BUY_ERROR;
		}
		ShenMiShangDianConfig shangDian = nMap.get(geWeiId);
		if(shangDian == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		ShenmiShangdian smsd = getShenmiShangdian(userRoleId, subId);
		
		if(isBuyGoodByConfigId(smsd.getBuyId(), shangDian.getId())){
			return AppErrorCode.SMSD_BUY_CHONGFU;
		}
		
		//需要货币数量
		int hb = shangDian.getNow();
		//元宝是否足够
		Object[] mo = moneyBoolean(userRoleId, shangDian.getMoneytype(), hb);
		if(mo != null){
			return mo;
		}
		
		Map<String, Integer> goodMap = new HashMap<String, Integer>();
		goodMap.put(shangDian.getItem(), shangDian.getCount());
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//消耗货币
		if(hb > 0 ){
			xiaoHaoMoney(userRoleId, shangDian.getMoneytype(), hb);
		}
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_GET_SMSD, LogPrintHandle.GET_RFB_SMSD, LogPrintHandle.GBZ_RFB_SMSD, true);
		
		//增加购买的物品
		smsd.setBuyId(setBuyGood(smsd.getBuyId(), shangDian.getId()));
		smsd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		shenmiShangdianDao.cacheUpdate(smsd, userRoleId);
		
		//公告
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(shangDian.getItem()); 
		saveLogAndNotify(subId,userRoleId, goodsConfig, shangDian.getCount(), busMsgQueue);
		//日志打印 
		try {
			GamePublishEvent.publishEvent(new SMSDBuyLogEvent(userRoleId, hb,shangDian.getMoneytype(),getRoleName(userRoleId), shangDian.getItem(), shangDian.getCount()));
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		return new Object[]{1,subId,geWeiId};
	}
	
	private String getRoleName(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		return role.getName();
	}
	
	/**
	 * 保存广播日志并全服广播
	 * @param userRoleId
	 * @param goodsConfig
	 * @param count
	 * @param busMsgQueue
	 */
	private void saveLogAndNotify(int subId,long userRoleId,GoodsConfig goodsConfig,int count,BusMsgQueue busMsgQueue){
		//全局通知
		if(goodsConfig.isNotify()){
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			
			if(role.isGm()){
				return;//GM不广播
			}
			
			//记录通知
			String roleName = role.getName();
			

			ShenMiShangDianLog log = new ShenMiShangDianLog();
			
			log.setRoleName(roleName);
			log.setGoodsId(goodsConfig.getId());
			log.setGoodsCount(count);
			log.setCreateTime(GameSystemTime.getSystemMillTime());
			
			try {
				shenMiShangDianLogDao.insertDb(log);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			
			busMsgQueue.addBroadcastMsg(ClientCmdType.SMSD_SYSTEM_NOTIFY, new Object[]{subId,roleName,goodsConfig.getId(),count});
		}
	}
	
	/**
	 * 根据货币类型判断是否有足够的货币
	 * @param userRoleId
	 * @param moneyType
	 * @return
	 */
	private Object[] moneyBoolean(Long userRoleId ,int moneyType,int value){
		/*money(1：元宝，2：礼券，3：金币)*/
		if(moneyType == GoodsCategory.GOLD){
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,value, userRoleId);
			if(null != goldError){ 
				return goldError;
			}
		}else if(moneyType == GoodsCategory.BGOLD){
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.BGOLD,value, userRoleId);
			if(null != goldError){ 
				return goldError;
			}
		}else if(moneyType == GoodsCategory.MONEY){
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.MONEY,value, userRoleId);
			if(null != goldError){ 
				return goldError;
			}
		}
		return null;
	}
	
	/**
	 * 根据货币类型消耗货币
	 * @param userRoleId
	 * @param money
	 * @param value
	 */
	private void xiaoHaoMoney(Long userRoleId,int moneyType,int value){
		if(moneyType == GoodsCategory.GOLD){
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, value, userRoleId, LogPrintHandle.CONSUME_SMSD_GM, true,LogPrintHandle.CBZ_SMSD_GM);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,value,LogPrintHandle.CONSUME_SMSD_GM,QQXiaoFeiType.CONSUME_SMSD_GM,1});
			}
		}else if(moneyType == GoodsCategory.BGOLD){
		
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, value, userRoleId, LogPrintHandle.CONSUME_SMSD_GM, true,LogPrintHandle.CBZ_SMSD_GM);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,value,LogPrintHandle.CONSUME_SMSD_GM,QQXiaoFeiType.CONSUME_SMSD_GM,1});
			}
		}else if(moneyType == GoodsCategory.MONEY){
		
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, value, userRoleId, LogPrintHandle.CONSUME_SMSD_GM, true,LogPrintHandle.CBZ_SMSD_GM);
		}
	}
	
	/**
	 * 增加购买的物品
	 * @param buyId
	 * @param configId
	 * @return
	 */
	private String setBuyGood(String buyId,int configId){
		if(buyId == null || "".equals(buyId)){
			return configId+"";
		}else{
			return buyId +","+configId;
		}
		
	}
	
	/**
	 * 判断是否购买过这个配置ID的物品
	 * @return
	 */
	private boolean isBuyGoodByConfigId(String buyId,int configId){
		if(buyId == null || "".equals(buyId)){
			return false;
		}
		String[] buy = buyId.split(",");
		for (int i = 0; i < buy.length; i++) {
			if(configId == Integer.parseInt(buy[i])){//存在记录，已经买过
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 刷新
	 * @param userRoleId
	 * @param version
	 * @param subId
	 * @return
	 */
	public Object[] shuaxin(Long userRoleId,Integer version,int subId){
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
		
		ShenMiShangDianConfigGroup config = ShenMiShangDianConfigExportService.getInstance().loadByMap(subId);
		
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		ShenmiShangdian smsd = getShenmiShangdian(userRoleId, subId);

		//判断元宝
		int yb = config.getSxGold();
		Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,yb, userRoleId);
		if(null != goldError){ 
			return goldError;
		}
		
		Map<Integer, ShenMiShangDianConfig> map = new HashMap<Integer, ShenMiShangDianConfig>();
		Object[] returnObj = new Object[6];
		for (int i = 0; i < returnObj.length; i++) {
			//根据商品格位获取商品集合（6个）
			List<ShenMiShangDianConfig> shangDians = ShenMiShangDianConfigExportService.getInstance().loadByLittleid(subId,i+1);
			//商店物品货架
			Map<Integer, Float> chouMap = getChouMap(shangDians);
			
			Integer jiang = Lottery.getRandomKey2(chouMap);
			if(jiang == null){
				for (Integer key : chouMap.keySet()) {
					jiang = key;
					break;
				}
			}
			ShenMiShangDianConfig shangDian = ShenMiShangDianConfigExportService.getInstance().loadByKeyId(subId,jiang);
			
			returnObj[i] = new Object[]{shangDian.getId(),shangDian.getItem(),shangDian.getMoneytype(),shangDian.getOld(),shangDian.getNow(),shangDian.getLittleid(),shangDian.getCount()};
			
			map.put(i+1, shangDian);
		}
		ShenMiShangDianUtils.setSMSDMap(userRoleId,map,subId);
		//消耗元宝
		if(yb > 0 ){
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, yb, userRoleId, LogPrintHandle.CONSUME_SMSD_SX, true,LogPrintHandle.CBZ_SMSD_SX);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,yb,LogPrintHandle.CONSUME_SMSD_SX,QQXiaoFeiType.CONSUME_SMSD_SX,1});
			}
		}
		
		//修改数据
		smsd.setShuaxiTime(System.currentTimeMillis());
		smsd.setBuyId("");//清空购买的物品配置ID
		smsd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		shenmiShangdianDao.cacheUpdate(smsd, userRoleId);
		
		//刷新之后个人版本号+1
		ShenMiShangDianUtils.setUserBanBen(userRoleId, ShenMiShangDianUtils.getUserBanBen(userRoleId)+1);
		
		return new Object[]{1,new Object[]{
				subId,
				getNextSxTime(userRoleId, subId),
				getUtilGood(userRoleId, subId),
				ShenMiShangDianUtils.getUserBanBen(userRoleId)
		}};
	}
	
	/**
	 * 检测刷新
	 * @param userRoleId
	 * @param version
	 * @param subId
	 * @return
	 */
	public Object[] jcShuaxin(Long userRoleId,Integer version,int subId){
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
		ShenMiShangDianConfigGroup config = ShenMiShangDianConfigExportService.getInstance().loadByMap(subId);
		
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		ShenmiShangdian smsd = getShenmiShangdian(userRoleId, subId);
		
		
		long sxTime = getSxTime(userRoleId, subId);
		if(sxTime > System.currentTimeMillis()){
			long xTime = getNextSxTime(userRoleId, subId);//在上次刷新之后的下次刷新时间
			return new Object[]{subId,xTime};
		}else{
			//表示刷新时间已过,刷新物品
			Map<Integer, ShenMiShangDianConfig> map = new HashMap<Integer, ShenMiShangDianConfig>();
			Object[] returnObj = new Object[6];
			for (int i = 0; i < returnObj.length; i++) {
				//根据商品格位获取商品集合（6个）
				List<ShenMiShangDianConfig> shangDians = ShenMiShangDianConfigExportService.getInstance().loadByLittleid(subId,i+1);
				//商店物品货架
				Map<Integer, Float> chouMap = getChouMap(shangDians);
				
				Integer jiang = Lottery.getRandomKey2(chouMap);
				if(jiang == null){
					for (Integer key : chouMap.keySet()) {
						jiang = key;
						break;
					}
				}
				ShenMiShangDianConfig shangDian = ShenMiShangDianConfigExportService.getInstance().loadByKeyId(subId,jiang);
				
				returnObj[i] = new Object[]{
						shangDian.getLittleid(),
						shangDian.getItem(),
						shangDian.getMoneytype(),
						shangDian.getNow(),
						shangDian.getOld(),
						isBuyGoodByConfigId(smsd.getBuyId(), shangDian.getId()),
						shangDian.getCount()
					};
				
				map.put(i+1, shangDian);
			}
			
			
			ShenMiShangDianUtils.setSMSDMap(userRoleId,map,subId);
			long s = smsd.getShuaxiTime();
			long time = config.getSxTime() * 1000;
			while (s+time < System.currentTimeMillis()) {
				s = s + time;
			}
			//保存自动刷新时间
			smsd.setShuaxiTime(s);
			smsd.setBuyId("");
			smsd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			shenmiShangdianDao.cacheUpdate(smsd, userRoleId);
			
			ShenMiShangDianUtils.setUserBanBen(userRoleId, ShenMiShangDianUtils.getUserBanBen(userRoleId)+1);
			
			return new Object[]{
					subId,
					getNextSxTime(userRoleId, subId),
					returnObj,
					ShenMiShangDianUtils.getUserBanBen(userRoleId)
			};
		}
		
	}
	
	/**
	 * 获取随机MAP
	 * @param list
	 * @return
	 */
	private Map<Integer, Float> getChouMap(List<ShenMiShangDianConfig> list){
		if(list == null || list.size() <= 0 ){
			return null;
		}
		Map<Integer, Float> map = new HashMap<Integer, Float>();
		for(ShenMiShangDianConfig shangdian : list){
			map.put(shangdian.getId(), shangdian.getOdds());
		}
		return map;
		
	}
	
	/**
	 * 玩家第一次打开面板，计算初始化时间
	 * @param subId
	 * @return
	 */
	private Long setSxTime(Integer subId){
		long s = 0;
		ShenMiShangDianConfigGroup config = ShenMiShangDianConfigExportService.getInstance().loadByMap(subId);
		//从未刷新过，设置当日刷新时间为0点
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		try {
			Date dt = sdf.parse(sdf.format(new Date()));
			s =  dt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		//配置设置每隔XX秒刷新
		long time = config.getSxTime()* 1000;//转毫秒
		//如果上次刷新时间 + 刷新所需时间大于当前时间，表示未到刷新时间
		if(s + time > System.currentTimeMillis()){
			return s;
		}else{
			while (s+time < System.currentTimeMillis()) {
				s = s + time;
			}
			return s;
		}
		
	}
	
	
	/**
	 * 获取刷新时间
	 * @param subId
	 * @return
	 */
	private Long getSxTime(Long userRoleId,Integer subId){
		
		ShenMiShangDianConfigGroup config = ShenMiShangDianConfigExportService.getInstance().loadByMap(subId);
		ShenmiShangdian smsd = getShenmiShangdian(userRoleId, subId);
		
		long s = smsd.getShuaxiTime();
		//配置设置每隔XX秒刷新
		long time = config.getSxTime()* 1000;//转毫秒
		//如果上次刷新时间 + 刷新所需时间大于当前时间，表示未到刷新时间
		return s + time;
	}
	/**
	 * 获取剩余刷新时间
	 * @param subId
	 * @return
	 */
	private Long getNextSxTime(Long userRoleId,Integer subId){
		
		ShenMiShangDianConfigGroup config = ShenMiShangDianConfigExportService.getInstance().loadByMap(subId);
		ShenmiShangdian smsd = getShenmiShangdian(userRoleId, subId);
		
		long s = smsd.getShuaxiTime();
		//配置设置每隔XX秒刷新
		long time = config.getSxTime()* 1000;//转毫秒
		//如果上次刷新时间 + 刷新所需时间大于当前时间，表示未到刷新时间
		if(s + time > System.currentTimeMillis()){
			return s + time - System.currentTimeMillis();
		}else{
			while (s+time < System.currentTimeMillis()) {
				s = s + time;
			}
			return s+time - System.currentTimeMillis();
		}
	}

	/**
	 * 获取神秘商店物品
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	private Object[] getUtilGood(Long userRoleId,Integer subId){
		List<Object[]> list = new ArrayList<>();
		
		ShenMiShangDianConfigGroup config = ShenMiShangDianConfigExportService.getInstance().loadByMap(subId);
		Map<Integer, ShenMiShangDianConfig> map = ShenMiShangDianUtils.getSMSDMap(userRoleId, subId);
		//上次刷新时间 + 间隔时间  >当前时间  则取内存数据 （内存没有数据 需刷新）
		ShenmiShangdian smsd = getShenmiShangdian(userRoleId, subId);
		long s = smsd.getShuaxiTime();
		long time = config.getSxTime() * 1000;
		if(s + time > System.currentTimeMillis() && map != null && map.size() > 0){
			for (int i = 0; i < 6; i++) {
				ShenMiShangDianConfig shangDian =map.get(i+1);
				if(shangDian == null){
					//根据商品格位获取商品集合（6个）
					List<ShenMiShangDianConfig> shangDians = ShenMiShangDianConfigExportService.getInstance().loadByLittleid(subId,i+1);
					//商店物品货架
					Map<Integer, Float> chouMap = getChouMap(shangDians);
					
					Integer jiang = Lottery.getRandomKey2(chouMap);
					if(jiang == null){
						for (Integer key : chouMap.keySet()) {
							jiang = key;
							break;
						}
					}
					shangDian = ShenMiShangDianConfigExportService.getInstance().loadByKeyId(subId,jiang);
					map.put(i+1, shangDian);
				}
				list.add(new Object[]{
						shangDian.getLittleid(),
						shangDian.getItem(),
						shangDian.getMoneytype(),
						shangDian.getNow(),
						shangDian.getOld(),
						isBuyGoodByConfigId(smsd.getBuyId(), shangDian.getId()),
						shangDian.getCount()
					});
			}
			ShenMiShangDianUtils.setSMSDMap(userRoleId,map,subId);
		}else{
			if(map == null){
				map = new HashMap<>();
			}
			smsd.setBuyId("");
			for (int i = 0; i < 6; i++) {
				//根据商品格位获取商品集合（6个）
				List<ShenMiShangDianConfig> shangDians = ShenMiShangDianConfigExportService.getInstance().loadByLittleid(subId,i+1);
				//商店物品货架
				Map<Integer, Float> chouMap = getChouMap(shangDians);
				
				Integer jiang = Lottery.getRandomKey2(chouMap);
				if(jiang == null){
					for (Integer key : chouMap.keySet()) {
						jiang = key;
						break;
					}
				}
				ShenMiShangDianConfig shangDian = ShenMiShangDianConfigExportService.getInstance().loadByKeyId(subId,jiang);
				
				list.add(new Object[]{
						shangDian.getLittleid(),
						shangDian.getItem(),
						shangDian.getMoneytype(),
						shangDian.getNow(),
						shangDian.getOld(),
						isBuyGoodByConfigId(smsd.getBuyId(), shangDian.getId()),
						shangDian.getCount()
					});
				
				map.put(i+1, shangDian);
			}
			ShenMiShangDianUtils.setSMSDMap(userRoleId,map,subId);
			
			while (s+time < System.currentTimeMillis()) {
				s = s + time;
			}
			//保存自动刷新时间
			smsd.setShuaxiTime(s);
			smsd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			shenmiShangdianDao.cacheUpdate(smsd, userRoleId);
			
			ShenMiShangDianUtils.setUserBanBen(userRoleId, ShenMiShangDianUtils.getUserBanBen(userRoleId)+1);
		}
		
		return list.toArray();
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){

		ShenMiShangDianConfigGroup config = ShenMiShangDianConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		return new Object[]{
				config.getPic(),
				config.getSxGold(),
				config.getXianshi(),
				getLog(),
				null,
				getNextSxTime(userRoleId, subId),//下次刷新时间
				getUtilGood(userRoleId, subId),//获取神秘商店物品，没有就初始化
				ShenMiShangDianUtils.getUserBanBen(userRoleId)//个人版本号
		};
	}
	
	/**
	 * 获取全服购买日志
	 * @return
	 */
	private Object[] getLog(){
		List<ShenMiShangDianLog> xunbaoLogs = null;
		try {
			xunbaoLogs = shenMiShangDianLogDao.getXunbaonfoByIdDb();
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		if(xunbaoLogs == null || xunbaoLogs.size() <= 0){
			return null;
		}
		List<Object[]> list = new ArrayList<>();
		for (int i = 0; i < xunbaoLogs.size(); i++) {
			ShenMiShangDianLog log = xunbaoLogs.get(i);
			list.add(new Object[]{log.getRoleName(),log.getGoodsId(),log.getGoodsCount()});
		}
		return list.toArray();
		
	}
	
}