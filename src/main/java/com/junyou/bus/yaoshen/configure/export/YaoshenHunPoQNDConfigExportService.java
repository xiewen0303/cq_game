package com.junyou.bus.yaoshen.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 *  魂魄潜能丹
 * @author lxn
 *
 */
@Component
public class YaoshenHunPoQNDConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "HunPoQianNengBiao.jat";
	private Map<String, Map<String, Long>> configMap=new HashMap<>();
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				String id = CovertObjectUtil.object2String(tmp.get("id"));
				Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
				configMap.put(id,new ReadOnlyMap<>(attrs));
			}
		}
	}
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	public Map<String, Map<String, Long>>  getConfigMap() {
		return configMap;
	}
 

}
