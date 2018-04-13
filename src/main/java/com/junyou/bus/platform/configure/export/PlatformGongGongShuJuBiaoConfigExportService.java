package com.junyou.bus.platform.configure.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.ChuanQiConfigUtil;

@Component
public class PlatformGongGongShuJuBiaoConfigExportService extends
		AbsClasspathConfigureParser {
	

	private Map<String, AdapterPublicConfig> configs;

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object obj = GameConfigUtil.getPublicResource(data);
		Map<String, AdapterPublicConfig> configs = new HashMap<>();
		Map<String, Object> tmp = (Map<String, Object>) obj;
		if (null != tmp) {
			for (Entry<String, Object> entry : tmp.entrySet()) {
				String key = entry.getKey();
				Map<String, Object> value = (Map<String, Object>) entry.getValue();
				AdapterPublicConfig config = PlatformGongGongShuJuConfigCreateFactory.createPublicConfig(key, value);
				if (config != null) {
					configs.put(config.getId(), config);
				}
			}
		}
		this.configs = configs;
	}

	protected String getConfigureName() {
		return "GongGongShuJuBiao_" + ChuanQiConfigUtil.getPlatfromId()	+ ".jat";
	}

	@SuppressWarnings("unchecked")
	public <P> P loadPublicConfig(String mod) {
		return (P) configs.get(mod);
	}
}
