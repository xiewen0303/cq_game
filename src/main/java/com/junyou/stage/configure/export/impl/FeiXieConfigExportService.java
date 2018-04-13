package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @description 飞鞋表 
 *
 * @author DaoZhen Yuan
 * @date 2015-03-11 15:45:46
 */
@Service
public class FeiXieConfigExportService extends AbsClasspathConfigureParser{
	
	private Map<String,FeiXieConfig> feixieMap;
	
	/**
	  * configFileName
	 */
	private String configureName = "FeiXie.jat";
	
	private String zuobiaoChar = "zuobiao";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		
		Map<String,FeiXieConfig> tmpFeixieMap = new HashMap<String, FeiXieConfig>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				FeiXieConfig config = createFeiXieConfig(tmp);
				tmpFeixieMap.put(config.getId(), config);
			}
		}
		
		this.feixieMap = tmpFeixieMap;
	}
	
	public FeiXieConfig createFeiXieConfig(Map<String, Object> tmp) {
		FeiXieConfig config = new FeiXieConfig();	
		config.setId(CovertObjectUtil.object2String(tmp.get("id")));					
		config.setFree(CovertObjectUtil.object2Boolean(tmp.get("free")));
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
		
		List<int[]> zuobiaos = new ArrayList<int[]>();
		for (int i = 1; i < 20; i++) {
			String value = CovertObjectUtil.object2String(tmp.get(zuobiaoChar+i));
			if(!CovertObjectUtil.isEmpty(value)){
				String[] tmpArr = value.split("\\|");
				zuobiaos.add(new int[]{Integer.parseInt(tmpArr[0]),Integer.parseInt(tmpArr[1])});
			}
		}									
		
		if(zuobiaos.size() > 0){
			config.setZuobiaos(zuobiaos);
		}
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public FeiXieConfig loadById(String id){
		return feixieMap.get(id);
	}
}