package com.junyou.bus.oncechong.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.oncechong.configure.export.OnceChongConfig;
import com.junyou.bus.oncechong.configure.export.OnceChongConfigExportService;
import com.junyou.bus.oncechong.configure.export.OnceChongConfigGroup;
import com.junyou.bus.oncechong.dao.OncechongDao;
import com.junyou.bus.oncechong.entity.RoleOncechong;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RfbActivityPartInLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;


@Service
public class OnceChongService  extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		//判断配置
		OnceChongConfigGroup configGroup = OnceChongConfigExportService.getInstance().loadByMap(subId);
		if(configGroup== null){
			return false;
		}
		
		Map<Integer, RoleOncechong> roleOncechongs = getOnceChong(userRoleId, subId);
		
		for (int configId : configGroup.getConfigMap().keySet()) {
			
			RoleOncechong roleOncechong = roleOncechongs.get(configId);
			if(roleOncechong == null){
				continue;
			}
			if(roleOncechong.getChongCount() <= roleOncechong.getReceiveCount()){
				continue;
			}
			return true;
		}
		return false;
	}
	
	@Autowired
	private OncechongDao oncechongDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	
	public List<RoleOncechong> initOncechong(Long userRoleId){
		return oncechongDao.initOncechong(userRoleId);
	}
	
	
	private Map<Integer,RoleOncechong> getOnceChong(Long userRoleId,final int subId){
		Map<Integer,RoleOncechong> result = new HashMap<>();
		
		List<RoleOncechong> list = oncechongDao.cacheLoadAll(userRoleId,new IQueryFilter<RoleOncechong>() {
			@Override
			public boolean check(RoleOncechong entity) {
				if(subId == entity.getSubId()){
					return true;
				}
				return false;
			}

			@Override
			public boolean stopped() {
				return false;
			}
		});
		if(list == null) {return result;}
		for (RoleOncechong roleOncechong : list) {
			result.put(roleOncechong.getConfigId(), roleOncechong);
		}
		
		updateCheck(subId, result);
		return result;
	}
	
	
	
	public Object[] lingqu(Long userRoleId,Integer version,int subId,int configId){
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
		
		//判断配置
		OnceChongConfig config = OnceChongConfigExportService.getInstance().loadByKeyId(subId,configId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Map<Integer, RoleOncechong> roleOncechongs = getOnceChong(userRoleId, subId);
		if(roleOncechongs == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RoleOncechong roleOncechong = roleOncechongs.get(configId);
		if(roleOncechong == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(roleOncechong.getChongCount() <= roleOncechong.getReceiveCount()){
			return AppErrorCode.NO_LEICHONG_COUNT;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		Map<String,GoodsConfigureVo> jiangli = null;
		if(roleOncechong.getReceiveCount() == 0){
			 jiangli = config.getFirstJianLiMap(role.getConfigId().byteValue());
		}else{
			 jiangli = config.getJianLiMap(role.getConfigId().byteValue());
		}
		
	 
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBagVo(jiangli, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		
		//更新玩家领取状态
		roleOncechong.setReceiveCount(roleOncechong.getReceiveCount() + 1);
		roleOncechong.setUpdateTime(System.currentTimeMillis());
		oncechongDao.cacheUpdate(roleOncechong, userRoleId);
		//发放奖励
		roleBagExportService.putGoodsVoAndNumberAttr(jiangli, userRoleId, GoodsSource.GOODS_GET_ONCEC, LogPrintHandle.GET_RFB_LEICHONG, LogPrintHandle.GBZ_RFB_ONCECHONG, true);
	
		super.checkIconFlag(userRoleId, subId);
		
		//打印活动参与日志
		GamePublishEvent.publishEvent(
		        new RfbActivityPartInLogEvent(
		                LogPrintHandle.REFABU_ONCECHONGZHI,
		                configSong.getActivityId(), 
		                configSong.getSubName(), 
		                configSong.getSubActivityType(), 
		                configSong.getStartTimeByMillSecond(), 
		                configSong.getEndTimeByMillSecond(), 
		                userRoleId
		        )
	  );
		
		return new Object[]{1,subId,configId};
	}
	
	
	/**
	 * 版本号不变是推送
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getLingQuStatus(long userRoleId,Integer subId){
		
		OnceChongConfigGroup config = OnceChongConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		//处理数据
		Map<Integer,RoleOncechong> datas = getOnceChong(userRoleId, subId);
		Map<Integer, OnceChongConfig> configMap = config.getConfigMap();
		List<Object[]> czList = new ArrayList<>();
		
		for(Map.Entry<Integer, OnceChongConfig> entry : configMap.entrySet()){
			int configId = entry.getKey();
			RoleOncechong roleOncecong = datas.get(configId);
			int reviceCount = 0;
			int chongCount = 0;
			if(roleOncecong != null){
				chongCount = roleOncecong.getChongCount();
				reviceCount = roleOncecong.getReceiveCount();
			}
			czList.add(new Object[]{configId,chongCount,reviceCount});
		}
			
		return new Object[]{subId,czList.toArray()};
	}
	
	
	
//	/**
//	 * 获取领取次数
//	 * @return
//	 */
//	private int getLingQuCount(String lingquStatus,Integer configId){
//		String[] lingqu = lingquStatus.split("\\|");
//		if(lingqu == null || lingqu.length <= 0){
//			return 0;
//		}
//		for (int i = 0; i < lingqu.length; i++) {
//			if(lingqu[i] == null || "".equals(lingqu[i])){
//				continue;
//			}
//			String[] status = lingqu[i].split(",");
//			if(Integer.parseInt(status[0]) == configId.intValue()){
//				return Integer.parseInt(status[1]);
//			}
//		}
//		return 0;
//	}
	
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){

		OnceChongConfigGroup config = OnceChongConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据  configId = roleOncechong
		Map<Integer,RoleOncechong> oncecongDatas = getOnceChong(userRoleId, subId);		
//		//判断活动循环数据
//		updateJianCe(subId, oncecongDatas);
		
		//活动迟上线，记录今天已经充值的元宝数
		yuanbaoJianCe(oncecongDatas);
		
		Map<Integer, OnceChongConfig> configMap = config.getConfigMap();
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		List<Object[]> voList = new ArrayList<>();
		
		for(Map.Entry<Integer, OnceChongConfig> entry : configMap.entrySet()){
			OnceChongConfig onceChongConfig = entry.getValue();
			int configId = onceChongConfig.getId();
			RoleOncechong  roleOncecong = oncecongDatas.get(configId);
			int chongCount = 0;
			int receiveCount = 0;
			if(roleOncecong != null){
				chongCount = roleOncecong.getChongCount();
				receiveCount = roleOncecong.getReceiveCount();
			}
			Object[] voObj = new Object[]{
					onceChongConfig.getId(),
					entry.getValue().getBeginValue(),
					entry.getValue().getEndValue(),
					chongCount,
					receiveCount,
					onceChongConfig.getFirstJianLiClientMap(role.getConfigId().byteValue()),
					onceChongConfig.getJianLiClientMap(role.getConfigId().byteValue()),
					onceChongConfig.getFanhuana(),
					onceChongConfig.getFanhuanb()
			};
			voList.add(voObj);
		}
		
		return new Object[]{
				config.getPic(),
				config.getDes(),
				voList.toArray()
		};
		
	}

	/**
	 * 	TODO 单笔充值迟上线，今日之前的充值是否要计入内
	 * @param oncechong  
	 */
	private void yuanbaoJianCe(Map<Integer,RoleOncechong> oncechong){
		

//		RoleYuanbaoRecord record = roleYuanbaoRecordService.getRoleYuanBaoRecord(leichong.getUserRoleId());
//		if(record.getCzValue() > leichong.getRechargeVal()){
//			leichong.setRechargeVal(record.getCzValue());
//			leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//			
//			oncechongDao.cacheUpdate(leichong, leichong.getUserRoleId());
//		}
	}
	
	private void updateCheck(int subId,Map<Integer,RoleOncechong> oncechongs){
		if(oncechongs == null || oncechongs.size() == 0){
			return;
		}
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
//		if(configSong.getTimeType() != ActivityTimeType.TIME_4_KAI_FU_LOOP && configSong.getTimeType() != ActivityTimeType.TIME_5_HE_FU_LOOP){
//			return;
//		}
		
		for (Entry<Integer,RoleOncechong> entry : oncechongs.entrySet()) {
			RoleOncechong oncechong = entry.getValue();
			long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
			long modifyTime = oncechong.getUpdateTime();
			long nowTime = GameSystemTime.getSystemMillTime();
			if(startTime  > modifyTime && startTime < nowTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
				oncechong.setChongCount(0);
				oncechong.setReceiveCount(0);
				oncechong.setUpdateTime(System.currentTimeMillis());
				
				oncechongDao.cacheUpdate(oncechong, oncechong.getUserRoleId());
			}
		}
	}
	
//	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
//		LeiChongConfigGroup config = LeiChongConfigExportService.getInstance().loadByMap(subId);
//		if(config == null){
//			return null;
//		}
//		//处理数据
//		Leichong leichong = getLeiChong(userRoleId, subId);		
//		Map<Integer, LeiChongConfig> configMap = config.getConfigMap();
//		List<Object[]> czList = new ArrayList<>();
//		for(Map.Entry<Integer, LeiChongConfig> entry : configMap.entrySet()){
//			
//			czList.add(new Object[]{entry.getValue().getId(),getLingQuCount(leichong.getLingquStatus(), entry.getValue().getId())});
//		}
//			
//		return new Object[]{subId,czList.toArray()};
//				
//	}
	
	public void rechargeYb(Long userRoleId,Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, OnceChongConfigGroup> groups = OnceChongConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		long nowTime = System.currentTimeMillis();
		//循环充值礼包配置数据
		for(Entry<Integer, OnceChongConfigGroup> entry : groups.entrySet()){
			int subId = entry.getKey();
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			Map<Integer,RoleOncechong> roleOncechongDatas = getOnceChong(userRoleId, subId);
			
//			//检测
//			updateJianCe(entry.getKey(), roleOncechongDatas);
			boolean flag = false;
			OnceChongConfigGroup configGroup = entry.getValue();
			Map<Integer, OnceChongConfig> onceChongConfigs =  configGroup.getConfigMap();
			for (OnceChongConfig onceChongConfig : onceChongConfigs.values()) {
				if(onceChongConfig.getBeginValue() <= addVal && onceChongConfig.getEndValue() >= addVal){
					RoleOncechong roleOncechong = roleOncechongDatas.get(onceChongConfig.getId());
					if(roleOncechong == null){
						roleOncechong = new RoleOncechong();
						roleOncechong.setChongCount(0);
						roleOncechong.setConfigId(onceChongConfig.getId());
						roleOncechong.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
						roleOncechong.setReceiveCount(0);
						roleOncechong.setSubId(subId);
						roleOncechong.setUpdateTime(nowTime);
						roleOncechong.setUserRoleId(userRoleId);
						oncechongDao.cacheInsert(roleOncechong, userRoleId);
					}
					roleOncechong.setChongCount(roleOncechong.getChongCount() + 1);
					roleOncechong.setUpdateTime(nowTime);
					
					oncechongDao.cacheUpdate(roleOncechong, userRoleId);
					
					flag = true;
				}
			}
			
			Object[] tuisong =  getLingQuStatus(userRoleId, subId);
			if(tuisong != null && flag){
				BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_ONCECHONG, tuisong);
			}

			super.checkIconFlag(userRoleId, subId);
		}
	}
}