package com.junyou.bus.lj.configure.export;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

@Component
public class LianJinConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "LianJin.jat";

	private Map<Integer, Integer> typeMax = new HashMap<>();
	private Map<String, LianJinConfig> configMaps = new HashMap<>();
	
	private LianJinConfig createChengHaoPeiZhiConfig(Map<String, Object> tmp) {
		
		LianJinConfig config = new LianJinConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setNeedtype(CovertObjectUtil.object2int(tmp.get("needtype")));
		config.setNum(CovertObjectUtil.object2int(tmp.get("num")));
		config.setNeedcount(CovertObjectUtil.object2int(tmp.get("needcount")));
		config.setExp(CovertObjectUtil.object2int(tmp.get("exp")));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		if(ObjectUtil.isEmpty(dataList)){
			ChuanQiLog.error("LianJinRank is null!");
			return ;
		}

		Map<String, LianJinConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				LianJinConfig config = createChengHaoPeiZhiConfig(tmp);
				configMap.put(config.getNeedtype()+"_"+config.getNum(), config);
				
				Integer count = 0;
				if((count = typeMax.get(config.getNeedtype())) == null || count < config.getNum()){
					typeMax.put(config.getNeedtype(), config.getNum());
				}
			}
		}
		this.configMaps = configMap;
	}

	 
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public int getMax(int type){
		return typeMax.get(type);
	}
	
	public LianJinConfig loadByTypeAndCount(int type, int count) {
		return configMaps.get(type+"_"+count);
	}
}
