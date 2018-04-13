package com.junyou.bus.zuoqi.configure.export;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 
 * @description 坐骑升阶表
 * 
 * @author wind
 * @date 2015-03-31 19:07:18
 */
@Component
public class YuJianQianNengBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private YuJianQianNengBiaoConfig config = null;

	/**
	 * configFileName
	 */
	private String configureName = "YuJianQianNengBiao.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {

			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				config = createYuJianQianNengBiaoConfig(tmp);
			}
		}
	}

	public YuJianQianNengBiaoConfig createYuJianQianNengBiaoConfig(
			Map<String, Object> tmp) {
		YuJianQianNengBiaoConfig config = new YuJianQianNengBiaoConfig();
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public YuJianQianNengBiaoConfig getConfig() {
		return config;
	}
}