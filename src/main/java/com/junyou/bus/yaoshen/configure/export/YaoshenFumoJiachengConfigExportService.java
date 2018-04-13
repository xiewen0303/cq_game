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
 * @author lxn
 * 
 */
@Component
public class YaoshenFumoJiachengConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "YaoShenFuMoJiaCheng.jat";

	private  Map<Integer, YaoshenFumoJiachengConfig>  configMap;

	private YaoshenFumoJiachengConfig createFumoBaseConfig(Map<String, Object> tmp) {
		YaoshenFumoJiachengConfig config = new YaoshenFumoJiachengConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setFumolevel(CovertObjectUtil.object2int(tmp.get("fumolevel")));
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
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				YaoshenFumoJiachengConfig config = createFumoBaseConfig(tmp);
				configMap.put(config.getFumolevel(), config);
				 
			}
		}
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	/**
	 * 根据全身附魔总等级获取额外属性,每个档位累加
	 * @param countLevel
	 * @return
	 */
	public Map<String, Long>  countAttrByLevel(int countLevel) {
		Map<String, Long> countAttrMap  = new HashMap<>();
		
		for (Entry<Integer, YaoshenFumoJiachengConfig> entry : configMap.entrySet()) {
			int count  = entry.getKey();
			if(count<=countLevel){
				ObjectUtil.longMapAdd(countAttrMap, entry.getValue().getAttrMap());	
			}
		}
		return  countAttrMap;
	}

}
