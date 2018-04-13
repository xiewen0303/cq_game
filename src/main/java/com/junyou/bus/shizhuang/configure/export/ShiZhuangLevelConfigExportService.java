package com.junyou.bus.shizhuang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.bus.shizhuang.entity.ShiZhuangLevelConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ShiZhuangLevelConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "ShiZhuangShengJiBiao.jat";

	private Map<Integer,ShiZhuangLevelConfig> configMap;

	private ShiZhuangLevelConfig createShiZhuangLevelConfig(Map<String, Object> tmp) {
		ShiZhuangLevelConfig config = new ShiZhuangLevelConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("szlevel")));
		Map<String, Integer> items = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("needitem")));
		config.setItems(items);

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAtts(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer,ShiZhuangLevelConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShiZhuangLevelConfig config = createShiZhuangLevelConfig(tmp);
				configMap.put(config.getLevel(), config);
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ShiZhuangLevelConfig getConfig(Integer level){
		return configMap.get(level);
	}
	
}
