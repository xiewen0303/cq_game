package com.junyou.bus.yaoshen.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 妖神潜能丹
 * 
 * @author wind
 * @date 2015-03-31 19:07:18
 */
@Component
public class YaoshenQianNengBiaoConfigExportService extends
		AbsClasspathConfigureParser {
	
	private Map<String,YaoshenQianNengBiaoConfig>  configMap;

	/**
	 * configFileName
	 */
	private String configureName = "YaoShenBaTiQianNeng.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
//		YaoshenConstants  = new 
		configMap  = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {

			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				String id = CovertObjectUtil.object2String(tmp.get("id"));
				YaoshenQianNengBiaoConfig yaoshenQianNengBiaoConfig = createYaoshenQianNengBiaoConfig(tmp);
				configMap.put(id, yaoshenQianNengBiaoConfig);
				
			}
		}
	}

	public YaoshenQianNengBiaoConfig createYaoshenQianNengBiaoConfig(
			Map<String, Object> tmp) {
		YaoshenQianNengBiaoConfig config = new YaoshenQianNengBiaoConfig();
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public YaoshenQianNengBiaoConfig getConfig(String id) {
		return configMap.get(id);
	}
}