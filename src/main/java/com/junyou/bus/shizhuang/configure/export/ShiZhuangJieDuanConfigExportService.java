package com.junyou.bus.shizhuang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ShiZhuangJieDuanConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "ShiZhuangJieDuan.jat";

	private Map<Integer,ShiZhuangJieDuanConfig> configMap;

	private ShiZhuangJieDuanConfig createShiZhuangJieDuanConfigConfigConfig(Map<String, Object> tmp) {
		ShiZhuangJieDuanConfig config = new ShiZhuangJieDuanConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setShizhuangshuxing(CovertObjectUtil.object2int(tmp.get("shizhuangshuxing")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,ShiZhuangJieDuanConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShiZhuangJieDuanConfig config = createShiZhuangJieDuanConfigConfigConfig(tmp);
				configMap.put(config.getLevel(), config);
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ShiZhuangJieDuanConfig getConfig(Integer id){
		return configMap == null ? null : configMap.get(id);
	}
	
}
