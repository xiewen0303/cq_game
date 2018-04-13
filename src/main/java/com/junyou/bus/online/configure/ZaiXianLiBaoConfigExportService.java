package com.junyou.bus.online.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 在线奖励
 */
@Component
public class ZaiXianLiBaoConfigExportService extends AbsClasspathConfigureParser {
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "ZaiXianLiBaoBiao.jat";
	
	private Map<Integer,ZaiXianLiBaoConfig> onlineConfigs;
	
	private int maxDay;
	
	public int getMaxDay(){
		return maxDay;
	}
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer,ZaiXianLiBaoConfig> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZaiXianLiBaoConfig config = createOnlineConfig(tmp);
				configs.put(config.getDay(), config);
//				GoodsConfigChecker.registCheck(config);
				
				if(maxDay < config.getDay()){
					maxDay = config.getDay();
				}
			}
		}
		this.onlineConfigs = configs;
	}
	
	/**
	 * @param tmp
	 * @return
	 */
	public ZaiXianLiBaoConfig createOnlineConfig(Map<String, Object> tmp) {
		ZaiXianLiBaoConfig config = new ZaiXianLiBaoConfig(); 
		config.setDay(CovertObjectUtil.object2int(tmp.get("day")));
		
		String itemStr = CovertObjectUtil.obj2StrOrNull(tmp.get("item"));
		if(itemStr == null){
			return config;
		}
		
		String timeStr = CovertObjectUtil.obj2StrOrNull(tmp.get("time"));
		if(timeStr == null){
			return config;
		}
		
		Map<Integer,Map<String,Integer>> awards = new HashMap<>();
		
		String[] datas = itemStr.split("\\|");
		
		String[] times  = timeStr.split("\\|");
		int[] keys = new int[times.length];
		
		for (int i = 0; i < times.length; i++) {
			int time = CovertObjectUtil.object2int(times[i]);
			awards.put(time, CovertObjectUtil.object2Map(datas[i]));
			keys[i] = time;
		}
		config.setAwards(awards);
		config.setKeys(keys);
		return config;
	}
	
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZaiXianLiBaoConfig loadById(Integer id){
		return onlineConfigs.get(id);
	}

	public int getIndex(int day, int time) {
		ZaiXianLiBaoConfig  config = loadById(day);
		if(config == null || config.getKeys() == null){
			return -1;
		}
		int i = 0;
		for (int da : config.getKeys()) {
			if(da == time){
				return i;
			}
			i ++ ;
		}
		return -1;
	}
	
}