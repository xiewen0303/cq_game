package com.junyou.bus.shizhuang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Component
public class ShiZhuangJinJieConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "ShiZhuangJinJie.jat";

	private Map<String,ShiZhuangJinJieConfig> configMap;
	/**
	 * 进阶最高等级
	 */
	private Map<Integer,Integer> endLevelMap = new HashMap<>();
	
	private ShiZhuangJinJieConfig createShiZhuangJinJieConfigConfig(Map<String, Object> tmp) {
		ShiZhuangJinJieConfig config = new ShiZhuangJinJieConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("lv")));
		int needCount = CovertObjectUtil.object2int(tmp.get("number"));
		Map<String,Integer> items = new HashMap<>();
		items.put(CovertObjectUtil.obj2StrOrNull(tmp.get("id1")), needCount);
		config.setCostItem(items);
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setMallid(CovertObjectUtil.object2String(tmp.get("mallid")));
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		config.setSex(CovertObjectUtil.object2int(tmp.get("sex")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		int endLevel = 0;
		Map<String,ShiZhuangJinJieConfig> configMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShiZhuangJinJieConfig config = createShiZhuangJinJieConfigConfig(tmp);
				configMap.put(config.getId()+"_"+config.getLevel(), config);
				if(endLevelMap.containsKey(config.getId())){
					endLevel = endLevelMap.get(config.getId()) < config.getLevel()?config.getLevel():endLevelMap.get(config.getId()); 
					endLevelMap.put(config.getId(), endLevel);
				}else{
					endLevelMap.put(config.getId(), endLevel);
				}
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ShiZhuangJinJieConfig getConfig(String id){
		return configMap==null ? null: configMap.get(id);
	}
	
	public Integer getMaxLevel(Integer id){
		return endLevelMap.get(id);
	}
	
	public static void main(String[] args) {
		int a = 1;
		int b = 2;
		System.out.println(a+""+b);
		System.out.println(GameSystemTime.getSystemMillTime());
	}
	
}
