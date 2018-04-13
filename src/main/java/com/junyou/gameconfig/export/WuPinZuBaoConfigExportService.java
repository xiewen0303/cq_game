package com.junyou.gameconfig.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.lottery.Lottery;

/**
 * 
 * @description 物品组包表配置 
 *
 * @author DaoZhen Yuan
 * @date 2013-02-27 09:50:30
 */
@Component
public class WuPinZuBaoConfigExportService{
	
	/**
	  * configFileName
	 */
	private String configureName = "WuPinZuBao.jat";
	
	private Map<String, WuPinZuBaoConfig> configs = new HashMap<>();
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				WuPinZuBaoConfig config = createWuPinZuBaoConfig(tmp);
				
				if(config != null){
					configs.put(config.getId(), config);
				}
			}
		}
	}
	
	public WuPinZuBaoConfig createWuPinZuBaoConfig(Map<String, Object> tmp) {
		WuPinZuBaoConfig config = new WuPinZuBaoConfig();	
							
		config.setId(CovertObjectUtil.object2String(tmp.get("id")));
		if("".equals(config.getId())){
			return null;
		}
		
		config.setNeedJb(CovertObjectUtil.object2int(tmp.get("moneyneed")));
		config.setNeedYb(CovertObjectUtil.object2int(tmp.get("goldneed")));
		config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("levelneed")));
		config.setNeedItemId(CovertObjectUtil.object2String(tmp.get("itemneed")));
		config.setNeedItemCount(CovertObjectUtil.object2int(tmp.get("itemneedcount")));
		
		String dropName = "drop";
		String typeName = "type";
		String countName = "count";
		String oddsName = "odds";
		
		Map<WuPinZuBaoValue,Float> zubaoMap = new HashMap<WuPinZuBaoValue, Float>();
		for (int i = 1; i <= 100; i++) {
			
			String dropId = CovertObjectUtil.object2String(tmp.get(dropName+i));
			if(!CovertObjectUtil.isEmpty(dropId)){
				WuPinZuBaoValue value = new WuPinZuBaoValue();
				value.setId(dropId);
				
				int number = CovertObjectUtil.object2int(tmp.get(dropName+i+countName));
				if(number == 0){
					number = 1;
				}
				
				value.setNumber(number);
				value.setZubao(CovertObjectUtil.object2boolean(tmp.get(dropName+i+typeName)));
				value.setOdds(CovertObjectUtil.object2Float(tmp.get(dropName+i+oddsName)));
				
				zubaoMap.put(value, value.getOdds());
			}
			
		}
		
		if(zubaoMap.size() > 0){
			config.setZubaoMap(zubaoMap);
		}
		
		return config;
	}
	
	
	/**
	 * 根据组包roll规则
	 * @return goodsId,goodsCount
	 */
	public Map<String,Integer> componentRoll(String componentDropId) {
		WuPinZuBaoConfig zuBaoConfig = loadById(componentDropId);
		if(zuBaoConfig == null){
			return null;
		}
		Map<String,Integer> finalMap = new HashMap<String, Integer>();
		
		dropZubao(componentDropId, finalMap, 1);
		
		return finalMap;
	}
	
	/**
	 * roll组包
	 * @param zubaoOddsList
	 * @param finalMap
	 * @param layer 层
	 */
	private void dropZubao(String dropId,Map<String,Integer> finalMap,int layer){
		
		/**
		 * 组包嵌套达到5层,自动停止,并且会给默认物品
		 */
		if(layer >= 5){
			finalMap.put("money", 1);
			return;
		}
		
		WuPinZuBaoConfig zuBaoConfig = loadById(dropId);
		if(zuBaoConfig == null){
			finalMap.put("money", 1);
			return;
		}
		
		Map<WuPinZuBaoValue, Float> zubaoOddsList = zuBaoConfig.getZubaoMap();
		WuPinZuBaoValue wuPinZuBaoValue = Lottery.getRandomKey(zubaoOddsList);
		if(wuPinZuBaoValue != null){
			if(wuPinZuBaoValue.isZubao()){
				
				for (int i = 1; i <= wuPinZuBaoValue.getNumber(); i++) {
					dropZubao(wuPinZuBaoValue.getId(), finalMap, layer+1);
				}
			}else{
				finalMap.put(wuPinZuBaoValue.getId(), wuPinZuBaoValue.getNumber());
			}
			
		}
		
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public WuPinZuBaoConfig loadById(String id){
		return configs.get(id);
	}
}