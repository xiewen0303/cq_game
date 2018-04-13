package com.junyou.bus.equip.configure.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description 装备升阶表 
 *
 * @author ZHONGDIAN
 * @date 2015-06-01 17:26:39
 */
@Component
public class ShengJiBiaoConfigExportService extends AbsClasspathConfigureParser{

	Map<Integer, ShengJiBiaoConfig> shengjiMap = new HashMap<Integer, ShengJiBiaoConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "ShengJiBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShengJiBiaoConfig config = createShengJiBiaoConfig(tmp);
								
				shengjiMap.put(config.getZblevel(), config);
			}
		}
	}
	
	public ShengJiBiaoConfig createShengJiBiaoConfig(Map<String, Object> tmp) {
		ShengJiBiaoConfig config = new ShengJiBiaoConfig();	
							
		config.setSuccessrate(CovertObjectUtil.object2int(tmp.get("successrate")));
											
		config.setNum(CovertObjectUtil.object2int(tmp.get("num")));
											
		config.setZblevel(CovertObjectUtil.object2int(tmp.get("zblevel")));
											
		config.setProp(CovertObjectUtil.object2String(tmp.get("prop")));
											
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
											
		config.setMincs(CovertObjectUtil.object2int(tmp.get("mincs")));
		
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
							
		return config;
	}
	
	public List<String> getConsumeIds(String id1) {
		List<String> result = new ArrayList<>();
		if(id1 != null){
			List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
			result.addAll(ids);
		}
		return result; 
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ShengJiBiaoConfig loadById(Integer id){
		return shengjiMap.get(id);
	}
}