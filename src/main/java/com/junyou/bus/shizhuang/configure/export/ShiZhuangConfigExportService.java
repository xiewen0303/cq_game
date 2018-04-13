package com.junyou.bus.shizhuang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.bus.shizhuang.entity.ShiZhuangConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ShiZhuangConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "ShiZhuangJiHuoBiao.jat";

	private Map<Integer,ShiZhuangConfig> configMap;

	private ShiZhuangConfig createShiZhuangConfig(Map<String, Object> tmp) {
		ShiZhuangConfig config = new ShiZhuangConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		Map<String,Integer> items = new HashMap<>();
		items.put(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem")), 1);
		config.setCostItem(items);
		config.setGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setSex(CovertObjectUtil.object2int(tmp.get("sex")));
		config.setXftime(CovertObjectUtil.object2int(tmp.get("xftime")));
		config.setXfgold(CovertObjectUtil.object2int(tmp.get("xfgold")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer,ShiZhuangConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShiZhuangConfig config = createShiZhuangConfig(tmp);
				configMap.put(config.getId(), config);
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ShiZhuangConfig getConfig(Integer id){
		return configMap.get(id);
	}
	
}
