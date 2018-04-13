package com.junyou.gameconfig.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 配置解析工具类
 * @author DaoZheng Yuan
 * 2015年1月28日 下午4:56:33
 */
public class ConfigAnalysisUtils {

	/**
	 * 解物品配置信息，所有物品不含强化信息，格式为"id1:数量|id2:数量"
	 * @param configAttName
	 * @return
	 */
	public static Map<String,Integer> getConfigMap(String configAttName){
		Map<String,Integer> rewardMap = null;
		
		if(!CovertObjectUtil.isEmpty(configAttName)){
			
			rewardMap = new HashMap<String, Integer>();
			
			String[] rewardItems = configAttName.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
			for (String string : rewardItems) {
				String[] innerItem = string.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
				
				if("".equals(string) || "".equals(innerItem[0])){
					continue;
				}
				
				if(rewardMap.containsKey(innerItem[0])){
					int count = rewardMap.get(innerItem[0]) + CovertObjectUtil.object2int(innerItem[1]);
					
					rewardMap.put(innerItem[0], count);
				}else{
					int count = CovertObjectUtil.object2int(innerItem[1]);
					
					rewardMap.put(innerItem[0], count);
				}
			}
		}
		
		return rewardMap;
	}
	
	/**
	 * 解物品配置信息，所有物品不含强化信息，格式为"id1:数量:强化等级|id2:数量:强化等级"
	 * @param configAttName
	 * @return
	 */
	public static Map<String,GoodsConfigureVo> getConfigVoMap(String configAttName){
		Map<String,GoodsConfigureVo> rewardMap = null;
		
		if(!CovertObjectUtil.isEmpty(configAttName)){
			
			rewardMap = new HashMap<String, GoodsConfigureVo>();
			
			String[] rewardItems = configAttName.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
			for (String string : rewardItems) {
				String[] innerItem = string.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
				
				if("".equals(string) || "".equals(innerItem[0])){
					continue;
				}
				
				//物品数量默认值：1
				int count = 1;
				if(innerItem.length >= 2){
					count = CovertObjectUtil.object2int(innerItem[1]);
				}
				
				//强化等级
				int qhLevel = 0;
				if(innerItem.length >= 3){
					qhLevel = CovertObjectUtil.object2int(innerItem[2]);
				}
				
				if(rewardMap.containsKey(innerItem[0])){
					GoodsConfigureVo vo = rewardMap.get(innerItem[0]);
					count = vo.getGoodsCount() + count;
					vo.setGoodsCount(count);
					if(qhLevel > 0){
						vo.setQhLevel(qhLevel);
					}
					
					rewardMap.put(innerItem[0], vo);
				}else{
					GoodsConfigureVo vo = new GoodsConfigureVo(innerItem[0], count);
					if(qhLevel > 0){
						vo.setQhLevel(qhLevel);
					}
					
					rewardMap.put(innerItem[0], vo);
				}
			}
		}
		
		return rewardMap;
	}
	
	/**
	 * 解物品配置信息，物品可包含强化信息，格式为"id1:数量:强化等级|id2:数量"
	 * @param configAttName
	 * @param goods	不含强化信息的物品map
	 * @param equipList	含强化信息的装备列表
	 * @return	所有物品的map，用于入背包时检测使用
	 */
	public static Map<String,Integer> getConfigMap(String configAttName,Map<String,Integer> goods,List<Object[]> equipList){
		Map<String,Integer> checkMap = new HashMap<>();
		if(!CovertObjectUtil.isEmpty(configAttName)){
			
			String[] rewardItems = configAttName.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
			for (String string : rewardItems) {
				String[] innerItem = string.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
				
				if("".equals(string) || "".equals(innerItem[0])){
					continue;
				}
				int count = CovertObjectUtil.object2int(innerItem[1]);
				if(innerItem.length > 2){//含强化信息
					int qh = CovertObjectUtil.object2int(innerItem[2]);
					Object[] equip = new Object[]{innerItem[0], count,qh};
					equipList.add(equip);
				}else{//不含强化信息
					if(goods.containsKey(innerItem[0])){
						goods.put(innerItem[0], goods.get(innerItem[0]) + count);
					}else{
						goods.put(innerItem[0], count);
					}
				}
				if(checkMap.containsKey(innerItem[0])){
					count = checkMap.get(innerItem[0]) + count;
				}
				checkMap.put(innerItem[0], count);
			}
		}
		return checkMap;
	}
	
	/**
	 * 把一个map添加到另外一个map，如果key重复，数量叠加
	 * @param from	来源
	 * @param target	目标
	 */
	public static void putAllMap(Map<String,Integer> from,Map<String,Integer> target){
		for (Entry<String, Integer> entry : from.entrySet()) {
			int count = entry.getValue();
			if(target.containsKey(entry.getKey())){
				count += target.get(entry.getKey());
			}
			target.put(entry.getKey(), count);
		}
	}
	
	/**
	 * 属性转换
	 * @param tmp
	 * @return
	 */
	public static Map<String, Long> setAttributeVal(Map<String, Object> tmp) {
		Map<String, Long> intMap = new HashMap<String, Long>();
		
		for (EffectType effectType : EffectType.values()) {
			long val = CovertObjectUtil.obj2long(tmp.get(effectType.name()));
			if (val > 0) {
				intMap.put(effectType.name(), val);
			}
		}
		return intMap;
	}
	
	/**
	 * 属性转换(pro\value形式)
	 * @param tmp
	 * @return
	 */
	public static Map<String, Long> setAttributeValForKV(Map<String, Object> tmp) {
		Map<String, Long> longMap = new HashMap<String, Long>();
		
		for (int i = 1; i < 100; i++) {
			String key = CovertObjectUtil.obj2StrOrNull(tmp.get("pro"+i));
			if(ObjectUtil.strIsEmpty(key)){
				break;
			}
			Long value = CovertObjectUtil.object2Long(tmp.get("value"+i));
			longMap.put(key, value);
		}
		return longMap;
	}
	
	/**
	 * 解物品配置信息，所有物品可支持强化信息，格式为"id1:数量|id2:数量"或"id1:数量:强化等级|id2:数量:强化等级"
	 * @param configAttName
	 * @return
	 */
	public static Object[] getConfigArray(String configAttName){
		List<Object[]> rewardArr = null;
		
		if(!CovertObjectUtil.isEmpty(configAttName)){
			
			rewardArr = new ArrayList<>();
			
			String[] rewardItems = configAttName.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
			for (String string : rewardItems) {
				String[] innerItem = string.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
				
				if("".equals(string) || "".equals(innerItem[0])){
					continue;
				}
				
				Object[] goodsObj = null;
				if(innerItem.length > 2){
					goodsObj = new Object[]{innerItem[0], innerItem[1], innerItem[2]};
				}else if(innerItem.length > 1){
					goodsObj = new Object[]{innerItem[0], innerItem[1]};
				}else{
					goodsObj = new Object[]{innerItem[0], 1};
				}
				rewardArr.add(goodsObj);
			}
		}
		
		if(rewardArr != null && rewardArr.size() > 0){
			return rewardArr.toArray();
		}
		return null;
	}
	
	
	public static Map<String,Integer> cover2Map(String itemStr){
		if(itemStr == null){
			return null;
		}
		Map<String,Integer> result = new HashMap<>();
		
		for (String item : itemStr.trim().split("\\|")) {
			String[] kv= item.split(":");
			result.put(kv[0].trim(),CovertObjectUtil.object2int(kv[1].trim()));
		}
		return result;
	}
}