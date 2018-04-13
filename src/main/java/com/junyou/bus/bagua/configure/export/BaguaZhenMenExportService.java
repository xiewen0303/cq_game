package com.junyou.bus.bagua.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class BaguaZhenMenExportService extends AbsClasspathConfigureParser {

	private Map<Integer, BaguaZhenMenConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "BaGuaZhenMen.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, BaguaZhenMenConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				BaguaZhenMenConfig config = createBaguaZhenMenConfig(tmp);
				configs.put(config.getOrder(), config);
			}
		}
	}

	public BaguaZhenMenConfig createBaguaZhenMenConfig(Map<String, Object> tmp) {
		BaguaZhenMenConfig config = new BaguaZhenMenConfig();
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setChuanshang(CovertObjectUtil.object2int(tmp.get("chuanshang")));
		config.setChuanxia(CovertObjectUtil.object2int(tmp.get("chuanxia")));
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
	public BaguaZhenMenConfig loadByOrder(int order) {
		return configs.get(order);
	}

}