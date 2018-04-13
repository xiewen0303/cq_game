package com.junyou.bus.tianyu.configure.export;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @description 天羽潜能丹
 * @date 2015-03-31 19:07:18
 */
@Component
public class TianYuQianNengBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private TianYuQianNengBiaoConfig config = null;

	/**
	 * configFileName
	 */
	private String configureName = "TianYuQianNengBiao.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
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

	public TianYuQianNengBiaoConfig createChiBangQianNengBiaoConfig(
			Map<String, Object> tmp) {
		TianYuQianNengBiaoConfig config = new TianYuQianNengBiaoConfig();
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public TianYuQianNengBiaoConfig getConfig() {
		return config;
	}
}