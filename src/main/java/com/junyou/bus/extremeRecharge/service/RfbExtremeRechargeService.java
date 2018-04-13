package com.junyou.bus.extremeRecharge.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.extremeRecharge.configure.export.RfbExtremeRechargeConfig;
import com.junyou.bus.extremeRecharge.configure.export.RfbExtremeRechargeConfigExportService;
import com.junyou.bus.extremeRecharge.dao.RfbExtremeRechargeDao;
import com.junyou.bus.extremeRecharge.entity.RfbExtremeRecharge;
import com.junyou.bus.extremeRecharge.filter.RfbExtremeRechargeFilter;
import com.junyou.bus.recharge.export.RechargeExportService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 
 * @description:热发布至尊充值 
 *
 *	@author ChuBin
 *
 * @date 2016-11-24
 */
@Service
public class RfbExtremeRechargeService {
	@Autowired
	private RfbExtremeRechargeDao rfbExtremeRechargeDao;
	@Autowired
	private RechargeExportService rechargeExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private EmailExportService emailExportService;
	
	public Object getRefbInfo(Long userRoleId, Integer subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null || !configSong.isRunActivity()) {
            return null;
        }
        
        //int(领取状态，0未领取,1可领未领,2已领) 
        int receiveStatus =0;
        RfbExtremeRechargeConfig config =RfbExtremeRechargeConfigExportService.getInstance().loadBySubId(subId);
        if (config == null) {
            return null;
        }
        
        int yb =getRechargeYbNum(userRoleId, configSong);
        RfbExtremeRecharge  info = getRfbExtremeRecharge(userRoleId, subId);
        if(info.getCount() !=0){
        	receiveStatus =2;
        }else if (yb >=config.getYb()) {
        	receiveStatus =1;
		}
        
		return new Object[]{
				config.getRes(),
				config.getAni(),
				yb,
				config.getYb(),
				config.getZlpus(),
				config.getItem(),
				receiveStatus,
		};
	}
	
	public Object[] receiveReward(Long userRoleId, Integer version, Integer subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null) {
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
        
        RfbExtremeRechargeConfig  config = RfbExtremeRechargeConfigExportService.getInstance().loadBySubId(subId);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        
        if(config.getYb() ==null){
        	return AppErrorCode.CONFIG_ERROR;
        }
        
        int yb = getRechargeYbNum(userRoleId, configSong);
        if(yb <config.getYb()){
        	return AppErrorCode.YUANBAO_NOT_ENOUGH;
        }
        RfbExtremeRecharge info  = getRfbExtremeRecharge(userRoleId, subId);
        
        if(info.getCount() !=0){
        	return AppErrorCode.GET_ALREADY;
        }
        
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBagVo(config.getJiangItem(), userRoleId);
		if(bagCheck != null){
			//背包满了用邮件发送
			sendToEmail(userRoleId, config);
		}else{
			//发放奖励
			roleBagExportService.putGoodsVoAndNumberAttr(config.getJiangItem(), userRoleId, GoodsSource.EXTREME_RECHARGE_RECEIVE, LogPrintHandle.GET_EXTREME_RECHARGE_ITEM, LogPrintHandle.GBZ_EXTREME_RECHARGE_ITEM, true);
		}
		
		//更新信息
		info.setCount(info.getCount()+1);
		info.setUpdateTime(GameSystemTime.getSystemMillTime());
		rfbExtremeRechargeDao.cacheUpdate(info, userRoleId);
		UserRole userRole =	roleExportService.getUserRole(userRoleId);
		
		//发送全服公告
		sendNotice(config, userRole);
		return new Object[]{AppErrorCode.SUCCESS,subId};
	}

	private void sendNotice(RfbExtremeRechargeConfig config, UserRole userRole) {
		String goodsName = "";
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(config.getItem());
		if(goodsConfig !=null){
			goodsName = goodsConfig.getName();
		}
		BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT2, new Object[]{AppErrorCode.EXTREME_RECHARGE_REVEIVE, new Object[]{userRole.getName(),goodsName}});
	}

	private void sendToEmail(Long userRoleId, RfbExtremeRechargeConfig config) {
		String title = EmailUtil.getCodeEmail(AppErrorCode.EXTREME_RECHARGE_EMAIL_REVEIVE_TITLE);
		String content = EmailUtil
				.getCodeEmail(AppErrorCode.EXTREME_RECHARGE_EMAIL_REVEIVE);
		String[] attachment = EmailUtil.getAttachmentsForGoodsVo(config
				.getJiangItem());
		for (String e : attachment) {
			emailExportService.sendEmailToOne(userRoleId,title, content,
					GameConstants.EMAIL_TYPE_SINGLE, e);
		}
	}
	
	private int getRechargeYbNum(Long userRoleId, ActivityConfigSon configSong) {
		return rechargeExportService.getTotalRechargesByTime(
				userRoleId,
				configSong.getStartTimeByMillSecond(),
				configSong.getEndTimeByMillSecond());
	}
	
	public List<RfbExtremeRecharge> initRfbExtremeRecharge(Long userRoleId) {
		return rfbExtremeRechargeDao.initRfbExtremeRecharge(userRoleId);
	}
	
	
	 /**
     * 获取玩家活动数据
     * 
     * @param userRoleId
     * @param subId
     * @return
     */
    private RfbExtremeRecharge getRfbExtremeRecharge(Long userRoleId, int subId) {
        RfbExtremeRecharge entity = null;
        List<RfbExtremeRecharge> list = rfbExtremeRechargeDao.cacheLoadAll(userRoleId, new RfbExtremeRechargeFilter(subId));
        long nowTime = GameSystemTime.getSystemMillTime();
        if (list == null || list.size() <= 0) {
            entity = new RfbExtremeRecharge();
            entity.setCount(0);
            entity.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            entity.setSubId(subId);
            entity.setUserRoleId(userRoleId);
            entity.setUpdateTime(nowTime);
            entity.setCreateTime(new Timestamp(nowTime));
            rfbExtremeRechargeDao.cacheInsert(entity, userRoleId);
        } else {
            entity = list.get(0);
        }
        return entity;
    }

    
    //[0:int(子活动的全局ID),1:int(已充值金额),2:int(领取状态，0未领取,1可领未领,2已领)]
	public Object getRFbExtremeRechargeStates(Long userRoleId, int subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null || !configSong.isRunActivity()) {
            return null;
        }
        
		//int(领取状态，0未领取,1可领未领,2已领) 
        int receiveStatus =0;
        RfbExtremeRechargeConfig config =RfbExtremeRechargeConfigExportService.getInstance().loadBySubId(subId);
        if (config == null) {
            return null;
        }
        
        int yb =getRechargeYbNum(userRoleId, configSong);
        RfbExtremeRecharge  info = getRfbExtremeRecharge(userRoleId, subId);
        if(info.getCount() !=0){
        	receiveStatus =2;
        }else if (yb >=config.getYb()) {
        	receiveStatus =1;
		}
        
        return new Object[]{subId,yb,receiveStatus};
	}
	
}
