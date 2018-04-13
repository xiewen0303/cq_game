package com.junyou.bus.resource.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.dati.configure.DaTiJiangLiConfig;
import com.junyou.bus.dati.configure.DaTiPublicConfig;
import com.junyou.bus.dati.export.DaTiJiangLiConfigExportService;
import com.junyou.bus.daytask.export.TaskDayExportService;
import com.junyou.bus.fuben.export.FubenExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.resource.dao.RoleResourceBackDao;
import com.junyou.bus.resource.entity.RoleResourceBack;
import com.junyou.bus.resource.entity.ZiYuanJiaGeConfig;
import com.junyou.bus.resource.export.ZiYuanJiaGeConfigExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yabiao.service.export.YabiaoExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ZiYuanPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
/**
 * @author LiuYu
 * 2015-7-7 下午3:25:33
 */
@Service
public class RoleResourceBackService {
	@Autowired
	private RoleResourceBackDao resourceBackDao;
	@Autowired
	private FubenExportService fubenExportService;
	@Autowired
	private TaskDayExportService taskDayExportService;
	@Autowired
	private YabiaoExportService yabiaoExportService;
	@Autowired
	private DaTiJiangLiConfigExportService daTiJiangLiConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ZiYuanJiaGeConfigExportService ziYuanJiaGeConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongShuJuBiaoConfigExportService;
	
	public void onlineHandle(Long userRoleId){
		getRoleResourceBack(userRoleId);
	}
	
	private RoleResourceBack getRoleResourceBackNoCheck(Long userRoleId){
		RoleResourceBack resourceBack = resourceBackDao.cacheAsynLoad(userRoleId, userRoleId);
		if(resourceBack == null){
			resourceBack = new RoleResourceBack();
			resourceBack.setUserRoleId(userRoleId);
			resourceBack.setUpdateTime(0l);
			resourceBackDao.cacheInsert(resourceBack, userRoleId);
		}
		return resourceBack;
	}
	
	private RoleResourceBack getRoleResourceBack(Long userRoleId){
		RoleResourceBack resourceBack = resourceBackDao.cacheAsynLoad(userRoleId, userRoleId);
		if(resourceBack == null){
			resourceBack = new RoleResourceBack();
			resourceBack.setUserRoleId(userRoleId);
			resourceBack.setUpdateTime(GameSystemTime.getSystemMillTime());
			resourceBackDao.cacheInsert(resourceBack, userRoleId);
		}else if(!DatetimeUtil.dayIsToday(resourceBack.getUpdateTime())){
			calTime(resourceBack);
			resourceBack.changeState();
			resourceBack.setUpdateTime(GameSystemTime.getSystemMillTime());
			resourceBackDao.cacheUpdate(resourceBack, userRoleId);
		}
		return resourceBack;
	}
	
	private void calTime(RoleResourceBack resourceBack){
		//日常任务
		Map<String,Map<String,Integer>> map1 = getMapByType(resourceBack, GameConstants.RESOURCE_TYPE_DAY_TASK);
		//公会日常
		Map<String,Map<String,Integer>> map2 = getMapByType(resourceBack, GameConstants.RESOURCE_TYPE_GUILD_TASK);
		taskDayExportService.calDayFubenResource(map1, map2, resourceBack.getUserRoleId());
		//守护副本
		Map<String,Map<String,Integer>> map3 = getMapByType(resourceBack, GameConstants.RESOURCE_TYPE_SHOUHU_FUBEN);
		fubenExportService.calShouHuFubenResource(map3, resourceBack.getUserRoleId());
		//日常副本
		Map<String,Map<String,Integer>> map4 = getMapByType(resourceBack, GameConstants.RESOURCE_TYPE_DAY_FUBEN);
		fubenExportService.calDayFubenResource(map4, resourceBack.getUserRoleId());
		//答题
		calDaTiResource(resourceBack);
		//押镖
		Map<String,Map<String,Integer>> map6 = getMapByType(resourceBack, GameConstants.RESOURCE_TYPE_YABIAO_ACTIVE);
		yabiaoExportService.calDayFubenResource(map6, resourceBack.getUserRoleId());
		
		
	}
	/**
	 * 计算答题找回次数
	 * @param resourceBack
	 */
	private void calDaTiResource(RoleResourceBack resourceBack){
		Long updateTime = resourceBack.getUpdateMap().get(GameConstants.RESOURCE_TYPE_DATI_ACTIVE);
		if(updateTime == null || updateTime < 1){
			return;
		}
		Map<String,Map<String,Integer>> map = getMapByType(resourceBack, GameConstants.RESOURCE_TYPE_DATI_ACTIVE);
		int day = DatetimeUtil.twoDaysDiffence(updateTime);
		if(day < 1){
			return;
		}else if(day > GameConstants.RESOURCE_BACK_MAX_DAY){
			day = GameConstants.RESOURCE_BACK_MAX_DAY;
		}
		RoleWrapper role = roleExportService.getLoginRole(resourceBack.getUserRoleId());
		if(role == null){
			return;
		}
		DaTiJiangLiConfig daTiJiangLiConfig = daTiJiangLiConfigExportService.getConfigByLevel(role.getLevel());
		if (daTiJiangLiConfig == null) {
			return;// 无该等级奖励
		}
		DaTiPublicConfig daTiPublicConfig = gongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_DATI);
		if(daTiPublicConfig == null){
			return;
		}
		Map<String,Integer> dayMap = new HashMap<>();
		dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, daTiJiangLiConfig.getExp() * daTiPublicConfig.getTiSum());
		dayMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, daTiJiangLiConfig.getZhenqi() * daTiPublicConfig.getTiSum());
		for (int i = 1; i <= day; i++) {
			map.put(i+"", new HashMap<>(dayMap));
		}
	}
	/**
	 * 根据类型取map 
	 * @param resourceBack
	 * @param type
	 * @return
	 */
	private Map<String,Map<String,Integer>> getMapByType(RoleResourceBack resourceBack,String type){
		Map<String,Map<String,Integer>> map = resourceBack.getInfoMap().get(type);
		if(map == null){
			map = new HashMap<>();
			resourceBack.getInfoMap().put(type, map);
		}else{
			Long updateTime = resourceBack.getUpdateMap().get(type);
			if(updateTime != null && updateTime > 0){
				int day = DatetimeUtil.twoDaysDiffence(updateTime);
				if(day >= 3){
					map.clear();
				}else if(day > 0){
					for (int i = GameConstants.RESOURCE_BACK_MAX_DAY; i > 0; i--) {
						Map<String, Integer> subMap = map.remove(i+"");
						if(subMap == null || subMap.size() < 1){
							continue;
						}
						int t = i + day;
						if(t <= GameConstants.RESOURCE_BACK_MAX_DAY){
							map.put(t+"", subMap);
						}
					}
					
				}
			}
		}
		resourceBack.getUpdateMap().put(type,GameSystemTime.getSystemMillTime());
		return map;
	}
	
	/**
	 * 参与了答题活动
	 * @param userRoleId
	 */
	public void updateDaTiTime(Long userRoleId){
		RoleResourceBack resourceBack = getRoleResourceBack(userRoleId);
		resourceBack.getUpdateMap().put(GameConstants.RESOURCE_TYPE_DATI_ACTIVE,GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME);
		resourceBack.changeTime();
		resourceBackDao.cacheUpdate(resourceBack, userRoleId);
	}
	
	/**
	 * 获取找回资源信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getResource(Long userRoleId){
		RoleResourceBack resourceBack = getRoleResourceBack(userRoleId);
		List<Object[]> list = new ArrayList<>();
		for (Entry<String, Map<String, Map<String, Integer>>> entry : resourceBack.getInfoMap().entrySet()) {
			Map<String, Map<String, Integer>> map = entry.getValue();
			if(map == null || map.size() < 1){
				continue;
			}
			Object[] resource = getResourceInfo(entry.getKey(), map);
			if(resource != null){
				list.add(resource);
			}
		}
		
		for (Entry<String, Long> acceptUpdateTime : resourceBack.getAcceptUpdateTimeMap().entrySet()) {
			if(acceptUpdateTime.getValue() > DatetimeUtil.getDate00Time()){
				list.add(new Object[]{acceptUpdateTime.getKey(),0,null});
			}
		}
		if(list.size() > 0){
			return list.toArray();
		}else{
			return null;
		}
	}
	
	private Object[] getResourceInfo(String type,Map<String, Map<String, Integer>> map){
		Map<String,Integer> resource = new HashMap<>();
		for (Map<String, Integer> subMap : map.values()) {
			if(subMap != null && subMap.size() > 0){
				ObjectUtil.mapAdd(resource, subMap);
			}
		}
		if(resource.size() > 0){
			List<Object[]> list = new ArrayList<>();
			for (Entry<String, Integer> entry : resource.entrySet()) {
				if(entry.getValue() > 0){
					list.add(new Object[]{entry.getKey(),entry.getValue()});
				}
			}
			if(list.size() > 0){
				return new Object[]{CovertObjectUtil.object2Integer(type),map.size(),list.toArray()};
			}
		}
		return null;
	}
	
	public void changeTypeMap(Long userRoleId,Map<String,Map<String,Integer>> map,String type){
		if(map == null || map.size() < 1){
			return;
		}
		boolean change = false;
		for (Map<String, Integer> resource : map.values()) {
			for (Integer value : resource.values()) {
				if(value > 0){
					change = true;
					break;
				}
			}
			if(change){
				break;
			}
		}
		if(change){
			RoleResourceBack resourceBack = getRoleResourceBackNoCheck(userRoleId);
			Map<String,Map<String,Integer>> oldMap = getMapByType(resourceBack, type);
			oldMap.putAll(map);
			resourceBack.getUpdateMap().put(type,GameSystemTime.getSystemMillTime());
			resourceBack.changeState();
			resourceBackDao.cacheUpdate(resourceBack, userRoleId);
		}
	}
	
	public Object[] reciveResourceById(Long userRoleId,Integer id,Integer costType){
		RoleResourceBack resourceBack = getRoleResourceBack(userRoleId);
		Map<String,Map<String,Integer>> map = resourceBack.getInfoMap().get(id.toString());
		if(map == null || map.size() < 1){
			return AppErrorCode.NO_CAN_FIND_RESOURCE;//无可领取奖励
		}
		Map<String,Integer> reward = new HashMap<>();
		for (Map<String, Integer> subMap : map.values()) {
			if(subMap != null && subMap.size() > 0){
				ObjectUtil.mapAdd(reward, subMap);
			}
		}
		if(reward.size() < 1){
			return AppErrorCode.NO_CAN_FIND_RESOURCE;//无可领取奖励
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;//角色不存在
		}
		int cost = calCostMoney(role.getLevel(), costType, reward);
		if(cost < 1){
			return AppErrorCode.NO_CAN_FIND_RESOURCE;//无可领取奖励
		}
		ZiYuanPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ZIYUAN);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;//配置异常
		}
		Object[] result = AppErrorCode.CONFIG_ERROR;//配置异常
		//消耗
		if(costType.equals(GameConstants.RESOURCE_BACK_COST_TYPE_YINLIANG)){
			result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, cost, userRoleId, LogPrintHandle.CONSUME_RESOURCE_BACK, true, LogPrintHandle.CBZ_RESOURCE_BACK);
			ObjectUtil.mapTimes(reward, config.getMoneyRate());
		}else if(costType.equals(GameConstants.RESOURCE_BACK_COST_TYPE_YUANBAO)){
			result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, cost, userRoleId, LogPrintHandle.CONSUME_RESOURCE_BACK, true, LogPrintHandle.CBZ_RESOURCE_BACK);
			ObjectUtil.mapTimes(reward, config.getGoldRate());
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ() && result == null){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,cost,LogPrintHandle.CONSUME_RESOURCE_BACK,QQXiaoFeiType.CONSUME_RESOURCE_BACK,1});
			}
		}
		if(result != null){
			return result;
		}
		
		resourceBack.getInfoMap().remove(id.toString());
		resourceBack.changeState();
		
		resourceBack.getAcceptUpdateTimeMap().put(id.toString(), System.currentTimeMillis());
		resourceBack.changeTypeUpdateTime();
		resourceBackDao.cacheUpdate(resourceBack, userRoleId);
		
		//获得
		roleBagExportService.putGoodsAndNumberAttr(reward, userRoleId, GoodsSource.NONE, LogPrintHandle.GET_RESOURCE_BACK, LogPrintHandle.GBZ_RESOURCE_BACK, true);
		return new Object[]{1,id};
	}
	
	public Object[] reciveResourceAll(Long userRoleId,Integer costType){
		RoleResourceBack resourceBack = getRoleResourceBack(userRoleId);
		long nowTime = System.currentTimeMillis();
		Set<Integer> configIds = new HashSet<>();
		Map<String,Integer> reward = new HashMap<>();
		for (Entry<String,Map<String,Map<String,Integer>>> entry : resourceBack.getInfoMap().entrySet()) {
			String type = entry.getKey();
			
			for (Map<String, Integer> subMap : entry.getValue().values()) {
				if(subMap != null && subMap.size() > 0){
					ObjectUtil.mapAdd(reward, subMap);
					resourceBack.getAcceptUpdateTimeMap().put(type,nowTime);
					configIds.add(Integer.parseInt(type));
				}
			}
		}
		if(reward.size() < 1){
			return AppErrorCode.NO_CAN_FIND_RESOURCE;//无可领取奖励
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;//角色不存在
		}
		int cost = calCostMoney(role.getLevel(), costType, reward);
		if(cost < 1){
			return AppErrorCode.NO_CAN_FIND_RESOURCE;//无可领取奖励
		}
		
		ZiYuanPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ZIYUAN);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;//配置异常
		}
		Object[] result = AppErrorCode.CONFIG_ERROR;//配置异常
		//消耗
		if(costType.equals(GameConstants.RESOURCE_BACK_COST_TYPE_YINLIANG)){
			result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, cost, userRoleId, LogPrintHandle.CONSUME_RESOURCE_BACK, true, LogPrintHandle.CBZ_RESOURCE_BACK);
			ObjectUtil.mapTimes(reward, config.getMoneyRate());
		}else if(costType.equals(GameConstants.RESOURCE_BACK_COST_TYPE_YUANBAO)){
			result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, cost, userRoleId, LogPrintHandle.CONSUME_RESOURCE_BACK, true, LogPrintHandle.CBZ_RESOURCE_BACK);
			ObjectUtil.mapTimes(reward, config.getGoldRate());
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ() && result == null){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,cost,LogPrintHandle.CONSUME_RESOURCE_BACK,QQXiaoFeiType.CONSUME_RESOURCE_BACK,1});
			}
		}
		if(result != null){
			return result;
		}
		resourceBack.getInfoMap().clear();
		resourceBack.changeState();
		resourceBack.changeTypeUpdateTime();
//		resourceBack.changeTypeUpdateTime();
		
		resourceBackDao.cacheUpdate(resourceBack, userRoleId);
		
		//获得
		roleBagExportService.putGoodsAndNumberAttr(reward, userRoleId, GoodsSource.NONE, LogPrintHandle.GET_RESOURCE_BACK, LogPrintHandle.GBZ_RESOURCE_BACK, true);
		return new Object[]{AppErrorCode.OK,configIds.toArray()};
	}
	
	private int calCostMoney(int level,Integer costType,Map<String,Integer> reward){
		double cost = 0;
		ZiYuanJiaGeConfig jiageConfig = ziYuanJiaGeConfigExportService.loadByLevel(level);
		double base = 0;
		if(costType.equals(GameConstants.RESOURCE_BACK_COST_TYPE_YINLIANG)){
			base = jiageConfig.getNeedMoney();
		}else if(costType.equals(GameConstants.RESOURCE_BACK_COST_TYPE_YUANBAO)){
			base = jiageConfig.getNeedGold();
		}else{
			return 0;
		}
		for (Entry<String, Integer> entry : reward.entrySet()) {
			double need = entry.getValue() * base;
			if(ModulePropIdConstant.EXP_GOODS_ID.equals(entry.getKey())){//经验
				need = need / jiageConfig.getExp();
			}else if(ModulePropIdConstant.MONEY_GOODS_ID.equals(entry.getKey())){//银两
				need = need / jiageConfig.getMoney();
			}else if(ModulePropIdConstant.RONGYU_GOODS_ID.equals(entry.getKey())){//荣誉
				need = need / jiageConfig.getRongyu();
			}else if(ModulePropIdConstant.MONEY_ZHENQI_ID.equals(entry.getKey())){//真气
				need = need / jiageConfig.getZhenqi();
			}
			cost += need;
		}
		long need = (long)cost;
		if(need < cost){
			need++;
		}
		if(need > Integer.MAX_VALUE){
			return -1;
		}
		return (int)need;
	}
	
	/**
	 * 获取资源找回是否有奖励
	 * @param userRoleId
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的5次方
	 */
	public int getResourceBackStateValue(Long userRoleId){
		//int(0)二进制   奖励 有为1，无为0   -是否有资源找回(5)
		int state = 0;
		RoleResourceBack resourceBack = getRoleResourceBack(userRoleId);  
		for (Map<String, Map<String, Integer>> map : resourceBack.getInfoMap().values()) {
			for (Map<String, Integer> subMap : map.values()) {
				if(subMap != null && subMap.size() > 0){
					for (Integer value : subMap.values()) {
						if(value > 0){
							return 32;
						}
					}
				}
			}
		}
		return state;
	}
}
