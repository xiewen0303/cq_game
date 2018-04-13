package com.junyou.bus.onlinerewards.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.onlinerewards.configue.export.OnlineRewardsConfig;
import com.junyou.bus.onlinerewards.configue.export.OnlineRewardsConfigExportService;
import com.junyou.bus.onlinerewards.configue.export.OnlineRewardsConfigGroup;
import com.junyou.bus.onlinerewards.dao.RoleOnlineRewardsDao;
import com.junyou.bus.onlinerewards.entity.RoleOnlineRewards;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RfbOnlineRewardsLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 热发布在线奖励
 * @author lxn
 *
 */
@Service
public class OnlineRewardsService {

	
	@Autowired
	private RoleOnlineRewardsDao roleOnlineRewardsDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	/**
	 * 登陆后加载到缓存
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleOnlineRewards> initOnlineRewards(Long userRoleId) {
		return roleOnlineRewardsDao.initRoleOnlineRewards(userRoleId);
	}
	/**
	 * 玩家上线逻辑
	 */
	public void onlineHandle(Long userRoleId){
		// 判断配置
		Map<Integer, OnlineRewardsConfigGroup> allActivity = OnlineRewardsConfigExportService.getInstance().getAllConfig();
		if (allActivity == null || allActivity.size() == 0) {
			return;
		}
		for (Entry<Integer, OnlineRewardsConfigGroup> entry : allActivity.entrySet()) { // 可能配了多个活动
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if (configSong == null || !configSong.isRunActivity()) {
				continue;
			}
			int subId = entry.getKey();
			RoleOnlineRewards roleOnlineRewards = getRoleOnlineRewards(userRoleId, subId);
			roleOnlineRewards.setStartTime(GameSystemTime.getSystemMillTime());//更新计时
			roleOnlineRewards.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleOnlineRewardsDao.cacheUpdate(roleOnlineRewards, userRoleId);
		}
	}
	
	/**
	 * 玩家下线逻辑 
	 */
	public void offlineHandle(Long userRoleId){
		// 判断配置
		Map<Integer, OnlineRewardsConfigGroup> allActivity = OnlineRewardsConfigExportService.getInstance().getAllConfig();
		if (allActivity == null || allActivity.size() == 0) {
			return;
		}
		for (Entry<Integer, OnlineRewardsConfigGroup> entry : allActivity.entrySet()) { // 可能配了多个活动
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if (configSong == null || !configSong.isRunActivity()) {
				continue;
			}
			int subId = entry.getKey();
			RoleOnlineRewards roleOnlineRewards = getRoleOnlineRewards(userRoleId, subId);
			//计算在线时长
			long curTime = GameSystemTime.getSystemMillTime();
			Long addTime = curTime - roleOnlineRewards.getStartTime();
			roleOnlineRewards.setTodayOnlineTime(roleOnlineRewards.getTodayOnlineTime()+addTime);
			roleOnlineRewards.setUpdateTime(curTime);
			roleOnlineRewardsDao.cacheUpdate(roleOnlineRewards, userRoleId);
		}
	}
	/**
	 * 初始化某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId) {
		OnlineRewardsConfigGroup group = OnlineRewardsConfigExportService.getInstance().loadConfigBySubId(subId);
		if (group == null) {
			return null;//配置不存在
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return null;
		}
		Object[] data = new Object[5];
		data[0] = group.getPic();
		data[1] = group.getDes();
		data[2] = group.getClientData();
		Object[] doneData = getOnlineRewardsStates(userRoleId, subId);
		data[3] = doneData[1];
		data[4] = doneData[2];
		return data;
	}
	
	/**
	 * 获取状态数据,处理数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getOnlineRewardsStates(Long userRoleId, int subId) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return null;
		}
		Object[] data = new Object[3];
		// 处理数据
		RoleOnlineRewards entity = getRoleOnlineRewards(userRoleId, subId);
		Long addTime = GameSystemTime.getSystemMillTime() - entity.getStartTime();
		data[0] = subId;
		data[1] = entity.getTodayOnlineTime()+ addTime;
		data[2] = entity.getState();
		return data;
	}
	/**
	 * 获取奖励
	 * @param userRoleId
	 * @param subId
	 * @param version
	 * @return
	 */
	public Object[] getRewards(Long userRoleId, Integer subId, Integer version,int id) {
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (activity == null) {
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		// 版本不一样
		if (activity.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, activity.getClientVersion(),
					newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		OnlineRewardsConfig config = OnlineRewardsConfigExportService.getInstance().loadByKeyId(subId,id);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RoleOnlineRewards roleOnlineRewards = getRoleOnlineRewards(userRoleId,subId);
		Long addTime = GameSystemTime.getSystemMillTime() - roleOnlineRewards.getStartTime();
		long countTime = roleOnlineRewards.getTodayOnlineTime()+addTime ;
		if(countTime/60000<config.getTime()){
			//时间未到
			return AppErrorCode.RFB_ONLINE_REWARDS_TIME_NOT_ENOUGH;
		}
		
		if (!BitOperationUtil.calState(roleOnlineRewards.getState(), id-1)) {
			
			return AppErrorCode.GET_ALREADY;
		}
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(config.getRewards(), userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		roleOnlineRewards.setTodayOnlineTime(countTime);
		roleOnlineRewards.setStartTime(GameSystemTime.getSystemMillTime());
		roleOnlineRewards.setState(BitOperationUtil.chanageState(roleOnlineRewards.getState(), id-1));
		roleOnlineRewards.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleOnlineRewardsDao.cacheUpdate(roleOnlineRewards, userRoleId);
		
		// 背包新增 道具
		roleBagExportService.putInBag(config.getRewards(), userRoleId, GoodsSource.RFB_ONLINE_REWARDS, true);
		//日志
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(config.getRewards(), null);
		GamePublishEvent.publishEvent(new RfbOnlineRewardsLogEvent(userRoleId,jsonArray,id));
		
		return new Object[]{1,subId,id};
	}
	/**
	 * 只有活动开启了这里才会被调用
	 * 获取在线奖励对象数据
	 */
	private RoleOnlineRewards getRoleOnlineRewards(Long userRoleId, final int subId){
		RoleOnlineRewards roleOnlineRewards = getRoleOnlineRewardsEntity(userRoleId,subId);
		ActivityConfigSon configSong = null;
		if(roleOnlineRewards==null){
			RoleWrapper roleWrapper =  getRoleWrapper(userRoleId);
			long startTime = 0;
			  configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
			if(configSong!=null){
				if(roleWrapper.getOnlineTime()<configSong.getStartTime()){
					//玩家一直在线活动后上
					startTime  = configSong.getStartTime();
				}else{
					startTime  = roleWrapper.getOnlineTime();	
				}	
			}
			roleOnlineRewards = createOnlineRewards(userRoleId, subId,startTime);
			roleOnlineRewardsDao.cacheInsert(roleOnlineRewards, userRoleId);
			 
		}else{
			if(!DatetimeUtil.dayIsToday(roleOnlineRewards.getUpdateTime())){
				//跨天
				long startTime  = 0L;
				RoleWrapper roleWrapper =  getRoleWrapper(userRoleId);
				long date00Time = DatetimeUtil.getDate00Time();//当日凌晨的时间
				if(roleWrapper.getOnlineTime()<date00Time){
					startTime  = date00Time; //在线跨天
				}else{
					startTime  = roleWrapper.getOnlineTime();
				}
				roleOnlineRewards.setTodayOnlineTime(0L);
				roleOnlineRewards.setStartTime(startTime);
				roleOnlineRewards.setState(0);
				roleOnlineRewards.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleOnlineRewardsDao.cacheUpdate(roleOnlineRewards, userRoleId);
			}
		}
 		return roleOnlineRewards;
	}
	private RoleWrapper getRoleWrapper(long e){
		RoleWrapper role =null;
		if(publicRoleStateService.isPublicOnline(e)){
			 role = roleExportService.getLoginRole(e);
		}else{
			 role = roleExportService.getUserRoleFromDb(e);
		}
		return role;
	}
	
	private RoleOnlineRewards getRoleOnlineRewardsEntity(Long userRoleId, final int subId){
		List<RoleOnlineRewards> list = roleOnlineRewardsDao.cacheLoadAll(userRoleId, new IQueryFilter<RoleOnlineRewards>(){
			private boolean stop = false;
			@Override
			public boolean check(RoleOnlineRewards info) {
				 
				if (subId==info.getSubId().intValue()) {
					stop = true;
				}
				return stop;
			}

			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	private RoleOnlineRewards createOnlineRewards(Long userRoleId,int subId,long startTime){
		RoleOnlineRewards entity = new RoleOnlineRewards();
		entity.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		entity.setUserRoleId(userRoleId);
		entity.setState(0);
		entity.setStartTime(startTime);//玩家在线时长从这个点开始计时
		entity.setSubId(subId);
		entity.setTodayOnlineTime(0L);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		entity.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		return entity;
	}

}
