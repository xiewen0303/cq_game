package com.junyou.bus.shoplimit.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.shoplimit.ShopLimitConstants;
import com.junyou.bus.shoplimit.configure.export.XianShiLiBaoConfig;
import com.junyou.bus.shoplimit.configure.export.XianShiLiBaoConfigExportService;
import com.junyou.bus.shoplimit.dao.ShopLimitInfoDao;
import com.junyou.bus.shoplimit.entity.ShopLimitInfo;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.OpenPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.common.ObjectUtil;

/**
 * 限时礼包
 * @author  作者：wind
 * @version 创建时间：2017-10-17 上午10:40:59
 */
@Service
public class ShopLimitService{

	@Autowired
	private ShopLimitInfoDao shopLimitInfoDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private XianShiLiBaoConfigExportService limitShopConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private EmailExportService emailExportService;
	/**
	 * 限时礼包
	 * @param userRoleId
	 * @return
	 */
	public Object[] getLimitLibao(Long userRoleId) {
		ShopLimitInfo info = getShopLimit(userRoleId);
		if(info == null){
			ChuanQiLog.error("not open LimitLibao");
			return AppErrorCode.FUNCTION_NOT_OPEN;
		}
		if(isOver(userRoleId)){
			ChuanQiLog.error("is over");
			return AppErrorCode.TERRITORY_ACTIVE_END;
		}
		
		//设置下个
		nextInfo(userRoleId);
		
		XianShiLiBaoConfig  config = limitShopConfigExportService.loadById(info.getConfigId());
		return new Object[]{1,info.getConfigId(),info.getOpenTime() + config.getTotalTime(),info.getState(),info.getRechargeTotal()};
	}
	
	/**
	 * 判断是否活动结束
	 * @return
	 */
	private boolean isOver(long userRoleId){
		XianShiLiBaoConfig config = limitShopConfigExportService.loadById(limitShopConfigExportService.getMaxConfigId());
		if(config == null){
			ChuanQiLog.error("isOver config is not exits");
			return false;
		}
		
		ShopLimitInfo shopLimitInfo = getShopLimit(userRoleId);
		
		if(shopLimitInfo.getOpenTime() + config.getTotalTime() < System.currentTimeMillis() ){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param userRoleId
	 * @param buyId 
	 * @return
	 */
	public Object[] buyShopLimit(Long userRoleId, int buyId){
		ShopLimitInfo info = getShopLimit(userRoleId);
		if(info == null){
			ChuanQiLog.error("not open LimitLibao!");
			return AppErrorCode.FUNCTION_NOT_OPEN;
		}
		int configId = info.getConfigId();
		XianShiLiBaoConfig config = limitShopConfigExportService.loadById(configId);
		if(config ==  null){
			ChuanQiLog.error("buyShopLimit config is not exits!");
			return AppErrorCode.NO_FIND_CONFIG;
		}
		//check time
		long nowTime = System.currentTimeMillis();
		if(config.getTotalTime() + info.getOpenTime() < nowTime){
			ChuanQiLog.error("buyShopLimit config is outtime,activityTime:"+config.getTotalTime() + info.getOpenTime()+"\tnowTime"+nowTime);
			return AppErrorCode.LIMIT_BUY_GQ;
		}
		
		if(info.getState() == ShopLimitConstants.STATE_GET){
			return AppErrorCode.LIMIT_BUY_YGM;
		}
		
		if(config.getType() == ShopLimitConstants.GOLD_BUY){
			
			Map<String,Integer> jiangli = config.getJiangli();
			if(!ObjectUtil.isEmpty(jiangli)){
				Object[] obj = roleBagExportService.checkPutGoodsAndNumberAttr(config.getJiangli(), userRoleId);
				if(obj != null){
					return obj;
				}
			}
			
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getNeedMoney(), userRoleId, LogPrintHandle.CONSUME_LIMITLB, true, LogPrintHandle.CBZ_XIANSHILIBAO);
			if(result != null){
				return result;
			}
			//发送奖励
			if(!ObjectUtil.isEmpty(jiangli)){
				roleBagExportService.putGoodsAndNumberAttr(config.getJiangli(), userRoleId, GoodsSource.LIMIT_LIBAO, LogPrintHandle.GBZ_ZADAN, LogPrintHandle.GBZ_LIMIT_SHOP, true);
			}
		}else if(config.getType() == ShopLimitConstants.GOLD_RECHARGE){
			if(info.getState() == ShopLimitConstants.STATE_NO){
				return AppErrorCode.LIMIT_BUY_CZBZ;
			}
			
			Map<String,Integer> jiangli = config.getJiangli();
			if(!ObjectUtil.isEmpty(jiangli)){
				Object[] obj = roleBagExportService.checkPutGoodsAndNumberAttr(config.getJiangli(), userRoleId);
				if(obj != null){
					return obj;
				}
				//发送奖励
				roleBagExportService.putGoodsAndNumberAttr(config.getJiangli(), userRoleId, GoodsSource.LIMIT_LIBAO, LogPrintHandle.GBZ_ZADAN, LogPrintHandle.GBZ_LIMIT_SHOP, true);
			}
			
		}else{
			ChuanQiLog.error("limit shop type is error!");
			return AppErrorCode.LIMIT_BUY_TYPE_ERROR;
		}
		
		info.setState(ShopLimitConstants.STATE_GET);
		shopLimitInfoDao.cacheUpdate(info, userRoleId);
		
		return new Object[]{1,configId};
	}
	
	/**
	 * 检查开启限时礼包
	 * @param userRoleId
	 */
	public void checkOpen(Long userRoleId) {
		OpenPublicConfig openPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.XIAN_SHI_LI_BAO);
		if(openPublicConfig == null){
			ChuanQiLog.error("public config is not exits!");
			return;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		
		if (openPublicConfig.getOpen() > role.getLevel()){
			return;
		}
		
		ShopLimitInfo shopLimitInfo = getShopLimit(userRoleId);
		if(shopLimitInfo == null){
			shopLimitInfo = create(userRoleId);
		}
		XianShiLiBaoConfig xianShiLiBaoConfig = limitShopConfigExportService.loadById(shopLimitInfo.getConfigId());
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TUISONG_INFO, new Object[]{shopLimitInfo.getConfigId(),shopLimitInfo.getOpenTime()+ xianShiLiBaoConfig.getTotalTime(),shopLimitInfo.getState()});
	}
	
	private ShopLimitInfo create(long userRoleId){
		int configId = limitShopConfigExportService.getMinConfigId();
		ShopLimitInfo shoplimtInfo = new ShopLimitInfo();
		shoplimtInfo.setConfigId(configId);
		shoplimtInfo.setOpenTime(System.currentTimeMillis());
		shoplimtInfo.setRechargeTotal(0L);
		shoplimtInfo.setState(0);
		shoplimtInfo.setUserRoleId(userRoleId);
		
		shopLimitInfoDao.cacheInsert(shoplimtInfo, userRoleId);
		return shoplimtInfo;
	}
	
	public void onlineHandle(Long userRoleId){
		ShopLimitInfo shopLimitInfo = getShopLimit(userRoleId);
		if(shopLimitInfo == null){
			return;
		}
		
		long nowTime = System.currentTimeMillis();
		
		if(isOver(userRoleId)){
			ChuanQiLog.error("activity is over");
			return;
		}
		
		int configId = shopLimitInfo.getConfigId();
		XianShiLiBaoConfig config =  limitShopConfigExportService.loadById(configId);
		
		//设置下个
		if(nowTime >= shopLimitInfo.getOpenTime() + config.getTotalTime()){
			nextInfo(userRoleId);
		}
		config =  limitShopConfigExportService.loadById(shopLimitInfo.getConfigId());
		BusMsgSender.send2One(userRoleId, ClientCmdType.TUISONG_INFO, new Object[]{shopLimitInfo.getConfigId(),shopLimitInfo.getOpenTime()+ config.getTotalTime(),shopLimitInfo.getState()});
	}
	
	private void nextInfo(long userRoleId){
		ShopLimitInfo shopLimitInfo = getShopLimit(userRoleId);
		if(shopLimitInfo == null){
			ChuanQiLog.error("getNextInfo error,old is not exits!");
			return;
		}
		long nowTime = System.currentTimeMillis();
		
		XianShiLiBaoConfig configNow =  limitShopConfigExportService.loadById(shopLimitInfo.getConfigId());
		if(nowTime < shopLimitInfo.getOpenTime() + configNow.getTotalTime()){
			return;
		}
		
		for (int i = limitShopConfigExportService.getMinConfigId(); i <= limitShopConfigExportService.getMaxConfigId(); i++) {
			XianShiLiBaoConfig config = limitShopConfigExportService.loadById(i);
			if(config == null){
				ChuanQiLog.error("getNextInfo config id is not exits!");
				continue;
			}
			if(shopLimitInfo.getOpenTime() + config.getTotalTime() >  nowTime  && shopLimitInfo.getConfigId() != i){
				if(shopLimitInfo.getState() == ShopLimitConstants.STATE_YES){
					sendEmail(shopLimitInfo);
				}
				shopLimitInfo.setConfigId(i);
				shopLimitInfo.setRechargeTotal(0L);
				shopLimitInfo.setState(0);
				shopLimitInfoDao.cacheUpdate(shopLimitInfo, userRoleId);
				break;
			}
		}
	}
	
	private void sendEmail(ShopLimitInfo shopLimitInfo){
		XianShiLiBaoConfig config = limitShopConfigExportService.loadById(shopLimitInfo.getConfigId());
		if(config == null){
			ChuanQiLog.error("getNextInfo config id is not exits!");
			return;
		}
		if(config.getType() == ShopLimitConstants.GOLD_RECHARGE){
			if(shopLimitInfo.getState()  == ShopLimitConstants.STATE_NO ){
				String content = EmailUtil.getCodeEmail(GameConstants.XIANSHILIBAO_TITLE);
				String[] attachments = EmailUtil.getAttachments(config.getJiangli());
				String title = EmailUtil.getCodeEmail(GameConstants.XIANSHILIBAO_CONTET);
				for (String attachment : attachments) {
					emailExportService.sendEmailToOne(shopLimitInfo.getUserRoleId(), title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
				}
			}
		}
	}

	private ShopLimitInfo getShopLimit(Long userRoleId) {
		return shopLimitInfoDao.cacheLoad(userRoleId, userRoleId);
	}
	
	
	
	public void rechargeYb(Long userRoleId,Long addVal11, Map<String, Long[]> rechargeDatas){
		if(addVal11 < 0){
			return;
		}
		
		ShopLimitInfo shopLimitInfo = getShopLimit(userRoleId);
		if(shopLimitInfo == null){
			return;
		}
		if(isOver(userRoleId)){
			return;
		}
		int configId = shopLimitInfo.getConfigId();
		XianShiLiBaoConfig config = limitShopConfigExportService.loadById(configId);
		if(config == null){
			ChuanQiLog.error("config is not exits,configId"+configId);
			return;
		}
		
		long endTime = config.getTotalTime() + shopLimitInfo.getOpenTime();
		long beginTime = config.getTotalTime() + shopLimitInfo.getOpenTime() - config.getTime();
		
		long realGold = 0;
		for (Long[] rechargeData : rechargeDatas.values()) {
			if( beginTime <= rechargeData[0] && endTime >=  rechargeData[0]){
				realGold  += rechargeData[1];
			}
		}
		
		if(config.getType() != ShopLimitConstants.GOLD_RECHARGE){
			return;
		}
		
		shopLimitInfo.setRechargeTotal(shopLimitInfo.getRechargeTotal() + realGold);
		if(shopLimitInfo.getState() == ShopLimitConstants.STATE_NO ){
			if(shopLimitInfo.getRechargeTotal() >= config.getNeedMoney()){
				shopLimitInfo.setState(ShopLimitConstants.STATE_YES);
			}
		}
		
		shopLimitInfoDao.cacheUpdate(shopLimitInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEIJI_GOLD, shopLimitInfo.getRechargeTotal());
	}


	public ShopLimitInfo initLimitShopInfo(Long userRoleId) {
		return shopLimitInfoDao.initShopLimitInfo(userRoleId);
	}
}