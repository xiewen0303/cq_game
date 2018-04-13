package com.junyou.bus.equip.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 神武装备升阶表 
 *
 * @author ZHONGDIAN
 * @date 2016-03-22 16:51:37
 */
@Component
public class ShenWuJinJieBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<String, ShenWuJinJieBiaoConfig> configs = new HashMap<String, ShenWuJinJieBiaoConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "ShenWuJinJieBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShenWuJinJieBiaoConfig config = createShenWuJinJieBiaoConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public ShenWuJinJieBiaoConfig createShenWuJinJieBiaoConfig(Map<String, Object> tmp) {
		ShenWuJinJieBiaoConfig config = new ShenWuJinJieBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2String(tmp.get("id")));
											
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
											
		config.setSuccessrate(CovertObjectUtil.object2int(tmp.get("successrate")));
											
		config.setNextid(CovertObjectUtil.object2String(tmp.get("nextid")));
		
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		
		String item = CovertObjectUtil.object2String(tmp.get("needitem"));
		if(item != null && !"".equals(item)){
			String[] str = item.split(":");
			config.setNeeditem(str[0]);
			config.setCount(Integer.parseInt(str[1]));
		}
		
							
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
	
	public ShenWuJinJieBiaoConfig loadById(String id){
		return configs.get(id);
	}
}