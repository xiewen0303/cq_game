package com.junyou.public_.trade.service;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.TradesRollback;
import com.junyou.bus.bag.dao.RoleBagDao;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.TradeLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.trade.TradeData;
import com.junyou.public_.trade.TradeParam;
import com.kernel.spring.container.DataContainer;
import com.kernel.sync.annotation.PublicSyncClass;

/**
 * 交易数据交换
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-16 下午5:42:18
 */
@Component
@PublicSyncClass(component = GameConstants.COMPONENET_TREAD_NAME)
public class TradeChangeService {

	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleBagDao roleBagDao;
	
	public void tradeChangeData(Long userRoleId,Long otherRoleId,TradeParam tradeParam){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
		TradeData otherData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString());
		
		//1.验证双方交易状态必须是[回馈了双方都确认]状态
		if(selfData.getState() != GameConstants.FREED_BACK_TRADE){
			tradeParam.setErrorCode(new Object[]{AppErrorCode.ROLE_STATE_TRADE_ERROR,selfData.getSelfName()});
			tradeParam.setSuccess(false);
			return;
		}
		if(otherData.getState() != GameConstants.FREED_BACK_TRADE){
			tradeParam.setErrorCode(new Object[]{AppErrorCode.ROLE_STATE_TRADE_ERROR,otherData.getSelfName()});
			tradeParam.setSuccess(false);
			return;
		}
		
		//元宝最后验证
		if(!checkMoney(selfData, otherData)){
			tradeParam.setErrorCode(new Object[]{AppErrorCode.TRADE_EXCEPTION});
			tradeParam.setSuccess(false);
			return;
		}
		
		tradeParam.setSelfName(selfData.getSelfName());
		tradeParam.setOtherName(otherData.getSelfName());
		
		//3.物品入背包【物品交易】,这里不考虑guid对应的数量，
		
		List<Long> selfGoods=null;
		if(selfData.getGoodsGuids() != null){
			selfGoods = new ArrayList<>(selfData.getGoodsGuids().keySet());
		}
		List<Long> targetGoods=null;
		if(otherData.getGoodsGuids() != null){
			targetGoods=new ArrayList<>(otherData.getGoodsGuids().keySet());	
		}
		
		TradesRollback tradesRollBack = roleBagExportService.changeGoods(selfGoods, targetGoods, userRoleId, otherRoleId, GoodsSource.TRADE, true);
		if(!tradesRollBack.isSuccee()){
			//数据回滚 
			roleBagExportService.rollBackTrades(tradesRollBack);
			
			tradeParam.setErrorCode(new Object[]{AppErrorCode.GOODS_TRADE_ERROR});
			return;
		}
		
		//4.金钱交易
		Object[] moneyData = changeMoney(selfData, otherData);
		Long selfMoney = (Long) moneyData[0];
		Long otherMoney = (Long) moneyData[1];
		
		tradeParam.setSelfMoney(selfMoney);
		tradeParam.setOtherMoney(otherMoney);
		
		//清理两个人的TradeData数据
		dataContainer.removeData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
		dataContainer.removeData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString());
		
		Object[] selfClientParams = getClientGoodsParam(otherData.getGoodsCounts(),userRoleId);
		Object[] otherClientParams = getClientGoodsParam(selfData.getGoodsCounts(),otherRoleId);
		
		tradeParam.setSelfClientParams(selfClientParams);
		tradeParam.setOtherClientParams(otherClientParams);
		
		tradeParam.setSuccess(true);
		 
		//记录日志
		GamePublishEvent.publishEvent(new TradeLogEvent(userRoleId, selfData.getSelfName(), otherRoleId, otherData.getSelfName(), getLogGoodsParam(selfData.getGoodsCounts(), otherRoleId), getLogGoodsParam(otherData.getGoodsCounts(), userRoleId), selfData.getYb(), otherData.getYb()));
	}
	
	
	/**
	 * 输出客户端需要的参数
	 * @param goods
	 * @return
	 */
	private Object[] getClientGoodsParam(Map<Long,Integer> goods,long userRoleId){
		Object[] result = null;
		if(goods != null && goods.size() > 0){
			result = new Object[goods.size()*2];
			
			int index = 0;
			for (Entry<Long,Integer> entry : goods.entrySet()) {
				RoleItem  roleItem = roleBagDao.cacheLoad(entry.getKey(), userRoleId);
				result[index++] = roleItem.getGoodsId();
				result[index++] = goods.get(entry.getKey());
			}
		}
		
		return result;
	}
	
	/**
	 * 交易日志记录需要的参数
	 * @param goods
	 * @return
	 */
	private JSONArray getLogGoodsParam(Map<Long,Integer> goods,long userRoleId){
		JSONArray result = new JSONArray();
		if(goods != null && goods.size() > 0){
			List<Map<String,Object>> tempData = new ArrayList<>();
			for (Entry<Long,Integer> entry : goods.entrySet()) {
				RoleItem  roleItem = roleBagDao.cacheLoad(entry.getKey(), userRoleId);
				Map<String,Object> formatData = new HashMap<>();
				formatData.put("goodsId", roleItem.getGoodsId());
				formatData.put("count", roleItem.getCount());
				formatData.put("guid", roleItem.getId());
				tempData.add(formatData);
			}
			result.addAll(tempData);
		}
		return result;
	}
	
	
	/**
	 * 最后数据交换前验证元宝
	 * @param selfData
	 * @param otherData
	 * @return
	 */
	private boolean checkMoney(TradeData selfData,TradeData otherData){
		
		long selfRoleId = selfData.getUserRoleId();
		long otherRoleId = otherData.getUserRoleId();
		
		synchronized (GameConstants.TRADE_YB_LOCK) {
			if(selfData.getYb() > 0 && null != accountExportService.isEnoughtValue(GoodsCategory.GOLD, selfData.getYb(), selfRoleId)){
				return false;
			}
			
			if(otherData.getYb() > 0 && null != accountExportService.isEnoughtValue(GoodsCategory.GOLD, otherData.getYb(),otherRoleId)){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 交易货币
	 * @param selfData
	 * @param otherData
	 * @return [0:selfMoney,1:otherMoney]
	 */
	private Object[] changeMoney(TradeData selfData,TradeData otherData ){
		
		long selfRoleId = selfData.getUserRoleId();
		long otherRoleId = otherData.getUserRoleId();
		 
		if(selfData.getYb() > 0){
			//自己的
			accountExportService.decrCurrencyForTradeWithNotify(GoodsCategory.GOLD, selfData.getYb(), selfRoleId, LogPrintHandle.CONSUME_TRADE, false, LogPrintHandle.CBZ_TRADE);
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(selfData.getUserRoleId(), InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,(int)selfData.getYb(),LogPrintHandle.CONSUME_TRADE,QQXiaoFeiType.CONSUME_TRADE,1});
			}
			//对方的
			accountExportService.incrCurrencyWithNotify(GoodsCategory.GOLD, selfData.getYb(), otherRoleId, LogPrintHandle.GET_TRADE, LogPrintHandle.GBZ_TRADE);
		}
		if(otherData.getYb() > 0){
			//对方的
			accountExportService.decrCurrencyForTradeWithNotify(GoodsCategory.GOLD, otherData.getYb(), otherRoleId, LogPrintHandle.CONSUME_TRADE, false, LogPrintHandle.CBZ_TRADE);
			//自己的
			accountExportService.incrCurrencyWithNotify(GoodsCategory.GOLD, otherData.getYb(), selfRoleId, LogPrintHandle.GET_TRADE, LogPrintHandle.GBZ_TRADE);
		}
		
		return new Object[]{selfData.getYb(),otherData.getYb()};
	}
}
