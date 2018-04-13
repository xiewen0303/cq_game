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
 

/**
 * 道具类型排序表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:43:38
  *@Description:
 */
@Component
public class BagConfigExportService  extends AbsClasspathConfigureParser { 
	 
	/**
	  * configFileName
	 */
	private String configureName = "BeiBaoBiao.jat";
	
	
	private HashMap<Integer,BagConfig> qiangHuaBiaos=null;

	/**
	 * 背包启始格位
	 */
	private int bagStartSlot=BagContants.START_BAG_SLOT;
 
	 
	private Integer bagEndSlot;
	
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
		qiangHuaBiaos=new HashMap<Integer,BagConfig>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				BagConfig  config=createQiangHuaConfig(tmp);
				qiangHuaBiaos.put(config.getId(),config);
			}
		}
		 
	}
	
	private BagConfig createQiangHuaConfig(Map<String, Object> tmp){
		BagConfig config=new BagConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		
		if(bagEndSlot == null){
			bagEndSlot=config.getId()-1;
		}
		
		if(config.getId()>maxEndSlot){
			maxEndSlot=config.getId();
		}
		
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("needtime")));
		 
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	public int getBagStartSlot() {
		return bagStartSlot;
	}

	public int getBagEndSlot() {
		return bagEndSlot;
	}

	public int getMaxEndSlot() {
		return maxEndSlot;
	}
	
	public BagConfig getBagConfigBySlot(int slot){
		return qiangHuaBiaos.get(slot);
	}
}