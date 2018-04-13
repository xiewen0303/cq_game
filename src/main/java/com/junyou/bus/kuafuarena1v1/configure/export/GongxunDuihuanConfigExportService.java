package com.junyou.bus.kuafuarena1v1.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class GongxunDuihuanConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, GongxunDuihuanConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "GeRenGongXunDuiHuan.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, GongxunDuihuanConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				GongxunDuihuanConfig config = createGongxunDuihuanConfig(tmp);
				configs.put(config.getOrder(), config);
			}
		}
	}

	public GongxunDuihuanConfig createGongxunDuihuanConfig(
			Map<String, Object> tmp) {
		GongxunDuihuanConfig config = new GongxunDuihuanConfig();

		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));

		config.setItem(CovertObjectUtil.object2String(tmp.get("item")));

		config.setNeedgx(CovertObjectUtil.object2int(tmp.get("needgx")));

		config.setNeeddw(CovertObjectUtil.object2int(tmp.get("needdw")));

		config.setMaxcount(CovertObjectUtil.object2int(tmp.get("maxcount")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 根据id获得配置
	 * 
	 * @param id
	 * @return
	 */
	public GongxunDuihuanConfig loadById(int order) {
		return configs.get(order);
	}
}
