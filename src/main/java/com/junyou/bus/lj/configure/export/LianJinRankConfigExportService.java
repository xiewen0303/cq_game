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
public class LianJinRankConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "LianJinRank.jat";

	private Map<Integer, LianJinRankConfig> configMap = new HashMap<>();
	private int maxLevel;
	
	private LianJinRankConfig createChengHaoPeiZhiConfig(Map<String, Object> tmp) {
		
		LianJinRankConfig config = new LianJinRankConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLvl(CovertObjectUtil.object2int(tmp.get("lvl")));
		config.setAwards(CovertObjectUtil.object2Map(tmp.get("libao")));
		config.setBasemoney(CovertObjectUtil.object2int(tmp.get("basemoney")));
		config.setExtramoney(CovertObjectUtil.object2int(tmp.get("extramoney")));
		config.setBasepercent(CovertObjectUtil.object2int(tmp.get("basepercent")));
		config.setMaxexp(CovertObjectUtil.object2int(tmp.get("maxexp")));
		
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		if(ObjectUtil.isEmpty(dataList)){
			ChuanQiLog.error("LianJinRank is null!");
			return ;
		}

		Map<Integer, LianJinRankConfig> configMap = new HashMap<Integer, LianJinRankConfig>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				LianJinRankConfig config = createChengHaoPeiZhiConfig(tmp);
				configMap.put(config.getLvl(), config);
				
				if(config.getLvl() > maxLevel){
					this.maxLevel = config.getLvl();
				}
			}
		}
		this.configMap = configMap;
	}

	 
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public LianJinRankConfig loadByLevel(Integer level) {
		return configMap.get(level);
	}

	public int getMaxLevel() {
		return maxLevel;
	}

}
