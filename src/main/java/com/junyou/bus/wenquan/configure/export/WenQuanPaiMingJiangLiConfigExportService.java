package com.junyou.bus.wenquan.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class WenQuanPaiMingJiangLiConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "WenQuanPaiMingJiangLi.jat";

	private Map<Integer, WenQuanPaiMingJiangLiConfig> configMap;

	private WenQuanPaiMingJiangLiConfig createWenQuanPaiMingJiangLiConfig(Map<String, Object> tmp) {
		WenQuanPaiMingJiangLiConfig config = new WenQuanPaiMingJiangLiConfig();
		Map<String, Integer> items = ConfigAnalysisUtils
				.getConfigMap(CovertObjectUtil.object2String(tmp
						.get("jiangitem")));
		config.setMin(CovertObjectUtil.object2int(tmp
				.get("min")));
		config.setMax(CovertObjectUtil.object2int(tmp
				.get("max")));
		config.setJiangitem(items);
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer, WenQuanPaiMingJiangLiConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				WenQuanPaiMingJiangLiConfig config = createWenQuanPaiMingJiangLiConfig(tmp);
				for(int i=config.getMin();i<=config.getMax();i++){
					if(configMap.get(i)==null){
						configMap.put(i, config);
					}
				}
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public Map<Integer, WenQuanPaiMingJiangLiConfig> getConfigs(){
		return configMap;
	}
}
