package com.junyou.bus.mall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.mall.configure.export.ReFaBuShangChengBiaoConfig;
import com.junyou.bus.mall.configure.export.ReFaBuShangChengBiaoConfigExportService;
import com.junyou.bus.mall.configure.export.SuiShenShangDianBiaoConfigExportService;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.MallBuyLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;

@Service
public class MallService{

	@Autowired
	private ReFaBuShangChengBiaoConfigExportService reFaBuShangChengBiaoConfigExportService;
	@Autowired
	private SuiShenShangDianBiaoConfigExportService suiShenShangDianBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	
	public Object[] shopList(Long userRoleId, String version) {
		
		String sign = reFaBuShangChengBiaoConfigExportService.getReFaBuSign();
		if(sign == null){
//			sign = "test";
			return null;
		}
		
		//版本比较
		if(sign.equals(version)){
			return new Object[]{0};
		}else{
			Object datas = reFaBuShangChengBiaoConfigExportService.shopAllCompress();
			return new Object[]{AppErrorCode.SUCCESS, sign, datas};
		}
	}

	public Object[] buyStoreGoods(Long userRoleId, Integer id,Integer clientCount) {
		ReFaBuShangChengBiaoConfig wpConfig = null;
		if(id < GameConstants.RFB_MALL_ID){
			wpConfig = suiShenShangDianBiaoConfigExportService.loadById(id);
		}else {
			wpConfig = reFaBuShangChengBiaoConfigExportService.loadById(id);
		}
		if(wpConfig == null){
			return AppErrorCode.MALLPZ_ERROR;
		}
		
		String goodsId = wpConfig.getSellid();
		GoodsConfig goodsConfig = getGoodsConfigById(goodsId);
		if(goodsConfig == null){
			return AppErrorCode.NO_GOODS;
		}
		
		int accountType = wpConfig.getMoneytype();
		if(accountType != GoodsCategory.MONEY && accountType != GoodsCategory.GOLD && accountType != GoodsCategory.BGOLD){
			return AppErrorCode.ACCOUNT_TYPE_ERROR;
		}
		//判断钱是足够
		int costValue = wpConfig.getPrice() * clientCount;
		
		//数据异常
		if(costValue <= 0 || costValue / clientCount != wpConfig.getPrice()){
			return AppErrorCode.MALLPZ_ERROR;
		}
		int count = wpConfig.getCount() * clientCount;
		//数据异常
		if(count <= 0){
			return AppErrorCode.MALLPZ_ERROR;
		}
		
		Object[] result = accountExportService.isEnought(accountType, costValue, userRoleId);
		if(result != null){
			return result; 
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodsId, count, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//消耗货币
		accountExportService.decrCurrencyWithNotify(accountType, costValue, userRoleId, LogPrintHandle.CONSUME_MALL, true, LogPrintHandle.CBZ_MALL);
		//腾讯OSS消费上报
		if(PlatformConstants.isQQ()){
			if(accountType == GoodsCategory.GOLD){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,costValue,goodsId,goodsConfig.getName(),count});
			}else if(accountType == GoodsCategory.BGOLD){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,costValue,goodsId,goodsConfig.getName(),count});
			}
		}
		//添加物品到背包
		RoleItemInput goods = BagUtil.createItem(goodsId, count, 0);
		roleBagExportService.putInBag(goods, userRoleId, GoodsSource.MALL_BUY, true);
		
		//打印日志
		printMallLog(userRoleId, goodsId, count, accountType, costValue);
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.SHANGCHENG_BUY, null});
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
		return new Object[]{AppErrorCode.SUCCESS,new Object[]{id, costValue}};
	}
	
	private GoodsConfig getGoodsConfigById(String itemId){
		return goodsConfigExportService.loadById(itemId);
	}
	
	/**
	 * 打印日志 
	 * @param userRoleId
	 * @param goodsId
	 * @param goodsCount
	 * @param moneyType
	 * @param price
	 */
	private void printMallLog(Long userRoleId,String goodsId,int goodsCount,int moneyType,int price){
		try {
			String roleName = null;
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			if(role != null){
				roleName = role.getName();
			}else{
				roleName = "-";
			}
			
			//抛出商城购买日志
			GamePublishEvent.publishEvent(new MallBuyLogEvent(userRoleId, roleName, goodsId, goodsCount, moneyType, price));
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
}
