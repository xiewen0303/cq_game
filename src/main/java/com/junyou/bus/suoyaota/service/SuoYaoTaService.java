package com.junyou.bus.suoyaota.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.login.entity.RefabuSevenLogin;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.suoyaota.configure.export.SuoYaoTaConfigExportService;
import com.junyou.bus.suoyaota.dao.RefbSuoyaotaDao;
import com.junyou.bus.suoyaota.entity.RefbSuoyaota;
import com.junyou.bus.suoyaota.entity.SuoYaoTaCengConfig;
import com.junyou.bus.suoyaota.entity.SuoYaoTaConfig;
import com.junyou.bus.suoyaota.entity.SuoYaoTaInfo;
import com.junyou.bus.suoyaota.entity.SuoYaoTaSlotConfig;
import com.junyou.bus.tongyong.dao.ActityCountLogDao;
import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xunbao.export.XunBaoExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XunBaoLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @author LiuYu
 * 2015-8-3 上午10:36:16
 */
@Service
public class SuoYaoTaService{
	@Autowired
	private RefbSuoyaotaDao refbSuoyaotaDao;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private XunBaoExportService xunBaoExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ActityCountLogDao actityCountLogDao;
	private Map<Integer,Map<Long,SuoYaoTaInfo>> suoYaoTaMap = new HashMap<>();
	
	public List<RefbSuoyaota> initRefbSuoyaota(Long userRoleId) {
		return refbSuoyaotaDao.initRefbSuoyaota(userRoleId);
	}
	
	private RefbSuoyaota getRefbSuoyaota(Long userRoleId,Integer subId){
		RefbSuoyaota refbSuoyaota = null;
		final Integer sid = subId;
		List<RefbSuoyaota> refbSuoyaotas = refbSuoyaotaDao.cacheLoadAll(userRoleId, new IQueryFilter<RefbSuoyaota>() {
			private boolean stop = false;
			@Override
			public boolean check(RefbSuoyaota entity) {
				if(entity.getSubId().equals(sid)){
					stop = true;
				}
				return stop;
			}

			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if(refbSuoyaotas != null && refbSuoyaotas.size() > 0){
			refbSuoyaota = refbSuoyaotas.get(0);
		}else{
			refbSuoyaota = createRefbSuoyaota(userRoleId, subId);
			refbSuoyaotaDao.cacheInsert(refbSuoyaota, userRoleId);
		}
		return refbSuoyaota;
	}
	
	private RefbSuoyaota createRefbSuoyaota(Long userRoleId,Integer subId){
		RefbSuoyaota refbSuoyaota = new RefbSuoyaota();
		refbSuoyaota.setId(IdFactory.getInstance().generateId(ServerIdType.RFB_GOODS));
		refbSuoyaota.setUserRoleId(userRoleId);
		refbSuoyaota.setSubId(subId);
		refbSuoyaota.setCurCeng(1);
		refbSuoyaota.setCurLucky(0);
		refbSuoyaota.setUpdateTime(GameSystemTime.getSystemMillTime());
		return refbSuoyaota;
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
		SuoYaoTaConfig suoYaoTaConfig = SuoYaoTaConfigExportService.getInstance().loadByMap(subId);
		if(suoYaoTaConfig == null){
			return null;
		}
		RefbSuoyaota suoyaota = getRefbSuoyaota(userRoleId, subId);
		//循环检测
		suoyaota = updateJianCe(subId, suoyaota);
		
		Object[] roleInfo = new Object[]{
			suoyaota.getCurCeng(),
			suoYaoTaConfig.getCost(),
			GameConstants.SUOYAOTA_ADD_LUCKY,
			suoyaota.getCurLucky()
		};
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
		SuoYaoTaInfo suoYaoTaInfo = getSuoYaoTaInfo(suoYaoTaConfig,userRoleId);
		return new Object[]{suoYaoTaConfig.getInfo(),suoYaoTaConfig.getBg(),suoYaoTaInfo.getClientItems(),roleInfo,new Object[]{count,suoYaoTaConfig.getMaxCount()}};
	}
	
	private RefbSuoyaota updateJianCe(int subId,RefbSuoyaota suoyaota){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = suoyaota.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			suoyaota.setCurCeng(1);
			suoyaota.setCurLucky(0);
			suoyaota.setUpdateTime(GameSystemTime.getSystemMillTime());
			refbSuoyaotaDao.cacheUpdate(suoyaota, suoyaota.getUserRoleId());
		}
		return suoyaota;
	}
	
	private SuoYaoTaInfo getSuoYaoTaInfo(SuoYaoTaConfig suoYaoTaConfig,Long userRoleId){
		Map<Long,SuoYaoTaInfo> map = suoYaoTaMap.get(suoYaoTaConfig.getId());
		if(map == null){
			synchronized (suoYaoTaMap) {
				map = suoYaoTaMap.get(suoYaoTaConfig.getId());
				if(map == null){
					map = new HashMap<>();
					suoYaoTaMap.put(suoYaoTaConfig.getId(), map);
				}
			}
		}
		SuoYaoTaInfo suoYaoTaInfo = map.get(userRoleId);
		if(suoYaoTaInfo == null){
			suoYaoTaInfo = new SuoYaoTaInfo();
			suoYaoTaInfo.setUserRoleId(userRoleId);
			initItems(suoYaoTaConfig,suoYaoTaInfo);
			map.put(userRoleId, suoYaoTaInfo);
		}
		return suoYaoTaInfo;
	}
	
	private void initItems(SuoYaoTaConfig suoYaoTaConfig,SuoYaoTaInfo suoyaota){
		Map<Integer,Map<Integer,GoodsConfigureVo>> items = new HashMap<>();
		List<Object[]> clientList = new ArrayList<>();
		for (Entry<Integer, SuoYaoTaCengConfig> entry : suoYaoTaConfig.getCengInfo().entrySet()) {
			Map<Integer,GoodsConfigureVo> cengItems = new HashMap<>();
			List<Object> clientCeng = new ArrayList<>();
			for (Entry<Integer, SuoYaoTaSlotConfig> slotEntry : entry.getValue().getSlotInfo().entrySet()) {
				Integer slot = slotEntry.getKey();
				SuoYaoTaSlotConfig config = slotEntry.getValue();
				GoodsConfigureVo gift = Lottery.getRandomKeyByInteger(config.getItemOdds(),config.getItemAllOdd());
				cengItems.put(slot, gift);
				clientCeng.add(new Object[]{gift.getGoodsId(),gift.getGoodsCount()});
			}
			items.put(entry.getKey(), cengItems);
			clientList.add(new Object[]{entry.getValue().getMaxLucky(),clientCeng.toArray()});
		}
		suoyaota.setItems(items);
		suoyaota.setClientItems(clientList.toArray());
	}
	
	public Object[] getRefbState(Long userRoleId, Integer subId){
		SuoYaoTaConfig suoYaoTaConfig = SuoYaoTaConfigExportService.getInstance().loadByMap(subId);
		if(suoYaoTaConfig == null){
			return null;
		}
		RefbSuoyaota suoyaota = getRefbSuoyaota(userRoleId, subId);
		SuoYaoTaInfo suoYaoTaInfo = getSuoYaoTaInfo(suoYaoTaConfig,userRoleId);
		return new Object[]{suoyaota.getSubId(),suoYaoTaInfo.getClientItems(),suoyaota.getCurCeng(),suoYaoTaConfig.getCost(),GameConstants.SUOYAOTA_ADD_LUCKY,suoyaota.getCurLucky()};
	}
	
	public Object[] chou(Long userRoleId, Integer subId,Integer version,BusMsgQueue busMsgQueue){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			if(newSubHandleData != null){
				Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			}
			return null;
		}
		SuoYaoTaConfig suoYaoTaConfig = SuoYaoTaConfigExportService.getInstance().loadByMap(subId);
		if(suoYaoTaConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		boolean insert = true;
		//如果配置了次数
		if(suoYaoTaConfig.getMaxCount() > 0){
			//判断玩家次数
			ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
			if(log != null){
				insert = false;
				if(log.getCount() != null && log.getCount()+1 > suoYaoTaConfig.getMaxCount()){
					return AppErrorCode.ACTITY_MAX_COUNT;
				}
			}
		}
		RefbSuoyaota refbSuoyaota = getRefbSuoyaota(userRoleId, subId);
		SuoYaoTaCengConfig cengConfig = suoYaoTaConfig.getCengInfo().get(refbSuoyaota.getCurCeng());
		if(cengConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Object[] result = xunBaoExportService.isFull(userRoleId);
		if(result != null){
			return result;
		}
		
		Object[] ret = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, suoYaoTaConfig.getCost(), userRoleId, LogPrintHandle.CONSUME_SUOYAOTA, true, LogPrintHandle.CBZ_SUOYAOTA);
		if(ret != null){
			return ret;
		}else{
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,suoYaoTaConfig.getCost(),LogPrintHandle.CONSUME_SUOYAOTA,QQXiaoFeiType.CONSUME_SUOYAOTA,1});
			}
		}
		
		SuoYaoTaInfo suoYaoTaInfo = getSuoYaoTaInfo(suoYaoTaConfig, userRoleId);
		Object[] cengInfo = (Object[])suoYaoTaInfo.getClientItems()[refbSuoyaota.getCurCeng()-1];
		Integer index = null;
		String newItemId = null;
		int newItemCount = 0;
		Map<Integer,GoodsConfigureVo> cengItems = suoYaoTaInfo.getItems().get(refbSuoyaota.getCurCeng());
		if(cengItems != null && cengInfo != null){
			index = Lottery.getRandomKeyByInteger(cengConfig.getSlotOdds(), cengConfig.getSlotAllOdd());
			GoodsConfigureVo gift = cengItems.get(index);
			Map<String,Integer> goods = new HashMap<>();
			goods.put(gift.getGoodsId(), gift.getGoodsCount());
			List<Object[]> outClients = new ArrayList<Object[]>();
			xunBaoExportService.putIn(userRoleId, goods, outClients, busMsgQueue, LogPrintHandle.XUNBAO_TYPE_CBZ);
			int nextCeng = refbSuoyaota.getCurCeng()+1;
			if(nextCeng > suoYaoTaConfig.getCengInfo().size()){
				//已达到最大层数
				initItems(suoYaoTaConfig, suoYaoTaInfo);
				refbSuoyaota.setCurLucky(0);
				refbSuoyaota.setCurCeng(1);
				busMsgQueue.addMsg(userRoleId, ClientCmdType.CANGBAOGE_INFO, new Object[]{subId,suoYaoTaInfo.getClientItems(),refbSuoyaota.getCurCeng(),suoYaoTaConfig.getCost(),GameConstants.SUOYAOTA_ADD_LUCKY,refbSuoyaota.getCurLucky()});
			}else if(index == 0 || refbSuoyaota.getCurLucky() >= cengConfig.getMaxLucky()){
				refbSuoyaota.setCurLucky(0);
				refbSuoyaota.setCurCeng(nextCeng);
			}else{
				SuoYaoTaSlotConfig config = cengConfig.getSlotInfo().get(index);
				GoodsConfigureVo newGift = Lottery.getRandomKeyByInteger(config.getItemOdds(),config.getItemAllOdd());
				cengItems.put(index, newGift);
				Object[] clientItems = (Object[])cengInfo[1];
				newItemId = newGift.getGoodsId();
				newItemCount = newGift.getGoodsCount();
				clientItems[index] = new Object[]{newItemId,newItemCount};
				refbSuoyaota.setCurLucky(refbSuoyaota.getCurLucky()+1);
			}
			refbSuoyaota.setUpdateTime(GameSystemTime.getSystemMillTime());
			refbSuoyaotaDao.cacheUpdate(refbSuoyaota, userRoleId);
			
			JSONArray receiveItems = new JSONArray();
			LogFormatUtils.parseJSONArray(goods, receiveItems);
			GamePublishEvent.publishEvent(new XunBaoLogEvent(userRoleId, suoYaoTaConfig.getCost(), getRoleName(userRoleId), null, 0, receiveItems, LogPrintHandle.XUNBAO_TYPE_PT, LogPrintHandle.XUNBAO_TYPE_PT));
		}
		if(suoYaoTaConfig.getMaxCount() > 0){
			if(insert){
				ActityCountLog log = new ActityCountLog();
				log.setUserRoleId(userRoleId);
				log.setCount(1);
				log.setUpdateTime(GameSystemTime.getSystemMillTime());
				actityCountLogDao.insertDb(log, subId);
			}else{
				actityCountLogDao.addActivityCount(subId, userRoleId, 1);
			}
		}
		/**
		 * 成功:[0:int(1),
      1:int(子活动ID),
      2:int(序号id),
      3:String(新填充进来的物品id),
      4:int(新填充进来的物品物品数量),
      5:int(可成功进入下一层id(1-6),-1表示还停留在当前层)]
		 */
		return new Object[]{1,subId,index,newItemId,newItemCount,refbSuoyaota.getCurCeng()};
	}
	
	private String getRoleName(long userRoleId){
		RoleWrapper role =	roleExportService.getLoginRole(userRoleId);
		return role==null?"":role.getName();
	}
}
