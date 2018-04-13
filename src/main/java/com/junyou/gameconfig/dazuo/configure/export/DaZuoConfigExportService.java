package com.junyou.gameconfig.dazuo.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 打坐经验表 
 *
 * @author wind
 * @date 2015-03-20 11:21:39
 */
@Component
public class DaZuoConfigExportService extends AbsClasspathConfigureParser {
	
 
	Map<Integer,DaZuoConfig> dazuoConfigs =null;
	/**
	  * configFileName
	 */
	private String configureName = "DaZuoBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		dazuoConfigs = new HashMap<Integer, DaZuoConfig>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DaZuoConfig config = createDaZuoConfig(tmp);
				dazuoConfigs.put(config.getLevel(), config);	 
			}
			
		}
	}
	
	public DaZuoConfig createDaZuoConfig(Map<String, Object> tmp) {
		DaZuoConfig config = new DaZuoConfig();	
							
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
											
		config.setSxexp(CovertObjectUtil.object2int(tmp.get("sxexp")));
											
		config.setSkill1exp(CovertObjectUtil.object2int(tmp.get("skill1exp")));
											
		config.setSxzhenqi(CovertObjectUtil.object2int(tmp.get("sxzhenqi")));
											
		config.setZhenqi(CovertObjectUtil.object2int(tmp.get("zhenqi")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public DaZuoConfig loadById(int level){
		return dazuoConfigs.get(level);
	}
}