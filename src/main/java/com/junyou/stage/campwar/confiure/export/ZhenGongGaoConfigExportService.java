package com.junyou.stage.campwar.confiure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 阵营战公共配置 
 *
 * @author LiNing
 * @date 2015-04-10 04:16:57
 */
@Component
public class ZhenGongGaoConfigExportService extends AbsClasspathConfigureParser {
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhenGongGao.jat";
	private Map<Integer, ZhenGongGaoConfig> gonggaoMap;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<Integer, ZhenGongGaoConfig> map = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhenGongGaoConfig config = createZhenGongGaoConfig(tmp);
				
				map.put(config.getSrnum(), config);
			}
		}
		
		this.gonggaoMap = map;
	}
	
	public ZhenGongGaoConfig createZhenGongGaoConfig(Map<String, Object> tmp) {
		ZhenGongGaoConfig config = new ZhenGongGaoConfig();	
		Integer srnum = CovertObjectUtil.object2int(tmp.get("srnum"));	
		config.setSrnum(srnum);
		config.setGonggao(CovertObjectUtil.object2int(tmp.get("gonggao")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZhenGongGaoConfig getConfig(Integer num){
		return gonggaoMap.get(num);
	}
	
}