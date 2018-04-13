package com.junyou.bus.tafang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.tafang.configure.TaFangNpcLevelConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-9 下午6:29:44
 */
@Service
public class TaFangNpcLevelConfigExportService extends AbsClasspathConfigureParser {
	
	private String configureName = "TaFangShengJi.jat";
	
	private Map<String, TaFangNpcLevelConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Map<String, TaFangNpcLevelConfig> tempConfigs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TaFangNpcLevelConfig config = createTaFangNpcLevelConfig(tmp);
				tempConfigs.put(config.getNpcId(), config);
			}
		}
		
		this.configs = tempConfigs;
	}
	
	private TaFangNpcLevelConfig createTaFangNpcLevelConfig(Map<String, Object> tmp){
		TaFangNpcLevelConfig config = new TaFangNpcLevelConfig();
		config.setNpcId(CovertObjectUtil.object2String(tmp.get("npcid")));
		config.setNextId(CovertObjectUtil.object2String(tmp.get("npcid1")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public TaFangNpcLevelConfig getConfig(String id){
		if(configs == null){
			return null;
		}
		return configs.get(id);
	}
	
}
