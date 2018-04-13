package com.junyou.bus.tongtian.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 升阶表
 */
@Component
public class TongtianRoadMaxAttrConfigExportService extends AbsClasspathConfigureParser {
	
	private  Map<Integer, Map<String, Long>> batiMap  = new HashMap<>();
	private  Map<Integer, Map<String, Long>> mowenMap = new HashMap<>();
	private  Map<Integer, Map<String, Long>> hunpoMap = new HashMap<>();
	private  Map<Integer, Map<String, Long>> moyinMap = new HashMap<>();
	
	/**
	 * configFileName
	 */
	private String configureName = "TongTianYaoShenZhuFu.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null) {
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				 createMapConfig(tmp);
			}
		}
	}
	private void createMapConfig(Map<String, Object> tmp) {
		int type =  CovertObjectUtil.object2int(tmp.get("type"));
		int level =  CovertObjectUtil.object2int(tmp.get("level"));
		Map<String, Long> attrs = null;
		
		if(type==1){
			attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
			batiMap.put(level, new ReadOnlyMap<>(attrs));
		}
		if(type==2){
			attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
			mowenMap.put(level, new ReadOnlyMap<>(attrs));
		}
		if(type==3){
			attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
			hunpoMap.put(level, new ReadOnlyMap<>(attrs));
		}
		if(type==4){
			attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
			moyinMap.put(level, new ReadOnlyMap<>(attrs));
		}
	}

	protected String getConfigureName() {
		return configureName;
	}
	public Map<Integer, Map<String, Long>> getBatiMap() {
		return batiMap;
	}
	public Map<Integer, Map<String, Long>> getMowenMap() {
		return mowenMap;
	}
	public Map<Integer, Map<String, Long>> getMoyinMap() {
		return moyinMap;
	}
	public Map<Integer, Map<String, Long>> getHunpoMap() {
		return hunpoMap;
	}
}