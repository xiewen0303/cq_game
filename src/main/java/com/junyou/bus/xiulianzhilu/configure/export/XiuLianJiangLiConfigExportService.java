package com.junyou.bus.xiulianzhilu.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class XiuLianJiangLiConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "XiuLianJiangLi.jat";

	private Map<Integer, XiuLianJiangLiConfig> configMap = new HashMap<>();
	
	private XiuLianJiangLiConfig createChengHaoPeiZhiConfig(Map<String, Object> tmp) {
		XiuLianJiangLiConfig config = new XiuLianJiangLiConfig();
		 config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setChenghaoJifen(CovertObjectUtil.object2Integer(tmp.get("jifen")));
		config.setJlType(CovertObjectUtil.object2Integer(tmp.get("type")));
		config.setDay(CovertObjectUtil.object2Integer(tmp.get("day")));
		config.setJlLevel(CovertObjectUtil.object2Integer(tmp.get("level")));
		config.setSpItem(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("spitem")).replaceAll(GameConstants.CONFIG_SPLIT_CHAR, "")));
		config.setTime(CovertObjectUtil.object2Integer(tmp.get("time")));
		config.setJfJiaZhi(CovertObjectUtil.object2Integer(tmp.get("value")));

		Map<Integer, String[]> itemMap = new HashMap<>();
		String sitem = "item";
		for (int i = 1; i < 10; i++) {
			String item = CovertObjectUtil.object2String(tmp.get(sitem+i));
			if(item.isEmpty()){
				break;
			}
			item = item.replaceAll(GameConstants.CONFIG_SPLIT_CHAR, "");
			String[] str = item.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			itemMap.put(i-1, str);
		}
		if(itemMap.size() > 0){
			config.setPtItem(itemMap);
		}
		
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer, XiuLianJiangLiConfig> configMap = new HashMap<Integer, XiuLianJiangLiConfig>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				XiuLianJiangLiConfig config = createChengHaoPeiZhiConfig(tmp);
				configMap.put(config.getId(), config);
			}
		}
		this.configMap = configMap;
	}

	
	/**
	 * 获取最大奖池等级的配置
	 * @param day
	 * @param type
	 * @return
	 */
	public XiuLianJiangLiConfig getConfigByMaxJcLevel(Integer type){
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		XiuLianJiangLiConfig config = null;
		for(XiuLianJiangLiConfig c : configMap.values()) {
			if(c.getJlType().intValue() == type){
				if(config == null || config.getJlLevel() < c.getJlLevel()){
					config = c;
				}
			}
		}
		return config;
	}
	public XiuLianJiangLiConfig getConfigByDayAndType(Integer day,Integer type){
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		for(XiuLianJiangLiConfig c : configMap.values()) {
				if(CovertObjectUtil.object2int(c.getDay()) == day && c.getJlType().intValue() == type){
					return c;
				}
		}
		return null;
	}
	public XiuLianJiangLiConfig getConfigByJcLevelAndType(Integer jcLevel,Integer type){
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		for(XiuLianJiangLiConfig c : configMap.values()) {
			if(c.getJlLevel().intValue() == jcLevel && c.getJlType().intValue() == type){
				return c;
			}
		}
		return null;
	}
	public XiuLianJiangLiConfig getConfigByJcLevel(Integer jcLevel){
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		for(XiuLianJiangLiConfig c : configMap.values()) {
			if(c.getJlLevel().intValue() == jcLevel){
				return c;
			}
		}
		return null;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public XiuLianJiangLiConfig loadById(Integer id) {
		return configMap.get(id);
	}

}
