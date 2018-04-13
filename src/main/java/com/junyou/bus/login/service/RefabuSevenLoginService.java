package com.junyou.bus.login.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.login.configure.RefabuLoginConfig;
import com.junyou.bus.login.configure.RefabuLoginConfigExportService;
import com.junyou.bus.login.configure.RefabuLoginConfigGroup;
import com.junyou.bus.login.dao.RefabuSevenLoginDao;
import com.junyou.bus.login.entity.RefabuSevenLogin;
import com.junyou.bus.login.filter.RefabuSevenLoginFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;


@Service
public class RefabuSevenLoginService{
	
	
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RefabuSevenLoginDao refabuSevenLoginDao;
	
	public List<RefabuSevenLogin> initRefabuSevenLogin(Long userRoleId){
		return refabuSevenLoginDao.initRefabuSevenLogin(userRoleId);
	}
	/**
	 *  合服七日奖励初始化
	 * @param userRoleId
	 * @return
	 */
	public void onlineHandle(Long userRoleId) {
		Map<Integer, RefabuLoginConfigGroup> groups = RefabuLoginConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, RefabuLoginConfigGroup> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			RefabuLoginConfigGroup config = RefabuLoginConfigExportService.getInstance().loadByMap(entry.getKey());
			if(config == null){
				continue;
			}
			getSevenLogin(userRoleId,entry.getKey());
			
		}
	}
	
	public Object[] getSevenLoginInfo(Long userRoleId,Integer version,int subId){
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
		RefabuSevenLogin login = getSevenLogin(userRoleId, subId);
		return new Object[]{subId,login.getLoginDays(),login.getRewardDays()};
	}
	
	/**
	 * 领取七日登陆天数奖励
	 * @param userRoleId
	 * @param day
	 * @return
	 */
	public Object[] getSevenLoginReward(Long userRoleId,Integer version,int subId,Integer id){
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
		RefabuLoginConfigGroup configGroup = RefabuLoginConfigExportService.getInstance().loadByMap(subId);
		
		if(configGroup == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RefabuLoginConfig config = configGroup.getConfigMap().get(id);
		if(config==null){
			return AppErrorCode.PARAMETER_ERROR;
		}
		int day  = config.getId();
		RefabuSevenLogin sevenLogin = getSevenLogin(userRoleId,subId);
		//登陆天数不足
		if(sevenLogin.getLoginDays() < day){
			return AppErrorCode.LOGIN_DAYS_LESS;
		}
		//当前天数是否领取过奖励
		if(!BitOperationUtil.calState(sevenLogin.getRewardDays(), day-1)){
			return AppErrorCode.LOGIN_DAY_REWARDED;
		}
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(config.getItem(), userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		
		sevenLogin.setRewardDays(BitOperationUtil.chanageState(sevenLogin.getRewardDays(), day-1));
		refabuSevenLoginDao.cacheUpdate(sevenLogin, userRoleId);
		roleBagExportService.putGoodsAndNumberAttr(config.getItem(), userRoleId, GoodsSource.RFB_SEVEN_DAY_REWARD,
				LogPrintHandle.GET_RFB_SEVEN_LOGIN_GIFT,LogPrintHandle.GBZ_RFB_SEVEN_LOGIN_GIFT, true);

		return new Object[]{1,subId,config.getId()};
	}
	
	
	
	public Object[] getRefbInfo(Long userRoleId, int subId) {
		RefabuLoginConfigGroup configGroup = RefabuLoginConfigExportService.getInstance().loadByMap(subId);
		
		if(configGroup == null){
			return null;
		}
		RefabuSevenLogin login = getSevenLogin(userRoleId, subId);
		Object[] obje  = new Object[]{
				configGroup.getPic(),
				configGroup.getClientItem().toArray(),
				login.getLoginDays(),
				login.getRewardDays()
		};
		
		return obje;
	}
	
	/**
	 * 获取七日登陆实体[上线处理]
	 * @param userRoleId
	 * @return
	 */
	private RefabuSevenLogin getSevenLogin(Long userRoleId,int subId){
		List<RefabuSevenLogin> list = refabuSevenLoginDao.cacheLoadAll(userRoleId, new RefabuSevenLoginFilter(subId));
		if(list == null || list.size() <= 0){
			RefabuSevenLogin login = new RefabuSevenLogin();
			login.setId(IdFactory.getInstance().generateId(ServerIdType.REFABU));
			login.setLoginDays(1);
			login.setRewardDays(0);
			login.setUserRoleId(userRoleId);
			login.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			login.setUpdateTime(GameSystemTime.getSystemMillTime());
			login.setSubId(subId);
			refabuSevenLoginDao.cacheInsert(login, userRoleId);
			
			return login;
		}else{
			RefabuSevenLogin login = list.get(0);
			//非同一天
			if(!DatetimeUtil.dayIsToday(login.getUpdateTime())){
				login = updateJianCe(subId, login);
				login.setLoginDays(login.getLoginDays()+1);
				login.setUpdateTime(GameSystemTime.getSystemMillTime());
				refabuSevenLoginDao.cacheUpdate(login, userRoleId);
			}
			return login;
		}
	}
	
	private RefabuSevenLogin updateJianCe(int subId,RefabuSevenLogin login){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = login.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			login.setLoginDays(0);
			login.setRewardDays(0);
			refabuSevenLoginDao.cacheUpdate(login, login.getUserRoleId());
		}
		return login;
	}
	
}
