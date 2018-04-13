package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.PataConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 爬塔配置解析
 * @author LiuYu
 * 2015-6-11 上午11:50:38
 */
@Service
public class PataConfigService extends AbsClasspathConfigureParser {
	
	private final String configureName = "PaTa.jat";
	private Map<Integer,PataConfig> configs;

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,PataConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				PataConfig config = createPataConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	private PataConfig createPataConfig(Map<String, Object> tmp){
		PataConfig config = new PataConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setExp(CovertObjectUtil.obj2long(tmp.get("jiangexp")));
		config.setZq(CovertObjectUtil.obj2long(tmp.get("jiangzhen")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("jiangmoney")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
//		config.setFuhuo(true);
		String monster = CovertObjectUtil.object2String(tmp.get("bossid"));
		Map<String,Integer> map = new HashMap<>();
		map.put(monster, 1);
		config.setWantedMap(map);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public PataConfig loadById(Integer id){
		return configs.get(id);
	}
}
