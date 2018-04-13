package com.junyou.bus.cuxiao.configure.export;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 促销表
 */
@Component
public class JingJieFanLiConfigExportService extends AbsClasspathConfigureParser {

	/**
	 * configFileName
	 */
	private String configureName = "JingJieFanLiBiao.jat";

	private Map<Integer, JingJieFanLiConfig> map  = new HashMap<>();

	protected void configureDataResolve(byte[] data) {
		if (data == null) {
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);

		JingJieFanLiConfig config  = null;
		Map<String, Integer> rewardMap  =null;
		String rewards  = null;
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				config  = new JingJieFanLiConfig();
				config.setId(CovertObjectUtil.object2int(tmp.get("id")));
				config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
				config.setType( CovertObjectUtil.object2int(tmp.get("type")));
				config.setRewards(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("awards"))));
				config.setXprice(CovertObjectUtil.object2int(tmp.get("xprice")));
				map.put(config.getId(), config);
			}
		}
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public JingJieFanLiConfig getConfig(int id) {
		return map.get(id);
	}
}