package com.junyou.bus.shenqi.configure.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ShenQiXiLianConfigExportService extends AbsClasspathConfigureParser {

	private List<ShenQiXiLianConfig> configList;

	/**
	 * configFileName
	 */
	private String configureName = "ShenQiXiLianShuXing.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		ConfigMd5SignManange.addConfigSign(configureName, data);
		Object[] dataList = GameConfigUtil.getResource(data);
		configList = new ArrayList<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShenQiXiLianConfig config = createShenQiShuXingConfig(tmp);
				configList.add(config);
			}
		}
		Collections.sort(configList);
	}

	public ShenQiXiLianConfig createShenQiShuXingConfig(Map<String, Object> tmp) {
		ShenQiXiLianConfig shenQiXiLianConfig = new ShenQiXiLianConfig();
		Map<String, JSONArray> attrMap = new HashMap<>();
		for (Entry<String, Object> entry : tmp.entrySet()) {
			String str = (String) entry.getKey();
			if (str.contains("x")) {
				String value = CovertObjectUtil.object2String(tmp.get(str));
				attrMap.put(str, JSONArray.parseArray(value));
			}
		}
		int rank = CovertObjectUtil.object2int(tmp.get("rank"));
		shenQiXiLianConfig.setXiLianMap( new ReadOnlyMap<>(attrMap));
		shenQiXiLianConfig.setRank(rank);
		return shenQiXiLianConfig;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 根据rank获得配置
	 * 
	 * @param id
	 * @return
	 */
	public ShenQiXiLianConfig loadByRank(int rank) {
		for (int i = 0; i < configList.size(); i++) {
			ShenQiXiLianConfig config = configList.get(i);
			if (rank <= config.getRank()) {
				return config;
			}
		}
		return null;
	}

	/**
	 * 获得全部的配置
	 * 
	 * @return
	 */
	public List<ShenQiXiLianConfig> getAll() {
		return configList;
	}

}
