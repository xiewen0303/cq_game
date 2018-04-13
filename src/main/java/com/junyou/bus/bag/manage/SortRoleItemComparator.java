package com.junyou.bus.bag.manage;

import java.text.NumberFormat;
import java.util.Comparator;

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
/**
 * 物品整理排序
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-12-30上午10:28:49
 *@Description: 
 */
public class SortRoleItemComparator implements Comparator<RoleItem> {

	@Override
	public int compare(RoleItem roleItem1, RoleItem roleItem2) {
		if(roleItem1 == null || roleItem2 == null){
			return 0;
		}
		
		long order1=getOrderValue(roleItem1);
		long order2=getOrderValue(roleItem2);
		if(order1>order2){
			return -1;
		}else if(order1<order2){
			return 1;
		}
		return 0;
	}
	
	/**
	 *  [类型3位][品级2位][部位2位][等级3位]
	 * @param roleItem
	 * @return
	 */
	private long getOrderValue(RoleItem roleItem){
		StringBuffer sb=new StringBuffer();
		
		GoodsConfig goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(roleItem.getGoodsId());
		int type=goodsConfig.getCategory();
		Integer typeOrder=BusConfigureHelper.getGoodsTypeOrderConfigExportService().getOrderByType(type+"");
	 
		typeOrder=typeOrder!=null?BagUtil.getMaxValueByNumber(3)-typeOrder:0;
		//类型
		sb.append(getStringValue(typeOrder, 3));
		
		//品质
		int rareLevel=goodsConfig.getRareLevel();
		rareLevel=BagUtil.getMaxValueByNumber(2)-rareLevel;
		sb.append(getStringValue(rareLevel, 2));
		
		//部位
		int eqpart=goodsConfig.getEqpart();
		int eqpartOrder=0;
		if(eqpart!=0){
			Integer order=BusConfigureHelper.getEquipOrderConfigExportService().getEqpartOrders(eqpart);
			eqpartOrder = order!=null ? BagUtil.getMaxValueByNumber(2)-order:0;
		}
		sb.append(getStringValue(eqpartOrder, 2));
		
		//等级
		int levelReq=goodsConfig.getLevelReq();
		sb.append(getStringValue(BagUtil.getMaxValueByNumber(3)-levelReq, 3));
		
		return Long.valueOf(sb.toString());
	}
	
	private String getStringValue(int data,int size){
		NumberFormat formatter = NumberFormat.getNumberInstance();     
		formatter.setMinimumIntegerDigits(size);     
		formatter.setGroupingUsed(false);     
		return formatter.format(data);
	}
	 
}
