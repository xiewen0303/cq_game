package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.DayFubenConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class DayFubenConfigService extends AbsClasspathConfigureParser {
	private final String configureName = "DanRenFuBenBiao.jat";
	private Map<Integer,DayFubenConfig> configs;
	
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,DayFubenConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DayFubenConfig config = createDayFubenConfig(tmp);
				configs.put(config.getId(), config);

				//注册需要解析的配置
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configs = configs;
	}

	private DayFubenConfig createDayFubenConfig(Map<String, Object> tmp){
		DayFubenConfig config = new DayFubenConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
//		config.setFuhuo(CovertObjectUtil.object2int(tmp.get("life")) == 1);
		config.setCount(CovertObjectUtil.object2int(tmp.get("num")));
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		Map<String,Integer> first = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("first")));
		config.setFirst(first);
		Map<String,Integer> dayFirst = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("firstday")));
		config.setDayFirst(dayFirst);
		Map<String,Integer> prop = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("prop")));
		config.setProp(prop);
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
		config.setExp(CovertObjectUtil.object2int(tmp.get("exp")));
		return config;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public DayFubenConfig loadById(Integer id){
		return configs.get(id);
	}

}
