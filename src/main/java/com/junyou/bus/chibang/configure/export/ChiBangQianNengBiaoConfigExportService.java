package com.junyou.bus.chibang.configure.export;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 
 * @description 翅膀潜能丹
 * 
 * @author wind
 * @date 2015-03-31 19:07:18
 */
@Component
public class ChiBangQianNengBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private ChiBangQianNengBiaoConfig config = null;

	/**
	 * configFileName
	 */
	private String configureName = "ChiBangQianNengBiao.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {

			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				config = createChiBangQianNengBiaoConfig(tmp);
			}
		}
	}

	public ChiBangQianNengBiaoConfig createChiBangQianNengBiaoConfig(
			Map<String, Object> tmp) {
		ChiBangQianNengBiaoConfig config = new ChiBangQianNengBiaoConfig();
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ChiBangQianNengBiaoConfig getConfig() {
		return config;
	}
}