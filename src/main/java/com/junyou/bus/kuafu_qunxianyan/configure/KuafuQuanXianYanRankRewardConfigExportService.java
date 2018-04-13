package com.junyou.bus.kuafu_qunxianyan.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class KuafuQuanXianYanRankRewardConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, KuafuQuanXianYanRankRewardConfig> configs = null;

	private int maxRank = 0;
	public static int joinRewardId = -1;

	/**
	 * configFileName
	 */
	private String configureName = "QunXianYanPaiMingJiangLi.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, KuafuQuanXianYanRankRewardConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				KuafuQuanXianYanRankRewardConfig config = createKuafuBossRankRewardConfig(tmp);
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

	public KuafuQuanXianYanRankRewardConfig createKuafuBossRankRewardConfig(
			Map<String, Object> tmp) {
		KuafuQuanXianYanRankRewardConfig config = new KuafuQuanXianYanRankRewardConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		if (config.getId() != joinRewardId) {
			config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
			config.setMax(CovertObjectUtil.object2int(tmp.get("max")));
		}
		String jiangItemStr = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		Map<String, Integer> jiangItem = ConfigAnalysisUtils.getConfigMap(jiangItemStr);
		config.setJiangitem(jiangItem);
		if(!CovertObjectUtil.isEmpty(jiangItemStr)){
			String str = "";
			String[] rewardItems = jiangItemStr.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
			for (String string : rewardItems) {
				if("".equals(str)){
					str = string;
				}else{
					str = str+","+string;
				}
				
			}
			config.setJiangitemStr(str);
		}
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public KuafuQuanXianYanRankRewardConfig loadByRank(int rank) {
		return configs.get(rank);
	}
	
	public KuafuQuanXianYanRankRewardConfig getConfigByRank(Integer rank){
		for (Integer id : configs.keySet()) {
			KuafuQuanXianYanRankRewardConfig config = configs.get(id);
			if(rank.intValue() >= config.getMin() && rank.intValue() <= config.getMax()){
				return config;
			}
		}
		return null;
	}

	public int getMaxRank() {
		return maxRank;
	}
}
