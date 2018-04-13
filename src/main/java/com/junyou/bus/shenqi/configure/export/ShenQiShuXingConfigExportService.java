package com.junyou.bus.shenqi.configure.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ShenQiShuXingConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, ShenQiShuXingConfig> configs = null;

	private List<ShenQiShuXingConfig> configList = null;
	/**
	 * configFileName
	 */
	private String configureName = "ShenQiShuXing.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, ShenQiShuXingConfig>();
		configList = new ArrayList<ShenQiShuXingConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShenQiShuXingConfig config = createShenQiShuXingConfig(tmp);
				configs.put(config.getId(), config);
				configList.add(config);
			}
		}
		Collections.sort(configList);
	}

	public ShenQiShuXingConfig createShenQiShuXingConfig(Map<String, Object> tmp) {
		ShenQiShuXingConfig config = new ShenQiShuXingConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));

		config.setName(CovertObjectUtil.object2String(tmp.get("name")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));

		String condition = CovertObjectUtil.object2String(tmp.get("condition"));
		List<Integer> conditionList = new ArrayList<Integer>();
		String[] conditionArray = condition.split("\\|");
		for (String e : conditionArray) {
			conditionList.add(Integer.parseInt(e));
		}
		config.setConditionList(conditionList);

		List<Integer> needCountList = new ArrayList<Integer>();
		String needCount = CovertObjectUtil.object2String(tmp.get("needcount"));
		String[] needCountArray = needCount.split("\\|");
		for (String e : needCountArray) {
			if(e.contains(":")){
				Map<String, Integer> needItem = ConfigAnalysisUtils
						.getConfigMap(e);
				config.setNeedItem(new ReadOnlyMap<>(needItem));
				needCountList.add(null);
			}else{
				needCountList.add(Integer.parseInt(e));
			}
		}
		config.setNeedCountList(needCountList);
		
		config.setSkill(CovertObjectUtil.object2String(tmp.get("skill")));
		config.setSkill2(CovertObjectUtil.object2String(tmp.get("skill2")));
		return config;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 根据id获得配置
	 * 
	 * @param id
	 * @return
	 */
	public ShenQiShuXingConfig loadById(int id) {
		return configs.get(id);
	}

	/**
	 * 获得全部的配置
	 * 
	 * @return
	 */
	public List<ShenQiShuXingConfig> getAll() {
		return configList;
	}

}
