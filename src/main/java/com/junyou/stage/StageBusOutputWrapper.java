package com.junyou.stage;

import com.junyou.stage.model.element.goods.Goods;

/**
 * @description 业务信息输出
 * @author ShiJie Chi
 * @date 2012-3-26 下午3:00:30 
 */
public class StageBusOutputWrapper {

	/**
	 * 传送到业务的goods格式
	 */
	public static Object goods(Goods goods, int itemType, boolean isRecord, String userName) {
		//TODO  
		return null;
//		
//		JSONArray uniqueList = goods.getUniqueList();
//		
//		if(isRecord){
//			uniqueList = ItemType.setUnqiueList(uniqueList, itemType, goods.getUniqueId() + ":" + goods.getCount(), null, userName);
//		}
//		
//		return new Object[]{
//				goods.getId(),
//				goods.getGoodsId(),
//				goods.getCount(),
//				goods.getGoodsLevel(),
//				goods.getRareLevel(),
//				goods.getExpireTime(),
//				goods.isBind(),
//				goods.getAttributes(),
//				goods.getJinyouLevel(),
//				goods.getQianhuaLevel(),
//				goods.getXiLian(),
//				goods.getItemType(),
//				goods.getItemCreateTime(),
//				goods.getUniqueId(),
//				uniqueList,
//				goods.getPtJianding1(),
//				goods.getPtJianding2(),
//				goods.getGjJianding1(),
//				goods.getGjJianding2(),
//				goods.getZzJianding1(),
//				goods.getZzJianding2()
//		};
	}

}
