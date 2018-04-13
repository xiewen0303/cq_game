package com.junyou.event.util;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.OperationType;
import com.junyou.bus.bag.vo.RoleItemOperation;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.log.ChuanQiLog;

public class LogFormatUtils {
	/**
	 * 物品主键Id
	 */
	private static final String GUID ="guid";
	
	/**
	 * 物品配置Id
	 */
	private static final String GOODSID ="goodsId";
	
	/**
	 * 物品数量
	 */
	private static final String COUNT ="count";
	
	/**
	 * 强化等级
	 */
	private static final String QH = "qh";
	
	/**
	 * 转换成日志对应的格式
	 * @param bagSlots
	 * @return
	 */
	public static void parseJSONArray(BagSlots bagSlots,JSONArray result) {
		if(bagSlots == null )return;
		List<Map<String,Object>> datas = new ArrayList<>(); 
		try {
			List<RoleItemOperation> roleItemActions = bagSlots.getRoleItemVos();
			
			if(roleItemActions != null && roleItemActions.size()>0){
				
				for (RoleItemOperation roleItemAction : roleItemActions) {
					
					Map<String,Object> cellData =new  HashMap<String, Object>();
					
					cellData.put(GUID,roleItemAction.getRoleItem().getId());
					cellData.put(GOODSID, roleItemAction.getRoleItem().getGoodsId());
					
					
					
					int oldCount = roleItemAction.getOldCount();
					int newCount = roleItemAction.getRoleItem().getCount(); 
					
					int count =0;
					if(roleItemAction.getOperationType() == OperationType.DELETE){
						count = newCount;
					}else{
						count = Math.abs(newCount - oldCount); 
					}
					
					cellData.put(COUNT, count); 
					
					datas.add(cellData);
				}
				result.addAll(datas);
			}
		} catch (Exception e) {
			  ChuanQiLog.error("物品转换成日志格式异常: bagSlots:"+JSONObject.toJSONString(bagSlots), e);
		}
	}

	/**
	 * 转换成日志对应的格式
	 * @param goodsMap
	 * @param receiveItems
	 */
	public static void parseJSONArray(Map<String, Integer> goodsMap,JSONArray receiveItems) {
		 if(goodsMap == null || goodsMap.size() == 0){
			 return;
		 }
		 
		 for (Entry<String, Integer> entry : goodsMap.entrySet()) {
		 	 Map<String,Object> entity = new HashMap<>();
		 	 entity.put(GOODSID, entry.getKey());
		 	 entity.put(COUNT,entry.getValue());
		 	 receiveItems.add(entity);
		 }
	}
	/**
	 * 转换成日志对应的格式
	 * @param goodsMap
	 * @param receiveItems
	 */
	public static void parseJSONArrayVo(Map<String, GoodsConfigureVo> goodsMap,JSONArray receiveItems) {
		if(goodsMap == null || goodsMap.size() == 0){
			return;
		}
		
		for (Entry<String, GoodsConfigureVo> entry : goodsMap.entrySet()) {
			Map<String,Object> entity = new HashMap<>();
			entity.put(GOODSID, entry.getValue().getGoodsId());
			entity.put(COUNT,entry.getValue().getGoodsCount());
			entity.put(QH,entry.getValue().getQhLevel());
			receiveItems.add(entity);
		}
	}
}
