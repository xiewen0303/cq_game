package com.junyou.bus.yaoshen.configure.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 *  破神
 * @author lxn
 *
 */
@Component
public class YaoshenHunPoPoShenConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "HunPoPoShen.jat";
	private Map<String,YaoshenHunPoPoshenConfig> configMap=new HashMap<>();
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				String id = CovertObjectUtil.object2String(tmp.get("id"));
				int id1 = CovertObjectUtil.object2int(tmp.get("id1"));
				Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
				YaoshenHunPoPoshenConfig config = new YaoshenHunPoPoshenConfig();
				config.setId(id);
				config.setId1(id1);
				config.setAttMap(new ReadOnlyMap<>(attrs));
				configMap.put(id,config);
			}
		}
	}
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	public Map<String, YaoshenHunPoPoshenConfig>  getConfigMap() {
		
		return configMap;
	}
 

}
