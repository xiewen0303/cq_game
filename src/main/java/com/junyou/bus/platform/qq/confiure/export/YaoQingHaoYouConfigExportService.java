package com.junyou.bus.platform.qq.confiure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 腾讯邀请好友 
 *
 * @author ZHONGDIAN
 * @date 2015-12-30 17:23:34
 */
@Component
public class YaoQingHaoYouConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer,YaoQingHaoYouConfig> configs =  new HashMap<Integer,YaoQingHaoYouConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "YaoQingHaoYou.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				YaoQingHaoYouConfig config = createYaoQingHaoYouConfig(tmp);
								
				configs.put(config.getId(),config);
			}
		}
	}
	
	public YaoQingHaoYouConfig createYaoQingHaoYouConfig(Map<String, Object> tmp) {
		YaoQingHaoYouConfig config = new YaoQingHaoYouConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setFnum(CovertObjectUtil.object2int(tmp.get("fnum")));
											
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
											
		config.setItem(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem"))));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public YaoQingHaoYouConfig loadById(Integer id){
		return configs.get(id);
	}
}