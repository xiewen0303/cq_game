package com.junyou.bus.shenmo.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.shenmo.configure.ShenMoCampConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-9-24 下午5:03:32
 */
@Service
public class ShenMoCampConfigExportService extends AbsClasspathConfigureParser{
	
	private String configureName = "ShenMoZhenYingBiao.jat";
	private Map<Integer,ShenMoCampConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Map<Integer,ShenMoCampConfig> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShenMoCampConfig config = createShenMoCampConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private ShenMoCampConfig createShenMoCampConfig(Map<String, Object> tmp){
		ShenMoCampConfig config = new ShenMoCampConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		String zb = CovertObjectUtil.object2String(tmp.get("zuobiao"));
		String[] zbInfo = zb.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		if(zbInfo != null && zbInfo.length > 1){
			config.setBrith(new int[]{CovertObjectUtil.object2int(zbInfo[0]),CovertObjectUtil.object2int(zbInfo[1])});
		}
		return config;
	}
	
	public ShenMoCampConfig getConfig(Integer id){
		if(configs == null){
			return null;
		}
		return configs.get(id);
	}
	
	
}
