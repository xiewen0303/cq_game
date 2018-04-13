package com.junyou.bus.tafang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.tafang.configure.TaFangExpConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-12 下午4:28:26
 */
@Service
public class TaFangExpConfigServiceExport extends AbsClasspathConfigureParser{

	private String configureName = "TaFangJiangLi.jat";
	private Map<Integer,TaFangExpConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Map<Integer, TaFangExpConfig> tempConfigs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TaFangExpConfig config = createTaFangExpConfig(tmp);
				tempConfigs.put(config.getId(), config);
			}
		}
		
		this.configs = tempConfigs;
	}
	
	private TaFangExpConfig createTaFangExpConfig(Map<String, Object> tmp){
		TaFangExpConfig config = new TaFangExpConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setRate(CovertObjectUtil.object2int(tmp.get("jiangbs")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public TaFangExpConfig getConfigById(int id){
		if(configs == null){
			return null;
		}
		return configs.get(id);
	}
	
}
