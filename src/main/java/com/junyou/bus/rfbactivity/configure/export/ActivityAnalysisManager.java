package com.junyou.bus.rfbactivity.configure.export;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.element.goods.Collect;
import com.junyou.utils.codec.AmfUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.kernel.spring.SpringApplicationContext;


/**
 * 主活动解析管理(单实例)
 * @author DaoZheng Yuan
 * 2015年5月19日 上午11:31:41
 */
public class ActivityAnalysisManager {

    private static RefabuActivityExportService refabuActivityExportService = SpringApplicationContext.getApplicationContext().getBean(RefabuActivityExportService.class);
    
	private static final ActivityAnalysisManager INSTANCE = new ActivityAnalysisManager();
	
	
	private Map<Integer, ActivityConfig> refabuConfig = new HashMap<>();
	private Map<Integer, ActivityConfigSon> zihuodongConfig = new HashMap<>();
	
	private ActivityAnalysisManager(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static ActivityAnalysisManager getInstance() {
		return INSTANCE;
	}
	
	public ActivityConfig loadById(Integer id){
		if(refabuConfig == null){
			return null;
		}
		ActivityConfig config = refabuConfig.get(id);
		if(config == null || config.isDel()){
			return null;
		}
		
		return config;
	}

	
	/**
	 * 获取子活动（删除的子活动除外）
	 * @param id
	 * @return
	 */
	public ActivityConfigSon loadByZiId(Integer id){
		if(zihuodongConfig == null){
			return null;
		}
		
		ActivityConfigSon config = zihuodongConfig.get(id);
		//活动未null，并活动删除，活动是否在活动期间内
		if(config == null || config.isDel()){
			return null;
		}
		
		return config;
	}
	
	/**
	 * 获取子活动（删除和不在活动期间内的除外）正在进行的活动
	 * @param id
	 * @return
	 */
	public ActivityConfigSon loadRunByZiId(Integer id){
		if(zihuodongConfig == null){
			return null;
		}
		
		ActivityConfigSon config = zihuodongConfig.get(id);
		if(config == null || config.isDel() || !config.isRunActivity()){
			return null;
		}
		
		return config;
	}
	
	public Map<Integer, ActivityConfig> loadAll(){
		return refabuConfig;
	}
	
	
	/**
	 * 判断新老配置时间是否有改变
	 * @param newConfig
	 * @param oldConfig
	 * @return
	 */
	private boolean isTimeChanage(ActivityConfig newConfig, ActivityConfig oldConfig){
		if(newConfig == null || oldConfig == null){
			return false;
		}
		
		//时间类型不一致
		if(newConfig.getTimeType() != oldConfig.getTimeType()){
			return true;
		}
		
		boolean isChanage = false;
		switch (newConfig.getTimeType()) {
		
		case ActivityTimeType.TIME_0_SJ:
			if(!newConfig.getStartTime().equals(oldConfig.getStartTime()) || !newConfig.getEndTime().equals(oldConfig.getEndTime())){
				isChanage = true;
			}
			
			break;
			
		default:
			if(newConfig.getStartDay() != oldConfig.getStartDay() || newConfig.getEndDay() != oldConfig.getEndDay()){
				isChanage = true;
			}
			break;
		}
		
		
		return isChanage;
	}
	
	/**
	 * 主活动中某个子活动的配置信息改动
	 * @param data
	 * @param activityId
	 * @param subId
	 */
	public void chanageConfigureDataSon(byte[] data, Integer activityId, Integer subId){
		if(data == null){
			ChuanQiLog.error(" chanageConfigureDataSon 1 data is error! ");
			return;
		}
		
		JSONObject json = JsonUtils.getJsonObjectByBytes(data);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" chanageConfigureDataSon 2 data is error! ");
			return;
		}
		Map<String,Object> tmpMap = (Map<String, Object>)json;
		
		String md5Value = Md5Utils.md5Bytes(data);
		
		ActivityConfig newConfig = null;
		ActivityConfig oldConfig = refabuConfig.get(activityId);
		if(oldConfig != null){
			
			//客户端版本号业务处理
			if(md5Value.equals(oldConfig.getMd5Sign())){
				//版本号没变说明数据，不用再次解析
				return;
			}
			
			newConfig = createReFaBuShangChengBiaoConfig(tmpMap);
			if(newConfig == null){
				return;
			}
			boolean isXj = activitySonHandle(newConfig, subId, activityId, oldConfig);//子活动业务处理
			
			//判断是否下架
			if(isXj){
				ActivityConfigSon oldConfigSon = oldConfig.getZihuodongMap().get(subId);
				if(oldConfigSon != null){
					
					//子活动配置标识删除
					oldConfigSon.setDel(true);
					zihuodongConfig.put(subId, oldConfigSon);
				}
			}
		}
	}
	
	/**
	 * 子活动修改业务处理
	 * @param newConfig
	 * @param subId
	 * @param clientVersion
	 * @return true:下架
	 */
	private boolean activitySonHandle(ActivityConfig newConfig, Integer subId, Integer activityId, ActivityConfig oldConfig){
		//最后结果处理
		if(newConfig.isRunActivity()){
			Map<Integer, ActivityConfigSon> map = newConfig.getZihuodongMap();
			if(map == null || map.size() <= 0){
				return true;
			}
			
			ActivityConfigSon configSon = map.get(subId);
			if(configSon == null){
				return true;
			}
			
			//变更子活动的客户端版本号
			configSon.setClientVersion(oldConfig.getClientVersion() + 1);
			//子活动修改
			zihuodongConfig.put(subId, configSon);
			//主活动修改
			oldConfig.getZihuodongMap().put(subId, configSon);
			oldConfig.setClientVersion(oldConfig.getClientVersion() + 1);
			refabuConfig.put(activityId, oldConfig);
			return false;
		}
		return true;
	}
	
	/**
	 * 主活动变化重新解析
	 * @param data
	 * @param zhuId
	 * @return
	 */
	public void changeConfigureDataResolve(byte[] data,int activityId,Map<Integer,Integer> subTypeMap){
		if(data == null){
			ChuanQiLog.error(" changeConfigureDataResolve 1 data is error! ");
			return;
		}
		JSONObject json = JsonUtils.getJsonObjectByBytes(data);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" changeConfigureDataResolve 2 data is error! ");
			return;
		}
		Map<String,Object> tmpMap = (Map<String, Object>)json;
		
		String md5Value = Md5Utils.md5Bytes(data);
		int clientVersion = 1;
		
		ActivityConfig newConfig = null;
		ActivityConfig oldConfig = refabuConfig.get(activityId);
		if(oldConfig != null){
			//客户端版本号业务处理
			clientVersion = oldConfig.getClientVersion()+1;
			if(md5Value.equals(oldConfig.getMd5Sign())){
				//版本号没变说明数据，不用再次解析
				return;
			}
			
			newConfig = createReFaBuShangChengBiaoConfig(tmpMap);
			if(newConfig == null){
				return;
			}
			//最后结果处理
			if(newConfig.isRunActivity()){
				if(isTimeChanage(newConfig, oldConfig)){
					Object[] time = newConfig.setActivityTime();
					BusMsgSender.send2All(ClientCmdType.RFB_ACTIVITY_TIME_UPDATE, new Object[]{activityId, time[0], time[1]});
				}
				if(!oldConfig.getName().equals(newConfig.getName())){
					Object[] object = new Object[]{activityId, newConfig.getName()};
					BusMsgSender.send2All(ClientCmdType.RFB_ACTIVITY_NAME_UPDATE, object);
				}
				if(!oldConfig.getIcon().equals(newConfig.getIcon())){
					Object[] object = new Object[]{activityId, newConfig.getIcon()};
					BusMsgSender.send2All(ClientCmdType.RFB_ACTIVITY_ICON_UPDATE, object);
				}
			}else {
				//如果不在当前时间内就通知下架
				BusMsgSender.send2All(ClientCmdType.RFB_ACTIVITY_DELETE, activityId);
			}

			//修改版本号
			newConfig.setMd5Sign(md5Value);
			newConfig.setClientVersion(clientVersion);
			//加入全局管理
			refabuConfig.put(newConfig.getId(), newConfig);
			Map<Integer, ActivityConfigSon> subMap = newConfig.getZihuodongMap();
			if(subMap.size() > 0){
				//子活动处理
				for (Map.Entry<Integer, ActivityConfigSon> entry : subMap.entrySet()) {
					ActivityConfigSon configSon = entry.getValue();
					ActivityConfigSon oldConfigSon = zihuodongConfig.get(entry.getKey());
					if(oldConfigSon != null){
						//变更子活动的客户端版本号
						configSon.setClientVersion(oldConfigSon.getClientVersion() + 1);
					}
					//新增子活动
					zihuodongConfig.put(entry.getKey(), configSon);
				}
			}
		}else{
			
			int hefuDays = ServerInfoServiceManager.getInstance().getHefuDays();
			int timeType = CovertObjectUtil.object2int(tmpMap.get("activityType"));
			/*
			 * 合服循环和开服循环互斥，有开服循环就不可以有合服循环，有合服就不可有开服循环
			 */
//			if(timeType == ActivityTimeType.TIME_4_KAI_FU_LOOP && hefuDays > 0){
//				//合过服的服务器，不加载开服循环
//				return;
//			}else if(timeType == ActivityTimeType.TIME_5_HE_FU_LOOP && hefuDays == 0){
//				//没合过服的服务器,不加载合服循环
//				return;
//			}
			
			//新增主活动
			newConfig = createReFaBuShangChengBiaoConfig(tmpMap);
			if(newConfig == null){
				return;
			}
			
			//为每个主活动添加自己的版本号
			newConfig.setMd5Sign(md5Value);
			newConfig.setClientVersion(clientVersion);
			
			//加入全局管理
			refabuConfig.put(newConfig.getId(), newConfig);
			if(newConfig.getZihuodongMap().size() > 0){
				zihuodongConfig.putAll(newConfig.getZihuodongMap());
			}
			//最后结果处理
			if(newConfig.isRunActivity()){
			    Collection<GameSession> ioSessions = GameSessionManager.getInstance().getRoleListSession();
		        byte[] bytes = AmfUtil.convertMsg2Bytes(ClientCmdType.RFB_ACTIVITY_NAME_ADD,newConfig.getMainHdData());
		        for(GameSession session : ioSessions){
		            if(null != session){
		                Long userRoleId = session.getRoleId();
		                if(refabuActivityExportService.isShowActivity(userRoleId, newConfig)){
		                    //通知新增主活动
		                    session.sendMsg(bytes);
		                }
		            }
		        }
			}
		}
		
		
		Map<Integer, ActivityConfigSon> configSons = newConfig.getZihuodongMap();
		//处理回调的subMap
		if(configSons != null && configSons.size() > 0){
			for (Map.Entry<Integer, ActivityConfigSon> entry : configSons.entrySet()) {
				subTypeMap.put(entry.getKey(), entry.getValue().getSubActivityType());
			}
		}
	}
	
	/**
	 * 解析主活动配置数据
	 * @param data
	 * @param subMap(调用者可以取到的handle)  key:子活动id,value:业务活动type
	 */
	public void analysisConfigureDataResolve(byte[] data,Map<Integer,Integer> subMap){
		if(data == null){
			ChuanQiLog.error(" analysisConfigureDataResolve 1 data is error! ");
			return;
		}
		JSONObject json = JsonUtils.getJsonObjectByBytes(data);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" analysisConfigureDataResolve 2 data is error! ");
			return;
		}


		Map<String,Object> tmpMap = (Map<String, Object>)json;
		int curId = CovertObjectUtil.object2int(tmpMap.get("id"));
		int clientVersion = 1;
		String md5Value = Md5Utils.md5Bytes(data);
		
		ActivityConfig oldConfig = refabuConfig.get(curId);
		//客户端版本号业务处理
		if(oldConfig != null){
			clientVersion = oldConfig.getClientVersion()+1;
			if(md5Value.equals(oldConfig.getMd5Sign())){
				//版本号没变说明数据，不用再次解析
				return;
			}
		}
		
		int hefuDays = ServerInfoServiceManager.getInstance().getHefuDays();
		int timeType = CovertObjectUtil.object2int(tmpMap.get("activityType"));
		/*
		 * 合服循环和开服循环互斥，有开服循环就不可以有合服循环，有合服就不可有开服循环
		 */
//		if(timeType == ActivityTimeType.TIME_4_KAI_FU_LOOP && hefuDays > 0){
//			//合过服的服务器，不加载开服循环
//			return;
//		}else if(timeType == ActivityTimeType.TIME_5_HE_FU_LOOP && hefuDays == 0){
//			//没合过服的服务器,不加载合服循环
//			return;
//		}
		
		ActivityConfig config = createReFaBuShangChengBiaoConfig(tmpMap);
		if(config == null){
			return;
		}
		//为每个主活动添加自己的版本号
		config.setMd5Sign(md5Value);
		config.setClientVersion(clientVersion);
		
		Map<Integer, ActivityConfigSon>  configSons = config.getZihuodongMap();
		refabuConfig.put(config.getId(), config);
		zihuodongConfig.putAll(configSons);
		
		
		//处理回调的subMap
		for (Map.Entry<Integer, ActivityConfigSon> entry : configSons.entrySet()) {
			subMap.put(entry.getKey(), entry.getValue().getSubActivityType());
		}
	}
	
	
	
	private ActivityConfig createReFaBuShangChengBiaoConfig(Map<String, Object> tmp){
		ActivityConfig config = new ActivityConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setStartTime(DatetimeUtil.parseDateMillTime(CovertObjectUtil.object2String(tmp.get("startTime"))));
		config.setEndTime(DatetimeUtil.parseDateMillTime(CovertObjectUtil.object2String(tmp.get("endTime"))));
		config.setStartDay(CovertObjectUtil.object2int(tmp.get("startDay")));
		config.setEndDay(CovertObjectUtil.object2int(tmp.get("endDay")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setIcon(CovertObjectUtil.object2String(tmp.get("icon")));
		config.setModel(CovertObjectUtil.object2int(tmp.get("model")));
		config.setDisplay(CovertObjectUtil.object2int(tmp.get("display")));
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
		config.setTimeType(CovertObjectUtil.object2int(tmp.get("activityType")));
		config.setStartHour(CovertObjectUtil.object2String(tmp.get("startHour")));
		config.setEndHour(CovertObjectUtil.object2String(tmp.get("endHour")));
		config.setDelay(CovertObjectUtil.object2int(tmp.get("delay")));
		config.setFolder(CovertObjectUtil.object2int(tmp.get("folder")));
		String skey = CovertObjectUtil.object2String(tmp.get("skey"));
		if(!CovertObjectUtil.isEmpty(skey)){
			config.setSkey(skey);
		}
		
		JSONArray jsonArray = JSONObject.parseArray(CovertObjectUtil.object2String(tmp.get("subActivity")));
		Map<Integer, ActivityConfigSon> zihuodongMap = new HashMap<>();
		if(jsonArray != null && jsonArray.size() > 0){
			for(Object boject: jsonArray){
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>)JSONObject.parse(boject.toString());
				boolean isDel = CovertObjectUtil.object2boolean(map.get("isDel"));
				if(isDel){
					//子活动删除，不读取配置
					continue;
				}
				
				ActivityConfigSon activityConfigSon = new ActivityConfigSon();
				activityConfigSon.setId(CovertObjectUtil.object2int(map.get("id")));
				activityConfigSon.setStartTime(DatetimeUtil.parseDateMillTime(CovertObjectUtil.object2String(map.get("startTime"))));
				activityConfigSon.setEndTime(DatetimeUtil.parseDateMillTime(CovertObjectUtil.object2String(map.get("endTime"))));
				activityConfigSon.setStartDay(CovertObjectUtil.object2int(map.get("startDay")));
				activityConfigSon.setEndDay(CovertObjectUtil.object2int(map.get("endDay")));
				activityConfigSon.setTimeType(CovertObjectUtil.object2int(tmp.get("activityType")));
				activityConfigSon.setSubName(CovertObjectUtil.object2String(map.get("subName")));
				activityConfigSon.setStartHour(CovertObjectUtil.object2String(map.get("startHour")));
				activityConfigSon.setEndHour(CovertObjectUtil.object2String(map.get("endHour")));
				activityConfigSon.setSubActivityType(CovertObjectUtil.object2int(map.get("subActivityType")));
				activityConfigSon.setOrder(CovertObjectUtil.object2int(map.get("order")));
				activityConfigSon.setSkin(CovertObjectUtil.object2int(map.get("skin")));
				activityConfigSon.setCycleDays(CovertObjectUtil.object2int(map.get("cycleDays")));
				activityConfigSon.setXhEndDay(CovertObjectUtil.object2int(map.get("xhEndDays")));
				activityConfigSon.setAscription(CovertObjectUtil.object2Integer(map.get("ascription")));
				String sonSkey = CovertObjectUtil.object2String(map.get("skey"));
				if(!CovertObjectUtil.isEmpty(sonSkey)){
					activityConfigSon.setSkey(sonSkey);
				}
				activityConfigSon.setDel(isDel);
				activityConfigSon.setActivityId(config.getId());
				if(ReFaBuUtil.isRfbType(activityConfigSon.getSubActivityType())){
					zihuodongMap.put(activityConfigSon.getId(), activityConfigSon);
				}
			}
		}
		/*if(zihuodongMap == null || zihuodongMap.size() <= 0){
			return null;
		}*/
		config.setZihuodongMap(zihuodongMap);
											
		return config;
	}

	//删除主活动
	public void deleteActivity(Integer activityId){
		if(refabuConfig.containsKey(activityId)){
			refabuConfig.remove(activityId);
		}
	}

	public Collection<ActivityConfigSon> loadAllSubActivitys() {
		return zihuodongConfig.values();
	}
}
