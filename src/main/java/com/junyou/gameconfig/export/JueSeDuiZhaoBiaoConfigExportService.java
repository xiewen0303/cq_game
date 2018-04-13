package com.junyou.gameconfig.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 角色对照表
 * @author LiuYu
 * @date 2015-8-8 下午4:15:08
 */
@Component
public class JueSeDuiZhaoBiaoConfigExportService extends AbsClasspathConfigureParser{
	
	/**
	  * configFileName
	 */
	private String configureName = "JueSeDuiZhaoBiao.jat";
	
	private Map<Integer, JueSeDuiZhaoBiaoConfig> configs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, JueSeDuiZhaoBiaoConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				
				JueSeDuiZhaoBiaoConfig config = createJueSeDuiZhaoBiaoConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	private JueSeDuiZhaoBiaoConfig createJueSeDuiZhaoBiaoConfig(Map<String, Object> tmp) {
		JueSeDuiZhaoBiaoConfig config = new JueSeDuiZhaoBiaoConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setSex(CovertObjectUtil.object2int(tmp.get("sex")));
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public int getSex(Integer id){
		JueSeDuiZhaoBiaoConfig config = configs.get(id);
		if(config != null){
			return config.getSex();
		}
		return 0;
	}
}