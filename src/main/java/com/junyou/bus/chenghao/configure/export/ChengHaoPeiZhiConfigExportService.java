package com.junyou.bus.chenghao.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.bus.chenghao.constants.ChenghaoConstants;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ChengHaoPeiZhiConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "ChenHaoPeiZhi.jat";

	private Map<Integer, ChengHaoPeiZhiConfig> configMap;
	
	private ChengHaoPeiZhiConfig createChengHaoPeiZhiConfig(
			Map<String, Object> tmp) {
		ChengHaoPeiZhiConfig config = new ChengHaoPeiZhiConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setType1(CovertObjectUtil.object2int(tmp.get("type1")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setRes(CovertObjectUtil.object2String(tmp.get("res")));
		config.setNeedlevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setNeeditem(CovertObjectUtil.object2String(tmp.get("needitem")));
		config.setId1(CovertObjectUtil.object2int(tmp.get("id1")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setPingtaiid(CovertObjectUtil.object2String(tmp.get("pingtaiid")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));

		Map<String, Long> extraAttrs = new HashMap<String, Long>();
		String p1 = CovertObjectUtil.object2String(tmp.get("pro1"));
		Long pV1 = CovertObjectUtil.object2Long(tmp.get("provalue1"));
		String p2 = CovertObjectUtil.object2String(tmp.get("pro2"));
		Long pV2 = CovertObjectUtil.object2Long(tmp.get("provalue2"));
		String p3 = CovertObjectUtil.object2String(tmp.get("pro3"));
		Long pV3 = CovertObjectUtil.object2Long(tmp.get("provalue3"));
		if (p1 != null && pV1 != null) {
			extraAttrs.put(p1, pV1);
		}
		if (p2 != null && pV2 != null) {
			extraAttrs.put(p2, pV2);
		}
		if (p3 != null && pV3 != null) {
			extraAttrs.put(p3, pV3);
		}
		if (extraAttrs.size() > 0) {
			config.setExtraAttrs(extraAttrs);
		}
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer, ChengHaoPeiZhiConfig> configMap = new HashMap<Integer, ChengHaoPeiZhiConfig>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ChengHaoPeiZhiConfig config = createChengHaoPeiZhiConfig(tmp);
				configMap.put(config.getId(), config);
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ChengHaoPeiZhiConfig loadById(Integer id) {
		return configMap.get(id);
	}

}
