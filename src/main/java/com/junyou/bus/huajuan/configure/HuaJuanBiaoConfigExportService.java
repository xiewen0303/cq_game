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
public class HuaJuanBiaoConfigExportService extends AbsClasspathConfigureParser {

	private Map<Integer, HuaJuanBiaoConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "HuaJuanBiao.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, HuaJuanBiaoConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				HuaJuanBiaoConfig config = createHuaJuanBiaoConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
	}

	public HuaJuanBiaoConfig createHuaJuanBiaoConfig(Map<String, Object> tmp) {
		HuaJuanBiaoConfig config = new HuaJuanBiaoConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setRarelevel(CovertObjectUtil.object2int(tmp.get("rarelevel")));
		config.setHuajuanexp(CovertObjectUtil.object2int(tmp.get("huajuanexp")));
		config.setExclusive(CovertObjectUtil.object2int(tmp.get("exclusive")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		config.setLiebiaoid(CovertObjectUtil.object2int(tmp.get("liebiaoid")));

		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public HuaJuanBiaoConfig loadById(Integer id) {
		return configs.get(id);
	}

}
