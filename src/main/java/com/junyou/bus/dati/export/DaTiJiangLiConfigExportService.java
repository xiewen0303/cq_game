package com.junyou.bus.dati.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.dati.configure.DaTiJiangLiConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class DaTiJiangLiConfigExportService extends AbsClasspathConfigureParser{

	private String configureName="DaTiJiangLi.jat";
	
	//private List<DaTiJiangLiConfig> daTiJiangLiConfigs;
	Map<Integer, DaTiJiangLiConfig> configMap;
	@Override
	protected void configureDataResolve(byte[] data) {
		//List<DaTiJiangLiConfig> configs=new ArrayList<>();
		Map<Integer, DaTiJiangLiConfig> configMap=new HashMap<Integer, DaTiJiangLiConfig>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object object : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>) object;
			if (null != tmp) {
				DaTiJiangLiConfig config = createDaTiJiangLi(tmp);
				process(config, configMap);
				//configs.add(config);
			}
		}
		this.configMap=configMap;
		//this.daTiJiangLiConfigs=configs;
	}
	
	private void process(DaTiJiangLiConfig config ,Map<Integer, DaTiJiangLiConfig> configMap){
		for (int level = config.getMinLevel(); level <= config.getMaxLevel(); level++) {
			configMap.put(level, config);
		}
	}

	private DaTiJiangLiConfig createDaTiJiangLi(Map<String, Object> tmp) {
		DaTiJiangLiConfig daTiJiangLiConfig=new DaTiJiangLiConfig();
		daTiJiangLiConfig.setMinLevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		daTiJiangLiConfig.setMaxLevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
		daTiJiangLiConfig.setExp(CovertObjectUtil.object2int(tmp.get("exp")));
		daTiJiangLiConfig.setZhenqi(CovertObjectUtil.object2int(tmp.get("zhenqi")));
		daTiJiangLiConfig.setJifen(CovertObjectUtil.object2int(tmp.get("jifen")));
		daTiJiangLiConfig.setMiao(CovertObjectUtil.object2int(tmp.get("miao")));
		daTiJiangLiConfig.setExp1(CovertObjectUtil.object2int(tmp.get("exp1")));
		daTiJiangLiConfig.setZhenqi1(CovertObjectUtil.object2int(tmp.get("zhenqi1")));
		daTiJiangLiConfig.setJiangItem(String.valueOf(tmp.get("jiangitem")));
		daTiJiangLiConfig.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		daTiJiangLiConfig.setDiyi(String.valueOf(tmp.get("diyi")));
		daTiJiangLiConfig.setDier(String.valueOf(tmp.get("dier")));
		daTiJiangLiConfig.setDisan(String.valueOf(tmp.get("disan")));
		daTiJiangLiConfig.setPu(String.valueOf(tmp.get("pu")));
		return daTiJiangLiConfig;
	}


	@Override
	protected String getConfigureName() {
		return configureName;
	}


/*
	public List<DaTiJiangLiConfig> getDaTiJiangLiConfigs() {
		return daTiJiangLiConfigs;
	}
	*/
	public Map<Integer,DaTiJiangLiConfig> getDaTiJiangLiConfigs() {
		return configMap;
	}
	
	public DaTiJiangLiConfig getConfigByLevel(int level){
		return configMap.get(level);
	}

}
