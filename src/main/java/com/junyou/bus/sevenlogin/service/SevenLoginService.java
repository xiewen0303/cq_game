package com.junyou.bus.sevenlogin.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.sevenlogin.configure.SevenLoginConfig;
import com.junyou.bus.sevenlogin.configure.SevenLoginConfigExportService;
import com.junyou.bus.sevenlogin.dao.SevenLoginDao;
import com.junyou.bus.sevenlogin.entity.SevenLogin;
import com.junyou.err.AppErrorCode;
import com.junyou.event.SevenLoginRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;


@Service
public class SevenLoginService{
	
	@Autowired
	private SevenLoginDao sevenLoginDao;
	
	@Autowired
	private SevenLoginConfigExportService sevenLoginConfigExportService;
		
	@Autowired
	private RoleBagExportService roleBagExportService;
		
	@Autowired
	private RoleExportService roleExportService;
		
	/**
	 * 七日奖励初始化
	 * @param userRoleId
	 * @return
	 */
	public Object[] getSevenLoginInfo(Long userRoleId){
		SevenLogin sevenLogin = getSevenLogin(userRoleId);
		return new Object[]{sevenLogin.getLoginDays(),sevenLogin.getRewardDays()};
	}
	
	/**
	 * 领取七日登陆天数奖励
	 * @param userRoleId
	 * @param day
	 * @return
	 */
	public Object[] getSevenLoginReward(Long userRoleId,Integer day){
		if(day < 1 || day > 7){
			return AppErrorCode.PARAMETER_ERROR;
		}
		
		SevenLoginConfig config = sevenLoginConfigExportService.loadById(day);
		if(config == null){
			//配置异常
			return AppErrorCode.CONFIG_ERROR;
		}
		
		SevenLogin sevenLogin = getSevenLogin(userRoleId);
		//登陆天数不足
		if(sevenLogin.getLoginDays() < day){
			return AppErrorCode.LOGIN_DAYS_LESS;
		}
		
		//当前天数是否领取过奖励
		if(!BitOperationUtil.calState(sevenLogin.getRewardDays(), day-1)){
			return AppErrorCode.LOGIN_DAY_REWARDED;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		Map<String,Integer> awards = config.getJobAwards(role.getConfigId().byteValue());
		
		if(roleBagExportService.checkPutGoodsAndNumberAttr(awards, userRoleId) != null){
			return AppErrorCode.BAG_NOEMPTY;
		}
		
		sevenLogin.setRewardDays(BitOperationUtil.chanageState(sevenLogin.getRewardDays(), day-1));
		sevenLoginDao.cacheUpdate(sevenLogin, userRoleId);
		roleBagExportService.putGoodsAndNumberAttr(awards, userRoleId, GoodsSource.SEVEN_DAY_REWARD,LogPrintHandle.GET_SEVEN_LOGIN_GIFT,LogPrintHandle.GBZ_SEVEN_LOGIN_GIFT, true);
		
		//打印日志
		JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getAwards(), null);
		GamePublishEvent.publishEvent(new SevenLoginRewardLogEvent(userRoleId,role.getName(),goods,day));
		return new Object[]{1,day};
	}
	
	/**
	 * 获取七日登陆实体[上线处理]
	 * @param userRoleId
	 * @return
	 */
	private SevenLogin getSevenLogin(Long userRoleId){
		SevenLogin sevenLogin = sevenLoginDao.cacheAsynLoad(userRoleId, userRoleId);
		if(sevenLogin != null){
			if(sevenLogin.getLoginDays() >= 7) {
				return sevenLogin;
			}
			//非同一天
			if(!DatetimeUtil.dayIsToday(sevenLogin.getUpdateTime())){
				sevenLogin.setLoginDays(sevenLogin.getLoginDays()+1);
				sevenLogin.setUpdateTime(GameSystemTime.getSystemMillTime());
				sevenLoginDao.cacheUpdate(sevenLogin, userRoleId);
			}
		}else{
			sevenLogin = new SevenLogin();
			sevenLogin.setLoginDays(1);
			sevenLogin.setRewardDays(0);
			sevenLogin.setUserRoleId(userRoleId);
			sevenLogin.setUpdateTime(GameSystemTime.getSystemMillTime());
			sevenLoginDao.cacheInsert(sevenLogin, userRoleId);
		}
		return sevenLogin;
	}
	
	
	/**
	 * 获取七登是否有奖励
	 * @param userRoleId
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的3次方
	 */
	public int getSevenLoginStateValue(Long userRoleId){
		//int(0)二进制   奖励 有为1，无为0  顺序为  签到(0)，在线(1)，离线(2)，七登(3)
		int state = 0;
		
		SevenLogin sevenLogin = getSevenLogin(userRoleId);
		for (int i = 0; i < 7; i++) {
			//逐天判断是否有奖励没领取
			if(sevenLogin.getLoginDays() >= (i + 1)){
				//判断是否有奖励 
				boolean hasJianLi = BitOperationUtil.calState(sevenLogin.getRewardDays(),i);
				if(hasJianLi){
					return 8;//返回2的3次方
				}
			}
		}
		
		return state;
	}
	
}
