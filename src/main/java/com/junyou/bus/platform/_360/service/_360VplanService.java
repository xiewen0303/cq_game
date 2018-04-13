package com.junyou.bus.platform._360.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform._360.util._360Util;
import com.junyou.bus.platform.common.dao.Role360VplanDao;
import com.junyou.bus.platform.common.entity.Role360Vplan;
import com.junyou.bus.platform.common.entity.VplanMessage;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event._360VplanRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.math.BitOperationUtil;
import com.junyou.utils.md5.Md5Utils;
import com.kernel.spring.container.DataContainer;

@Service
public class _360VplanService {

	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private Role360VplanDao role360VplanDao;

	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;

	@Autowired
	private RefabuActivityExportService refabuActivityExportService;
	
	/**
	 * 初始化360V计划会员的信息
	 */
	public void init360Vinfo(Long userRoleId) {
		if (ChuanQiConfigUtil.getPlatfromId().equals(PlatformPublicConfigConstants._360_PLAT_NAME)) {
//				init360VplanInfo(userRoleId);
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_360_VPLAN_INFO, userRoleId);
		}

	}

	/**
	 * 领取每日礼包 二进制第一位表示年费礼包
	 */
	public Object[] getDayReward(Long userRoleId) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		VplanMessage vplanMessage = dataContainer.getData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString());
		if (vplanMessage == null) {
			return AppErrorCode.GET_PLATFORM_INFO_ERROR;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("N")) {
			return AppErrorCode.NO_360_VPLAN_USER;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("E")) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;
		}
		if (vplanMessage.getData().getEndTime() < GameSystemTime.getSystemMillTime() / 1000) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;// 过期了
		}
		int level = vplanMessage.getData().getLevel();
		if (level <= 0) {
			return AppErrorCode._360_VPLAN_LEVEL_NOT_ENOUGH;
		}
		PtCommonPublicConfig config = (PtCommonPublicConfig) getConfigByModName(PlatformPublicConfigConstants._360_VPLAN_DAY_LB);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Role360Vplan role360Vplan = getRole360Vplan(userRoleId);
		if (DatetimeUtil.dayIsToday(role360Vplan.getDayGiftTime())) {
//			if (!BitOperationUtil.calState(role360Vplan.getDayGift(), level)) {
//				return AppErrorCode.GET_ALREADY_ERROR; //已结领取
//			}
			//是当天就表示当日已经领过，当天不管等级是否升级也不能领取
			return AppErrorCode.GET_ALREADY_ERROR; //已结领取
		}
		int state = role360Vplan.getDayGift();
		Map<String, Integer> items = new HashMap<String, Integer>();
		Map<String, Integer> levelItems = config.getItems().get("item" + level);
		items.putAll(levelItems);
		boolean isYear = false;
		if (vplanMessage.getData().getType().equals("Y")) {
			// 年会员额外奖励
			Map<String, Integer> etxItems = config.getItems().get("year");
			items.putAll(etxItems);
			state = BitOperationUtil.chanageState(state, 0);
			isYear = true;
		}
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		state = BitOperationUtil.chanageState(state, level);
		role360Vplan.setDayGift(state);
		role360Vplan.setDayGiftTime(GameSystemTime.getSystemMillTime());
		role360VplanDao.cacheUpdate(role360Vplan, userRoleId);
		// ****进背包****
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new _360VplanRewardLogEvent(userRoleId, jsonArray, 0, level));
		return new Object[] { AppErrorCode.SUCCESS, level, isYear };
	}

	/**
	 * 升级礼包领取状态
	 */
	public int getUpgradeRewardState(Long userRoleId) {
		if (getState(userRoleId) != null) {
			return 0;
		}
		return getRole360Vplan(userRoleId).getUpgradeGift();
	}

	/**
	 * 开通(特供)礼包领取状态
	 */
	public int getKtGigtState(Long userRoleId) {
		if (getState(userRoleId) != null) {
			return 0;
		}
		return getRole360Vplan(userRoleId).getTgGift();
	}

	/**
	 * 消费礼包状态
	 */
	public Object[] getXfGigtState(Long userRoleId) {
		Object[] ret = getState(userRoleId);
		if (ret != null) {
			return null;
		}
		Role360Vplan role360Vplan = getRole360Vplan(userRoleId);
		if(GameSystemTime.getSystemMillTime()>role360Vplan.getConsumeEndTime()){
			role360Vplan.setConsumeGift(0);
			role360Vplan.setConsumeEndTime(GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME * 7);
			role360Vplan.setConsumeTotalGold(0);
			role360VplanDao.cacheUpdate(role360Vplan, userRoleId);
		}
		return new Object[] {role360Vplan.getConsumeGift(), role360Vplan.getConsumeEndTime() };
	}

	/**
	 * 本轮消费礼包金额
	 */
	public int getXfGigtConsume(Long userRoleId) {
		if (getState(userRoleId) != null) {
			return 0;
		}
		Role360Vplan role360Vplan = getRole360Vplan(userRoleId);
		if(GameSystemTime.getSystemMillTime()>role360Vplan.getConsumeEndTime()){
			role360Vplan.setConsumeGift(0);
			role360Vplan.setConsumeEndTime(GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME * 7);
			role360Vplan.setConsumeTotalGold(0);
			role360VplanDao.cacheUpdate(role360Vplan, userRoleId);
			return 0;
		}
		return role360Vplan.getConsumeTotalGold();
	}

	/**
	 * 请求状态的时候如果不是会员或者过期会员统一返回0
	 */
	private Object[] getState(Long userRoleId) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		VplanMessage vplanMessage = dataContainer.getData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString());
		if (vplanMessage == null) {
			return AppErrorCode.GET_PLATFORM_INFO_ERROR;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("N")) {
			return AppErrorCode.NO_360_VPLAN_USER;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("E")) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;
		}
		return null;
	}

	/**
	 * 领取开通礼包
	 */
	public Object[] getKtGigt(Long userRoleId) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		VplanMessage vplanMessage = dataContainer.getData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString());
		if (vplanMessage == null) {
			return AppErrorCode.GET_PLATFORM_INFO_ERROR;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("N")) {
			return AppErrorCode.NO_360_VPLAN_USER;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("E")) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;
		}
		if (vplanMessage.getData().getEndTime() < GameSystemTime.getSystemMillTime() / 1000) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;// 过期了
		}
		PtCommonPublicConfig config = (PtCommonPublicConfig) getConfigByModName(PlatformPublicConfigConstants._360_VPLAN_TG_LB);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Role360Vplan role360Vplan = getRole360Vplan(userRoleId);
		
		Map<String, Integer> items = new HashMap<>();//
		int type = 2;
		if (vplanMessage.getData().getType().equals("Y")) {
			ObjectUtil.mapAdd(items, config.getItems().get("item2"));
			//如果之前没领过月礼包
			if(BitOperationUtil.calState(role360Vplan.getTgGift(), 0)){
				ObjectUtil.mapAdd(items, config.getItems().get("item1"));
			}
		} else {
			type = 1;
			items = config.getItems().get("item1");
		}
		if(type==2){
			//年会员也可以领取月礼包
			if (!BitOperationUtil.calState(role360Vplan.getTgGift(), 0) && !BitOperationUtil.calState(role360Vplan.getTgGift(), 1)) {
				return AppErrorCode.GET_ALREADY_ERROR;
			}
		}else{
			if (!BitOperationUtil.calState(role360Vplan.getTgGift(),0)) {
				return AppErrorCode.GET_ALREADY_ERROR;
			}
		}
		
		int state = BitOperationUtil.chanageState(role360Vplan.getTgGift(), type - 1);
		if(type==2){
			state= BitOperationUtil.chanageState(state, 0);	//年会员同事领取月会员礼包
		}
		role360Vplan.setTgGift(state);
		role360VplanDao.cacheUpdate(role360Vplan, userRoleId);
		// ****进背包****
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new _360VplanRewardLogEvent(userRoleId, jsonArray, 2));
		return new Object[] { 1, state };
	}

	/**
	 * 等级礼包
	 */
	public Object[] getUpgradeReward(Long userRoleId, int level) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		VplanMessage vplanMessage = dataContainer.getData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString());
		if (vplanMessage == null) {
			return AppErrorCode.GET_PLATFORM_INFO_ERROR;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("N")) {
			return AppErrorCode.NO_360_VPLAN_USER;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("E")) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;
		}
		if (vplanMessage.getData().getEndTime() < GameSystemTime.getSystemMillTime() / 1000) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;// 过期了
		}
		PtCommonPublicConfig config = (PtCommonPublicConfig) getConfigByModName(PlatformPublicConfigConstants._360_VPLAN_LEVEL_LB);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (config.getInfo().get(level + "") == null) {
			return AppErrorCode.NO_360_VPLAN_LEVEL_LB;
		}

		int giftLevel = Integer.parseInt((String) config.getInfo().get(level + ""));
		if (role.getLevel() < giftLevel) {
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		Map<String, Integer> items = config.getItems().get(level + "");
		if (items == null) {
			return AppErrorCode.NO_360_VPLAN_LEVEL_LB;
		}
		Role360Vplan role360Vplan = getRole360Vplan(userRoleId);
		if (!BitOperationUtil.calState(role360Vplan.getUpgradeGift(), level - 1)) {
			return new Object[] { 2, level };// 已经领取
		}

		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		int state = BitOperationUtil.chanageState(role360Vplan.getUpgradeGift(), level - 1);
		role360Vplan.setUpgradeGift(state);
		role360VplanDao.cacheUpdate(role360Vplan, userRoleId);
		// ****进背包****
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new _360VplanRewardLogEvent(userRoleId, jsonArray, 1, level));
		return new Object[] { AppErrorCode.SUCCESS, level };
	}

	/**
	 * 领取消费礼包
	 */
	public Object[] getConsumeReward(Long userRoleId, int id) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		VplanMessage vplanMessage = dataContainer.getData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString());
		if (vplanMessage == null) {
			return AppErrorCode.GET_PLATFORM_INFO_ERROR;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("N")) {
			return AppErrorCode.NO_360_VPLAN_USER;
		}
		if (vplanMessage.getData().getType().equalsIgnoreCase("E")) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;
		}
		if (vplanMessage.getData().getEndTime() < GameSystemTime.getSystemMillTime() / 1000) {
			return AppErrorCode.NO_360_VPLAN_EXPIRE;// 过期了
		}
		PtCommonPublicConfig config = (PtCommonPublicConfig) getConfigByModName(PlatformPublicConfigConstants._360_VPLAN_XF_LB);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (config.getInfo().get(String.valueOf(id)) == null) {
			return AppErrorCode.NO_360_VPLAN_LEVEL_LB;
		}
		Integer needGold = (Integer) config.getInfo().get(String.valueOf(id));
		Role360Vplan role360Vplan = getRole360Vplan(userRoleId);
		if (role360Vplan.getConsumeTotalGold() < needGold) {
			return AppErrorCode._360_VPLAN_LEVEL_GOLD_NOT_ENOUGH;
		}
		if(!BitOperationUtil.calState(role360Vplan.getConsumeGift(), id-1)){
			return AppErrorCode.GET_ALREADY_ERROR;
		}
		int state = BitOperationUtil.chanageState(role360Vplan.getConsumeGift(), id - 1);
		role360Vplan.setConsumeGift(state);
		role360VplanDao.cacheUpdate(role360Vplan, userRoleId);
		// ****进背包****
		Map<String, Integer> items = config.getItems().get(String.valueOf(id));
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
		GamePublishEvent.publishEvent(new _360VplanRewardLogEvent(userRoleId, jsonArray, 3, needGold));

		return new Object[] { 1, id };

	}
	/**
	 * V计划消费金额记录
	 * @param userRoleId
	 * @param cousumeGold
	 */
	public void sendRechargeToClient(Long userRoleId, Long cousumeGold) {
		if(cousumeGold==0){
			return;
		}
		//只有360平台才会进入
		if (ChuanQiConfigUtil.getPlatfromId().equals(PlatformPublicConfigConstants._360_PLAT_NAME)) {
			if (getState(userRoleId) != null) {
				 return;
			}
			Role360Vplan role360Vplan = this.getRole360Vplan(userRoleId);
			if(GameSystemTime.getSystemMillTime()>role360Vplan.getConsumeEndTime()){
				//这一轮消费到期 进入下一轮
				role360Vplan.setConsumeEndTime(GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME * 7);
				role360Vplan.setConsumeTotalGold(0);
				role360Vplan.setConsumeGift(0);
			}
			// 检查下充值记录
			RoleYuanbaoRecord roleYuanbaoRecord = refabuActivityExportService.getRoleYuanbaoRecord(userRoleId);
			if (roleYuanbaoRecord != null && roleYuanbaoRecord.getCzValue() > role360Vplan.getConsumeTotalGold()) {
				// 替换
				role360Vplan.setConsumeTotalGold(roleYuanbaoRecord.getCzValue());
			}
			role360Vplan.setConsumeTotalGold(role360Vplan.getConsumeTotalGold()+cousumeGold.intValue());
			role360VplanDao.cacheUpdate(role360Vplan, userRoleId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_360_XF_LB_GOLD, role360Vplan.getConsumeTotalGold());
		}
	}

	/**
	 * 获取对应的config
	 * 
	 * @param modName
	 *            对应的模块的名称
	 * @return
	 */
	public AdapterPublicConfig getConfigByModName(String modName) {
		return platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(modName);
	}

	/**
	 * 获取Role360Vplan
	 */
	private Role360Vplan getRole360Vplan(Long userRoleId) {
		Role360Vplan role360Vplan = role360VplanDao.cacheAsynLoad(userRoleId, userRoleId);
		if (role360Vplan == null) {
			role360Vplan = new Role360Vplan();
			role360Vplan.setUserRoleId(userRoleId);
			role360Vplan.setDayGift(0);
			role360Vplan.setDayGiftTime(0L);
			role360Vplan.setUpgradeGift(0);
			role360Vplan.setTgGift(0);
			role360Vplan.setConsumeEndTime(GameSystemTime.getSystemMillTime() + GameConstants.DAY_TIME * 7);// 七天一轮回
			role360Vplan.setConsumeGift(0);
			role360Vplan.setConsumeTotalGold(0);
			role360Vplan.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			// 后续加的字段都在这里加
			role360VplanDao.cacheInsert(role360Vplan, userRoleId);
		}
		return role360Vplan;
	}

	/**
	 * 获取V计划用户的信息
	 * 
	 * @param args
	 */
	public VplanMessage init360VplanInfo(Long userRoleId) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return null;
		}
		VplanMessage vplanMessage = dataContainer.getData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString());
		if (vplanMessage == null) {
			// 拉取会员信息
			String result = getVplanBaseInfo(role.getUserId());

			if (result.contains("\"errno\":0,")) {
				// 正常返回
				vplanMessage = JSON.parseObject(result, VplanMessage.class);
			} else {
				JSONObject json = JSONObject.parseObject(result);
				vplanMessage = new VplanMessage();
				int errno = (int) json.get("errno");
				vplanMessage.setErrno(errno);
				vplanMessage.setErrmsg((String) json.get("errmsg"));
				VplanMessage.Data data = vplanMessage.new Data();
				data.setType("N"); // 默认为非会员
				vplanMessage.setData(data);
				// 360平台数据错误
				if (ChuanQiConfigUtil.getPlatfromId().equals(PlatformPublicConfigConstants._360_PLAT_NAME)) {
					ChuanQiLog.error("平台id={},userId={},360V计划请求参数异常信息：{}",PlatformPublicConfigConstants._360_PLAT_NAME,role.getUserId(), result);
				}
			}
			dataContainer.putData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString(), vplanMessage);
		}
		return vplanMessage;
	}

	/**
	 * 获取平台返回的信息
	 * 
	 * @param uid
	 * @return
	 */
	private String getVplanBaseInfo(String uid) {
//		uid = "1386676079"; // TODO 测试阶段
		String gkey = _360Util.get360VplanGkey();
		String lkey = _360Util.get360VplanLkey();
		long time = GameSystemTime.getSystemMillTime();
		String version = _360Util.get360VplangVersion();
		String url = _360Util.get360VplanUrl();
		StringBuffer signBuf = new StringBuffer();
		signBuf.append(gkey).append(uid).append(time).append(lkey);
		String sign = Md5Utils.md5To32(signBuf.toString());

		StringBuffer paramsBuf = new StringBuffer();
		paramsBuf.append("uid=").append(uid).append("&gkey=").append(gkey).append("&time=").append(time).append("&sign=").append(sign).append("&version=").append(version);
		String result = HttpClientMocker.requestMockPost(url, paramsBuf.toString());
		return result;

	}

	public static void main(String[] args) {

		// TODO Auto-generated method stub

		String uid = "1460880438";
		
		String gkey = "hqg";
		String lkey = "c7802435f1004460a7bf86c2ed6d33a8";
		long time = GameSystemTime.getSystemMillTime();
		String version = "3.0";
		String url = "http://rcapi.360-g.net/vplan_wan?";
		StringBuffer signBuf = new StringBuffer();
		signBuf.append(gkey).append(uid).append(time).append(lkey);
		String sign = Md5Utils.md5To32(signBuf.toString());

		StringBuffer paramsBuf = new StringBuffer();
		paramsBuf.append("uid=").append(uid).append("&gkey=").append(gkey).append("&time=").append(time).append("&sign=").append(sign).append("&version=").append(version);
		String result = HttpClientMocker.requestMockPost(url, paramsBuf.toString());
//		VplanMessage vmessage = JSON.parseObject(result, VplanMessage.class);
//		System.out.println("完整链接：" + url + paramsBuf.toString());
		System.out.println("V计划会员信息：" + result);
	}
	
}
