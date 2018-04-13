package com.junyou.bus.cuilian.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class CuilianConsumeExportService extends AbsClasspathConfigureParser {

	private Map<Integer, CuilianConsumeConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "CuiLianXiaoHao.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, CuilianConsumeConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				CuilianConsumeConfig config = createCuilianConsumeConfig(tmp);
				configs.put(config.getTimes(), config);
			}
		}
	}

	public CuilianConsumeConfig createCuilianConsumeConfig(Map<String, Object> tmp) {
		CuilianConsumeConfig config = new CuilianConsumeConfig();
		config.setTimes(CovertObjectUtil.object2int(tmp.get("cishu")));
		Long needMoney = CovertObjectUtil.object2Long(tmp.get("needmoney"));
		config.setNeedMoney(needMoney==null?0:needMoney.longValue());
		config.setNeedBgold(CovertObjectUtil.object2int(tmp.get("needbgold")));
		config.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgold")));
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
	public CuilianConsumeConfig loadByTimes(int times) {
		return configs.get(times);
	}

}