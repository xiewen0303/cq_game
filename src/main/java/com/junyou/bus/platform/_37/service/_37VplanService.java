package com.junyou.bus.platform._37.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform._37.dao.Role37VplanDao;
import com.junyou.bus.platform.common.entity.Role37Vplan;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.bus.platform.utils._37Util;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event._370VplanRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;

/**
 * 37V计划
 * @author lxn
 *
 */
@Service
public class _37VplanService {

	@Autowired
	private Role37VplanDao  role37VplanDao;
	@Autowired
	private PtCommonService ptCommonService;
	@Autowired
	private RoleExportService  roleExportService;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService PlatformGongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RefabuActivityExportService refabuActivityExportService;
	
	private final static int LIMIT_LEVEL = _37Util.getVipLimitLevel(); //37V计划会员最低参与等级
	
	/**
	 * 领取每日礼包
	 */
	public Object[] getDayGift(long userRoleId){
		
		int vip = get37VplanLevel(userRoleId);
		if(vip < LIMIT_LEVEL){
			//VIP等级不足无法领取
			return AppErrorCode._37_VPLAN_VIP_LEVEL_NOT_ENOUGH;
		}
		PtCommonPublicConfig ptCommonPublicConfig = getConfig(PlatformPublicConfigConstants._37_VPLAN_DAY_LB);
		if(ptCommonPublicConfig==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Integer> items = ptCommonPublicConfig.getItems().get("item"+vip);
		if(items==null){
			//没有当前等级的VIP奖励
			ChuanQiLog.error("37V计划，没有当前等级的奖励,vip={}",vip);
			return AppErrorCode._37_VPLAN_LB_NO;
		}
		Role37Vplan role37Vplan = loadRole37Vplan(userRoleId);
		
		if(role37Vplan.getDayGiftState()==1){
			//已经领取
			return AppErrorCode._37_VPLAN_GIFT_GET_ALREADY;
		}
		
		role37Vplan.setDayGiftState(1); //1表示已领
		role37Vplan.setDayGiftUptime(GameSystemTime.getSystemMillTime());
		role37VplanDao.cacheUpdate(role37Vplan, userRoleId);
		
		
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		// ****进背包****
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new _370VplanRewardLogEvent(userRoleId, jsonArray,0,vip));
		
		return new Object[]{1,vip};
	}
	
	/**
	 * 获取升级等级礼包领取状态
	 */
	public int getUpgradeGiftState(long userRoleId){
		if(get37VplanLevel(userRoleId) < LIMIT_LEVEL){
			return 0;
		}
		return loadRole37Vplan(userRoleId).getLevelGift();
	}
	/**
	 * 领取升级等级礼包奖励
	 */
	public Object[] getUpgradeGift(long userRoleId, int level){
		
		int vip = get37VplanLevel(userRoleId);
		if(vip < LIMIT_LEVEL){
			//VIP等级不足无法领取
			return AppErrorCode._37_VPLAN_VIP_LEVEL_NOT_ENOUGH;
		}
		
		PtCommonPublicConfig config = getConfig(PlatformPublicConfigConstants._37_VPLAN_ROLE_LEVEL_LB);
		if(config==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		if (config.getInfo().get(level + "") == null) {
			return AppErrorCode._37_VPLAN_LB_NO;
		}
		Role37Vplan role37Vplan = loadRole37Vplan(userRoleId);
		if(!BitOperationUtil.calState(role37Vplan.getLevelGift(), level-1)){
			//已领领取
			return new Object[] {2, level }; 
		}
		
		int giftLevel = Integer.parseInt((String) config.getInfo().get(level + ""));
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role.getLevel() < giftLevel) {
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		Map<String, Integer> items = config.getItems().get(level + "");
		if (items == null) {
			return AppErrorCode._37_VPLAN_LB_NO;
		}
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		int state = BitOperationUtil.chanageState(role37Vplan.getLevelGift(), level - 1);
		role37Vplan.setLevelGift(state);
		role37VplanDao.cacheUpdate(role37Vplan, userRoleId);
		// ****进背包****
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new _370VplanRewardLogEvent(userRoleId, jsonArray,1,giftLevel));
		return new Object[]{1,level};
		
	}
	
	/**
	 * 消费礼包状态
	 */
	public Object[] getConsumeRewardState(Long userRoleId) {
		if(get37VplanLevel(userRoleId) < LIMIT_LEVEL){
			//VIP等级不足无法领取
			return null;
		}
		Role37Vplan role37Vplan = loadRole37Vplan(userRoleId);
		if(role37Vplan.getConsumeEndTime().longValue()==0){ //==0,消费礼包周期计时正式开始
			//开始一轮消费倒计时
			role37Vplan.setConsumeEndTime(GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME * 7);
			role37VplanDao.cacheUpdate(role37Vplan, userRoleId);
		}
		return new Object[] {role37Vplan.getConsumeGift(), role37Vplan.getConsumeEndTime() };
	}
	/**
	 * 本轮消费礼包金额
	 */
	public int getXfGigtConsume(Long userRoleId) {
		if(get37VplanLevel(userRoleId) < LIMIT_LEVEL){
			return 0;
		}
		return loadRole37Vplan(userRoleId).getConsumeTotalGold();
	}
	/**
	 * 领取消费礼包
	 */
	public Object[] getConsumeReward(Long userRoleId, int id) {
		if(get37VplanLevel(userRoleId) < LIMIT_LEVEL){
			//VIP等级不足无法领取
			return AppErrorCode._37_VPLAN_VIP_LEVEL_NOT_ENOUGH;
		}
		PtCommonPublicConfig config =  getConfig(PlatformPublicConfigConstants._37_VPLAN_LJXF_LB);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (config.getInfo().get(String.valueOf(id)) == null) {
			return AppErrorCode._37_VPLAN_LB_NO;
		}
		Integer needGold = (Integer) config.getInfo().get(String.valueOf(id));
		
		Role37Vplan role37Vplan = loadRole37Vplan(userRoleId);
		if (role37Vplan.getConsumeTotalGold() < needGold) {
			return AppErrorCode._37_VPLAN_LEVEL_GOLD_NOT_ENOUGH;
		}
		if(!BitOperationUtil.calState(role37Vplan.getConsumeGift(), id-1)){
			return AppErrorCode.GET_ALREADY_ERROR;
		}
		
		int state = BitOperationUtil.chanageState(role37Vplan.getConsumeGift(), id - 1);
		role37Vplan.setConsumeGift(state);
		role37VplanDao.cacheUpdate(role37Vplan, userRoleId);
		// ****进背包****
		Map<String, Integer> items = config.getItems().get(String.valueOf(id));
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new _370VplanRewardLogEvent(userRoleId, jsonArray,2,id));

		return new Object[] { 1, id };
	}
	/**
	 * V计划消费金额记录
	 * 活动上之后，消费活动七天一轮以玩家充值或者打开累计消费面板开始计算
	 * @param userRoleId
	 * @param cousumeGold
	 */
	public void sendRechargeToClient(Long userRoleId, Long cousumeGold) {
		if(cousumeGold==0){
			return;
		}
		//只有37wan平台才会进入
		if (ChuanQiConfigUtil.getPlatfromId().equals(PlatformPublicConfigConstants._37_PLAT_NAME)) {
			if(get37VplanLevel(userRoleId) < LIMIT_LEVEL){
				return ;
			}
			Role37Vplan role37Vplan = loadRole37Vplan(userRoleId);
			if(role37Vplan.getConsumeEndTime().longValue()==0){
				//开始一轮消费倒计时
				role37Vplan.setConsumeEndTime(GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME * 7);
			}
			// 检查下充值记录
			RoleYuanbaoRecord roleYuanbaoRecord = refabuActivityExportService.getRoleYuanbaoRecord(userRoleId);
			if (roleYuanbaoRecord != null && roleYuanbaoRecord.getCzValue() > role37Vplan.getConsumeTotalGold()) {
				// 替换
				role37Vplan.setConsumeTotalGold(roleYuanbaoRecord.getCzValue());
			}
			role37Vplan.setConsumeTotalGold(role37Vplan.getConsumeTotalGold()+cousumeGold.intValue());
			role37VplanDao.cacheUpdate(role37Vplan, userRoleId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_37WAN_CONSUM_VALUE, role37Vplan.getConsumeTotalGold());
		}
	}
	
	/**获取奖励配置**/
	private PtCommonPublicConfig getConfig(String modName){
		
		return (PtCommonPublicConfig)PlatformGongGongShuJuBiaoConfigExportService.loadPublicConfig(modName);
	}
	/***获取实体对象**/
	private Role37Vplan  loadRole37Vplan(long userRoleId){
		Role37Vplan role37Vplan = role37VplanDao.cacheAsynLoad(userRoleId, userRoleId);
		if(role37Vplan == null){
			role37Vplan  = new Role37Vplan();
			role37Vplan.setUserRoleId(userRoleId);
			role37Vplan.setDayGiftState(0);
			role37Vplan.setDayGiftUptime(0L);
			role37Vplan.setLevelGift(0);
			role37Vplan.setConsumeTotalGold(0);
			role37Vplan.setConsumeEndTime(0L);
			role37Vplan.setConsumeGift(0);
			role37Vplan.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			role37VplanDao.cacheInsert(role37Vplan, userRoleId);
		}else{
			//每日礼包跨天
			if(!DatetimeUtil.dayIsToday(role37Vplan.getDayGiftUptime())){
				role37Vplan.setDayGiftState(0);//0表示未领
				role37Vplan.setDayGiftUptime(GameSystemTime.getSystemMillTime());
			}
			//判断消费礼包活动时间是否过一轮
			if(role37Vplan.getConsumeEndTime().longValue()!=0 && GameSystemTime.getSystemMillTime()>role37Vplan.getConsumeEndTime()){
				role37Vplan.setConsumeGift(0);
				role37Vplan.setConsumeEndTime(GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME * 7);//下一轮重新开始
				role37Vplan.setConsumeTotalGold(0);
				role37VplanDao.cacheUpdate(role37Vplan, userRoleId);
			}
		}
		
		return role37Vplan;
	}
	//获取37会员的等级
	private  int get37VplanLevel(Long userRoleId){
		Map<String, String> info = ptCommonService.getRoleMap(userRoleId,PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL);
		int currentLevel = 0;
		if(info!=null && info.get(PlatformPublicConfigConstants.PLATFORM_37_VIP_PTLV)!=null ){
			currentLevel = Integer.parseInt(info.get(PlatformPublicConfigConstants.PLATFORM_37_VIP_PTLV));
		}
		return currentLevel;
	}
	
}
