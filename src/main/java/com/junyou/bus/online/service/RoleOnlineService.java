package com.junyou.bus.online.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.online.configure.OnlineConfig;
import com.junyou.bus.online.configure.OnlineConfigExportService;
import com.junyou.bus.online.configure.ZaiXianLiBaoConfig;
import com.junyou.bus.online.configure.ZaiXianLiBaoConfigExportService;
import com.junyou.bus.online.dao.RoleOnlineDao;
import com.junyou.bus.online.entity.RoleOnline;
import com.junyou.bus.platform.qq.service.export.TencentLuoPanExportService;
import com.junyou.bus.platform.taiwan.export.TaiWanExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.OnlineRewardLogEvent;
import com.junyou.event.OnlineTimeLogEvent;
import com.junyou.event.RoleInOrOutLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.nodecontrol.service.NodeControlService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.math.BitOperationUtil;


@Service
public class RoleOnlineService{
	
	@Autowired
	private RoleOnlineDao roleOnlineDao;
	@Autowired
	private OnlineConfigExportService onlineConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private NodeControlService nodeControlService;
	@Autowired
	private TencentLuoPanExportService tencentLuoPanExportService;
	@Autowired
	private TaiWanExportService taiWanExportService;
	@Autowired
	private ZaiXianLiBaoConfigExportService zaiXianLiBaoConfigExportService;
	
	/**
	 * 打印今日在线时长日志
	 * @param roleOnline
	 */
	private void printDayOnlineTimeLog(RoleOnline roleOnline){
		try{
			long nowMills = GameSystemTime.getSystemMillTime();
			RoleWrapper role = roleExportService.getLoginRole(roleOnline.getUserRoleId());
			long onlineTime = 0l;
			
			if(!DatetimeUtil.dayIsToday(roleOnline.getLoginTime())){
				onlineTime = nowMills - DatetimeUtil.getDate00Time();
			}else{
				onlineTime = nowMills - roleOnline.getLoginTime();
			}
			
			GamePublishEvent.publishEvent(new OnlineTimeLogEvent(role.getId(), role.getName(), onlineTime, nowMills));
		}catch (Exception e) {
			ChuanQiLog.error("在线时长打印日志出现异常。",e);
		}
	}
	
	
	/**
	 * 打印昨日在线时长
	 * @param roleOnline
	 * @return	是否有更新过登陆时间
	 */
	private boolean printYesterdayOnlineTimeLog(RoleOnline roleOnline){
		try{
			RoleWrapper role = roleExportService.getLoginRole(roleOnline.getUserRoleId());
			long onlineTime = 0l;
			
			if(!DatetimeUtil.dayIsToday(roleOnline.getLoginTime())){
				long date00Time = DatetimeUtil.getDate00Time();
				onlineTime = date00Time - roleOnline.getLoginTime();
				roleOnline.setOnlineTime(0l);
				roleOnline.setState(0);
				roleOnline.setLoginTime(date00Time);//防止打印两次昨日在线数据
				roleOnlineDao.cacheUpdate(roleOnline, roleOnline.getUserRoleId());
				
				GamePublishEvent.publishEvent(new OnlineTimeLogEvent(role.getId(), role.getName(), onlineTime, roleOnline.getLoginTime()));
				//登陆日志
				int sort = LogPrintHandle.ROLE_IO_ACCOUNT;
				if(nodeControlService.isWeiDuanLogin(role.getId())){
					sort = LogPrintHandle.ROLE_IO_ACCOUNT_DLQ;
				}
				String via = null;
				String pf = null;
				if(PlatformConstants.isQQ()){
					via  = tencentLuoPanExportService.getUserZhuCeVia(roleOnline.getUserRoleId());
					pf  = tencentLuoPanExportService.getUserZhuCePf(roleOnline.getUserRoleId());
				}
				if(PlatformConstants.isTaiWan()){
					pf  = taiWanExportService.getUserZhuCePf(roleOnline.getUserRoleId());
				}
				GamePublishEvent.publishEvent(new RoleInOrOutLogEvent(LogPrintHandle.ROLE_IN_OR_OUT, sort, role.getUserId(),null, null, role.getLastLoginIp(),via,pf));
				return true;
			}
		}catch (Exception e) {
			ChuanQiLog.error("在线时长打印日志出现异常。",e);
		}
		return false;
	}
	
	/**
	 * 跨天时打印昨日登陆在线时长
	 */
	public void quartzLog(){
		for (Long userRoleId : publicRoleStateExportService.getAllOnlineRoleids()) {
			try{
				RoleOnline roleOnline = getRoleOnline(userRoleId);
				printYesterdayOnlineTimeLog(roleOnline);
			}catch (Exception e) {
				ChuanQiLog.error(userRoleId+"在线时长打印日志出现异常。",e);
			}
		}
	}
	
	/**
	 * 登出操作
	 * @param userRoleId
	 */
	public void logout(Long userRoleId){
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		long nowMills = GameSystemTime.getSystemMillTime();
		//非同一天
		if(!DatetimeUtil.dayIsToday(roleOnline.getLoginTime())){
			roleOnline.setTotalOnlineTime(roleOnline.getOnlineTime()+roleOnline.getTotalOnlineTime());
			roleOnline.setOnlineTime(nowMills-DatetimeUtil.getDate00Time());
			roleOnline.setState(0);
		}else{
			long d = nowMills-roleOnline.getLoginTime();
			if(d > 0) roleOnline.setOnlineTime(roleOnline.getOnlineTime()+d);
		}
		
		//下线时打印本次登陆的在线时长
		printDayOnlineTimeLog(roleOnline);
		roleOnline.setLoginTime(nowMills);
		
		roleOnlineDao.cacheUpdate(roleOnline, userRoleId);
	}
	
	/**
	 * 登陆操作
	 * @param userRoleId
	 */
	public void login(Long userRoleId){
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		long nowMills = GameSystemTime.getSystemMillTime();
		//非同一天
		if(!DatetimeUtil.dayIsToday(roleOnline.getLoginTime())){
			roleOnline.setTotalOnlineTime(roleOnline.getOnlineTime()+roleOnline.getTotalOnlineTime());
			roleOnline.setOnlineTime(0l);
			roleOnline.setState(0);
		}
		roleOnline.setLoginTime(nowMills);
		roleOnlineDao.cacheUpdate(roleOnline, userRoleId);
	}
	
	/**
	 * 获取今日累计在线时长
	 * @param userRoleId
	 * @return
	 */
	public long getTodayLoginOnline(Long userRoleId){
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		long nowMills = GameSystemTime.getSystemMillTime();
		//非同一天
		if(!DatetimeUtil.dayIsToday(roleOnline.getLoginTime())){
			roleOnline.setTotalOnlineTime(roleOnline.getOnlineTime()+roleOnline.getTotalOnlineTime());
			//打印昨日在线时长
			if(!printYesterdayOnlineTimeLog(roleOnline)){
				long date00Time = DatetimeUtil.getDate00Time();
				roleOnline.setOnlineTime(0l);
				roleOnline.setState(0);
				roleOnline.setLoginTime(date00Time);
				roleOnlineDao.cacheUpdate(roleOnline, userRoleId);
			}
		}
		return roleOnline.getOnlineTime()+nowMills-roleOnline.getLoginTime();
	}
	
	
	/**
	 * 在线奖励初始化
	 * @param userRoleId
	 * @return
	 */
	public Object[] getOnlineRewardInfo(Long userRoleId){
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		List<Object> objs = getArrayInfo(roleOnline);
		return new Object[]{getTodayLoginOnline(userRoleId),roleOnline.getState(),objs.toArray()};
	}
	
	/**
	 * 客户端需要展示的奖励信息
	 * @param roleOnline
	 * @return
	 */
	private List<Object> getArrayInfo(RoleOnline roleOnline){
		List<Object> objs = new ArrayList<Object>();
		if(StringUtils.isNotBlank(roleOnline.getGoods1())){
			objs.add(new Object[]{1,roleOnline.getGoods1(),roleOnline.getCount1()});
		}
		if(StringUtils.isNotBlank(roleOnline.getGoods2())){
			objs.add(new Object[]{2,roleOnline.getGoods2(),roleOnline.getCount2()});
		}
		if(StringUtils.isNotBlank(roleOnline.getGoods3())){
			objs.add(new Object[]{3,roleOnline.getGoods3(),roleOnline.getCount3()});
		}
		if(StringUtils.isNotBlank(roleOnline.getGoods4())){
			objs.add(new Object[]{4,roleOnline.getGoods4(),roleOnline.getCount4()});
		}
		return objs;
	}
	
	private RoleOnline getRoleOnline(Long userRoleId){
		RoleOnline roleOnline = roleOnlineDao.cacheAsynLoad(userRoleId, userRoleId);
		if(roleOnline==null){
			roleOnline = new RoleOnline();
			roleOnline.setTotalOnlineTime(0L);
			roleOnline.setOnlineTime(0l);
			roleOnline.setState(0);
			roleOnline.setUserRoleId(userRoleId);
			roleOnline.setOnlineAwardsData("{}");
			roleOnlineDao.cacheInsert(roleOnline, userRoleId);
		}
		return roleOnline;
	}
	
	/**
	 * 领取在线奖励
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public Object[] getOnlineReward(Long userRoleId){
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		long onlineTime = getTodayLoginOnline(userRoleId);
		Map<String,Integer> finalItems = new HashMap<String, Integer>();
		Integer changeValue = roleOnline.getState();
		//验证在线时间是否达到
		for(int i = 1;i <= GameConstants.ROLE_ONLINE_VALUE;i++){
			//验证当前类型有无领取奖励
			if(!BitOperationUtil.calState(roleOnline.getState(), i - 1)){
				continue;
			}
			OnlineConfig config = onlineConfigExportService.loadById(i);
			//配置异常
			if(config == null){
				continue;
			}
			//时间是否达到
			if(onlineTime < config.getTime() * 60 * 1000l){
				break;
			}
			//抽取奖励
			byte key = Lottery.getRandomKey(config.getWeights());
			Map<String, Integer> items = config.getRewardItems().get(key);
			//奖励抽取出现异常
			if(items == null || items.size() == 0){
				continue;
			}
			ObjectUtil.mapAdd(finalItems, items);
			//更改状态
			changeValue = BitOperationUtil.chanageState(changeValue, i-1);
			changeOnlineState(i, roleOnline, items);
		}
		if(!finalItems.isEmpty()){			
			roleOnline.setState(changeValue);
			roleOnlineDao.cacheUpdate(roleOnline, userRoleId);
			String content = EmailUtil.getCodeEmail(AppErrorCode.ONLINE_EMAIL_CONTENT+"");
			roleBagExportService.putInBagOrEmail(finalItems, userRoleId, GoodsSource.ONLINE_REWARD, true,content);
			//打印日志
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			JSONArray goods = LogPrintHandle.getLogGoodsParam(finalItems, null);
			GamePublishEvent.publishEvent(new OnlineRewardLogEvent(userRoleId,role.getName(),goods,getTodayLoginOnline(userRoleId)));
			List<Object> objs = getArrayInfo(roleOnline);
			return new Object[]{AppErrorCode.SUCCESS,objs.toArray()};
		}	
		return AppErrorCode.ONLINE_REWARD_ERROR;
	}
	
	/**
	 * 更改领取奖励状态（客户端标示）
	 * @param type
	 * @param roleOnline
	 * @param items
	 */
	private void changeOnlineState(int type,RoleOnline roleOnline,Map<String,Integer> items){
		String goods = null;
		Integer count = 0;
		if(items != null && items.size() > 0){
			for(Entry<String,Integer> entry:items.entrySet()){
				goods = entry.getKey();
				count = entry.getValue();
				break;
			}
		}
		switch (type) {
		case 1:
			roleOnline.setGoods1(goods);
			roleOnline.setCount1(count);
			break;
		case 2:
			roleOnline.setGoods2(goods);
			roleOnline.setCount2(count);
			break;
		case 3:
			roleOnline.setGoods3(goods);
			roleOnline.setCount3(count);
			break;
		case 4:
			roleOnline.setGoods4(goods);
			roleOnline.setCount4(count);
			break;
		}
	}
	
	
	/**
	 * 获取在线是否有奖励
	 * @param userRoleId
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的1次方
	 */
	public int getOnlineStateValue(Long userRoleId){
		//int(0) 二进制   奖励 有为1，无为0  顺序为  签到(0)，在线(1)，离线(2)，七登(3)
		int state = 0;
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		long onlineTime = getTodayLoginOnline(userRoleId);
		
		//验证在线时间是否达到
		for(int i = 1;i <= GameConstants.ROLE_ONLINE_VALUE;i++){
			OnlineConfig config = onlineConfigExportService.loadById(i);
			//配置异常
			if(config == null){
				continue;
			}
			
			//时间是否达到
			if(BitOperationUtil.calState(roleOnline.getState(), i - 1) && onlineTime >= config.getTime() * 60 * 1000l){
				return 2;//在线(1) 2的1次方
			}
		}
		
		return state;
	}
	/**
	 * 获取玩家总得在线时间（单位毫秒）
	 * @param userRoleId
	 * @return
	 */
	public long getTotalOnlineTime(Long userRoleId){
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		//非同一天
		if(!DatetimeUtil.dayIsToday(roleOnline.getLoginTime())){
			roleOnline.setTotalOnlineTime(roleOnline.getOnlineTime() + roleOnline.getTotalOnlineTime());
			//打印昨日在线时长
			if(!printYesterdayOnlineTimeLog(roleOnline)){
				long date00Time = DatetimeUtil.getDate00Time();
				roleOnline.setOnlineTime(0l);
				roleOnline.setState(0);
				roleOnline.setLoginTime(date00Time);
				roleOnlineDao.cacheUpdate(roleOnline, userRoleId);
			}
			
		}
		return roleOnline.getOnlineTime() + GameSystemTime.getSystemMillTime() - roleOnline.getLoginTime() + roleOnline.getTotalOnlineTime();
	}
	
	/**
	 * 获取限时在线信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] awardOnlineInfo(Long userRoleId) {
		RoleOnline roleOnline = getRoleOnline(userRoleId);
	 
		int day = getActivityDay(userRoleId);
		if(day <0 ){
			ChuanQiLog.error("activity is end !");
			return AppErrorCode.TERRITORY_ACTIVE_END;
		}
		
		Object[] result = new Object[day];
		for (int i = 1; i <= day; i++) {
			result[i-1] =  new Object[]{i,CovertObjectUtil.object2int(roleOnline.getOnlineAwards().get(i))};			
		}
		
		return new Object[]{1,new Object[]{getTodayLoginOnline(userRoleId),result}};
	}
	
	
	private int getActivityDay(long userRoleId){
		UserRole  userRole = roleExportService.getUserRole(userRoleId);
		if(userRole == null){
			ChuanQiLog.error("awardOnlineInfo user is not online!");
			return -1;
		}
		int maxDay = zaiXianLiBaoConfigExportService.getMaxDay();
		int day = DatetimeUtil.getBetweenDay(userRole.getCreateTime().getTime());
		
		if(day > maxDay ){
			return -1;
		}
		return day;
	}

	/**
	 * 请求领取某个奖励
	 * @param userRoleId
	 * @param time
	 * @return
	 */
	public Object[] lingquOnline(Long userRoleId, int time) {
		RoleOnline roleOnline = getRoleOnline(userRoleId);
		int day = getActivityDay(userRoleId);
		if(day < 0){
			ChuanQiLog.error("activity is end !");
			return AppErrorCode.TERRITORY_ACTIVE_END;
		}
		int awardsState = CovertObjectUtil.object2int(roleOnline.getOnlineAwards().get(day));
		int index = zaiXianLiBaoConfigExportService.getIndex(day,time);
		long onlineTime = getTodayLoginOnline(userRoleId);
		if(onlineTime < time*60000){
			return AppErrorCode.ONLINE_REWARD_ERROR;
		}
		
		boolean flag = BitOperationUtil.calState(awardsState, index);
		if(!flag){
			return AppErrorCode.VIP_GIFT_HAS_REVICED;
		}
		
		ZaiXianLiBaoConfig config =  zaiXianLiBaoConfigExportService.loadById(day);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Map<String,Integer> awards = config.getAwards().get(time);
		if(ObjectUtil.isEmpty(awards)){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Object[] flagBag = roleBagExportService.checkPutGoodsAndNumberAttr(awards, userRoleId);
		if(flagBag != null){
			return flagBag;
		}
		
		roleOnline.getOnlineAwards().put(day, BitOperationUtil.chanageState(awardsState, index));
		
		roleBagExportService.putGoodsAndNumberAttr(awards, userRoleId,  GoodsSource.LIMIT_LIBAO, LogPrintHandle.GET_XSLB, LogPrintHandle.GBZ_LIMIT_SHOP, true);
		roleOnlineDao.cacheUpdate(roleOnline, userRoleId);
		return new Object[]{1,time};
	}
}
