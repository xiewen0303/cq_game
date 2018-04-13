package com.junyou.bus.hefuSevenlogin.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.hefuSevenlogin.configure.HefuSevenLoginConfig;
import com.junyou.bus.hefuSevenlogin.configure.HefuSevenLoginConfigExportService;
import com.junyou.bus.hefuSevenlogin.dao.HefuSevenLoginDao;
import com.junyou.bus.hefuSevenlogin.entity.HefuSevenLogin;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.HefuSevenLoginRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;


@Service
public class HefuSevenLoginService{
	
	@Autowired
	private HefuSevenLoginDao hefuSevenLoginDao;
	
	@Autowired
	private HefuSevenLoginConfigExportService hefuSevenLoginConfigExportService; 
		
	@Autowired
	private RoleBagExportService roleBagExportService;
		
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private AccountExportService accountExportService;
	
	/**
	 *  合服七日奖励初始化
	 * @param userRoleId
	 * @return
	 */
	public void onlineHandle(Long userRoleId) {
		if(getHefuState()){
			HefuSevenLogin hefuSevenLogin = this.getSevenLogin(userRoleId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.HEFU_SEVEN_REWARD_INIT, new Object[]{hefuSevenLogin.getLoginDays(),hefuSevenLogin.getRewardDays()});
		} 
	}
	
	/**
	 * 合服七日奖励初始化
	 * @param userRoleId
	 * @return
	 */
	public Object[] getSevenLoginInfo(Long userRoleId){
		if(getHefuState()){
			HefuSevenLogin hefuSevenLogin = getSevenLogin(userRoleId);
			return new Object[]{hefuSevenLogin.getLoginDays(),hefuSevenLogin.getRewardDays()};
		}else{
			return  AppErrorCode.HEFU_SEVEN_LOGIN_EXPIRE;
		}
	}
	
	/**
	 * 领取七日登陆天数奖励
	 * @param userRoleId
	 * @param day
	 * @return
	 */
	public Object[] getSevenLoginReward(Long userRoleId,int id){
		HefuSevenLoginConfig config = hefuSevenLoginConfigExportService.loadById(id);
		if(config==null){
			return AppErrorCode.PARAMETER_ERROR;
		}
		if(!getHefuState()){
			return AppErrorCode.HEFU_SEVEN_LOGIN_EXPIRE; 
		}
		int day  = config.getId();
		HefuSevenLogin sevenLogin = getSevenLogin(userRoleId);
		//登陆天数不足
		if(sevenLogin.getLoginDays() < day){
			return AppErrorCode.LOGIN_DAYS_LESS;
		}
		//当前天数是否领取过奖励
		if(!BitOperationUtil.calState(sevenLogin.getRewardDays(), day-1)){
			return AppErrorCode.LOGIN_DAY_REWARDED;
		}
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(config.getItems(), userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		//经验
		 if(config.getExp()>0){
			 roleExportService.incrExp(userRoleId,  config.getExp());
		 }
		 //银两
		 if(config.getMoney()>0){
			 accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_HEFU_SEVEN_LOGIN_GIFT, LogPrintHandle.GBZ_HEFU_SEVEN_LOGIN_GIFT);
		 }
		sevenLogin.setRewardDays(BitOperationUtil.chanageState(sevenLogin.getRewardDays(), day-1));
		hefuSevenLoginDao.cacheUpdate(sevenLogin, userRoleId);
		roleBagExportService.putGoodsAndNumberAttr(config.getItems(), userRoleId, GoodsSource.HEFU_SEVEN_DAY_REWARD,
				LogPrintHandle.GET_HEFU_SEVEN_LOGIN_GIFT,LogPrintHandle.GBZ_HEFU_SEVEN_LOGIN_GIFT, true);
		//打印日志
		JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getItems(), null);
		GamePublishEvent.publishEvent(new HefuSevenLoginRewardLogEvent(userRoleId,role.getName(),goods,day));
		return new Object[]{1,config.getId()};
	}
	
	/**
	 *1.是否合服 and 2.合服时间是否过了七天
	 */
	private boolean  getHefuState(){
		if(ServerInfoServiceManager.getInstance().getServerHefuTimes()>0){
			   long hefuTime = ServerInfoServiceManager.getInstance().getServerHefuTime().getTime();
				int day =  DatetimeUtil.twoDaysDiffence(hefuTime);
				if(day<8){ //八天后一律取消活动
					return true;
				}
		}	
		return false;
	}
	
	/**
	 * 获取七日登陆实体[上线处理]
	 * @param userRoleId
	 * @return
	 */
	private HefuSevenLogin getSevenLogin(Long userRoleId){
		HefuSevenLogin hefuSevenLogin = hefuSevenLoginDao.cacheAsynLoad(userRoleId, userRoleId);
		if(hefuSevenLogin != null){
			if(hefuSevenLogin.getLoginDays() >= 7) {
				return hefuSevenLogin;
			}
			//非同一天
			if(!DatetimeUtil.dayIsToday(hefuSevenLogin.getUpdateTime())){
				hefuSevenLogin.setLoginDays(hefuSevenLogin.getLoginDays()+1);
				hefuSevenLogin.setUpdateTime(GameSystemTime.getSystemMillTime());
				hefuSevenLoginDao.cacheUpdate(hefuSevenLogin, userRoleId);
			}
		}else{
			hefuSevenLogin = new HefuSevenLogin();
			hefuSevenLogin.setLoginDays(1);
			hefuSevenLogin.setRewardDays(0);
			hefuSevenLogin.setUserRoleId(userRoleId);
			hefuSevenLogin.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			hefuSevenLogin.setUpdateTime(GameSystemTime.getSystemMillTime());
			hefuSevenLoginDao.cacheInsert(hefuSevenLogin, userRoleId);
		}
		return hefuSevenLogin;
	}
	
	
	/**
	 * 获取七登是否有奖励
	 * @param userRoleId
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的6次方
	 */
	public int getSevenLoginStateValue(Long userRoleId){
		int state = 0;
		if(ServerInfoServiceManager.getInstance().getServerHefuTimes()<=0){
			return state;
		}
		HefuSevenLogin sevenLogin = getSevenLogin(userRoleId);
		for (int i = 0; i < 7; i++) {
			//逐天判断是否有奖励没领取
			if(sevenLogin.getLoginDays() >= (i + 1)){
				//判断是否有奖励 
				boolean hasJianLi = BitOperationUtil.calState(sevenLogin.getRewardDays(),i);
				if(hasJianLi){
					return 64;//返回2的6次方
				}
			}
		}
		
		return state;
	}
	
}
