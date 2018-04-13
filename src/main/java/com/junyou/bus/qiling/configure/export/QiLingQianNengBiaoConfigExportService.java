package com.junyou.bus.qiling.configure.export;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @description 器灵潜能丹
 * @date 2015-03-31 19:07:18
 */
@Component
public class QiLingQianNengBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private QiLingQianNengBiaoConfig config = null;

	/**
	 * configFileName
	 */
	private String configureName = "QiLingQianNengBiao.jat";

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

	public QiLingQianNengBiaoConfig createChiBangQianNengBiaoConfig(
			Map<String, Object> tmp) {
		QiLingQianNengBiaoConfig config = new QiLingQianNengBiaoConfig();
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public QiLingQianNengBiaoConfig getConfig() {
		return config;
	}
}