package com.junyou.bus.touzi.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * 投资计划配置 
 */
@Component
public class TouZiConfigExportService extends AbsGroupFileAbleConfigureInit{
	
	/**
	 * 投资
	 */
	private Map<Integer, TouZiConfig> configMap;
	
	/**
	 * 基金
	 */
	private Map<Integer, TouZiConfig> jiJinconfigMap;
	/**
	  * configFileName
	 */
//	private String configureName = "TouZi.jat";
	
	@Override
	protected String[] getGroupConfigureNames() {
		
		String[] configureNames = new String[]{
				"TouZi.jat"
				,"JiJin.jat"
		};

		return configureNames;
	}
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data,String configName){
		if(data == null){
			return;
		}
		
		Object[] dataList = GameConfigUtil.getResource(data);
		if(this.configMap == null){
			this.configMap = new HashMap<>();	
		}
		if(this.jiJinconfigMap  == null){
			this.jiJinconfigMap = new HashMap<>();	
		}
		
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TouZiConfig config = createTouZiConfig(tmp);
				if("TouZi.jat".equals(configName)){
					this.configMap.put(config.getId(), config);
				}else if("JiJin.jat".equals(configName)){
					this.jiJinconfigMap.put(config.getId(), config);
				}
			
			}
		}
	}
	
	
	
	public TouZiConfig createTouZiConfig(Map<String, Object> tmp) {
		TouZiConfig config = new TouZiConfig();	
		
		int maxDay = -1;
		Map<Integer, Map<String, Integer>> rewardMap = new HashMap<>();
		Map<String, Integer> allItemMap = new HashMap<>();
		for (int i = 1; i <= 32; i++) {
			String item = CovertObjectUtil.object2String(tmp.get("item" + i));
			Map<String, Integer> itemMap = new HashMap<>();
			
			if(!CovertObjectUtil.isEmpty(item)){
				String[] innerItem = item.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
				if(itemMap.containsKey(innerItem[0])){
					int count = itemMap.get(innerItem[0]) + CovertObjectUtil.object2int(innerItem[1]);
					
					itemMap.put(innerItem[0], count);
				}else{
					int count = CovertObjectUtil.object2int(innerItem[1]);
					
					itemMap.put(innerItem[0], count);
				}
				
				if(allItemMap.containsKey(innerItem[0])){
					int count = allItemMap.get(innerItem[0]) + CovertObjectUtil.object2int(innerItem[1]);
					
					allItemMap.put(innerItem[0], count);
				}else{
					int count = CovertObjectUtil.object2int(innerItem[1]);
					
					allItemMap.put(innerItem[0], count);
				}
			}
			
			if(itemMap != null && itemMap.size() > 0){
				if(maxDay == -1 || maxDay < i){
					maxDay = i;
				}
				
				//Map<天，奖励物品Map>
				rewardMap.put(i, itemMap);
			}
		}
		
		if(rewardMap != null && rewardMap.size() > 0){
			config.setItemMap(new ReadOnlyMap<>(rewardMap));
			Object[] cData = getItemData(allItemMap);
			if(cData != null){
				config.setClientItemMap(cData);
			}
		}
		
		config.setMaxDay(maxDay);
		int sDay = CovertObjectUtil.object2int(tmp.get("time1"));
		config.setStartDay(sDay <= 0 ? 1 : sDay);//0则是默认为第一天
		config.setEndDay(CovertObjectUtil.object2int(tmp.get("time2")));//0则不结束，没有结束天数
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		return config;
	}
	
	private Object[] getItemData(Map<String, Integer> itemMap){
		if(itemMap == null || itemMap.size() <= 0){
			return null;
		}
		
		Object[] data = new Object[itemMap.size()];
		int i = 0;
		for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
			data[i++] = new Object[]{entry.getKey(), entry.getValue()};
		}
		return data;
	}
	
	public TouZiConfig loadById(Integer id,int type){
		if(type == GameConstants.TOUZHI_TYPE){
			return configMap.get(id);	
		}else if(type == GameConstants.JIJING_TYPE){
			return jiJinconfigMap.get(id);
		}else{
			ChuanQiLog.error(" TouZiConfig loadById type is not exits,type="+type);
		}
		return null;
		
	}
	
	public Map<Integer, TouZiConfig> loadAll(int type){
		if(type == GameConstants.TOUZHI_TYPE){
			return configMap;	
		}else if(type == GameConstants.JIJING_TYPE){
			return jiJinconfigMap;
		}else{
			ChuanQiLog.error("TouZiConfig loadAll type is not exits,type="+type);
		}
		return null;
	}
	
}