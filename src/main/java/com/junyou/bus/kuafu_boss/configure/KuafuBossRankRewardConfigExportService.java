package com.junyou.bus.kuafu_boss.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class KuafuBossRankRewardConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, KuafuBossRankRewardConfig> configs = null;

	private int maxRank = 0;
	public static int joinRewardId = -1;

	/**
	 * configFileName
	 */
	private String configureName = "KuaFuBossPaiMingJiang.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, KuafuBossRankRewardConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				KuafuBossRankRewardConfig config = createKuafuBossRankRewardConfig(tmp);
				if (config.getId() != joinRewardId) {
					int min = config.getMin();
					int max = config.getMax();
					for (int i = min; i <= max; i++) {
						configs.put(i, config);
					}
					if (max > maxRank) {
						maxRank = max;
					}
				} else {
					configs.put(joinRewardId, config);
				}
			}
		}
	}

	public KuafuBossRankRewardConfig createKuafuBossRankRewardConfig(
			Map<String, Object> tmp) {
		KuafuBossRankRewardConfig config = new KuafuBossRankRewardConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		if (config.getId() != joinRewardId) {
			config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
			config.setMax(CovertObjectUtil.object2int(tmp.get("max")));
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

	public KuafuBossRankRewardConfig loadByRank(int rank) {
		return configs.get(rank);
	}

	public int getMaxRank() {
		return maxRank;
	}
}
