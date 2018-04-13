package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @description 技能招唤配置表
 *
 * @author DaoZhen Yuan
 * @date 2013-11-12 16:50:31
 */
@Component
public class ZhaoHuanBiaoConfigExportService{
	
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhaoHuanBiao.jat";
	
	private static int MAX_LEVEL = 100;
	
	private Map<Integer,ZhaoHuanBiaoConfig> configs = new HashMap<>();
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhaoHuanBiaoConfig config = createZhaoHuanBiaoConfig(tmp);
				configs.put(config.getLevel(), config);
				
				if(config.getLevel() > MAX_LEVEL){
					MAX_LEVEL = config.getLevel();
				}
			}
		}
	}
	
	public ZhaoHuanBiaoConfig createZhaoHuanBiaoConfig(Map<String, Object> tmp) {
		ZhaoHuanBiaoConfig config = new ZhaoHuanBiaoConfig();	
							
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		
		String monsterId = CovertObjectUtil.object2String(tmp.get("id1"));
		config.setMonsterIds(1, monsterId);
		monsterId = CovertObjectUtil.object2String(tmp.get("id2"));
		config.setMonsterIds(2, monsterId);
											
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZhaoHuanBiaoConfig loadById(int level){
		if(level > MAX_LEVEL){
			level = MAX_LEVEL;
		}
		
		return configs.get(level);
	}
}