package com.junyou.bus.chengjiu.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 成就配置表 
 *
 * @author ZHONGDIAN
 * @date 2015-06-26 15:24:01
 */
@Component
public class ChengJiuPeiZhiConfigExportService extends AbsClasspathConfigureParser  {
	
	private Map<Integer,ChengJiuPeiZhiConfig> configs = new HashMap<Integer, ChengJiuPeiZhiConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "ChengJiuPeiZhi.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ChengJiuPeiZhiConfig config = createChengJiuPeiZhiConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public ChengJiuPeiZhiConfig createChengJiuPeiZhiConfig(Map<String, Object> tmp) {
		ChengJiuPeiZhiConfig config = new ChengJiuPeiZhiConfig();	
							
		config.setType(CovertObjectUtil.object2int(tmp.get("type1")));
											
		config.setCjvalue(CovertObjectUtil.object2int(tmp.get("cjvalue")));
											
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setNumber(CovertObjectUtil.object2int(tmp.get("data1")));
		
		config.setChenghao(CovertObjectUtil.object2String(tmp.get("id1")));
		
		config.setPageType(CovertObjectUtil.object2int(tmp.get("type")));
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		if(attrs != null && attrs.size() > 0){
			config.setAttrs(new ReadOnlyMap<>(attrs));
		}
		
		config.setItems(ConfigAnalysisUtils.cover2Map(CovertObjectUtil.obj2StrOrNull(tmp.get("item"))));
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ChengJiuPeiZhiConfig loadById(Integer id){
		return configs.get(id);
	}
	
	public List<ChengJiuPeiZhiConfig> loadByType(final Integer type) {
		
		List<ChengJiuPeiZhiConfig> list = new ArrayList<>();
		for (Integer key : configs.keySet()) {
			ChengJiuPeiZhiConfig config = configs.get(key);
			if(config.getType().intValue() == type){
				list.add(config);
			}
		}
		return list;
	}
	/**
	 * 根据成就类型和成就值获取成就配置
	 * @param type
	 * @param number
	 * @return
	 */
	public List<ChengJiuPeiZhiConfig> loadByTypeAndNumber(final Integer type,final Integer number ) {
		
		List<ChengJiuPeiZhiConfig> list = new ArrayList<>();
		for (Integer key : configs.keySet()) {
			ChengJiuPeiZhiConfig config = configs.get(key);
			if(config.getType().intValue() == type && config.getNumber().intValue() == number){
				list.add(config);
			}
		}
		return list;
	}	
	
}