package com.junyou.bus.bag;

import com.junyou.bus.bag.manage.AbstractContainer;
import com.junyou.bus.bag.manage.BagItems;
import com.junyou.bus.bag.manage.BodyItems;
import com.junyou.bus.bag.manage.ChongwuItems;
import com.junyou.bus.bag.manage.OtherEquipItems;
import com.junyou.bus.bag.manage.ShenQiItems;
import com.junyou.bus.bag.manage.StorageItems;
  

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午5:55:34
 *@Description: 背包，身上,仓库等容器类型
 */
public enum ContainerType {
	  /**
	   * 背包容器类型
	   */
	  BAGITEM(1,false),
	  /**
	   * 身上容器类型
	   */
	  BODYTITEM(2,true),
	  /**
	   * 仓库容器类型
	   */
	  STORAGEITEM(3,false),
	  /**
	   * 坐骑上面的物品容器
	   */
	  ZUOQIITEM(4,true),
	  /**
	   * 拍卖行
	   */
	  PAIMAI(5,false),
	  /**
	   * 翅膀上面的物品容器
	   */
	  CHIBANGITEM(6,true),
	  /**
	   * 糖宝上面的物品容器
	   */
	  TANGBAOITEM(7,true),
	  /**
	   * 天工上面的物品容器
	   */
	  TIANGONGITEM(8,true),
	  /**
	   * 天裳上面的物品容器
	   */
	  TIANSHANGITEM(9,true),
	  /**
	   * 器灵上面的物品容器
	   */
	  QILINGITEM(10,true),
	  /**
	   * 天羽上面的物品容器
	   */
	  TIANYUITEM(11,true),
	  /**
	   * 宠物上面的物品容器
	   */
	  CHONGWUITEM(12,true),
	  /**
	   * 神器上面的物品容器
	   */
	  SHENQIITEM(13,true),
	  
	  /**
	   * 新圣剑上面的物品容器
	   */
	  WUQI(14,true);
	  
	  private int type;
	  
	  /**是否是装备*/
	  private boolean equip;
	  
	  
	  private ContainerType(int type,boolean equip){
		  this.type = type;
		  this.equip = equip;
	  }
	  
	  public int getType(){
		  return type;
	  }
	  
	 public boolean isEquip() {
		 return equip;
	 }

	public static AbstractContainer getClassObj(ContainerType e){
		  AbstractContainer result=null;
			switch (e) {
			case  BAGITEM:
				result=new BagItems(); break;
			case  BODYTITEM:
				result=new BodyItems();break;
			case  STORAGEITEM:
				result=new StorageItems();break;
			case  ZUOQIITEM:
				result=new OtherEquipItems(ZUOQIITEM);break;
			case  CHIBANGITEM:
				result=new OtherEquipItems(CHIBANGITEM);break;
			case  TANGBAOITEM:
				result=new OtherEquipItems(TANGBAOITEM);break;
			case  TIANGONGITEM:
				result=new OtherEquipItems(TIANGONGITEM);break;
			case  TIANSHANGITEM:
				result=new OtherEquipItems(TIANSHANGITEM);break;
			case  QILINGITEM:
				result=new OtherEquipItems(QILINGITEM);break;
			case  TIANYUITEM:
				result=new OtherEquipItems(TIANYUITEM);break;
			case  CHONGWUITEM:
			    result=new ChongwuItems();break;
			case  SHENQIITEM:
			    result=new ShenQiItems();break;
			case  WUQI:
				result=new OtherEquipItems(WUQI);break;
			default:
				break;
			} 
		  return result;
	  }
	  
	  
}
