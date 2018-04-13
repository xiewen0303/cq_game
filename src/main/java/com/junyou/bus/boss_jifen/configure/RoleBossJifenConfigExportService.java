package com.junyou.bus.boss_jifen.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @description: boss积分配置对外访问业务 
 *
 *	@author ChuBin
 *
 * @date 2017-1-3
 */
@Service
public class RoleBossJifenConfigExportService extends AbsClasspathConfigureParser {
	private String configureName = "BossJiFen.jat";

	Map<Integer, RoleBossJifenConfig> configs = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		if (data == null) {
			return;
		}

		ConfigMd5SignManange.addConfigSign(configureName, data);
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<String, Long> totalAttrs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				RoleBossJifenConfig config = createConfig(tmp);
				// 计算叠加属性
				ObjectUtil.longMapAdd(totalAttrs, config.getAttribute());
				Map<String, Long> copyMap = new HashMap<>();
				copyMap.putAll(totalAttrs);
				config.setTotalAttrs(copyMap);

				configs.put(config.getId(), config);
			}
		}
	}

	private RoleBossJifenConfig createConfig(Map<String, Object> tmp) {
		RoleBossJifenConfig config = new RoleBossJifenConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setCeng(CovertObjectUtil.object2int(tmp.get("cengshu")));
		config.setStar(CovertObjectUtil.object2int(tmp.get("xingshu")));
		config.setPointX(CovertObjectUtil.object2int(tmp.get("x")));
		config.setPointY(CovertObjectUtil.object2int(tmp.get("y")));
		config.setConsumeJifen(CovertObjectUtil.object2Long(tmp.get("point")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public RoleBossJifenConfig getConfigByConfigId(Integer configId) {
		return configs.get(configId);
	}

}
