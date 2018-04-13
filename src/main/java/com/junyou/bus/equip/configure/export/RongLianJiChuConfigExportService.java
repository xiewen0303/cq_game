package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @description 熔炼基础表 
 *
 * @author ZHONGDIAN
 * @date 2015-05-31 18:32:40
 */
@Component
public class RongLianJiChuConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer, RongLianJiChuConfig> ronglianjichu = new HashMap<>();
	
	/**
	  * configFileName
	 */
	private String configureName = "RongLianJiChu.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				RongLianJiChuConfig config = createRongLianJiChuBiao(tmp);
								
				ronglianjichu.put(1, config);
			}
		}
	}
	
	public RongLianJiChuConfig createRongLianJiChuBiao(Map<String, Object> tmp) {
		RongLianJiChuConfig config = new RongLianJiChuConfig();	
			
		config.setRlzmax(CovertObjectUtil.object2int(tmp.get("rlzmax")));
		
		config.setXuantie(CovertObjectUtil.object2int(tmp.get("xuantie")));
		
		int index = 1;
		Map<Object[],Float> itemMap = new HashMap<Object[], Float>();
		while(true){
			String item = CovertObjectUtil.obj2StrOrNull(tmp.get("item"+index));
			if(ObjectUtil.strIsEmpty(item)){
				break;
			}
			itemMap.put(new Object[]{item,CovertObjectUtil.object2int(tmp.get("count"+index))}, CovertObjectUtil.obj2float(tmp.get("odds"+index)));
			index++;
		}
		config.setItemMap(itemMap);
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public RongLianJiChuConfig loadById(int id){
		return ronglianjichu.get(id);
	}

}