package com.junyou.bus.kuafuarena1v1.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class KuaFuJingJiZongBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, KuaFuJingJiZongBiaoConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "KuaFuJingJiZongBiao.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, KuaFuJingJiZongBiaoConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				KuaFuJingJiZongBiaoConfig config = createKuaFuJingJiZongBiaoConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
	}

	public KuaFuJingJiZongBiaoConfig createKuaFuJingJiZongBiaoConfig(
			Map<String, Object> tmp) {
		KuaFuJingJiZongBiaoConfig config = new KuaFuJingJiZongBiaoConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));

		config.setLimitTimes(CovertObjectUtil.object2int(tmp.get("limitTimes")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 根据id获得配置
	 * 
	 * @param id
	 * @return
	 */
	public KuaFuJingJiZongBiaoConfig loadById(int id) {
		return configs.get(id);
	}
}
