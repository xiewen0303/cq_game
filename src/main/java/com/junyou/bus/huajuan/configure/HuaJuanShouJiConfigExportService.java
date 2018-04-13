package com.junyou.bus.huajuan.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class HuaJuanShouJiConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, Map<Integer, Map<Integer, HuaJuanShouJiConfig>>> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "HuaJuanShouJi.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, Map<Integer, Map<Integer, HuaJuanShouJiConfig>>>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				HuaJuanShouJiConfig config = createHuaJuanShouJiConfig(tmp);
				Integer liebiaoId = config.getLiebiaoid();
				Integer type = config.getType();
				Integer num = config.getNum();
				Map<Integer, Map<Integer, HuaJuanShouJiConfig>> liebiaoMap = configs
						.get(liebiaoId);
				if (liebiaoMap == null) {
					liebiaoMap = new HashMap<Integer, Map<Integer, HuaJuanShouJiConfig>>();
					configs.put(liebiaoId, liebiaoMap);
				}
				Map<Integer, HuaJuanShouJiConfig> typeMap = liebiaoMap
						.get(type);
				if (typeMap == null) {
					typeMap = new HashMap<Integer, HuaJuanShouJiConfig>();
					liebiaoMap.put(type, typeMap);
				}
				typeMap.put(num, config);

			}
		}
	}

	public HuaJuanShouJiConfig createHuaJuanShouJiConfig(Map<String, Object> tmp) {
		HuaJuanShouJiConfig config = new HuaJuanShouJiConfig();

		config.setLiebiaoid(CovertObjectUtil.object2int(tmp.get("liebiaoid")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setNum(CovertObjectUtil.object2int(tmp.get("num")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));

		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public HuaJuanShouJiConfig get(Integer liebiaoId, Integer type, Integer num) {
		Map<Integer, Map<Integer, HuaJuanShouJiConfig>> liebiaoMap = configs
				.get(liebiaoId);
		if (liebiaoMap == null) {
			return null;
		}
		Map<Integer, HuaJuanShouJiConfig> typeMap = liebiaoMap.get(type);
		if (typeMap == null) {
			return null;
		}
		return typeMap.get(num);
	}

}
