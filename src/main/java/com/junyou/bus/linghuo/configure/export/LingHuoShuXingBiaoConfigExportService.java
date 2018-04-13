package com.junyou.bus.linghuo.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 灵火属性表 
 *
 * @author ZHONGDIAN
 * @date 2015-07-04 18:55:34
 */
@Component
public class LingHuoShuXingBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer,LingHuoShuXingBiaoConfig> configs = null;
	/**
	  * configFileName
	 */
	private String configureName = "LingHuoShuXingBiao.jat";
	Map<String,Long> effectAttrMap = new HashMap<>(); 
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		configs = new HashMap<Integer, LingHuoShuXingBiaoConfig>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				LingHuoShuXingBiaoConfig config = createLingHuoShuXingBiaoConfig(tmp);
				
				configs.put(config.getId(), config);
			}
		}
	}
	
	public LingHuoShuXingBiaoConfig createLingHuoShuXingBiaoConfig(Map<String, Object> tmp) {
		LingHuoShuXingBiaoConfig config = new LingHuoShuXingBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));		
		
		config.setLv(CovertObjectUtil.object2int(tmp.get("lv")));
											
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		
		config.setBaseAttrs(attrs);
		
		if(attrs != null && attrs.size() > 0){
			for (String id : attrs.keySet()) {
				if(effectAttrMap.get(id) != null){
					effectAttrMap.put(id, effectAttrMap.get(id)+attrs.get(id));
				}else{
					effectAttrMap.put(id, attrs.get(id));
				}
			}
		}
		
		if(effectAttrMap.size() > 0){
			Map<String,Long> map = new HashMap<>(); 
			map.putAll(effectAttrMap);
			config.setAttrs(new ReadOnlyMap<>(map));
		}
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public LingHuoShuXingBiaoConfig loadById(int id){
		return configs.get(id);
	}
}