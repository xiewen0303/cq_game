package com.junyou.bus.bag.manage;

import java.util.Comparator;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
/**
 * 物品消耗排序
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-12-30上午10:28:49
 *@Description: 
 */
public class SortRemoveComparator implements Comparator<RoleItem> {

	@Override
	public int compare(RoleItem roleItem1, RoleItem roleItem2) {
		if(roleItem1 == null || roleItem2== null){
			return 0;
		}
		
		GoodsConfig goods1=BusConfigureHelper.getGoodsConfigExportService().loadById(roleItem1.getGoodsId());
		GoodsConfig goods2=BusConfigureHelper.getGoodsConfigExportService().loadById(roleItem2.getGoodsId());
		
		if(goods1 == null || goods2 == null){
			return 0;
		}
		
		//限时的在前，非限时的在后
		if(roleItem1.getExpireTime() >0|| roleItem2.getExpireTime()>0){
			if(roleItem1.getExpireTime()>roleItem2.getExpireTime()){
				return 1;
			}else if(roleItem1.getExpireTime()<roleItem2.getExpireTime()){
				return -1;
			}else{
				return 0;
			}
		}
		
		//绑定的在前，非绑定的在后
		if(goods1.getBangding() > goods2.getBangding()){
			return -1;
		}else if(goods1.getBangding() == goods2.getBangding()){
			return 0;
		}else{
			return 1;
		}
	}
}
