package com.junyou.bus.skill.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 糖宝技能格位表 
 *
 * @author ZHONGDIAN
 * @date 2016-06-28 10:24:09
 */
@Component
public class TangBaoJiNengKaiKongBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer, TangBaoJiNengKaiKongBiaoConfig> configs = new HashMap<Integer, TangBaoJiNengKaiKongBiaoConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "TangBaoJiNengKaiKongBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TangBaoJiNengKaiKongBiaoConfig config = createTangBaoJiNengKaiKongBiaoConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public TangBaoJiNengKaiKongBiaoConfig createTangBaoJiNengKaiKongBiaoConfig(Map<String, Object> tmp) {
		TangBaoJiNengKaiKongBiaoConfig config = new TangBaoJiNengKaiKongBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
												
		config.setNum(CovertObjectUtil.object2int(tmp.get("num")));
											
		config.setNeeditem(CovertObjectUtil.object2String(tmp.get("needitem")));
											
		config.setNeedid(CovertObjectUtil.object2int(tmp.get("needid")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public TangBaoJiNengKaiKongBiaoConfig loadById(Integer id){
		return configs.get(id);
	}
}