package com.junyou.public_.trade;

import com.junyou.bus.bag.BagOutputWrapper;
import com.junyou.bus.bag.OutputType;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.role.export.RoleWrapper;
 
public class TradeOutPutWrapper {

	public static Object[] getTradeRoleBaseInfo(RoleWrapper role){
//		int orleJob=BusConfigureHelper.getJobId(role.getConfigId());
		
		return new Object[]{role.getId(),role.getName(),role.getLevel(),role.getConfigId()};
	}
	
	public static Object[] modifyGoods(int tradeIndex,RoleItemExport goods,int count){
		if(goods!= null){
			return new Object[]{tradeIndex,BagOutputWrapper.getOutWrapperData(OutputType.EXPORT_TO_ITEMVO, goods)};
		}
		return new Object[]{tradeIndex,null};
	}
	
	public static Object[] finishTrade(Object[] clientParams,Long moneyData,String name){
		return new Object[]{name,clientParams,moneyData};
	}
	
}
