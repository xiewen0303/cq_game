/**
 * 
 */
package com.junyou.bus.daomoshouzha.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.daomoshouzha.configure.export.DaoMoShouZhaConfig;
import com.junyou.bus.daomoshouzha.configure.export.DaoMoShouZhaConfigExportService;
import com.junyou.bus.daomoshouzha.configure.export.DaoMoShouZhaConfigGroup;
import com.junyou.bus.daomoshouzha.dao.DaoMoShouZhaLogDao;
import com.junyou.bus.daomoshouzha.entity.DaoMoShouZhaLog;
import com.junyou.bus.daomoshouzha.utils.DaoMoCountUtils;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tongyong.dao.ActityCountLogDao;
import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;



/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class DaoMoShouZhaService { 
	
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private DaoMoShouZhaLogDao daoMoShouZhaLogDao;
	@Autowired
	private ActityCountLogDao actityCountLogDao;
	
	public Object[] zhuixun(Long userRoleId,Integer version,int subId,int count,BusMsgQueue busMsgQueue){
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
		DaoMoShouZhaConfigGroup config = DaoMoShouZhaConfigExportService.getInstance().loadByMap(subId);
		
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		boolean insert = true;
		//如果配置了次数
		if(config.getMaxCount() > 0){
			//判断玩家次数
			ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
			if(log != null){
				insert = false;
				if(log.getCount() != null && log.getCount()+count > config.getMaxCount()){
					return AppErrorCode.ACTITY_MAX_COUNT;
				}
			}
		}
		int yb =  config.getGold() * count;//追寻需要花费的元宝
		
		if(yb > 0){
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,yb, userRoleId);
			if(null != goldError){ 
				return goldError;
			}
		}
		List<Object[]> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			int userCount = DaoMoCountUtils.getUserDaoMoCount(userRoleId);
			DaoMoShouZhaConfig dmConfig  = null;
			if(userCount+1 >= 50){
				dmConfig = DaoMoShouZhaConfigExportService.getInstance().loadByKeyId(subId, userCount+1);
			}
			if(dmConfig == null){
				//进行抽奖（抽前世今生来世）
				Integer id = Lottery.getRandomKeyByInteger(config.getZpMap());
				dmConfig  = DaoMoShouZhaConfigExportService.getInstance().loadByKeyId(subId, id);
			}
			//继续抽(抽物品)
			String item = Lottery.getRandomKeyByInteger(dmConfig.getItemMap());
			if(item == null || "".equals(item)){
				return AppErrorCode.CONFIG_ERROR;
			}
			Object[] obj = item.split(":");
			list.add(new Object[]{obj[0].toString(),obj[1],dmConfig.getId()});
			
			//消耗元宝
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, config.getGold(), userRoleId, LogPrintHandle.CONSUME_DAOMO, true,LogPrintHandle.CBZ_DAOMO);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB, config.getGold(),LogPrintHandle.CONSUME_DAOMO,QQXiaoFeiType.CONSUME_DAOMO,1});
			}
			Map<String, Integer> goodMap = new HashMap<String, Integer>();
			
			goodMap.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
			//检查物品是否可以进背包
			Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
			if(bagCheck != null){
				String title = EmailUtil.getCodeEmail(AppErrorCode.DAOMO_EMAIL_TITLE);
				String content = EmailUtil.getCodeEmail(AppErrorCode.DAOMO_EMAIL);
				emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, item);
			}else{
				//物品进背包
				roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_DAOMO_SHOUZHA, LogPrintHandle.GET_RFB_DMSZ, LogPrintHandle.GBZ_RFB_DMSZ, true);
			}
			//玩家抽取次数加1
			DaoMoCountUtils.setUserDaoMoCount(userRoleId, userCount+1);
			
			//全服日志
			GoodsConfig goodsConfig = goodsConfigExportService.loadById(obj[0].toString()); 
			saveLogAndNotify(subId,userRoleId, goodsConfig, Integer.parseInt(obj[1].toString()),configSong.getSkey(),dmConfig.getId(),busMsgQueue);
		}
		
		
		/*for (int i = 0; i < list.size(); i++) {
			Object[] o = list.get(i);
			//全服日志
			GoodsConfig goodsConfig = goodsConfigExportService.loadById(o[0].toString()); 
			saveLogAndNotify(subId,userRoleId, goodsConfig, Integer.parseInt(o[1].toString()),configSong.getSkey(),(int) o[2],busMsgQueue);
		}*/
		if(config.getMaxCount() > 0){
			if(insert){
				ActityCountLog log = new ActityCountLog();
				log.setUserRoleId(userRoleId);
				log.setCount(count);
				log.setUpdateTime(GameSystemTime.getSystemMillTime());
				actityCountLogDao.insertDb(log, subId);
			}else{
				actityCountLogDao.addActivityCount(subId, userRoleId, count);
			}
		}
		return new Object[]{1,subId,list.toArray(),count};
	}
	
	
	/**
	 * 保存广播日志并全服广播
	 * @param userRoleId
	 * @param goodsConfig
	 * @param count
	 * @param busMsgQueue
	 */
	private void saveLogAndNotify(int subId,long userRoleId,GoodsConfig goodsConfig,int count,String key,int dmType,BusMsgQueue busMsgQueue){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		String roleName = role.getName();
		//全局通知
		if(goodsConfig.isNotify()){
			
			
			if(role.isGm()){
				return;//GM不广播
			}
			
			busMsgQueue.addBroadcastMsg(ClientCmdType.DAOMO_GONGGAO, new Object[]{subId,roleName,new Object[]{goodsConfig.getId(),count,dmType},key});
		}
		//记录通知
		DaoMoShouZhaLog log = new DaoMoShouZhaLog();
		
		log.setRoleName(roleName);
		log.setGoodsId(goodsConfig.getId());
		log.setGoodsCount(count);
		log.setCreateTime(GameSystemTime.getSystemMillTime());
		log.setUserRoleId(userRoleId);
		log.setDmType(dmType);
		try {
			daoMoShouZhaLogDao.insertDb(log);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		//busMsgQueue.addBroadcastMsg(ClientCmdType.ZP_ONE_SYSTEM_NOTIFY, new Object[]{subId,new Object[]{goodsConfig.getId(),count,roleName},key});
	}
	

	
	
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		DaoMoShouZhaConfigGroup config = DaoMoShouZhaConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
		int count = 0;
		if(log != null){
			long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
			long upTime = log.getUpdateTime();
			long dTime = GameSystemTime.getSystemMillTime();
			if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
				actityCountLogDao.cleanActivityCount(subId, userRoleId);
			}else{
				count = log.getCount();
			}
		}
		return new Object[]{
			config.getPic(),
			config.getGold(),
			config.getShowList().toArray(),
			getLog(userRoleId),
			new Object[]{count,config.getMaxCount()}
		};
	}
	
	/**
	 * 获取个人购买日志
	 * @return
	 */
	private Object[] getLog(Long userRoleId){
		List<DaoMoShouZhaLog> xunbaoLogs = null;
		try {
			xunbaoLogs = daoMoShouZhaLogDao.getXunbaonfoByIdDb(userRoleId);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		if(xunbaoLogs == null || xunbaoLogs.size() <= 0){
			return null;
		}
		List<Object[]> list = new ArrayList<>();
		for (int i = 0; i < xunbaoLogs.size(); i++) {
			DaoMoShouZhaLog log = xunbaoLogs.get(i);
			list.add(new Object[]{log.getGoodsId(),log.getGoodsCount(),log.getDmType()});
		}
		return list.toArray();
		
	}
	
}