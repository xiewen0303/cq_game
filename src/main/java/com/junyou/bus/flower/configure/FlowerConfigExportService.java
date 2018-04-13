package com.junyou.bus.flower.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-11-2 下午2:29:27
 */
@Service
public class FlowerConfigExportService extends AbsClasspathConfigureParser {
	private String configureName = "XianHuaPeiZhiBiao.jat";
	private Map<String,FlowerConfig> configs;
	
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		  configs = new HashMap<>();
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				FlowerConfig config = create(tmp);
				configs.put(config.getGoodId(), config);
				 
			}
		}
	}

	private FlowerConfig create(Map<String, Object> tmp) {
		FlowerConfig config = new FlowerConfig();
		config.setConfigId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setGoodId(CovertObjectUtil.object2String(tmp.get("itemid")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setNotice(CovertObjectUtil.object2int(tmp.get("gonggao")));
		config.setCharmValue(CovertObjectUtil.object2int(tmp.get("meilivalue")));
		return config;
	}
 
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public FlowerConfig loadByConfigId(String goodId){
		
		return configs.get(goodId);
	}
	 
	
}
