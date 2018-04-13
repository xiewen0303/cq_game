package com.junyou.bus.chengshen.configure.export;

 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 升阶表 
 */
@Component
public class ChengShenTitleConfigExportService extends AbsClasspathConfigureParser  {
	
	/**
	 * 结束等级
	 */
	private int endLevel;
	
	private Map<Integer,ChengShenTitleConfig> configs = null;
	
	
	/**
	  * configFileName
	 */
	private String configureName = "ChengShen.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		Map<Integer,ChengShenTitleConfig>	configsTemp = new HashMap<Integer, ChengShenTitleConfig>();
		endLevel = 0;
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ChengShenTitleConfig config = createConfig(tmp);
				configsTemp.put(config.getLevel(), config);
				endLevel = endLevel < config.getLevel()?config.getLevel():endLevel; 
			}
		}
		configs= configsTemp;
	}
	
	private ChengShenTitleConfig createConfig(Map<String, Object> tmp){
		ChengShenTitleConfig config = new ChengShenTitleConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setNeedValue(CovertObjectUtil.object2int(tmp.get("needShenHun")));
		config.setChengHaoId(CovertObjectUtil.object2int(tmp.get("chenhao")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrMap(new ReadOnlyMap<>(attrs));
		return config;
	}
	
	public int getMaxJjLevel(){
		return endLevel;
	}
	protected String getConfigureName() {
		return configureName;
	}
	
	public ChengShenTitleConfig loadByLevel(int level){
		return configs.get(level);
	}
	 
}