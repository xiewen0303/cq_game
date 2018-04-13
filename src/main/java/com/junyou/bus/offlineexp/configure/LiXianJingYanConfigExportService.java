package com.junyou.bus.offlineexp.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 离线经验奖励加载
 * @author jy
 *
 */
@Component
public class LiXianJingYanConfigExportService extends AbsClasspathConfigureParser {
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "LiXianJingYan.jat";
	
	private Map<Integer,Long> offlineExpConfigs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer,Long> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				configs.put(CovertObjectUtil.object2int(tmp.get("level")), CovertObjectUtil.object2Long(tmp.get("exp")));
			}
		}
		this.offlineExpConfigs = configs;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public Long getExpByLvl(int lvl){
		return offlineExpConfigs.get(lvl);
	}
	
}