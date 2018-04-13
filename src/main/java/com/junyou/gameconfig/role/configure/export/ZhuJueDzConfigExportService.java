package com.junyou.gameconfig.role.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 角色对照表
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-9 上午11:34:05
 */
@Component
public class ZhuJueDzConfigExportService extends AbsClasspathConfigureParser{

	
	/**
	 * 配置名
	 */
	private String configureName = "JueSeDuiZhaoBiao.jat";
	private Map<Integer,ZhuJueDzConfig> configs;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,ZhuJueDzConfig> configs = new HashMap<>();
		for (Object obj:dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhuJueDzConfig zhuJueDzConfig = createZhuJueDzConfig(tmp);
				configs.put(zhuJueDzConfig.getConfigId(),zhuJueDzConfig);
			}
		}
		this.configs = configs;
	}
	
	public ZhuJueDzConfig createZhuJueDzConfig(Map<String, Object> tmp) {
		ZhuJueDzConfig zhuJueDzConfig = new ZhuJueDzConfig();
		
		zhuJueDzConfig.setConfigId(CovertObjectUtil.object2int(tmp.get("id")));
		zhuJueDzConfig.setSex(CovertObjectUtil.object2int(tmp.get("sex")));
		
		return zhuJueDzConfig;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ZhuJueDzConfig loadById(Integer id){
		return configs.get(id);
	}
}
