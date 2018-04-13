package com.junyou.bus.equip.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 附属升阶表
 * @author LiuYu
 * @date 2015-7-24 下午3:49:10
 */
@Component
public class FuShuEquipShengJiConfigExportService extends AbsClasspathConfigureParser{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "FuShuShengJie.jat";
	
	private Map<Integer,FuShuEquipShengJiConfig> configs = null;
	
	private int minLevel = Integer.MAX_VALUE;
	
	protected String getConfigureName() {
		return configureName;
	} 

	@Override
	protected void configureDataResolve(byte[] data) {
		Map<Integer,FuShuEquipShengJiConfig> configs = new HashMap<>();
		int minLevel = Integer.MAX_VALUE;
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				FuShuEquipShengJiConfig  config = createFuShuEquipShengJiConfig(tmp);
				configs.put(config.getLevel(), config);
				if(minLevel > config.getLevel()){
					minLevel = config.getLevel();
				}
			}
		}
		this.configs = configs;
		this.minLevel = minLevel;
	}
	
	private FuShuEquipShengJiConfig createFuShuEquipShengJiConfig(Map<String, Object> tmp){
		FuShuEquipShengJiConfig config = new FuShuEquipShengJiConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("zblevel"))); 
		config.setItemCount(CovertObjectUtil.object2int(tmp.get("num")));
		config.setItemId1(CovertObjectUtil.obj2StrOrNull(tmp.get("prop")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
		config.setSuccess(CovertObjectUtil.object2int(tmp.get("successrate")));
		return config;
	}
	
	public FuShuEquipShengJiConfig getConfig(int level) {
		return configs.get(level);
	}

	public int getMinLevel() {
		return minLevel;
	}
	
}