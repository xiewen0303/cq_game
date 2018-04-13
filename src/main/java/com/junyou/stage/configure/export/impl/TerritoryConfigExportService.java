package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.stage.configure.TerritoryConfig;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class TerritoryConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "LingDiZhan.jat";

	private Map<Integer, TerritoryConfig> configMap;

	private TerritoryConfig createTerritoryConfig(Map<String, Object> tmp) {
		TerritoryConfig config = new TerritoryConfig();
		config.setMap(CovertObjectUtil.object2int(tmp.get("map")));

		String flag = CovertObjectUtil.object2String(tmp.get("zuobiao"));
		String[] zuobiao = flag.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setZuobiao(new int[] { Integer.parseInt(zuobiao[0]),
				Integer.parseInt(zuobiao[1]) });

		config.setMonster(CovertObjectUtil.object2String(tmp.get("monster")));

		String fuhuo1Str = CovertObjectUtil.object2String(tmp.get("fuhuo1"));
		String[] fuhuo1 = fuhuo1Str.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setFuhuo1(new int[] { Integer.parseInt(fuhuo1[0]),
				Integer.parseInt(fuhuo1[1]) });

		String fuhuo2Str = CovertObjectUtil.object2String(tmp.get("fuhuo2"));
		String[] fuhuo2 = fuhuo2Str.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setFuhuo2(new int[] { Integer.parseInt(fuhuo2[0]),
				Integer.parseInt(fuhuo2[1]) });

		config.setExp(CovertObjectUtil.object2Long(tmp.get("exp")));
		config.setNeedlevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setZhenqi(CovertObjectUtil.object2int(tmp.get("zhenqi")));

		config.setAddexp(CovertObjectUtil.object2Float(tmp.get("addexp")));
		config.setAddzhenqi(CovertObjectUtil.object2Float(tmp.get("addzhenqi")));
		Map<String, Integer> items = ConfigAnalysisUtils
				.getConfigMap(CovertObjectUtil.object2String(tmp
						.get("jiangitem")));
		config.setJiangitem(items);

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer, TerritoryConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				TerritoryConfig config = createTerritoryConfig(tmp);
				configMap.put(config.getMap(), config);
			}
		}

		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public TerritoryConfig getConfigByMapId(Integer mapId) {
		return configMap.get(mapId);
	}

	public List<TerritoryConfig> loadAllConfigs() {
		return new ArrayList<>(configMap.values());
	}
	
}
