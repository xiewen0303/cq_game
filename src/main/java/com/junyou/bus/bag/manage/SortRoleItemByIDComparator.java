package com.junyou.bus.bag.manage;

import java.util.Comparator;

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;

/**
 * 根据物品Id排序
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-14 下午6:15:09
 */
public class SortRoleItemByIDComparator implements Comparator<RoleItem> {

	@Override
	public int compare(RoleItem o1, RoleItem o2) {
		if(o1 == null || o2 == null){
			return 0;
		}
		
		GoodsConfig goods1=BusConfigureHelper.getGoodsConfigExportService().loadById(o1.getGoodsId());
		GoodsConfig goods2=BusConfigureHelper.getGoodsConfigExportService().loadById(o2.getGoodsId());

		if(goods1 == null || goods2 == null){
			return 0;
		}
			
		//类型
		int type1=goods1.getCategory();
		int type2=goods2.getCategory();
		if(type1!=type2){
			return 0;
		}
		
		//品质
		int rareLevel1=goods1.getRareLevel(); 
		int rareLevel2=goods2.getRareLevel(); 
		if(rareLevel1!=rareLevel2){
			return 0;
		}
		//部位
		int eqpart1=goods1.getEqpart();
		int eqpart2=goods2.getEqpart();
		if(eqpart1!=eqpart2){
			return 0;
		}
		
		//等级
		int itemLevel1=goods1.getItemLevel();
		int itemLevel2=goods2.getItemLevel();
		if(itemLevel1!=itemLevel2){
			return 0;
		}
		
		if(goods1.getId().compareTo(goods2.getId())>0){
			return 1;
		}else if(goods1.getId().compareTo(goods2.getId())<0){
			return -1;
		}
		
		return 0;
	}
}
