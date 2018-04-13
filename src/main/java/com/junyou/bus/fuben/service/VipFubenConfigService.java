package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.VipFubenConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class VipFubenConfigService extends AbsClasspathConfigureParser {
	private final String configureName = "VipFuBen.jat";
	private Map<Integer,VipFubenConfig> configs;
	
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,VipFubenConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				VipFubenConfig config = createVipFubenConfig(tmp);
				configs.put(config.getId(), config);

			}
		}
		this.configs = configs;
	}

	private VipFubenConfig createVipFubenConfig(Map<String, Object> tmp){
		VipFubenConfig config = new VipFubenConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setVip(CovertObjectUtil.object2int(tmp.get("needvip")));
		return config;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public VipFubenConfig loadById(Integer id){
		return configs.get(id);
	}

}
