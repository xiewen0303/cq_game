package com.junyou.bus.shenqi.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.equip.configure.export.ShenQiGeWeiConfig;
import com.junyou.bus.equip.configure.export.ShenQiGeWeiConfigExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.shenqi.configure.export.ShenQiJinJieConfig;
import com.junyou.bus.shenqi.configure.export.ShenQiJinJieConfigExportService;
import com.junyou.bus.shenqi.dao.ShenQiJinJieDao;
import com.junyou.bus.shenqi.entity.ShenQiEquip;
import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.junyou.bus.shenqi.entity.ShenQiJinjie;
import com.junyou.bus.shenqi.export.ShenQiEquipExportService;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.shenqi.filter.ShenQiJinJieFilter;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ShenQiJinJieLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.RandomUtil;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 
 * @description:神器进阶 
 *
 *	@author ChuBin
 *
 * @date 2016-11-29
 */
@Service
public class ShenQiJinJieService {
	@Autowired
	private ShenQiJinJieDao shenQiJinJieDao;
	@Autowired
	private ShenQiJinJieConfigExportService shenQiJinJieConfigExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private ShenQiGeWeiConfigExportService shenQiGeWeiConfigExportService;
	@Autowired
	private ShenQiEquipExportService shenQiEquipExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	
	public List<ShenQiJinjie> initShenQiJinJie(Long userRoleId) {
		return shenQiJinJieDao.initShenQiJinJie(userRoleId);
	}

	public Object[] startUpgrade(long userRoleId,int shenQiId , int curLevel,BusMsgQueue busMsgQueue, boolean isAutoGM) {
		ShenQiJinjie info = getShenQiJinjieById(userRoleId, shenQiId);
		if (info == null) {
			return AppErrorCode.FUNCTION_NOT_ACTIVE;
		}
		
		if(curLevel != info.getShenqiLevel()){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		return upgrade(info, userRoleId, busMsgQueue, isAutoGM, info.getShenqiLevel() + 1, false);
	}

	private Object[] upgrade(ShenQiJinjie info, long userRoleId, BusMsgQueue busMsgQueue, boolean isAutoGM,
			Integer targetLevel, boolean isAuto) {

		int shenQiLevel = info.getShenqiLevel();
		int maxJjLevel = shenQiJinJieConfigExportService.getMaxLevel(info.getShenQiId());
		if (shenQiLevel >= maxJjLevel) {
			return AppErrorCode.IS_MAX_LEVEL;
		}

		if (targetLevel <= shenQiLevel || targetLevel > maxJjLevel) {
			return AppErrorCode.CONFIG_ERROR;
		}

		ShenQiJinJieConfig config = shenQiJinJieConfigExportService.getConfigByIdAndLevel(info.getShenQiId(), shenQiLevel);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		Map<String, Integer> needResource = new HashMap<String, Integer>();
		int zfzVal = info.getZfzVal();

		Object[] result = upgradeResult(info.getShenQiId(),shenQiLevel, needResource, userRoleId, true, maxJjLevel, isAutoGM, targetLevel,
				zfzVal, isAuto, config.getGold(), config.getBgold());

		// result:{errorCode,qhlevel,zfzVal}
		Object[] erroCode = (Object[]) result[0];
		if (erroCode != null) {
			return erroCode;
		}

		int newlevel = (Integer) result[1];
		int newZfz = (Integer) result[2];
		Integer newNeedMoney = needResource.remove(GoodsCategory.MONEY + "");
		Integer newNeedGold = needResource.remove(GoodsCategory.GOLD + "");
		Integer newNeedBgold = needResource.remove(GoodsCategory.BGOLD + "");

		// 扣除金币
		if (newNeedMoney != null && newNeedMoney > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId,
					LogPrintHandle.CONSUME_SHENQI_SJ, true, LogPrintHandle.CBZ_SHENQI_SJ);
		}
		// 扣除元宝
		if (newNeedGold != null && newNeedGold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId,
					LogPrintHandle.CONSUME_SHENQI_SJ, true, LogPrintHandle.CBZ_SHENQI_SJ);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_SHENQI_SJ,
						QQXiaoFeiType.CONSUME_SHENQI_SJ, 1 });
			}
		}
		// 扣除绑定元宝
		if (newNeedBgold != null && newNeedBgold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId,
					LogPrintHandle.CONSUME_SHENQI_SJ, true, LogPrintHandle.CBZ_SHENQI_SJ);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_BYB, newNeedBgold, LogPrintHandle.CONSUME_SHENQI_SJ,
						QQXiaoFeiType.CONSUME_SHENQI_SJ, 1 });
			}
		}

		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.CONSUME_SHENQI_SJ,true, true);

		info.setZfzVal(newZfz);
		info.setShenqiLevel(newlevel);
		info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		if (newZfz > 0 && (newlevel != shenQiLevel || zfzVal == 0)) {
			info.setLastSjTime(GameSystemTime.getSystemMillTime());
		}
		shenQiJinJieDao.cacheUpdate(info, userRoleId);

		if (newlevel > shenQiLevel) {
			// 通知属性变化
			notifyStageChange(userRoleId, busMsgQueue);
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.SHENQI_EQUIP_GEWEI_CHANGE, null);
		}

		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		GamePublishEvent.publishEvent(new ShenQiJinJieLogEvent(LogPrintHandle.SHENQI_JINJIE_LOG,info.getShenQiId(), userRoleId,
				newNeedMoney, newNeedGold, consumeItemArray, shenQiLevel, newlevel, zfzVal, newZfz));
		
		return new Object[] { 1, info.getShenQiId(),newlevel ,newZfz};
	}

	private void notifyStageChange(long userRoleId, BusMsgQueue busMsgQueue) {
		busMsgQueue.addStageMsg(userRoleId, InnerCmdType.SHENQI_JINJIE_ATTR_CHANGE,
				getShenQiJinJieAttr(userRoleId));
	}

	/**
	 * 通知场景里面属性变化
	 * @param shenQiId 
	 */
	public void notifyStageChange(long userRoleId) {
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.SHENQI_JINJIE_ATTR_CHANGE,
				getShenQiJinJieAttr(userRoleId));
	}

	/**
	 * 
	 *@description: 
	 * @param id
	 * @param curLevel
	 * @param needResource
	 * @param userRoleId
	 * @param isSendErrorCode
	 * @param maxLevel
	 * @param isAutoGM
	 * @param targetLevel
	 * @param zfzVal
	 * @param isAuto
	 * @param yb
	 * @param byb
	 * @return
	 */
	private Object[] upgradeResult(int id ,int curLevel, Map<String, Integer> needResource, long userRoleId,
			boolean isSendErrorCode, int maxLevel, boolean isAutoGM, int targetLevel, int zfzVal, boolean isAuto,
			int yb, int byb) {

		if (curLevel >= targetLevel) {
			return new Object[] { null, curLevel };
		}

		ShenQiJinJieConfig zqConfig = shenQiJinJieConfigExportService.getConfigByIdAndLevel(id, curLevel);
		if (zqConfig == null) {
			Object[] errorCode = isSendErrorCode ? AppErrorCode.CONFIG_ERROR : null;
			return new Object[] { errorCode, curLevel, zfzVal };
		}

		Map<String, Integer> tempResources = new HashMap<>();
		List<String> needGoodsIds = shenQiJinJieConfigExportService.getConsumeIds(zqConfig.getConsumeId());
		int needCount = zqConfig.getCount();

		for (String goodsId : needGoodsIds) {
			int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);

			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= oldNeedCount + needCount) {
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = oldNeedCount + needCount - owerCount;
			tempResources.put(goodsId, owerCount - oldNeedCount);
		}

		if (isAutoGM && needCount > 0) {
			int bPrice = byb;// 绑定元宝的价格
			int bCount = 0;
			int nowNeedBgold = 0;
			for (int i = 0; i < needCount; i++) {
				nowNeedBgold = (bCount + 1) * bPrice;
				Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
				if (null != bgoldError) {
					break;
				}
				bCount++;
			}
			nowNeedBgold = bCount * bPrice;
			tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);

			needCount = needCount - bCount;
			int price = yb;
			int nowNeedGold = needCount * price;

			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
			if (null != goldError) {
				Object[] errorCode = isSendErrorCode ? goldError : null;
				return new Object[] { errorCode, curLevel, zfzVal };
			}

			tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
			needCount = 0;
		}

		if (needCount > 0) {
			Object[] errorCode = isSendErrorCode ? AppErrorCode.ITEM_NOT_ENOUGH : null;
			return new Object[] { errorCode, curLevel, zfzVal };
		}
		ObjectUtil.mapAdd(needResource, tempResources);

		boolean flag = isSJSuccess(zfzVal, zqConfig);
		if (!flag) {
			zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzMinAdd(), zqConfig.getZfzMaxAdd() + 1);
		}

		// 如果祝福值大于了最大值，算强化成功
		int maxzf = zqConfig.getZfzMax();
		if (flag || zfzVal >= maxzf) {
			zfzVal = 0;
			++curLevel;
		}

		// 如果不是自动,成功与否都退出
		if (!isAuto) {
			return new Object[] { null, curLevel, zfzVal };
		}

		// 成功之后达到了指定的目标等级就停止
		if (targetLevel <= curLevel) {
			return new Object[] { null, curLevel, zfzVal };
		}

		return upgradeResult(id,curLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal,
				isAuto, zqConfig.getGold(), zqConfig.getBgold());
	}

	public boolean isSJSuccess(int zfzValue, ShenQiJinJieConfig zqConfig) {

		int minzf = zqConfig.getZfzMin();

		if (zfzValue < minzf) {
			return false;
		}

		int pro = zqConfig.getSuccessRate();

		if (RandomUtil.getIntRandomValue(1, 101) > pro) {
			return false;
		}
		return true;
	}


	public List<ShenQiJinjie> getShenQiJinjie(long userRoleId) {
		return shenQiJinJieDao.cacheLoadAll(userRoleId);
	}

	public ShenQiJinjie getShenQiJinjieById(long userRoleId, Integer shenQiId) {
		List<ShenQiJinjie> list = shenQiJinJieDao.cacheLoadAll(userRoleId, new ShenQiJinJieFilter(shenQiId));
		if (list != null && list.size() != 0) {
			return list.get(0);
		}

		return null;
	}
	

	public Map<String, Long> getShenQiJinJieAttr(Long userRoleId) {
		List<ShenQiJinjie> list = getShenQiJinjie(userRoleId);
		if(ObjectUtil.isEmpty(list)){
			return null;
		}
	   
	   Map<String, Long> attrMap = new HashMap<>();
	   Map<String,Long> equipAttrMap = new HashMap<>();
	   for (ShenQiJinjie info : list) {
		   ShenQiJinJieConfig config = shenQiJinJieConfigExportService.getConfigByIdAndLevel(info.getShenQiId(), info.getShenqiLevel());
			if (config != null && config.getAttrs() != null) {
				
				 ObjectUtil.longMapAdd(attrMap, config.getAttrs());
				 List<ShenQiEquip> shenQiEquip = shenQiEquipExportService.getShenQiEquipByShenQiIdAndSlot(userRoleId, info.getShenQiId(), null);
				 if(shenQiEquip != null){
					 for (ShenQiEquip shenQiEquip2 : shenQiEquip) {
						 Map<String,Long> fujiaAttrMap = new HashMap<>();
						 RoleItem item = roleBagExportService.getItemBySlot1(shenQiEquip2.getSlot(), userRoleId, null, shenQiEquip2.getShenQiId());
						 if(item != null){
							 GoodsConfig goodsConfig =goodsConfigExportService.loadById(item.getGoodsId());
							 if(goodsConfig == null){
								 continue;
							 }
							 ObjectUtil.longMapAdd(equipAttrMap, goodsConfig.getEquipRealAttr(item.getTipinValue()));
//							 ObjectUtil.longMapAdd(equipAttrMap, goodsConfig.getEquipBaseAttr());
							 if(goodsConfig.getData1() != 0 ){
								 if(config.getAttrs().containsKey(EffectType.x5.name())){
									 fujiaAttrMap.put(EffectType.x5.name(), Math.round(config.getAttrs().get(EffectType.x5.name()) * (goodsConfig.getData1()/100d)));
								 }
							 }
							 if(goodsConfig.getData2().intValue() != 0){
								 if(config.getAttrs().containsKey(EffectType.x8.name())){
									 fujiaAttrMap.put(EffectType.x8.name(), Math.round(config.getAttrs().get(EffectType.x8.name()) * (goodsConfig.getData2()/100d)));
								 }
							 }
						 }
						 ObjectUtil.longMapAdd(attrMap,fujiaAttrMap);
					 }
				 }
			}
			
	   }
	   ObjectUtil.longMapAdd(attrMap,equipAttrMap);
		return attrMap;
	}

	/**
	 *@description: 
	 * @param userRoleId
	 * @return
	 */
	
//	[[[1,1,0]],[[1,[[-182,["sq003602",96116670466,-182]],[-181,["sq002603",96116670468,-181]]]]]]
	//Array[
    //0:Array[0:int(id),1:int(激活就传阶数),2:int(当前祝福值)],
    //1:Array[0:int(id),1:int(激活就传阶数),2:int(当前祝福值)],
    //...
    //](如果一个没有激活就传null)
	public Object[] getShenQiJinjieInfo(Long userRoleId) {
		checkActivatedShenQi(userRoleId);
		
		List<ShenQiJinjie> list = getShenQiJinjie(userRoleId);
		if(list ==null || list.size() ==0){
			return new Object[]{null,null};
		}
		
		List<Object[]> result = new ArrayList<>();
		Map<Integer, Map<Integer, ShenQiJinJieConfig>> configs = shenQiJinJieConfigExportService.getAllConfigs();
		for(Integer  id: configs.keySet()){
			ShenQiJinjie info = getShenQiJinjieById(userRoleId,id);
			if(info !=null){
				result.add(new Object[]{id,info.getShenqiLevel(),info.getZfzVal()});
			}
		}
		
		List<Object[]> resultVo = new ArrayList<>();
		Object[] equipVo = null;
		List<ShenQiInfo> shenQiInfos = shenQiExportService.getRoleActivatedShenqi(userRoleId);
		for (ShenQiInfo shenQiInfo : shenQiInfos) {
			List<Object[]> slotVo = new ArrayList<>();
			List<ShenQiGeWeiConfig> geWeiConfig = shenQiGeWeiConfigExportService.loadById(shenQiInfo.getShenQiId());
			if(geWeiConfig != null){
				for (ShenQiGeWeiConfig shenQiGeWeiConfig : geWeiConfig) {
					List<ShenQiEquip> equipLis = shenQiEquipExportService.getShenQiEquipByShenQiIdAndSlot(userRoleId,shenQiGeWeiConfig.getId(),shenQiGeWeiConfig.getGeWei());
					if(equipLis != null && equipLis.size() > 0){
						continue;
					}else{
						shenQiEquipExportService.checkGeWeiOpen(shenQiGeWeiConfig, userRoleId);
					}
				}
			}
			List<ShenQiEquip> shenQiEquip = shenQiEquipExportService.getShenQiEquipByShenQiIdAndSlot(userRoleId, shenQiInfo.getShenQiId(), null);
			
			if(shenQiEquip !=null){
				for (ShenQiEquip shenQiEquip2 : shenQiEquip) {
					equipVo =roleBagExportService.getAllShenQiEquips(userRoleId, shenQiInfo.getShenQiId(),shenQiEquip2.getSlot());
					slotVo.add(new Object[]{shenQiEquip2.getSlot(),equipVo});
				}
			}
			resultVo.add(new Object[]{shenQiInfo.getShenQiId(),slotVo.toArray()});
		}
		return new Object[]{result.toArray(),resultVo.toArray()};
	}

	/**
	 * 
	 *@description: 检查已激活的神器,判断是否需要创建新的记录
	 * @param userRoleId
	 */
	private void checkActivatedShenQi(Long userRoleId) {
		List<ShenQiInfo> list = shenQiExportService.getRoleActivatedShenqi(userRoleId);
		if (list == null || list.size() == 0) {
			// 激活数为0,不处理
			return;
		}
		
		boolean isChange = false;
		Map<Integer, Map<Integer, ShenQiJinJieConfig>> configs = shenQiJinJieConfigExportService.getAllConfigs();
		if(configs == null){
			ChuanQiLog.info("神器进阶配置不存在!!!!!");
			return;
		}
		
		for (ShenQiInfo shenQiInfo : list) {
			Integer shenQiId = shenQiInfo.getShenQiId();

			if (!configs.containsKey(shenQiId)) {
				// 玩家激活了该神器,但是神器进阶表中没有配置
				ChuanQiLog.info("神器进阶的配置中缺少指定神器id的配置: " + shenQiId);
				continue;
			}

			if (getShenQiJinjieById(userRoleId, shenQiId) == null) {
				ShenQiJinjie entity = createShenQiJinjie(userRoleId, shenQiId);
				shenQiJinJieDao.cacheInsert(entity, userRoleId);
				isChange =true;
			}
		}
		
		if (isChange) {
			notifyStageChange(userRoleId);
		}
	}
	
	public static void main(String[] args) {
		Float a = 2.0f;
		System.out.println(a.intValue());
	}

	private ShenQiJinjie createShenQiJinjie(Long userRoleId, Integer shenQiId) {
		ShenQiJinjie entity = new ShenQiJinjie();
		long nowTime = GameSystemTime.getSystemMillTime();
		entity.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		entity.setUserRoleId(userRoleId);
		entity.setShenQiId(shenQiId);
		entity.setShenqiLevel(1);
		entity.setLastSjTime(0l);
		entity.setZfzVal(0);
		entity.setUpdateTime(new Timestamp(nowTime));
		return entity;
	}
	
	public void chargeGeWeiChange(long userRoleId){
		List<Object[]> resultVo = new ArrayList<>();
		//拉取当前开启的所有格位信息
		List<ShenQiEquip> equipLis = shenQiEquipExportService.getShenQiEquip(userRoleId);
		//拉取当前激活的神器信息
		List<ShenQiInfo> shenQiInfos = shenQiExportService.getRoleActivatedShenqi(userRoleId);
		for (ShenQiInfo shenQiInfo : shenQiInfos) {
			List<ShenQiGeWeiConfig> geWeiConfig = shenQiGeWeiConfigExportService.loadById(shenQiInfo.getShenQiId());
			if(geWeiConfig != null){
				for (ShenQiGeWeiConfig shenQiGeWeiConfig : geWeiConfig) {
					List<ShenQiEquip> equip = shenQiEquipExportService.getShenQiEquipByShenQiIdAndSlot(userRoleId,shenQiGeWeiConfig.getId(),shenQiGeWeiConfig.getGeWei());
					if(equip != null && equip.size() > 0){
						continue;
					}else{
						//检测新开启的格位
						shenQiEquipExportService.checkGeWeiOpen(shenQiGeWeiConfig, userRoleId);
					}
				}
			}
		}
		//拉取当前开启的所有格位信息
		List<ShenQiEquip> equipLis2 = shenQiEquipExportService.getShenQiEquip(userRoleId);
		if(equipLis == null || equipLis.size() <= 0){
			if(equipLis2 != null && equipLis2.size() > 0){
				for (ShenQiEquip shenQiEquip : equipLis2) {
					resultVo.add(new Object[]{shenQiEquip.getShenQiId(),shenQiEquip.getSlot()});
				}
			}
		}else{
			if(equipLis2 == null || equipLis2.size() <= 0){
				return;
			}
			Map<String,Object> equipMap = new HashMap<>();
			for(ShenQiEquip shenQiEquip : equipLis){
				equipMap.put(shenQiEquip.getShenQiId()+"_"+shenQiEquip.getSlot(), shenQiEquip.getSlot());
			}
			for (ShenQiEquip shenQiEquip : equipLis2) {
				if(!equipMap.containsKey(shenQiEquip.getShenQiId()+"_"+shenQiEquip.getSlot())){
					resultVo.add(new Object[]{shenQiEquip.getShenQiId(),shenQiEquip.getSlot()});
				}
			}
		}
		if(resultVo.size() > 0){
			BusMsgSender.send2One(userRoleId, ClientCmdType.SHENQI_EQUIP_GEWEI_CHANGE, resultVo.toArray());
		}
	}
	
	
}
