package com.junyou.bus.skill.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 转职技能转换表 
 *
 * @author ZHONGDIAN
 * @date 2016-03-15 10:39:27
 */
@Component
public class JiNengDuiYingBiaoConfigExportService extends AbsClasspathConfigureParser   {
	
	private Map<String, JiNengDuiYingBiaoConfig> configs = new HashMap<String, JiNengDuiYingBiaoConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "JiNengDuiYingBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				JiNengDuiYingBiaoConfig config = createJiNengDuiYingBiaoConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public JiNengDuiYingBiaoConfig createJiNengDuiYingBiaoConfig(Map<String, Object> tmp) {
		JiNengDuiYingBiaoConfig config = new JiNengDuiYingBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2String(tmp.get("id")));
											
		config.setJob0(CovertObjectUtil.object2String(tmp.get("job0")));
											
		config.setJob1(CovertObjectUtil.object2String(tmp.get("job1")));
											
		config.setJob2(CovertObjectUtil.object2String(tmp.get("job2")));
											
		config.setJob3(CovertObjectUtil.object2String(tmp.get("job3")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public JiNengDuiYingBiaoConfig loadById(String id){
		return configs.get(id);
	}
}