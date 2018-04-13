package com.junyou.bus.assign.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.analysis.GameAppConfig;
import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.bus.assign.configure.QianDaoJiangLiConfig;
import com.junyou.bus.assign.configure.QianDaoJiangLiConfigExportService;
import com.junyou.bus.assign.dao.AssignDao;
import com.junyou.bus.assign.entity.Assign;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.offlineexp.export.OfflineExpExportService;
import com.junyou.bus.online.export.RoleOnlineExportService;
import com.junyou.bus.resource.export.RoleResourceBackExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.sevenlogin.export.SevenLoginExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.AssignLogEvent;
import com.junyou.event.AssignRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;


@Service
public class AssignService{
	
	@Autowired
	private AssignDao assignDao;
	@Autowired
	private QianDaoJiangLiConfigExportService assignConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleOnlineExportService roleOnlineExportService;
	@Autowired
	private OfflineExpExportService offlineExpExportService;
	@Autowired
	private SevenLoginExportService sevenLoginExportService;
	@Autowired
	private RoleResourceBackExportService roleResourceBackExportService;
	/**
	 * 签到初始化
	 * @param userRoleId
	 * @return
	 */
	public Object[] getAssignInfo(Long userRoleId){
		Assign assign = getAssign(userRoleId);
		return new Object[]{assign.getAssignDays(),assign.getAssignTotal(),assign.getAssignCount(),assign.getRetroactiveCount()};
	}
	
	public Assign getAssign(Long userRoleId){
		Assign assign = assignDao.cacheAsynLoad(userRoleId, userRoleId);
		if(assign == null){
			assign = new Assign();
			assign.setUserRoleId(userRoleId);
			assign.setAssignAll(0);
			assign.setCreateTime(GameSystemTime.getSystemMillTime());
			assignChangeState(assign);
			assignDao.cacheInsert(assign, userRoleId);
		}else if(assign.getUpdateTime() != null && !DatetimeUtil.isSameMonth(assign.getUpdateTime())){//下个月清空数据
			assign.setAssignAll(assign.getAssignCount()+assign.getAssignAll());
			assignChangeState(assign);
			assign.setUpdateTime(GameSystemTime.getSystemMillTime());
			assignDao.cacheUpdate(assign, userRoleId);
		}
		return assign;
	}
	
	/**
	 * 签到实体公用类型变化
	 * @param assign
	 */
	private void assignChangeState(Assign assign){
		assign.setAssignDays(0);
		assign.setAssignTotal(0);
		assign.setRetroactiveCount(0);
		assign.setAssignCount(0);
	}
	
	/**
	 * 当前日期签到
	 * @param userRoleId
	 * @param busMsgQueue
	 */
	public Object[] assign(Long userRoleId){
		//获取当前月份天数
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		Assign assign = getAssign(userRoleId);		
		//当前天数能否签名
		if(!BitOperationUtil.calState(assign.getAssignDays(), day-1)){
			return AppErrorCode.ASSIGN_EXISTS;
		}
		
		Integer changeValue = BitOperationUtil.chanageState(assign.getAssignDays(), day-1);
		assign.setAssignDays(changeValue);
		assign.setUpdateTime(GameSystemTime.getSystemMillTime());
		assign.setAssignCount(assign.getAssignCount()+1);
		assignDao.cacheUpdate(assign, userRoleId);
		//成就
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_QIANDAOCOUNT, 0});
			//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_QIANDAOCOUNT, 0);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		//打印日志
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		GamePublishEvent.publishEvent(new AssignLogEvent(userRoleId,role.getName(),LogPrintHandle.ASSIGN_IN,assign.getRetroactiveCount(), assign.getAssignCount(), assign.getAssignDays()));
		//推送给客户端进入成功
		return new Object[]{AppErrorCode.SUCCESS};
	}
	
	/**
	 * 领取累计奖励
	 * @param userRoleId
	 * @param rewardType
	 * @param busMsgQueue
	 */
	public Object[] assignTotal(Long userRoleId,Integer rewardType){
		//获取签到基础数据(客户端传递参数验证)
		QianDaoJiangLiConfig config = assignConfigExportService.loadById(rewardType);
		if(config == null){
			//配置异常
			return AppErrorCode.CONFIG_ERROR;
		}
		Assign assign = getAssign(userRoleId);
		//当前类型奖励是否领取
		if(!BitOperationUtil.calState(assign.getAssignTotal(), rewardType-1)){
			return AppErrorCode.ASSIGN_TYPE_REWARDED;
		}
		
		//是否达到领奖条件
		if(assign.getAssignCount().intValue() < config.getCount()){
			return AppErrorCode.ASSIGN_CONDITION_LESS;
		}
		
		if(roleBagExportService.checkPutInBag(config.getAwards(), userRoleId) != null){
			return AppErrorCode.BAG_NOEMPTY;
		}
		
		//更改累计签到状态
		Integer changeValue = BitOperationUtil.chanageState(assign.getAssignTotal(), rewardType-1);
		assign.setAssignTotal(changeValue);
		assignDao.cacheUpdate(assign, userRoleId);
		
		roleBagExportService.putInBag(config.getAwards(), userRoleId, GoodsSource.ASSIGN_TOTAL, true);
		
		//打印日志
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getAwards(), null);
		GamePublishEvent.publishEvent(new AssignRewardLogEvent(userRoleId,role.getName(),goods,rewardType));
		
		return new Object[]{AppErrorCode.SUCCESS, rewardType};
	}
	
	/**
	 * 一键补签
	 * @param userRoleId
	 * @param busMsgQueue
	 */
	public Object[] assignRetroactive(Long userRoleId){
		//根据vip获取当前玩家可以补签次数
		int retroactive = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_ASSIGN_BUQIAN);
		
		Assign assign = getAssign(userRoleId);
		//验证补签次数是否足够
		if(assign.getRetroactiveCount() >= retroactive){
			return AppErrorCode.ASSIGN_RETROACTIVE_LESS;
		}
		
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		//是否需要补签
		if(assign.getAssignCount() == day){
			return AppErrorCode.ASSIGN_RETROACTIVE_REFUSED;
		}
		
		//当前补签次数
		int currRetroactive = assign.getRetroactiveCount();
		//当前签到天数
		int currAssignCount = assign.getAssignCount();
		Integer tmpValue = assign.getAssignDays();
		
		long startTime = ServerInfoServiceManager.getInstance().getServerStartTime().getTime();
		
		int tmp = 0;
		boolean flagSameMonth = ObjectUtil.checkIsSameMonth(startTime, System.currentTimeMillis());
		if(flagSameMonth){
			tmp = ObjectUtil.getDayOfMonth(startTime)-1;
		}
		
		for(;tmp < day-1;tmp++){
			if(BitOperationUtil.calState(tmpValue,tmp)){
				tmpValue = BitOperationUtil.chanageState(tmpValue, tmp);
				currRetroactive++;
				currAssignCount++;
				if(currRetroactive >= retroactive){
					break;
				}
			}
		}
		
		//成就
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_QIANDAOCOUNT, 0});
			//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_QIANDAOCOUNT, 0);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		assign.setRetroactiveCount(currRetroactive);
		assign.setAssignDays(tmpValue);
		assign.setAssignCount(currAssignCount);
		assign.setUpdateTime(GameSystemTime.getSystemMillTime());
		assignDao.cacheUpdate(assign, userRoleId);

		//打印日志
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		GamePublishEvent.publishEvent(new AssignLogEvent(userRoleId,role.getName(),LogPrintHandle.ASSIGN_RETROACTIVE,currRetroactive, currAssignCount, tmpValue));
		return new Object[]{AppErrorCode.SUCCESS,assign.getRetroactiveCount(),assign.getAssignDays(),assign.getAssignCount()};
	}
	
	/**
	 * 今日的签到状态值
	 * @param userRoleId
	 * @return
	 */
	public int getTodayAssignStateValue(Long userRoleId){
		int state = 0;
		Assign assign = getAssign(userRoleId);
		//获取当前月份天数
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		//当前天数能否签名
		if(BitOperationUtil.calState(assign.getAssignDays(), day-1)){
			return 16;//今日是否已签到(4) 2的4次方
		}
		return state;
	}
	
	/**
	 * 获取签到是否有奖励
	 * @param userRoleId
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的0次方
	 */
	public int getAssignStateValue(Long userRoleId){
		//int(0) 二进制   奖励 有为1，无为0  顺序为  签到(0)，在线(1)，离线(2)，七登(3) 今日是否已签到(4)
		int state = 0;
		Assign assign = getAssign(userRoleId);
		if(assign.getAssignCount().intValue() == 0){
			//没有签到过，直接返回
			return state;
		}
		
		List<Integer> ids = assignConfigExportService.getAllConfigIds();
		for (Integer id : ids) {
			QianDaoJiangLiConfig config = assignConfigExportService.loadById(id);
			if(config == null){
				continue;
			}
			
			if(assign.getAssignCount().intValue() >= config.getCount() && BitOperationUtil.calState(assign.getAssignTotal(), id - 1)){
				return 1;//签到(0) 2的0次方
			}
		}
		
		return state;
	}
	
	/**
	 * 获取福利大厅是否在奖励奖励值
	 * @param userRoleId
	 * @return
	 */
	public int getFuLiDaTinStateValue(Long userRoleId){
		//签到(0)
		int state = getAssignStateValue(userRoleId);
		//在线(1)
		state = state + roleOnlineExportService.getOnlineStateValue(userRoleId);
		//离线(2)
		state = state + offlineExpExportService.getOfflineStateValue(userRoleId);
		//七登(3)
		state = state + sevenLoginExportService.getSevenLoginStateValue(userRoleId);
		//今日是否已签到(4)
		state = state + getTodayAssignStateValue(userRoleId);
		//是否有资源找回(5)
		state = state + roleResourceBackExportService.getResourceBackStateValue(userRoleId);
		return state;
	}
	/**
	 * 获得总得签到次数
	 * @param userRoleId
	 * @return
	 */
	public int getAssignAll(Long userRoleId) {
		Assign assign = getAssign(userRoleId);
		return assign.getAssignAll() + assign.getAssignCount();
	}
}
