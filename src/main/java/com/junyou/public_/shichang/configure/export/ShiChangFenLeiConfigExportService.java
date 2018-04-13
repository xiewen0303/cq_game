package com.junyou.public_.shichang.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 市场 
 *
 * @author wind
 * @date 2015-04-10 15:27:13
 */
@Component
public class ShiChangFenLeiConfigExportService extends AbsClasspathConfigureParser  {
	
	/**
	  * configFileName
	 */
	private String configureName = "ShiChangFenLei.jat";
	
	private Map<String,Integer> shiChangFenLeiConfigs = null;
	
	/**
	 * 分装备和非装备类中类型获得唯一的Key
	 * @param needJob
	 * @param eqpart
	 * @param type
	 * @return
	 */
	private String getShiChangType(int needJob,int eqpart,int type){
		if(type == GoodsCategory.EQUIP_TYPE){
			return needJob+"_"+eqpart+"_"+0;
		}else{
			return 0+"_"+0+"_"+type;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		 shiChangFenLeiConfigs = new HashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShiChangFenLeiConfig config = createShiChangFenLeiConfig(tmp);
				int needJob = config.getNeedjob();
				int eqpart = config.getEqpart();
				
				String type2 = config.getType2();
				if(!"".equals(type2)){
					String[] type2s = type2.split("\\|");
					for (String t2 : type2s) {
						String key =getscType(needJob, eqpart, t2);
						shiChangFenLeiConfigs.put(key, config.getId());
					}
				}else{
					String key =getscType(needJob, eqpart, type2);
					shiChangFenLeiConfigs.put(key, config.getId());
				}
			}
		}
	}
	
	private String getscType(int needJob,int eqpart,String type){
			return needJob+"_"+eqpart+"_"+("".equals(type)?0:type); 
	}
	
	public ShiChangFenLeiConfig createShiChangFenLeiConfig(Map<String, Object> tmp) {
		
		ShiChangFenLeiConfig config = new ShiChangFenLeiConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setNeedjob(CovertObjectUtil.object2int(tmp.get("needjob")));
											
		config.setEqpart(CovertObjectUtil.object2int(tmp.get("eqpart")));
											
		config.setType1(CovertObjectUtil.object2int(tmp.get("type1"))); 
											
		config.setType2(CovertObjectUtil.object2String(tmp.get("type2")));
											
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public Integer getType(int needJob,int eqpart,int type){
		String key = getShiChangType(needJob, eqpart, type);
		return shiChangFenLeiConfigs.get(key);
	}
}