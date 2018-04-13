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
 * @description 阵营战经验配置 
 *
 * @author LiNing
 * @date 2015-04-10 14:57:19
 */
@Component
public class ZhenYinZhanJingYanConfigExportService extends AbsClasspathConfigureParser{
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhenYinZhanJingYan.jat";
	private Map<Integer, Long> expMap;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, Long> tmpExpMap = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhenYinZhanJingYanConfig config = createZhenYinZhanJingYanConfig(tmp);
				
				tmpExpMap.put(config.getLevel(), config.getExp());
			}
		}
		
		this.expMap = tmpExpMap;
	}
	
	public ZhenYinZhanJingYanConfig createZhenYinZhanJingYanConfig(Map<String, Object> tmp) {
		ZhenYinZhanJingYanConfig config = new ZhenYinZhanJingYanConfig();	
							
		config.setExp(CovertObjectUtil.obj2long(tmp.get("exp")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 根据玩家等级获取阵营战经验
	 * @param level
	 * @return
	 */
	public Long getCampExpByLevel(Integer level){
		if(expMap == null || expMap.size() <= 0){
			return null;
		}
		return expMap.get(level);
	}
}