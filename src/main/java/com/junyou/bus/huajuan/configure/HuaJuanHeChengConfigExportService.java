package com.junyou.bus.huajuan.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class HuaJuanHeChengConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, HuaJuanHeChengConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "HuaJuanHeCheng.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, HuaJuanHeChengConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				HuaJuanHeChengConfig config = createHuaJuanHeChengConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
	}

	public HuaJuanHeChengConfig createHuaJuanHeChengConfig(
			Map<String, Object> tmp) {
		HuaJuanHeChengConfig config = new HuaJuanHeChengConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setItem(CovertObjectUtil.object2String(tmp.get("item")));
		config.setNeeditem(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil
				.object2String(tmp.get("needitem"))));
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));

		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public HuaJuanHeChengConfig loadById(Integer id) {
		return configs.get(id);
	}
}
