package com.junyou.bus.huiyanshijin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.huiyanshijin.configue.HuiYanShiJingConfig;
import com.junyou.bus.huiyanshijin.configue.HuiYanShiJingConfigExportService;
import com.junyou.bus.huiyanshijin.configue.HuiYanShiJingConfigGroup;
import com.junyou.bus.huiyanshijin.dao.HuiYanShiJinLogDao;
import com.junyou.bus.huiyanshijin.entity.HuiYanShiJinLog;
import com.junyou.bus.huiyanshijin.utils.HuiYanShiJingUtils;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.HuiYanShiJinLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;

/**
 * @author zhongdian
 * 2016-3-23 下午3:29:43
 */
@Service
public class HuiYanShiJingService {

	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private HuiYanShiJinLogDao huiYanShiJinLogDao;
	
	public Object[] waKuang(Long userRoleId,Integer version,int subId,int level,int kIndex){
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
		HuiYanShiJingConfigGroup config = HuiYanShiJingConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断元宝
		Integer gold = config.getGoldMap().get(level);
		if(gold == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,gold, userRoleId);
		if(null != goldError){ 
			return goldError;
		}
		//全服抽取次数
		int count = HuiYanShiJingUtils.getCountBySubId(subId, level);
		//判断全服次数是否达到要求
		int djCount = config.getDjCount();//大奖次数
		//根据抽取次数和级别获取配置
		Map<Integer, HuiYanShiJingConfig> configs = config.getConfigMap();
		if(configs == null || configs.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		HuiYanShiJingConfig hyConfig = null;
		if(count != 0 && (count +1) % djCount == 0){
			hyConfig = getQuFuConfigByLevel(configs, level);
		}else{
			hyConfig = getConfigByCountAndLevel(configs, level);
		}
		if(hyConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//抽矿石
		/*Map<String, Integer> zjMap = new HashMap<String, Integer>();
		zjMap.put(GameConstants.HUIYAN_ZHONGJIANG_GOLD, 0);
		zjMap.put(GameConstants.HUIYAN_ZHONGJIANG_GONGGAO, 0);
		Object[] zjObj = chouKuang(hyConfig,zjMap);
		if(zjObj == null || zjObj.length <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}*/
		//进行抽奖
		Object[] jiang = Lottery.getRandomKeyByInteger(hyConfig.getJlMap());
		if(jiang == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int kId = Integer.parseInt(jiang[0].toString());//抽出的矿石ID
		int zjGold = Integer.parseInt(jiang[1].toString());//矿石对应的元宝
		String isGongGao = jiang[2].toString();
		//扣元宝
		if(gold > 0){
			//消耗元宝
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_HUIYANSHIJIN, true,LogPrintHandle.CBZ_HUIYANSHIJIN);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,gold,LogPrintHandle.CONSUME_HUIYANSHIJIN,QQXiaoFeiType.CONSUME_HUIYANSHIJIN,1});
			}
		}
		//增加全服次数
		HuiYanShiJingUtils.addCountBySubId(subId, level);
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		String roleName = role.getName();
		/*//发中奖元宝
		int zjGold = zjMap.get(GameConstants.HUIYAN_ZHONGJIANG_GOLD);
		int isGongGao = zjMap.get(GameConstants.HUIYAN_ZHONGJIANG_GONGGAO);*/
		if(kId ==GameConstants.HUIYAN_ZHONGJIANG_KUANGSHI_ID && zjGold > 0){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.GOLD,zjGold, userRoleId, LogPrintHandle.GET_RFB_HUIYANSHIJIN, LogPrintHandle.GBZ_RFB_HUIYANSHIJIN);
			//公告
			if(isGongGao != null && !"".equals(isGongGao) && !"0".equals(isGongGao)){
				BusMsgSender.send2All(ClientCmdType.HUIYAN_TUISONG, new Object[]{subId,roleName,zjGold,configSong.getSkey()});
				addWaKuangLog(userRoleId, roleName, zjGold, subId);
			}
		}
		//打日志
		try {
			GamePublishEvent.publishEvent(new HuiYanShiJinLogEvent(userRoleId, gold,zjGold,roleName, level, count+1,subId));
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		return new Object[]{1,subId,new Object[]{kId,zjGold},kIndex};
	}
	private HuiYanShiJingConfig getQuFuConfigByLevel(Map<Integer, HuiYanShiJingConfig> configs,int level){
		if(level == 1){
			return configs.get(GameConstants.HYSJ_QUANFU_COUNT_ID_1);
		}else if(level == 2){
			return configs.get(GameConstants.HYSJ_QUANFU_COUNT_ID_2);
		}else if(level == 3){
			return configs.get(GameConstants.HYSJ_QUANFU_COUNT_ID_3);
		}
		return null;
	}
	
	private void addWaKuangLog(Long userRoleId,String roleName,int gold,int subId){
		HuiYanShiJinLog log =  new HuiYanShiJinLog();
		log.setUserRoleId(userRoleId);
		log.setRoleName(roleName);
		log.setGold(gold);
		log.setSubId(subId);
		log.setCreateTime(GameSystemTime.getSystemMillTime());
		
		huiYanShiJinLogDao.insertDb(log);
	}
	
	
	private Object[] chouKuang(HuiYanShiJingConfig hyConfig,Map<String, Integer> zjMap){
		List<Object[]> list = new ArrayList<>();
		boolean isZhong = false;//是否已经中奖（只能中1次奖）
		Map<Object[], Integer> jlMap = new HashMap<>();
		jlMap.putAll(hyConfig.getJlMap());
		for (int i = 0; i < GameConstants.HUIYAN_KUANGSHI_NUMBER; i++) {
			//进行抽奖
			Object[] jiang = Lottery.getRandomKeyByInteger(jlMap);
			if(jiang == null){
				continue;
			}
			int kId = Integer.parseInt(jiang[0].toString());//抽出的矿石ID
			int gold = Integer.parseInt(jiang[1].toString());//矿石对应的元宝
			if(kId == GameConstants.HUIYAN_ZHONGJIANG_KUANGSHI_ID){
				if(isZhong){
					for (int j = 0; j < 100; j++) {
						Object[] jiang1 = Lottery.getRandomKeyByInteger(jlMap);
						if(jiang1 == null){
							continue;
						}
						int kId1 = Integer.parseInt(jiang1[0].toString());//抽出的矿石ID
						int gold1 = Integer.parseInt(jiang1[1].toString());//矿石对应的元宝
						if(kId1 == GameConstants.HUIYAN_ZHONGJIANG_KUANGSHI_ID){
							jlMap.remove(jiang);
							continue;
						}else{
							list.add(new Object[]{kId1,gold1});
							break;
						}
					}
				}else{
					list.add(new Object[]{kId,gold});
					zjMap.put(GameConstants.HUIYAN_ZHONGJIANG_GOLD, gold);
					zjMap.put(GameConstants.HUIYAN_ZHONGJIANG_GONGGAO, 1);
					isZhong = true;
					jlMap.remove(jiang);
				}
				
			}else{
				list.add(new Object[]{kId,gold});
			}
			
		}
		return list.toArray();
	}
	
	private HuiYanShiJingConfig getConfigByCountAndLevel(Map<Integer, HuiYanShiJingConfig> configs,int level){
		for (Integer id: configs.keySet()) {
			HuiYanShiJingConfig config = configs.get(id);
			if(config.getRank().intValue() == level && config.getId() > 0){
					return config;
			}
		}
		return null;
	}
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		HuiYanShiJingConfigGroup config = HuiYanShiJingConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		//是否在有这个活动或者是否在时间内
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return null;
		}
		//判断活动开始时间
		if(configSong != null){
			long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
			long upTime = HuiYanShiJingUtils.getUpdateTime(subId);
			if(upTime != 0 && upTime < startTime){
				HuiYanShiJingUtils.cleanMap(subId);
			}
		}
		
		return new Object[]{
				config.getPic(),
				config.getDes(),
				config.getGoldList().toArray(),
				config.getIconList().toArray(),
				config.getMaxGold(),
				getLog(subId)
				
		};
	}
	/**
	 * 获取日志
	 * @return
	 */
	private Object[] getLog(int subId){
		List<HuiYanShiJinLog> xunbaoLogs = null;
		try {
			xunbaoLogs = huiYanShiJinLogDao.getHuiYanShiJinDb(subId);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		if(xunbaoLogs == null || xunbaoLogs.size() <= 0){
			return null;
		}
		List<Object[]> list = new ArrayList<>();
		for (int i = 0; i < xunbaoLogs.size(); i++) {
			HuiYanShiJinLog log = xunbaoLogs.get(i);
			list.add(new Object[]{log.getRoleName(),log.getGold()});
		}
		return list.toArray();
		
	}
}
