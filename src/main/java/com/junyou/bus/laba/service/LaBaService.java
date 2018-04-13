package com.junyou.bus.laba.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.laba.configure.export.LaBaConfig;
import com.junyou.bus.laba.configure.export.LaBaConfigExportService;
import com.junyou.bus.laba.configure.export.LaBaConfigGroup;
import com.junyou.bus.leichong.dao.LeichongDao;
import com.junyou.bus.leichong.entity.Leichong;
import com.junyou.bus.leichong.export.LeiChongExportService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tongyong.dao.ActityRecordLogDao;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;

@Service
public class LaBaService  extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		Leichong info = leiChongExportService.getLeiChong(userRoleId, subId);
		RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
//		LaBaConfig config = getNowConfig(subId, info.getBuqianCount()+1);
//		if(config == null){
//			ChuanQiLog.error("getNowConfig is null,subId="+subId+"\tBuqianCount:"+info.getBuqianCount());
//			return false;
//		}
		return getMaxCount(subId,info.getRechargeVal(),roleVip.getVipLevel()) > info.getBuqianCount();
	}
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ActityRecordLogDao actityRecordLogDao;
	@Autowired
	private LeiChongExportService leiChongExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private LeichongDao leichongDao;
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		LaBaConfigGroup group = LaBaConfigExportService.getInstance().loadByMap(subId);
		if (group == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Leichong leiCong = leiChongExportService.getLeiChong(userRoleId, subId);
		RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		
		Object[] data = new Object[7];
		data[0] = group.getBg();
		data[1] = group.getDesc();
		data[2] = actityRecordLogDao.getActivityCountBySubIdRecord(subId).toArray();
		List<Object[]> zhanBuVOs = new ArrayList<>();
		Map<Integer, LaBaConfig> labaConfigs = group.getLabaConfigs();
		if(labaConfigs != null){
			for (Entry<Integer,LaBaConfig> entry : labaConfigs.entrySet()) {
				Integer id = entry.getKey();
				LaBaConfig config = entry.getValue();
				zhanBuVOs.add(new Object[]{id,config.getVipLevel(),config.getChargenum(),config.getNeedGold(),config.getMingain()});
			}
		}else{
			ChuanQiLog.error("labaConfigs is null!");
		}
		
		data[3] = zhanBuVOs.toArray();
		data[4] = getMaxCount(subId,leiCong.getRechargeVal(),roleVip.getVipLevel()) - leiCong.getBuqianCount();
		data[5] = leiCong.getRechargeVal();
		LaBaConfig config = getNowConfig(subId,leiCong.getBuqianCount()+1);
		data[6] = config == null ? group.getMaxId() : config.getId();
		return data;
	}
	
	private LaBaConfig getNowConfig(int subId,int count) {
		LaBaConfigGroup group = LaBaConfigExportService.getInstance().loadByMap(subId);
		if(group.getLabaConfigs() == null){
			ChuanQiLog.error("LaBaConfigGroup is error !");
			return null;
		}
		
		for (LaBaConfig  config : group.getLabaConfigs().values()){
			if(config.getEndIndex() > count && count >= config.getBeginIndex()){
				return config;
			}
		}
		return null;
	}


//	private int getMaxCount(int subId,int rechargeVal,int vipLevel){
//		LaBaConfigGroup group = LaBaConfigExportService.getInstance().loadByMap(subId);
//		if(group.getLabaConfigs() == null){
//			return 0;
//		}
//		int count = 0;
//		for (Entry<Integer, LaBaConfig> element : group.getLabaConfigs().entrySet()) {
//			LaBaConfig config = element.getValue();
//			if(vipLevel >= config.getVipLevel() && rechargeVal >= config.getChargenum()){
//				count = config.getCount() > count ? config.getCount() : count;	
//			}
//		}
//		return count;
//	}
	
	private int getMaxCount(int subId,int rechargeVal,int vipLevel){
		LaBaConfigGroup group = LaBaConfigExportService.getInstance().loadByMap(subId);
		if(group.getLabaConfigs() == null){
			return 0;
		}
		int count = 0;
		for (Entry<Integer, LaBaConfig> element : group.getLabaConfigs().entrySet()) {
			LaBaConfig config = element.getValue();
			if(vipLevel >= config.getVipLevel() && rechargeVal >= config.getChargenum()){
				count += config.getCount();// > count ? config.getCount() : count;
			}
		}
		return count;
	}

	/**
	 * 获取状态数据,处理数据
	 * 备注：这里可以不响应给客户端，没有状态数据变更
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getLianChongStates(Long userRoleId, int subId) {
		LaBaConfigGroup group = LaBaConfigExportService.getInstance().loadByMap(subId);
		if (group == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Object[] result = new Object[4];
		
		Leichong info = leiChongExportService.getLeiChong(userRoleId, subId);
		RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		LaBaConfig config = getNowConfig(subId, info.getBuqianCount()+1);
		int targetConfigId =  config == null ? group.getMaxId() : config.getId();
//		if({
//			ChuanQiLog.error("getNowConfig is null,subId="+subId+"\tBuqianCount:"+info.getBuqianCount());
//			return AppErrorCode.CONFIG_ERROR;
//		}
		result[0] = subId;
		result[1] = targetConfigId;
		result[2] = getMaxCount(subId,info.getRechargeVal(),roleVip.getVipLevel()) - info.getBuqianCount();
		result[3] = info.getRechargeVal();
		return result;
	}
	
	/**
	 * 拉霸开始
	 */
	public Object[] laba(Long userRoleId,int subId, int version){
		
		RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper==null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		// 版本不一样
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		LaBaConfigGroup group = LaBaConfigExportService.getInstance().loadByMap(subId);
		if(group==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Leichong leicong = leiChongExportService.getLeiChong(userRoleId, subId);
		RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		
		int maxCount = getMaxCount(subId, leicong.getRechargeVal(), roleVip.getVipLevel());
		if(maxCount <= leicong.getBuqianCount()){
			return AppErrorCode.COUNT_FINISH;
		}
		LaBaConfig config = getNowConfig(subId,leicong.getBuqianCount() + 1);
		if(config == null){
			return AppErrorCode.DATA_ERROR;
		}
		
		Object[] goldFlag = accountExportService.decrCurrencyWithNotify(
				GoodsCategory.GOLD, config.getNeedGold(),
				userRoleId, LogPrintHandle.GET_LABA, true,
				LogPrintHandle.CBZ_CONSUME_LABA);
		
		if(goldFlag != null){
			return goldFlag;
		}
		
		Map<int[],Integer> targetRandom = config.getWeightVals();
		
		int[] data = Lottery.getRandomKeyByInteger(targetRandom);
		int addGold = RandomUtil.getRondom(data[0], data[1]);
		accountExportService.incrCurrencyWithNotify(GoodsCategory.GOLD, addGold, userRoleId,  LogPrintHandle.GET_LABA, LogPrintHandle.CBZ_CONSUME_LABA);
		
		leicong.setBuqianCount(leicong.getBuqianCount() + 1);
		
		leichongDao.cacheUpdate(leicong, userRoleId);
		
		int recordCount = group.getRecordCount();
		actityRecordLogDao.addActivityRecord(subId, new Object[]{roleWrapper.getName(),addGold}, recordCount);
		
		int nowCount = getMaxCount(subId,leicong.getRechargeVal(),roleVip.getVipLevel()) - leicong.getBuqianCount();
		
//		LaBaConfig nowConfig = getNowConfig(subId,leicong.getBuqianCount());
//		if(nowConfig  == null ){
//			return AppErrorCode.CONFIG_ERROR;
//		}
		
		this.checkIconFlag(userRoleId, subId);
		LaBaConfig newConfig = getNowConfig(subId,leicong.getBuqianCount()+1);
		if(newConfig != null && config.getId() != newConfig.getId()){
			BusMsgSender.send2One(userRoleId, ClientCmdType.LABA_REWARD_NOTICE53, new Object[]{subId,newConfig.getId(),nowCount,leicong.getRechargeVal()});
		}
		return new Object[]{1,subId,addGold,nowCount,newConfig == null ? config.getId() : newConfig.getId()};
	}
	

	public void rechargeYb(Long userRoleId,Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, LaBaConfigGroup> groups = LaBaConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, LaBaConfigGroup> entry: groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			int subId = entry.getKey();
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			Leichong leichong = leiChongExportService.getLeiChong(userRoleId, subId);	
			
			leichong.setRechargeVal((int) (leichong.getRechargeVal()+addVal));
			leichong.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			LaBaConfig config = getNowConfig(subId,leichong.getBuqianCount()+1);
			int targetConfigId = config == null ? entry.getValue().getMaxId() : config.getId();
			
			RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
			
			leichongDao.cacheUpdate(leichong, userRoleId);
			
			int syCount = getMaxCount(subId,leichong.getRechargeVal(),roleVip.getVipLevel()) - leichong.getBuqianCount();
			BusMsgSender.send2One(userRoleId, ClientCmdType.LABA_REWARD_NOTICE53, new Object[]{subId,targetConfigId,syCount,leichong.getRechargeVal()});
			
//			//检查通知客服端关闭掉Icon提示   TODO
			checkIconFlag(userRoleId, configSong.getId()); 
		}
	}
	
	public void checkVipLevelChange(Long userRoleId){
		Map<Integer, LaBaConfigGroup> groups = LaBaConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, LaBaConfigGroup> entry: groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			int subId = entry.getKey();
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			Leichong leichong = leiChongExportService.getLeiChong(userRoleId, subId);	
			
			LaBaConfig config = getNowConfig(subId,leichong.getBuqianCount()+1);
			int targetConfigId = config == null ? entry.getValue().getMaxId() : config.getId();
			
			RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
			
			int syCount = getMaxCount(subId,leichong.getRechargeVal(),roleVip.getVipLevel()) - leichong.getBuqianCount();
			BusMsgSender.send2One(userRoleId, ClientCmdType.LABA_REWARD_NOTICE53, new Object[]{subId,targetConfigId,syCount,leichong.getRechargeVal()});
			
			//检查通知客服端关闭掉Icon提示
			checkIconFlag(userRoleId, configSong.getId()); 
		}
	}
	
	public Object[] labaRecords(Long userRoleId, Integer subId, Integer version) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		// 版本不一样
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		
		List<Object> result = actityRecordLogDao.getActivityCountBySubIdRecord(subId);
		return new Object[]{subId,result.toArray()};
	}
}
