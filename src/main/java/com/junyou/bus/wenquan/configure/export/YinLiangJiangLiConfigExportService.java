package com.junyou.bus.wenquan.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class YinLiangJiangLiConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "YinLiangJiangLi.jat";

	private Map<Integer, YinLiangJiangLiConfig> configMap;

	private int maxYinLiangTimes;

	private YinLiangJiangLiConfig createYinLiangJiangLiConfig(
			Map<String, Object> tmp) {
		YinLiangJiangLiConfig config = new YinLiangJiangLiConfig();
		config.setCishu(CovertObjectUtil.object2int(tmp.get("cishu")));
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setZhenqi(CovertObjectUtil.object2int(tmp.get("zhenqi")));
		config.setJingyan(CovertObjectUtil.object2int(tmp.get("jingyan")));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer, YinLiangJiangLiConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				YinLiangJiangLiConfig config = createYinLiangJiangLiConfig(tmp);
				configMap.put(config.getCishu(), config);
				if (config.getCishu() > maxYinLiangTimes) {
					maxYinLiangTimes = config.getCishu();
				}
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public int getMaxYinLiangTimes() {
		return maxYinLiangTimes;
	}
	
	public YinLiangJiangLiConfig getConfig(int cishu){
		return configMap.get(cishu);
	}

}
