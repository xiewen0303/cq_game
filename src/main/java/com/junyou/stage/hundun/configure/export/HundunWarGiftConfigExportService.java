package com.junyou.stage.hundun.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 混沌奖励规则表 
 *
 * @author LiNing
 * @date 2015-07-25 13:43:23
 */
@Component
public class HundunWarGiftConfigExportService extends AbsClasspathConfigureParser{
	
	/**
	  * configFileName
	 */
	private String configureName = "HunDunZhanChangJiangLiBiao.jat";
	private Map<Integer, Map<Integer, HundunWarGiftConfig>> configs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer, Map<Integer, HundunWarGiftConfig>> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				HundunWarGiftConfig config = createHundunWarGiftConfig(tmp);
				Map<Integer, HundunWarGiftConfig> cengConfig = configs.get(config.getCeng());
				if(cengConfig == null){
					cengConfig = new HashMap<>();
					configs.put(config.getCeng(),cengConfig);
				}
				cengConfig.put(config.getRank(), config);
			}
		}
		this.configs = configs;
	}
	
	private HundunWarGiftConfig createHundunWarGiftConfig(Map<String, Object> tmp) {
		HundunWarGiftConfig config = new HundunWarGiftConfig();	
							
		config.setCeng(CovertObjectUtil.object2int(tmp.get("map")));
		config.setRank(CovertObjectUtil.object2int(tmp.get("paiming")));
		config.setGift(CovertObjectUtil.object2String(tmp.get("jiangli")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public Map<Integer, HundunWarGiftConfig> getCengConfigs(Integer ceng){
		if(!configs.containsKey(ceng)){
			return null;
		}
		return new ReadOnlyMap<>(configs.get(ceng));
	}
	
}