package com.junyou.stage.hundun.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 混沌战场规则表 
 *
 * @author LiNing
 * @date 2015-07-25 13:43:23
 */
@Component
public class HunDunWarGuiZeBiaoConfigExportService extends AbsClasspathConfigureParser{
	
	/**
	  * configFileName
	 */
	private String configureName = "HunDunZhanChangGuiZeBiao.jat";
	private Map<Integer, HunDunWarGuiZeBiaoConfig> hdConfig;
	private int MINID = -1;//最小Id
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer, HunDunWarGuiZeBiaoConfig> tmpHdConfig = new HashMap<>();
		Map<String, Integer> tmpMapConfig = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				HunDunWarGuiZeBiaoConfig config = createHunDunZhanChangGuiZeBiaoConfig(tmp);
				
				if(MINID <= 0 || MINID > config.getId().intValue()){
					MINID = config.getId().intValue();
				}
				tmpHdConfig.put(config.getId(), config);
			}
		}
		
		this.hdConfig = tmpHdConfig;
	}
	
	private HunDunWarGuiZeBiaoConfig createHunDunZhanChangGuiZeBiaoConfig(Map<String, Object> tmp) {
		HunDunWarGuiZeBiaoConfig config = new HunDunWarGuiZeBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setMinneed(CovertObjectUtil.object2int(tmp.get("minneed")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setSecond(CovertObjectUtil.object2int(tmp.get("second")));
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
		config.setNumber(CovertObjectUtil.object2int(tmp.get("number")));
		config.setData1(CovertObjectUtil.object2int(tmp.get("date1")));
		config.setPujiang(CovertObjectUtil.object2String(tmp.get("pujiang")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 获取当前Id中的下一层混沌配置
	 * @param id
	 * @return
	 */
	public HunDunWarGuiZeBiaoConfig getHdConfigByNextId(Integer id){
		for (int i = 1; i < 6; i++) {
			HunDunWarGuiZeBiaoConfig config = hdConfig.get(id + i);
			if(config != null){
				return config;
			}
		}
		return null;
	}
	
	/**
	 * 获取最小Id的混沌配置
	 */
	public HunDunWarGuiZeBiaoConfig getHdConfigByMinId(){
		return hdConfig.get(MINID);
	}
	
	public HunDunWarGuiZeBiaoConfig getHunDunWarGuiZeBiaoConfigById(Integer id){
		return hdConfig.get(id);
	}
}