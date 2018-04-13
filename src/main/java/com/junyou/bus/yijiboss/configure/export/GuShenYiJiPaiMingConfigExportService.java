package com.junyou.bus.yijiboss.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class GuShenYiJiPaiMingConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, GuShenYiJiPaiMingConfig> configs = null;

	private int maxRank = 0;
	public static int anyRank = -1;

	/**
	 * configFileName
	 */
	private String configureName = "GuShenYiJiPaiMing.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, GuShenYiJiPaiMingConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				GuShenYiJiPaiMingConfig config = createGuShenYiJiPaiMingConfig(tmp);
				int min = config.getMin();
				int max = config.getMax();
				for (int i = min; i <= max; i++) {
					configs.put(i, config);
				}
				if (max > maxRank) {
					maxRank = max;
				}
			}
		}
	}

	public GuShenYiJiPaiMingConfig createGuShenYiJiPaiMingConfig(
			Map<String, Object> tmp) {
		GuShenYiJiPaiMingConfig config = new GuShenYiJiPaiMingConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		if (config.getId() != anyRank) {
			config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
			config.setMax(CovertObjectUtil.object2int(tmp.get("max")));
		}else{
			config.setMin(anyRank);
			config.setMax(anyRank);
		}
		String jiangItemStr = CovertObjectUtil.object2String(tmp
				.get("jiangitem"));
		Map<String, Integer> jiangItem = ConfigAnalysisUtils
				.getConfigMap(jiangItemStr);
		config.setJiangitem(jiangItem);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public GuShenYiJiPaiMingConfig loadByRank(int rank) {
		return configs.get(rank);
	}

	public int getMaxRank() {
		return maxRank;
	}
}
