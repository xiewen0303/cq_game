package com.junyou.bus.bag.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.bus.bag.BagContants;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
 

/**
 * 道具类型排序表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:43:38
  *@Description:
 */
@Component
public class StorageConfigExportService extends AbsClasspathConfigureParser {
	 
	/**
	  * configFileName
	 */
	private String configureName = "CangKuBiao.jat";
	
	
	private HashMap<Integer,StorageConfig> configs=null;

	/**
	 * 背包启始格位
	 */
	private int startSlot=BagContants.START_STORAGE_SLOT;
	
	
	 
	private Integer endSlot=null;
	/**
	 * 容量
	 * @return
	 */
	private int maxEndSlot=0;
  
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		
		configs=new HashMap<Integer,StorageConfig>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				StorageConfig  config=createConfig(tmp);
				configs.put(config.getId(),config);
			}
		}
	}
	
	private StorageConfig createConfig(Map<String, Object> tmp){
		StorageConfig config =new StorageConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		
		if(endSlot == null){
			endSlot=config.getId()-1;
		}
		

		if(config.getId()>maxEndSlot){
			maxEndSlot=config.getId();
		}
		
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("needtime")));
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		
//		for (StorageConfig  sConfig : configs.values()) {
//			Map<String, Integer> oldAttrs = sConfig.getAttrs(); 
//			ObjectUtil.mapAdd(attrs, oldAttrs); 
//		}
		
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	 

	public int getStartSlot() {
		return startSlot;
	}

	public int getEndSlot() {
		return endSlot;
	} 
	
	public int getMaxEndSlot() {
		return maxEndSlot;
	}

	public StorageConfig getStorageConfigBySlot(int slot){
		return configs.get(slot);
	} 
}