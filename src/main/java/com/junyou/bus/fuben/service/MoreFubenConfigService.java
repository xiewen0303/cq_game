package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.HuanJingFubenConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

@Service
public class MoreFubenConfigService extends AbsClasspathConfigureParser {
	private final String configureName = "HuanJingFuBen.jat";
	private Map<Integer,HuanJingFubenConfig> configs;
	
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,HuanJingFubenConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				HuanJingFubenConfig config = createHuanJingFubenConfig(tmp);
				configs.put(config.getId(), config);
				//注册需要解析的配置
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configs = configs;
	}

	private HuanJingFubenConfig createHuanJingFubenConfig(Map<String, Object> tmp){
		HuanJingFubenConfig config = new HuanJingFubenConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		Map<String,Integer> reward = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("shoucijiangli")));
		config.setReward(reward);
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
		config.setJiangExp(CovertObjectUtil.object2int(tmp.get("jiangexp")));
		config.setJiangMoney(CovertObjectUtil.object2int(tmp.get("jiangmoney")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setTuijian(CovertObjectUtil.object2int(tmp.get("tuijian")));
		String monster = CovertObjectUtil.object2String(tmp.get("monster"));
		config.setFubenName(CovertObjectUtil.object2String(tmp.get("name")));
		if(!ObjectUtil.strIsEmpty(monster)){
			String[] monsterInfo = monster.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			Map<String,Integer> map = new HashMap<>();
			for(String info : monsterInfo){				
				map.put(info, 1);
			}
			config.setWantedMap(map);
		}
		return config;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public HuanJingFubenConfig loadById(Integer id){
		return configs.get(id);
	}

}
