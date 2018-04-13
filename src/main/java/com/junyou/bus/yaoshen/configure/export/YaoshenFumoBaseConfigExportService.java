package com.junyou.bus.yaoshen.configure.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 妖神魂魄
 * 
 * @author lxn
 * 
 */
@Component
public class YaoshenFumoBaseConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "YaoShenFuMoJiChu.jat";

	private Map<Integer, Map<Integer, YaoshenFumoBaseConfig>> configMap;
	private Map<Integer, Integer> maxLevelMap;// 左边格位升级最大等级限制map
	private Map<Integer, Integer> activeLevelMap;// 左边格位激活等级map

	private YaoshenFumoBaseConfig createFumoBaseConfig(Map<String, Object> tmp) {
		YaoshenFumoBaseConfig config = new YaoshenFumoBaseConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setId1(CovertObjectUtil.object2int(tmp.get("id1")));
		config.setId2(CovertObjectUtil.object2int(tmp.get("id2")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		String item = CovertObjectUtil.object2String(tmp.get("needitem"));
		int itemNum = CovertObjectUtil.object2int(tmp.get("count"));
		config.setConsumeitemId(item);
		config.setConsumeCount(itemNum);
		Map<String, Integer> map = new HashMap<>();
		map.put(item, itemNum);
		config.setConsumeMap(new ReadOnlyMap<>(map));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrMap(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		if (data == null) {
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		configMap = new HashMap<>();
		maxLevelMap = new HashMap<>();
		activeLevelMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				YaoshenFumoBaseConfig config = createFumoBaseConfig(tmp);
				Map<Integer, YaoshenFumoBaseConfig> attr = configMap.get(config.getId1());
				if (attr == null) {
					attr = new HashMap<>();
					configMap.put(config.getId1(), attr);
				}
				attr.put(config.getLevel(), config);
				//最大等级限制map
				Integer maxLevel = maxLevelMap.get(config.getId1());
				if (maxLevel == null || config.getLevel() > maxLevel) {
					maxLevelMap.put(config.getId1(), config.getLevel());
				}
				//格位激活等级map
				if(activeLevelMap.get(config.getId1())==null){
					activeLevelMap.put(config.getId1(), config.getId2());
				}
				
			}
		}
	}
	public Map<Integer, Integer> getActiveLevelMap() {
		return activeLevelMap;
	}
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public Map<Integer, Integer> getMaxLevelMap() {
		return maxLevelMap;
	}

	/**
	 * 统计获得的基础属性总和
	 * @param id
	 * @param level
	 * @return
	 */
	public Map<String, Long> getAllBaseAttr(Map<Integer, Integer[]> infoMap) {
		Map<String, Long> map =  new HashMap<>();
		if (infoMap != null) {
			for (Entry<Integer, Integer[]> entry : infoMap.entrySet()) {
				int id = entry.getKey();
				Integer[] data = entry.getValue();
				YaoshenFumoBaseConfig config = null;
				for (int i = 0; i < data.length; i++) {
					int level = data[i];
					config = getBaseConfigById(id, level);
					ObjectUtil.longMapAdd(map, config.getAttrMap());
				}
			}
		}
		return map;
	}

	public YaoshenFumoBaseConfig getBaseConfigById(int id, int level) {
		if (configMap.get(id) == null) {
			return null;
		}
		Map<Integer, YaoshenFumoBaseConfig> config = configMap.get(id);
		return config.get(level);
	}

}
