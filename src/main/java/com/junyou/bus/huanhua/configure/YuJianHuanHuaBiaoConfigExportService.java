package com.junyou.bus.huanhua.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class YuJianHuanHuaBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, YuJianHuanHuaBiaoConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "YuJianHuanHuaBiao.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, YuJianHuanHuaBiaoConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {

			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				YuJianHuanHuaBiaoConfig config = createYuJianHuanHuaBiaoConfig(tmp);

				configs.put(config.getId(), config);
			}
		}
	}

	public YuJianHuanHuaBiaoConfig createYuJianHuanHuaBiaoConfig(
			Map<String, Object> tmp) {
		YuJianHuanHuaBiaoConfig config = new YuJianHuanHuaBiaoConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		if (config.getId() < 0) {
			return config;
		}
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);

		config.setAttrs(new ReadOnlyMap<>(attrs));

		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		
		int czd = CovertObjectUtil.object2int(tmp.get("czdjc"));
		config.setCzd(czd==1);

		config.setNeeditem(CovertObjectUtil.object2String(tmp.get("needitem")));
		return config;
	}

	public YuJianHuanHuaBiaoConfig loadById(Integer id) {
		return configs.get(id);
	}

	protected String getConfigureName() {
		return configureName;
	}
}