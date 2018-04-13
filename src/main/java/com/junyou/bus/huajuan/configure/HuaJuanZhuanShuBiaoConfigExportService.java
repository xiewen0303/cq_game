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
public class HuaJuanZhuanShuBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, HuaJuanZhuanShuBiaoConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "HuaJuanZhuanShuBiao.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, HuaJuanZhuanShuBiaoConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				HuaJuanZhuanShuBiaoConfig config = createHuaJuanZhuanShuBiaoConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
	}

	public HuaJuanZhuanShuBiaoConfig createHuaJuanZhuanShuBiaoConfig(
			Map<String, Object> tmp) {
		HuaJuanZhuanShuBiaoConfig config = new HuaJuanZhuanShuBiaoConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public HuaJuanZhuanShuBiaoConfig loadById(Integer id) {
		return configs.get(id);
	}
}
