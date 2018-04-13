package com.junyou.bus.shenmo.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.shenmo.configure.ShenMoPaiMingJiangLiConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class ShenMoPaiMingJiangLiConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, ShenMoPaiMingJiangLiConfig> configs = null;
	
	private int maxRank =0;

	/**
	 * configFileName
	 */
	private String configureName = "ShenMoPaiMingJiangLi.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<Integer, ShenMoPaiMingJiangLiConfig> configs = new HashMap<>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShenMoPaiMingJiangLiConfig config = createShenMoPaiMingJiangLiConfig(tmp);
				int min = config.getMin();
				int max = config.getMax();
				for (int i = min; i <= max; i++) {
					configs.put(i, config);
				}
				if(max>maxRank){
					maxRank = max;
				}
			}
		}
		this.configs = configs;
	}

	public ShenMoPaiMingJiangLiConfig createShenMoPaiMingJiangLiConfig(
			Map<String, Object> tmp) {
		ShenMoPaiMingJiangLiConfig config = new ShenMoPaiMingJiangLiConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
		config.setMax(CovertObjectUtil.object2int(tmp.get("max")));
		String jiangStr = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		config.setJiangItems(jiangStr);
		Map<String, Integer> jiangItem = ConfigAnalysisUtils
				.getConfigMap(jiangStr);
		config.setJiangitemMap(jiangItem);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ShenMoPaiMingJiangLiConfig loadByRank(int rank) {
		return configs.get(rank);
	}

	public int getMaxRank() {
		return maxRank;
	}
}
